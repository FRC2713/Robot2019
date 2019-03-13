package org.iraiders.robot2019.robot.commands.drive;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.Robot;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class LineTrackingCommand extends Command {

  private XboxController xbox = OI.xBoxController;
  private double lastLeftStickVal = 0;
  private double lastRightStickVal = 0;
  private DriveSubsystem driveSubsystem;
  private double deadband = 0.07;

  private double defaultSnapValue = 0.62;
  private double joystickChangeLimit;

  public byte lineSensorByte = 0;

  public LineTrackingCommand(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    requires(driveSubsystem);
  }

  @Override
  protected void initialize() {
    driveSubsystem.roboDrive.setMaxOutput(Robot.prefs.getFloat("OIMaxSpeed", 1));
    joystickChangeLimit = Robot.prefs.getDouble("JoystickChangeLimit", .09);

    SmartDashboard.putNumber("Snap Scale Value", SmartDashboard.getNumber("Snap Scale Value", defaultSnapValue));

    driveSubsystem.roboDrive.setDeadband(deadband);

    OI.rumbleController(xbox, .5, 500);
  }

  @Override
  protected void execute() {
    double measuredLeft;
    double measuredTurn = 0;

    if (!driveSubsystem.leftLine.get()) {
      lineSensorByte |= 1;
    }

    if (!driveSubsystem.midLine.get()) {
      lineSensorByte |= 2;
    }

    if (!driveSubsystem.rightLine.get()) {
      lineSensorByte |= 4;
    }


    switch (lineSensorByte) {
      case 0:
        measuredTurn = 0;
        break;
      case 4:
        measuredTurn = SmartDashboard.getNumber("Snap Scale Value", defaultSnapValue);
        break;
      case 2:
        measuredTurn = 0;
        break;
      case 6:
        measuredTurn = SmartDashboard.getNumber("Snap Scale Value", defaultSnapValue) / 2;
        break;
      case 1:
        measuredTurn = -SmartDashboard.getNumber("Snap Scale Value", defaultSnapValue);
        break;
      case 5:
        measuredTurn = 0;
        break;
      case 3:
        measuredTurn = -SmartDashboard.getNumber("Snap Scale Value", defaultSnapValue) / 2;
        break;
      case 7:
        measuredTurn = 0;
        break;
      default:
        break;
    }


        measuredTurn *= -1; //make sure we are going the right direction

        measuredLeft = DriveSubsystem.slewLimit(xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
        measuredTurn = DriveSubsystem.slewLimit(measuredTurn, lastRightStickVal, joystickChangeLimit);
        driveSubsystem.roboDrive.arcadeDrive(measuredLeft, measuredTurn, true);

        lastLeftStickVal = measuredLeft;
        lastRightStickVal = measuredTurn;

    }

  @Override
  protected void end() { driveSubsystem.roboDrive.stopMotor(); }

  @Override
  protected boolean isFinished() { return false; }
}
