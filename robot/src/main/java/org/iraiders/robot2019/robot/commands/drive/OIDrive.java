package org.iraiders.robot2019.robot.commands.drive;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.Robot;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class OIDrive extends Command {
  private DriveSubsystem driveSubsystem;
  private XboxController xbox = OI.xBoxController;
  private boolean useTankInsteadOfBradford = false;

  private double lastLeftStickVal = 0;
  private double lastRightStickVal = 0;
  private double deadband = 0.17 ;
  Ultrasonic ultra = new Ultrasonic(RobotMap.ultraSonicPing,RobotMap.ultraSonicEcho);

  private double joystickChangeLimit;

  public OIDrive(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    requires(driveSubsystem);
  }

  @Override
  protected void initialize() {
    driveSubsystem.roboDrive.setMaxOutput(Robot.prefs.getFloat("OIMaxSpeed", 1));
    joystickChangeLimit = Robot.prefs.getDouble("JoystickChangeLimit", 1f);

    driveSubsystem.roboDrive.setDeadband(deadband-0.1);
    ultra.setAutomaticMode(true);
  }

  @Override
  protected void execute() {
    double measuredLeft;
    double measuredRight;

    if (xbox.getRawButtonPressed(8)) {
      useTankInsteadOfBradford = !useTankInsteadOfBradford;
      lastRightStickVal = 0;
      lastLeftStickVal = 0;
      OI.rumbleController(xbox, .5, 500);
    }
    double leftSnapScalar = 1;
    double rightSnapScalar = 1;
    double arcadeSnapScaler = 1;


    if (driveSubsystem.leftLine.get()) {
      rightSnapScalar = 1.0 ;
      leftSnapScalar = SmartDashboard.getNumber("Snap Scale Value", 1.0);
      arcadeSnapScaler = SmartDashboard.getNumber("Snap Scale Value", 1.0);
    } else if (driveSubsystem.rightLine.get()) {
      rightSnapScalar = SmartDashboard.getNumber("Snap Scale Value", 1.0);
      leftSnapScalar = 1.0 ;
      arcadeSnapScaler = -SmartDashboard.getNumber("Snap Scale Value", 1.0);
    } else {
      rightSnapScalar = 1;
      leftSnapScalar = 1;
      arcadeSnapScaler = 1;
    }

    if (useTankInsteadOfBradford) {
      measuredLeft = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredLeft = (measuredLeft > Math.abs(deadband)) ? measuredLeft + (leftSnapScalar-1) : measuredLeft ;
      measuredRight = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      measuredRight = (measuredRight > Math.abs(deadband)) ? measuredRight + (rightSnapScalar-1) : measuredRight ;
      driveSubsystem.roboDrive.tankDrive(measuredLeft, measuredRight, true);
    } else {
      measuredLeft = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredRight = DriveSubsystem.slewLimit(xbox.getX(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      if(measuredLeft > Math.abs(deadband) || measuredRight > Math.abs(deadband)) {
        measuredRight = measuredRight + ((arcadeSnapScaler < 0) ? arcadeSnapScaler + 1 : arcadeSnapScaler - 1);
      }
      driveSubsystem.roboDrive.arcadeDrive(measuredLeft, measuredRight, true);
    }
    if (SmartDashboard.getNumber("Snap Scale Value", 1.0)>1.0&& ultra.getRangeInches() < SmartDashboard.getNumber ("PreStopRange", 0.001))  {

    }
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
