package org.iraiders.robot2019.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.ResponsiveMotorCommand;
import org.iraiders.robot2019.robot.commands.SimpleMotorCommand;

public class IntakeSubsystem extends Subsystem {
  private final WPI_TalonSRX grabberIntakeRight = new WPI_TalonSRX(RobotMap.grabberIntakeRightTalonPort);
  private final WPI_TalonSRX grabberIntakeLeft = new WPI_TalonSRX(RobotMap.grabberIntakeLeftTalonPort);
  public final WPI_TalonSRX grabberIntakeJoint = new WPI_TalonSRX(RobotMap.grabberIntakeJointTalonPort);

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


    btn3.whileHeld(new ResponsiveMotorCommand(grabberIntakeRight,.75, ballIntakeLimitSwitch));
    btn4.whileHeld(new SimpleMotorCommand(grabberIntakeRight,-.75));
    btn5.whileHeld(new SimpleMotorCommand(grabberIntakeJoint,.5));
  }

  @Override
  protected void initDefaultCommand() {

  }
  public enum IntakePosition {
    STARTING
  }
}
