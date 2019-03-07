package org.iraiders.robot2019.robot.commands.drive;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.Robot;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class OIDrive extends Command {
  private DriveSubsystem driveSubsystem;
  private XboxController xbox = OI.xBoxController;
  private boolean useTankInsteadOfBradford = false;
  private boolean useLineTracking = false;

  private double lastLeftStickVal = 0;
  private double lastRightStickVal = 0;
  private double deadband = 0.07;

  private double defaultSnapValue = 0.34;
  //Ultrasonic ultra = new Ultrasonic(RobotMap.ultraSonicPing,RobotMap.ultraSonicEcho);

  private double joystickChangeLimit;

  public OIDrive(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    requires(driveSubsystem);
  }

  @Override
  protected void initialize() {
    driveSubsystem.roboDrive.setMaxOutput(Robot.prefs.getFloat("OIMaxSpeed", 1));
    joystickChangeLimit = Robot.prefs.getDouble("JoystickChangeLimit", 1f);

    SmartDashboard.putNumber("Snap Scale Value", defaultSnapValue);
    SmartDashboard.putNumber("PreStopRange", 0.001);

    driveSubsystem.roboDrive.setDeadband(deadband);
    //ultra.setAutomaticMode(true);
  }

  @Override
  protected void execute() {
    double measuredLeft;
    double measuredRight = 0;

    if (xbox.getRawButtonPressed(8)) {
      useTankInsteadOfBradford = !useTankInsteadOfBradford;
      lastRightStickVal = 0;
      lastLeftStickVal = 0;
      OI.rumbleController(xbox, .5, 500);
    }

    if (xbox.getRawButtonPressed(7) && (!driveSubsystem.midLine.get() || useLineTracking)) {

      useLineTracking = !useLineTracking;
      lastRightStickVal = 0;
      lastLeftStickVal = 0;
      OI.rumbleController(xbox, .5, 500);
    }

    SmartDashboard.putBoolean("LeftLine", !driveSubsystem.leftLine.get()); // invert because ls are inverted
    SmartDashboard.putBoolean("midLine", !driveSubsystem.midLine.get()); // on testbed green was off line
    SmartDashboard.putBoolean("rightLine", !driveSubsystem.midLine.get());

    byte lineSensorByte = 0;

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
        measuredRight = 0;
        break;
      case 4:
        measuredRight = SmartDashboard.getNumber("Snap Scale Value", defaultSnapValue);
        break;
      case 2:
        measuredRight = 0;
        break;
      case 6:
        measuredRight = SmartDashboard.getNumber("Snap Scale Value", defaultSnapValue)/2;
        break;
      case 1:
        measuredRight = -SmartDashboard.getNumber("Snap Scale Value", defaultSnapValue);
        break;
      case 5:
        measuredRight = 0;
        break;
      case 3:
        measuredRight = -SmartDashboard.getNumber("Snap Scale Value", defaultSnapValue)/2;
        break;
      case 7:
        measuredRight = 0;
        break;
      default:
        break;
    }

    measuredRight *= -1 ;

    if (useTankInsteadOfBradford && !useLineTracking) {
      measuredLeft = DriveSubsystem.slewLimit(xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredRight = DriveSubsystem.slewLimit(xbox.getY(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      driveSubsystem.roboDrive.tankDrive(measuredLeft, measuredRight, true);
    } else {
      measuredLeft = DriveSubsystem.slewLimit(xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredRight = (useLineTracking) ? DriveSubsystem.slewLimit(measuredRight, lastRightStickVal, joystickChangeLimit) : DriveSubsystem.slewLimit(-xbox.getX(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      driveSubsystem.roboDrive.arcadeDrive(measuredLeft, measuredRight, true);
    }

    lastLeftStickVal = measuredLeft;
    lastRightStickVal = measuredRight;

    //System.out.println("ultra: " + ultra.getRangeInches());

   //if (SmartDashboard.getNumber("Snap Scale Value", 1.0)>1.0&& ultra.getRangeInches() < SmartDashboard.getNumber ("PreStopRange", 0.001))  {
       //driveSubsystem.roboDrive.stopMotor();
    //}
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
