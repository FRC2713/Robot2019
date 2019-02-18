package org.iraiders.robot2019.robot.commands.lift;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Command;

public class LaserArduinoInterface extends Command {
  private SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);

  public LaserArduinoInterface() {

  }

  @Override
  protected void execute(){
    double distanceInches = Double.parseDouble(port.readString()) * 0.0393701;
    System.out.println("This is the distance: " + distanceInches);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
