package org.iraiders.robot2019.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PixyCamera {
  String name;
  PixyPacket values;
  I2C pixy;
  I2C.Port port = I2C.Port.kOnboard;
  PixyPacket[] packets;
  PixyException pExc;
  String print;

  public PixyCamera(String id, I2C argPixy, PixyPacket[] argPixyPacket, PixyException argPixyException,
                 PixyPacket argValues) {
    pixy = argPixy;
    packets = argPixyPacket;
    pExc = argPixyException;
    values = argValues;
    name = "Pixy_" + id;
  }

  // This method parses raw data from the pixy into readable integers
  public int cvt(byte upper, byte lower) {
    return (((int) upper & 0xff) << 8) | ((int) lower & 0xff);
  }

  private byte[] readData(int len) {
    byte[] rawData = new byte[len];
    try {
      pixy.readOnly(rawData, len);
    } catch (RuntimeException e) {
      SmartDashboard.putString(name + "Status", e.toString());
      System.out.println(name + "  " + e);
    }
    if (rawData.length < len) {
      SmartDashboard.putString(name + "Status", "raw data length " + rawData.length);
      System.out.println("byte array length is broken length=" + rawData.length);
      return null;
    }
    return rawData;
  }

  private int readWord() {
    byte[] data = readData(2);
    if (data == null) {
      return 0;
    }
    return cvt(data[1], data[0]);
  }

  private PixyPacket readBlock(int checksum) {
    // See Object block format section in
    // http://www.cmucam.org/projects/cmucam5/wiki/Porting_Guide#Object-block-format
    // Each block is 14 bytes, but we already read 2 bytes for checksum in
    // caller so now we need to read rest.

    byte[] data = readData(12);
    if (data == null) {
      return null;
    }
    PixyPacket block = new PixyPacket();
    block.Signature = cvt(data[1], data[0]);
    if (block.Signature <= 0 || block.Signature > 7) {
      return null;
    }
    block.X = cvt(data[3], data[2]);
    block.Y = cvt(data[5], data[4]);
    block.Width = cvt(data[7], data[6]);
    block.Height = cvt(data[9], data[8]);

    int sum = block.Signature + block.X + block.Y + block.Width + block.Height;
    if (sum != checksum) {
      return null;
    }
    return block;
  }

  private final int MAX_SIGNATURES = 7;
  private final int OBJECT_SIZE = 14;
  private final int START_WORD = 0xaa55;
  private final int START_WORD_CC = 0xaa5;
  private final int START_WORD_X = 0x55aa;

  public boolean getStart() {
    int numBytesRead = 0;
    int lastWord = 0xffff;
    // This while condition was originally true.. may not be a good idea if
    // something goes wrong robot will be stuck in this loop forever. So
    // let's read some number of bytes and give up so other stuff on robot
    // can have a chance to run. What number of bytes to choose? Maybe size
    // of a block * max number of signatures that can be detected? Or how
    // about size of block and max number of blocks we are looking for?
    while (numBytesRead < (OBJECT_SIZE * MAX_SIGNATURES)) {
      int word = readWord();
      numBytesRead += 2;
      if (word == 0 && lastWord == 0) {
        return false;
      } else if (word == START_WORD && lastWord == START_WORD) {
        return true;
      } else if (word == START_WORD_CC && lastWord == START_WORD) {
        return true;
      } else if (word == START_WORD_X) {
        byte[] data = readData(1);
        numBytesRead += 1;
      }
      lastWord = word;
    }
    return false;
  }

  private boolean skipStart = false;

  public PixyPacket[] readBlocks() {
    // This has to match the max block setting in pixymon?
    int maxBlocks = 2;
    PixyPacket[] blocks = new PixyPacket[maxBlocks];

    if (!skipStart) {
      if (getStart() == false) {
        return null;
      }
    } else {
      skipStart = false;
    }
    for (int i = 0; i < maxBlocks; i++) {
      // Should we set to empty PixyPacket? To avoid having to check for
      // null in callers?
      blocks[i] = null;
      int checksum = readWord();
      if (checksum == START_WORD) {
        // we've reached the beginning of the next frame
        skipStart = true;
        return blocks;
      } else if (checksum == START_WORD_CC) {
        // we've reached the beginning of the next frame
        skipStart = true;
        return blocks;
      } else if (checksum == 0) {
        return blocks;
      }
      blocks[i] = readBlock(checksum);
    }
    return blocks;
  }
}
