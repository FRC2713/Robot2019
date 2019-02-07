package org.iraiders.robot2019.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.Robot;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class OIDrive extends Command {
  private DriveSubsystem driveSubsystem;
  private boolean useTankInsteadOfBradford = false;
  private double lastLeftStickVal = 0;
  private double lastRightStickVal = 0;
  private XboxController xbox = OI.xBoxController;
  public boolean lineSensing = false;
  private double joystickChangeLimit;
  public boolean isLineSensing = false;
  private double snappingScalingValue = 1.0;
  private double leftSnappingScalingValue = 1.0;
  private double rightSnappingScalingValue = 1.0;

  public OIDrive(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    requires(driveSubsystem);
  }

  @Override
  protected void initialize() {
    driveSubsystem.roboDrive.setMaxOutput(Robot.prefs.getFloat("OIMaxSpeed", 1));
    joystickChangeLimit = Robot.prefs.getDouble("JoystickChangeLimit", 1f);
    snappingScalingValue = SmartDashboard.getNumber("Snapping scaling value", 1.0);
  }

  @Override
  protected void execute() {
    double measuredLeft;
    double measuredRight;

    if(isLineSensing == true) {
      if (driveSubsystem.leftLine.get() == true) {
        leftSnappingScalingValue *= (1+snappingScalingValue);
      }
      else if (driveSubsystem.rightLine.get() == true) {
        rightSnappingScalingValue *= (1+snappingScalingValue);
      }
    }
    else {
      leftSnappingScalingValue = 1;
      rightSnappingScalingValue = 1;
    }

    if (xbox.getRawButtonPressed(8)) {
      useTankInsteadOfBradford = !useTankInsteadOfBradford;
      lastRightStickVal = 0;
      lastLeftStickVal = 0;
      OI.rumbleController(xbox, .5, 500);
    }

    if (useTankInsteadOfBradford) {
      measuredLeft = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit)*leftSnappingScalingValue;
      measuredRight = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit)*rightSnappingScalingValue;
      driveSubsystem.roboDrive.tankDrive(measuredLeft, measuredRight, true);
    } else {
      measuredLeft = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit)*leftSnappingScalingValue;
      measuredRight = DriveSubsystem.slewLimit(xbox.getX(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit)*rightSnappingScalingValue;
      driveSubsystem.roboDrive.arcadeDrive(measuredLeft, measuredRight, true);
    }

    driveSubsystem.btn8.toggleWhenPressed(new enableLineSensors(this));


    isLineSensing = false;
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
