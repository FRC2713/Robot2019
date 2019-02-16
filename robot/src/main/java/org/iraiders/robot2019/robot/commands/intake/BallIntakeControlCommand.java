package org.iraiders.robot2019.robot.commands.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;

public class BallIntakeControlCommand extends Command {
  private IntakeSubsystem intakeSubsystem;
  private BallIntakeState currentState = BallIntakeState.STOPPED;
  private double intakeSpeed = .4;

  public BallIntakeControlCommand(IntakeSubsystem intakeSubsystem){
    this.intakeSubsystem = intakeSubsystem;
  }

  @Override
  protected void execute() {
    switch(currentState) {
      default:
      case STOPPED:
        intakeSubsystem.intakeTalon.set(ControlMode.PercentOutput, 0);
        break;

      case IN:
        intakeSubsystem.intakeTalon.set(ControlMode.PercentOutput, intakeSpeed);
        break;

      case OUT:
        intakeSubsystem.intakeTalon.set(ControlMode.PercentOutput, -intakeSpeed);
        break;
    }
    SmartDashboard.putString("Ball intake state", currentState.name());
  }

  @Override
  protected void end() {
    setBallIntakeState(BallIntakeState.STOPPED);
    execute();
  }

  public boolean setBallIntakeState(BallIntakeState desiredState) {
    currentState = desiredState;
    return true;
  }

  public BallIntakeState getBallIntakeState() {
    return currentState;
  }

  public enum BallIntakeState {
    IN, OUT, STOPPED
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}

