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
import org.iraiders.robot2019.robot.commands.intake.hatch.PistonCommand;
import org.iraiders.robot2019.robot.commands.intake.hatch.PlateCommand;

import static org.iraiders.robot2019.robot.RobotMap.*;

public class IntakeSubsystem extends Subsystem {
  private final BallIntakeMonitor ballIntakeMonitor = new BallIntakeMonitor(this);
  public final BallIntakeControlCommand ballIntakeControlCommand = new BallIntakeControlCommand(this, BallIntakeControlCommand.ballIntakeState);
  public final BallIntakeJointCommand ballIntakeJointCommand = new BallIntakeJointCommand(this, BallIntakeJointCommand.position);
  public final PistonCommand pistonCommand = new PistonCommand(this, PistonCommand.hatchPosition);
  public final PlateCommand plateCommand = new PlateCommand(this, PlateCommand.hatchPosition);

  public final DoubleSolenoid ballIntakeSolenoid = new DoubleSolenoid(RobotMap.ballIntakeUpNodeId, RobotMap.ballIntakeDownNodeId);
  public final DigitalInput ballIntakeLimitSwitch = new DigitalInput(RobotMap.ballIntakeLimitSwitchPort);
  public final DoubleSolenoid pistonSolenoid = new DoubleSolenoid(RobotMap.hatchInNodeId, RobotMap.hatchOutNodeId);
  public final DoubleSolenoid plateSolenoid = new DoubleSolenoid(RobotMap.hatchInNodeId, RobotMap.hatchOutNodeId);




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
  private void initControls() {
    GenericHID buttonBox = OI.arcadeController;
    ballIntakeMotorInButton.whileHeld(new BallIntakeControlCommand(this, BallIntakeControlCommand.ballIntakeState.IN));
    ballIntakeMotorOutButton.whileHeld(new BallIntakeControlCommand(this,BallIntakeControlCommand.ballIntakeState.OUT));
    ballIntakeJointUpButton.whileHeld(new BallIntakeJointCommand(this, BallIntakeJointCommand.position.UP));
    ballIntakeJointDownButton.whileHeld(new BallIntakeJointCommand(this, BallIntakeJointCommand.position.DOWN));
    pistonOutButton.whileHeld(new PistonCommand(this, pistonCommand.hatchPosition.EXTENDED));
    pistonInButton.whileHeld(new PistonCommand(this, pistonCommand.hatchPosition.RETRACTED));
    plateOutButton.whileHeld(new PistonCommand(this, plateCommand.hatchPosition.EXTENDED));
    plateInButton.whileHeld(new PistonCommand(this, plateCommand.hatchPosition.RETRACTED));

  }

  }
