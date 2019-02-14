package org.iraiders.robot2019.robot.commands.intake.hatch;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;

public class PlateCommand extends Command {
  private IntakeSubsystem intakeSubsystem;
  public static IntakeSubsystem.HatchPosition hatchPosition = IntakeSubsystem.HatchPosition.RETRACTED;

  public PlateCommand(IntakeSubsystem intakeSubsystem, IntakeSubsystem.HatchPosition hatchPosition) {
    this.intakeSubsystem = intakeSubsystem;
    this.hatchPosition = hatchPosition;
    update();
  }

  private void update() {
    switch(hatchPosition) {
      default:
      case EXTENDED:
        intakeSubsystem.plateSolenoid.set(DoubleSolenoid.Value.kReverse);
        break;

      case RETRACTED:
        intakeSubsystem.plateSolenoid.set(DoubleSolenoid.Value.kForward);
        break;
    }
  }

  public IntakeSubsystem.HatchPosition getPlatePosition() {
    return hatchPosition;
  }

  public boolean setPlatePosition(IntakeSubsystem.HatchPosition hp) {
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
