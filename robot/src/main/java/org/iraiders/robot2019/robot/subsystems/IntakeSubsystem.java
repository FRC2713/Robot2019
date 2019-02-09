package org.iraiders.robot2019.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeJointCommand;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeMonitor;
import org.iraiders.robot2019.robot.commands.intake.HatchCommand;

public class IntakeSubsystem extends Subsystem {
  private final BallIntakeMonitor ballIntakeMonitor = new BallIntakeMonitor(this);
  public final BallIntakeControlCommand ballIntakeControlCommand = new BallIntakeControlCommand();
  public final BallIntakeJointCommand ballIntakeJointCommand = new BallIntakeJointCommand(this);
  public final HatchCommand hatchCommand = new HatchCommand(this);

  public final DoubleSolenoid ballIntakeSolenoid = new DoubleSolenoid(RobotMap.ballIntakeUpNodeId, RobotMap.ballIntakeDownNodeId);
  public final DigitalInput ballIntakeLimitSwitch = new DigitalInput(RobotMap.ballIntakeLimitSwitchPort);
  public final DoubleSolenoid hatchSolenoidRight = new DoubleSolenoid(RobotMap.hatchInNodeId, RobotMap.hatchOutNodeId);
  public final DoubleSolenoid hatchSolenoidLeft = new DoubleSolenoid(RobotMap.hatchInNodeId, RobotMap.hatchOutNodeId);


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

/*
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
    ballIntakeMonitor.start();
  }

  public enum IntakeJointPosition {
    UP, DOWN
  }
  public enum HatchPosition {
    EXTENDED, RETRACTED
  }
}
