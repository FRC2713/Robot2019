package org.iraiders.robot2019.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;

public class BallIntakeMonitor extends Command {
  private IntakeSubsystem intakeSubsystem;
  private double firstDetectedTime = 0;

  public BallIntakeMonitor(IntakeSubsystem intakeSubsystem){
    this.intakeSubsystem = intakeSubsystem;
  }

  @Override
  protected void execute() {
    if (intakeSubsystem.ballIntakeLimitSwitch.get() || intakeSubsystem.intakeTalon.getOutputCurrent() >= 100) {
      if (firstDetectedTime == 0) firstDetectedTime = Timer.getFPGATimestamp();

      if (Timer.getFPGATimestamp() - firstDetectedTime >= .5) {
        intakeSubsystem.ballIntakeJointCommand.setIntakeJointPosition(IntakeSubsystem.IntakeJointPosition.UP);
        intakeSubsystem.ballIntakeControlCommand.setBallIntakeState(BallIntakeControlCommand.BallIntakeState.STOPPED);
      }
    } else {
      firstDetectedTime = 0;
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
