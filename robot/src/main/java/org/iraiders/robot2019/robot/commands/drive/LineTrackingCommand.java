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

  public double snapScaleValue = 0;
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

    snapScaleValue = SmartDashboard.getNumber("Snap Scale Value", 0.62);
    SmartDashboard.putNumber("Snap Scale Value", snapScaleValue); // In case it's vanished from the dashboard

    OI.rumbleController(xbox, .5, 500);
  }

  @Override
  protected void execute() {
    double measuredLeft;
    double measuredTurn = 0;

    lineSensorByte = 0;

    if (!driveSubsystem.leftLine.get()) {
      lineSensorByte |= 1;
    }

    if (!driveSubsystem.midLine.get()) {
      lineSensorByte |= 2;
    }

    if (!driveSubsystem.rightLine.get()) {
      lineSensorByte |= 4;
    }

    if (snapScaleValue == 0) return; // Don't move if disabled


    switch (lineSensorByte) {
      // No line sensors are detected
      case 0:
        measuredTurn = 0;
        break;
      // Just right line sensor detected
      case 4:
        measuredTurn = snapScaleValue;
        break;
      // Just middle line sensor detected (error)
      case 2:
        measuredTurn = 0;
        break;
      // Middle and right line sensor detected
      case 6:
        measuredTurn = snapScaleValue / 2;
        break;
      // left line sensor detected
      case 1:
        measuredTurn = -snapScaleValue;
        break;
      // Left and right line sensors detected (error)
      case 5:
        measuredTurn = 0;
        break;
      // Left and middle sensors detected
      case 3:
        measuredTurn = -snapScaleValue / 2;
        break;

      // All line sensors detected
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
  protected void end() {
    driveSubsystem.roboDrive.stopMotor();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
