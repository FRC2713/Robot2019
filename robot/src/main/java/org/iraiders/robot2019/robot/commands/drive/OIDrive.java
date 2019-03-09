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
  public XboxController xbox = OI.xBoxController;
  private boolean useTankInsteadOfBradford = false;

  private double lastLeftStickVal = 0;
  private double lastRightStickVal = 0;
  private double deadband = 0.07;

  //Ultrasonic ultra = new Ultrasonic(RobotMap.ultraSonicPing,RobotMap.ultraSonicEcho);

  private double joystickChangeLimit;

  public OIDrive(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    requires(driveSubsystem);
  }

  @Override
  protected void initialize() {

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

    if (useTankInsteadOfBradford ) {
      measuredLeft = DriveSubsystem.slewLimit(xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredRight = DriveSubsystem.slewLimit(xbox.getY(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      driveSubsystem.roboDrive.tankDrive(measuredLeft, measuredRight, true);
    } else {
      measuredLeft = DriveSubsystem.slewLimit(xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredRight =  DriveSubsystem.slewLimit(-xbox.getX(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      driveSubsystem.roboDrive.arcadeDrive(measuredLeft, measuredRight, true);
    }

    lastLeftStickVal = measuredLeft;
    lastRightStickVal = measuredRight;
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
