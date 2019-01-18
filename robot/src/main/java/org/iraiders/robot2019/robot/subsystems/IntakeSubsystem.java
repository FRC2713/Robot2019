package org.iraiders.robot2019.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.ResponsiveMotorCommand;
import org.iraiders.robot2019.robot.commands.SimpleMotorCommand;
import org.iraiders.robot2019.robot.commands.intake.GrabberControlCommand;

public class IntakeSubsystem extends Subsystem {
  private final WPI_TalonSRX grabberIntakeRight = new WPI_TalonSRX(RobotMap.grabberIntakeRightTalonPort);
  private final WPI_TalonSRX grabberIntakeLeft = new WPI_TalonSRX(RobotMap.grabberIntakeLeftTalonPort);
  public final WPI_TalonSRX grabberIntakeJoint = new WPI_TalonSRX(RobotMap.grabberIntakeJointTalonPort);

  public final DoubleSolenoid grabberSolenoid = new DoubleSolenoid(RobotMap.grabberOpenNodeId, RobotMap.grabberCloseNodeId);

  private final DigitalInput ballIntakeLimitSwitch = new DigitalInput(RobotMap.ballIntakeLimitSwitchPort);

  public IntakeSubsystem() {
    grabberIntakeLeft.set(ControlMode.Follower, RobotMap.grabberIntakeRightTalonPort);
    initControls();
  }

  private void initControls() {
    GenericHID buttonBox = OI.arcadeController;

    JoystickButton btn3 = new JoystickButton(buttonBox, 3);
    JoystickButton btn4 = new JoystickButton(buttonBox, 4);
    JoystickButton btn5 = new JoystickButton(buttonBox, 5);
    JoystickButton btn6 = new JoystickButton(buttonBox, 6);
    JoystickButton btn7 = new JoystickButton(buttonBox, 7);


    btn3.whileHeld(new ResponsiveMotorCommand(grabberIntakeRight,.75, ballIntakeLimitSwitch));
    btn4.whileHeld(new SimpleMotorCommand(grabberIntakeRight,-.75));
    btn5.whileHeld(new SimpleMotorCommand(grabberIntakeJoint,.5));
    btn6.whileHeld(new GrabberControlCommand(this, GrabberPosition.OPEN));
    btn7.whileHeld(new GrabberControlCommand(this, GrabberPosition.CLOSE));
  }

  @Override
  protected void initDefaultCommand() {

  }

  public enum IntakePosition {
    STARTING
  }

  public enum GrabberPosition {
    OPEN, CLOSE
  }
}
