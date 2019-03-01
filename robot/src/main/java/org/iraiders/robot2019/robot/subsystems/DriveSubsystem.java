package org.iraiders.robot2019.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.drive.OIDrive;

public class DriveSubsystem extends Subsystem {
  public final CANSparkMax frontLeft = new CANSparkMax(RobotMap.frontLeftTalonPort, CANSparkMaxLowLevel.MotorType.kBrushed);
  private final CANSparkMax backLeft = new CANSparkMax(RobotMap.backLeftTalonPort, CANSparkMaxLowLevel.MotorType.kBrushed);
  public final CANSparkMax frontRight = new CANSparkMax(RobotMap.frontRightTalonPort, CANSparkMaxLowLevel.MotorType.kBrushed);
  private final CANSparkMax backRight = new CANSparkMax(RobotMap.backRightTalonPort, CANSparkMaxLowLevel.MotorType.kBrushed);

  public final DigitalInput leftLine = new DigitalInput(RobotMap.leftLineSensorPort);
  public final DigitalInput midLine = new DigitalInput(RobotMap.midLineSensorPort);
  public final DigitalInput rightLine = new DigitalInput(RobotMap.rightLineSensorPort);

  private OIDrive oiDrive;
  public DifferentialDrive roboDrive = new DifferentialDrive(frontLeft, frontRight);

  public DriveSubsystem() {
    //backLeft.set(ControlMode.Follower, RobotMap.frontLeftTalonPort);
    //backRight.set(ControlMode.Follower, RobotMap.frontRightTalonPort);
    backLeft.follow(frontLeft);
    backRight.follow(frontLeft);
  }

  public void initTeleop() {
    oiDrive = new OIDrive(this);
    oiDrive.start();
  }

  @Override
  protected void initDefaultCommand() {

  }

  /**
   * Given a target number, current number, and increment, adjust current number by increment until we reach target
   * This is useful particularly in {@link OIDrive} where we need to ramp up to user input to avoid jerkiness
   *
   * @param target The number you eventually want to get to (ie. joystick speed)
   * @param current The current number you are at (so we know what to start at for the increment)
   * @param increment How much to increase current by until current = target
   * @see <a href="https://en.wikipedia.org/wiki/Slew_rate">Wikipedia article on Slew rates</a>
   * @return Adjusted target
   */
  public static double slewLimit(double target, double current, double increment) {
    increment = Math.abs(increment); // Professionally validating user input right here 👌
    double change = target - current;
    if (Math.abs(current) > Math.abs(target)) return target; // Always slow down immediately for safety concerns
    if (change > increment) { change = increment; }
    else if (change < -increment) { change = -increment; }
    return current + change;
  }
}
