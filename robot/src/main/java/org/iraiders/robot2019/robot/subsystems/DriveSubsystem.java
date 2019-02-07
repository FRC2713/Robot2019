package org.iraiders.robot2019.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.OIDrive;

public class DriveSubsystem extends Subsystem {
  public final WPI_TalonSRX frontLeft = new WPI_TalonSRX(RobotMap.frontLeftTalonPort);
  private final WPI_TalonSRX backLeft = new WPI_TalonSRX(RobotMap.backLeftTalonPort);
  public final WPI_TalonSRX frontRight = new WPI_TalonSRX(RobotMap.frontRightTalonPort);
  private final WPI_TalonSRX backRight = new WPI_TalonSRX(RobotMap.backRightTalonPort);


  public final DigitalInput leftLine = new DigitalInput(RobotMap.leftLineSensorPort);
  public final DigitalInput midLine = new DigitalInput(RobotMap.midLineSensorPort);
  public final DigitalInput rightLine = new DigitalInput(RobotMap.rightLineSensorPort);

  public GenericHID buttonBox = OI.arcadeController;
  public JoystickButton btn8 = new JoystickButton(buttonBox, 8);
  private OIDrive oiDrive;
  public DifferentialDrive roboDrive = new DifferentialDrive(frontLeft, frontRight);



  public DriveSubsystem() {
    backLeft.set(ControlMode.Follower, RobotMap.frontLeftTalonPort);
    backRight.set(ControlMode.Follower, RobotMap.frontRightTalonPort);
  }

  public void initTeleop() {
    oiDrive = new OIDrive(this);
    oiDrive.start();
  }

  private void initControls() {


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
