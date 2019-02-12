package org.iraiders.robot2019.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeJointCommand;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeMonitor;
import org.iraiders.robot2019.robot.commands.intake.hatch.PistonCommand;
import org.iraiders.robot2019.robot.commands.intake.hatch.PlateCommand;

import static org.iraiders.robot2019.robot.RobotMap.*;
import static org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand.BallIntakeState.OUT;
import static org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand.BallIntakeState.STOPPED;
import static org.iraiders.robot2019.robot.subsystems.IntakeSubsystem.IntakeJointPosition.DOWN;
import static org.iraiders.robot2019.robot.subsystems.IntakeSubsystem.IntakeJointPosition.UP;

public class IntakeSubsystem extends Subsystem {
  private final BallIntakeMonitor ballIntakeMonitor = new BallIntakeMonitor(this);
  public final BallIntakeControlCommand ballIntakeControlCommand = new BallIntakeControlCommand(this);
  public final BallIntakeJointCommand ballIntakeJointCommand = new BallIntakeJointCommand(this, UP);
  public final PistonCommand pistonCommand = new PistonCommand(this, PistonCommand.hatchPosition);
  public final PlateCommand plateCommand = new PlateCommand(this, PlateCommand.hatchPosition);

  public WPI_TalonSRX intake = new WPI_TalonSRX(RobotMap.ballIntakeMotorPort);
  public final DoubleSolenoid ballIntakeSolenoid = new DoubleSolenoid(RobotMap.ballIntakeUpNodeId, RobotMap.ballIntakeDownNodeId);
  public final DoubleSolenoid pistonSolenoid = new DoubleSolenoid(RobotMap.hatchInNodeId, RobotMap.hatchOutNodeId);
  public final DoubleSolenoid plateSolenoid = new DoubleSolenoid(RobotMap.hatchInNodeId, RobotMap.hatchOutNodeId);
  public final DigitalInput ballIntakeLimitSwitch = new DigitalInput(RobotMap.ballIntakeLimitSwitchPort);

  public IntakeSubsystem() {
    initControls();
  }

  private void initControls() {
    ballIntakeJointToggleButton.whenPressed(new InstantCommand(() -> this.ballIntakeJointCommand.setIntakeJointPosition(this.ballIntakeJointCommand.getIntakeJointPosition() == UP ? DOWN : UP)));

    ballIntakeMotorOutButton.whenPressed(new InstantCommand(() -> this.ballIntakeControlCommand.setBallIntakeState(OUT)));
    ballIntakeMotorOutButton.whenReleased(new InstantCommand(() -> this.ballIntakeControlCommand.setBallIntakeState(STOPPED)));

    pistonToggleButton.whenPressed(new InstantCommand(() -> this.pistonCommand.setPistonPosition(HatchPosition.EXTENDED)));
    pistonToggleButton.whenReleased(new InstantCommand(()-> this.pistonCommand.setPistonPosition(HatchPosition.RETRACTED)));

    plateToggleButton.whenPressed(new InstantCommand(()-> this.plateCommand.setPlatePosition(HatchPosition.EXTENDED)));
    plateToggleButton.whenReleased(new InstantCommand(()-> this.plateCommand.setPlatePosition(HatchPosition.RETRACTED)));

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
