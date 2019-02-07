package org.iraiders.robot2019.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand;

public class IntakeSubsystem extends Subsystem {
  public final BallIntakeControlCommand ballIntakeControlCommand = new BallIntakeControlCommand();

  public final DoubleSolenoid ballIntakeSolenoid = new DoubleSolenoid(RobotMap.ballIntakeUpNodeId, RobotMap.ballIntakeDownNodeId);
  private final DigitalInput ballIntakeLimitSwitch = new DigitalInput(RobotMap.ballIntakeLimitSwitchPort);
  // TODO stop intake roller and bring up when ^^^^ is triggered

  public IntakeSubsystem() {
    initControls();
  }

  private void initControls() {
    GenericHID buttonBox = OI.arcadeController;

    /*
    JoystickButton btn3 = new JoystickButton(buttonBox, 3);
    JoystickButton btn4 = new JoystickButton(buttonBox, 4);
    JoystickButton btn5 = new JoystickButton(buttonBox, 5);
    JoystickButton btn6 = new JoystickButton(buttonBox, 6);
    JoystickButton btn7 = new JoystickButton(buttonBox, 7);


    btn3.whileHeld(new ResponsiveMotorCommand(ballIntakeMotor,.75, ballIntakeLimitSwitch));
    btn4.whileHeld(new SimpleMotorCommand(ballIntakeMotor,-.75));
    btn5.whileHeld(new SimpleMotorCommand(grabberIntakeJoint,.5));
    btn6.whileHeld(new BallIntakeJointCommand(this, GrabberPosition.OPEN));
    btn7.whileHeld(new BallIntakeJointCommand(this, GrabberPosition.CLOSE));
    */
  }

  @Override
  protected void initDefaultCommand() {
    ballIntakeControlCommand.start();
  }

  public enum IntakeJointPosition {
    UP, DOWN
  }
}
