package org.iraiders.robot2019.robot.commands.intake.hatch;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;

public class GenericHatch {
  private final IntakeSubsystem intakeSubsystem;
  private IntakeSubsystem.HatchPosition position = IntakeSubsystem.HatchPosition.RETRACTED;
  private final DoubleSolenoid solenoid;

  public GenericHatch(IntakeSubsystem intakeSubsystem, DoubleSolenoid solenoid) {
    this.intakeSubsystem = intakeSubsystem;
    this.solenoid = solenoid;
    update();
  }

  private void update() {
    switch(position) {
      default:
      case EXTENDED:
        solenoid.set(DoubleSolenoid.Value.kReverse);
        break;

      case RETRACTED:
        solenoid.set(DoubleSolenoid.Value.kForward);
        break;
    }

    SmartDashboard.putString(solenoid.getName() + " State", position.name());
  }

  public IntakeSubsystem.HatchPosition getPosition() {
    return position;
  }

  public boolean setPosition(IntakeSubsystem.HatchPosition hp) {
    if (intakeSubsystem.ballIntakeJointCommand.getIntakeJointPosition() == IntakeSubsystem.IntakeJointPosition.DOWN) {
      return false;
    }
    position = hp;
    update();
    return true;
  }
}
