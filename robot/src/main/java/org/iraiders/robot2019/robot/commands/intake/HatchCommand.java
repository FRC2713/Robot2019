package org.iraiders.robot2019.robot.commands.intake;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;


public class HatchCommand extends Command {
  private IntakeSubsystem intakeSubsystem;
  private IntakeSubsystem.HatchPosition hatchPosition = IntakeSubsystem.HatchPosition.RETRACTED;

  public HatchCommand(IntakeSubsystem intakeSubsystem) {
    this.intakeSubsystem = intakeSubsystem;
    update();
  }

  private void update() {
    switch(hatchPosition) {
      default:
      case EXTENDED:
        intakeSubsystem.hatchSolenoidLeft.set(DoubleSolenoid.Value.kReverse);
        intakeSubsystem.hatchSolenoidRight.set(DoubleSolenoid.Value.kReverse);
        break;

      case RETRACTED:
        intakeSubsystem.hatchSolenoidLeft.set(DoubleSolenoid.Value.kForward);
        intakeSubsystem.hatchSolenoidRight.set(DoubleSolenoid.Value.kForward);
        break;
    }
  }

  public IntakeSubsystem.HatchPosition getHatchPosition() {
    return hatchPosition;
  }

  public boolean setHatchposition(IntakeSubsystem.HatchPosition hp) {
    if (intakeSubsystem.ballIntakeControlCommand.getBallIntakeState() ==
      BallIntakeControlCommand.BallIntakeState.OUT) {
      return false;
    }
    hatchPosition = hp;
    update();
    return true;
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
