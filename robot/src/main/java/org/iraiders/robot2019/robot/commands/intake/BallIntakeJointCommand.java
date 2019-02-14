package org.iraiders.robot2019.robot.commands.intake;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;

public class BallIntakeJointCommand extends Command {
  private IntakeSubsystem intakeSubsystem;
  public IntakeSubsystem.IntakeJointPosition position = IntakeSubsystem.IntakeJointPosition.UP;

 public BallIntakeJointCommand(IntakeSubsystem intakeSubsystem, IntakeSubsystem.IntakeJointPosition position){
   this.position = position;
   this.intakeSubsystem = intakeSubsystem;
 }

 private void update() {
   switch(position) {
     default:
     case UP:
       intakeSubsystem.ballIntakeSolenoid.set(DoubleSolenoid.Value.kForward);
       intakeSubsystem.ballIntakeControlCommand.setBallIntakeState(BallIntakeControlCommand.BallIntakeState.STOPPED);
       break;

     case DOWN:
       intakeSubsystem.ballIntakeSolenoid.set(DoubleSolenoid.Value.kReverse);
       intakeSubsystem.ballIntakeControlCommand.setBallIntakeState(BallIntakeControlCommand.BallIntakeState.IN);
       break;
   }
 }

  public IntakeSubsystem.IntakeJointPosition getIntakeJointPosition(){
   return position;
  }

  public boolean setIntakeJointPosition(IntakeSubsystem.IntakeJointPosition ijp){
    if (intakeSubsystem.plateCommand.getPlatePosition() == IntakeSubsystem.HatchPosition.EXTENDED
    || intakeSubsystem.pistonCommand.getPistonPosition() == IntakeSubsystem.HatchPosition.EXTENDED) {
      return false;
    }
    position = ijp;
    update();
    return true;
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
