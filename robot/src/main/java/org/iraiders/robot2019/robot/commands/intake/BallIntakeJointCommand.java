package org.iraiders.robot2019.robot.commands.intake;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;

public class BallIntakeJointCommand extends InstantCommand {
  private IntakeSubsystem intakeSubsystem;
  private IntakeSubsystem.IntakeJointPosition position;

 public BallIntakeJointCommand(IntakeSubsystem intakeSubsystem, IntakeSubsystem.IntakeJointPosition position){
   this.intakeSubsystem = intakeSubsystem;
   this.position = position;
 }

 @Override
 protected void initialize() {
   switch(position) {
     default:
     case UP:
       intakeSubsystem.ballIntakeSolenoid.set(DoubleSolenoid.Value.kForward);
       break;

     case DOWN:
       // TODO safety check, do NOT put down if hatch intake extended
       intakeSubsystem.ballIntakeSolenoid.set(DoubleSolenoid.Value.kReverse);
       break;
   }
 }
}
