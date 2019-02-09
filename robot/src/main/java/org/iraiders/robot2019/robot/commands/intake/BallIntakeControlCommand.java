package org.iraiders.robot2019.robot.commands.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.RobotMap;

public class BallIntakeControlCommand extends Command {
  private BallIntakeState currentState = BallIntakeState.STOPPED;
  private WPI_TalonSRX intake = new WPI_TalonSRX(RobotMap.ballIntakeMotorPort);
  private double intakeSpeed = .4;

  @Override
  protected void execute() {
    switch(currentState) {
      default:
      case STOPPED:
        intake.set(ControlMode.PercentOutput, 0);
        break;

      case IN:
        intake.set(ControlMode.PercentOutput, intakeSpeed);
        break;

      case OUT:
        intake.set(ControlMode.PercentOutput, -intakeSpeed);
        break;
    }
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

