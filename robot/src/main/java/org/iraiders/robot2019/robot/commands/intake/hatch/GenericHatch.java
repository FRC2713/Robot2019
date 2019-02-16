package org.iraiders.robot2019.robot.commands.intake.hatch;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;

public class GenericHatch {
  private final IntakeSubsystem intakeSubsystem;
  private IntakeSubsystem.HatchPosition hatchPosition = IntakeSubsystem.HatchPosition.RETRACTED;
  private final DoubleSolenoid solenoid;

  public GenericHatch(IntakeSubsystem intakeSubsystem, DoubleSolenoid solenoid) {
    this.intakeSubsystem = intakeSubsystem;
    this.solenoid = solenoid;
    update();
  }

  private void update() {
    switch(hatchPosition) {
      default:
      case EXTENDED:
        solenoid.set(DoubleSolenoid.Value.kReverse);
        break;

      case RETRACTED:
        solenoid.set(DoubleSolenoid.Value.kForward);
        break;
    }
  }

  public IntakeSubsystem.HatchPosition getPosition() {
    return hatchPosition;
  }

  public boolean setPosition(IntakeSubsystem.HatchPosition hp) {
    if (intakeSubsystem.ballIntakeControlCommand.getBallIntakeState() ==
      BallIntakeControlCommand.BallIntakeState.OUT) {
      return false;
    }
    hatchPosition = hp;
    update();
    return true;
  }
}
