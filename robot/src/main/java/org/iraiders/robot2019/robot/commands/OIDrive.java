package org.iraiders.robot2019.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.Robot;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class OIDrive extends Command {
  private DriveSubsystem driveSubsystem;
  private boolean useTankInsteadOfBradford = false;
  private double lastLeftStickVal = 0;
  private double lastRightStickVal = 0;
  private XboxController xbox = OI.xBoxController;

  public OIDrive(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    requires(driveSubsystem);
  }

  @Override
  protected void execute() {
    driveSubsystem.roboDrive.setMaxOutput(Robot.prefs.getFloat("OIMaxSpeed", 1));
    driveSubsystem.roboDrive.setSafetyEnabled(false);

    double joystickChangeLimit = Robot.prefs.getDouble("JoystickChangeLimit", 1f);

    double measuredLeft;
    double measuredRight;

    if (xbox.getRawButtonPressed(8)) {
      useTankInsteadOfBradford = !useTankInsteadOfBradford;
      lastRightStickVal = 0;
      lastLeftStickVal = 0;
      OI.rumbleController(xbox, .5, 500);

    }

    if (useTankInsteadOfBradford) {
      measuredLeft = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredRight = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      driveSubsystem.roboDrive.tankDrive(measuredLeft, measuredRight, true);
    } else {
      measuredLeft = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredRight = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      driveSubsystem.roboDrive.arcadeDrive(measuredLeft, measuredRight, true);
    }

  }

  @Override
  protected void end() {
    driveSubsystem.roboDrive.arcadeDrive(0, 0, false);
    driveSubsystem.roboDrive.setSafetyEnabled(true);

  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
