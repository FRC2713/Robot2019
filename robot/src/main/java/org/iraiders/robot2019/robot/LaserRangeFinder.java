package org.iraiders.robot2019.robot;

import edu.wpi.first.wpilibj.I2C;

import java.nio.ByteBuffer;

public class LaserRangeFinder extends I2C {
  private int deviceID;
  public LaserRangeFinder(int deviceID){
    super(Port.kOnboard,deviceID);
    this.deviceID = deviceID;
  }

  public void startRangeFinding() {
    write(0x0087,0x40);
  }

  public void stopRangeFinding() {
    write(0x0087, 0x00);
  }

  public short getDistance() {
    byte[] bytearray = new byte[2];

    read(0x0096, 2, bytearray);
    return toShort(bytearray);
  }

  public static short toShort(byte[] bytearray) {
    return ByteBuffer.wrap(bytearray).getShort();
  }
}
