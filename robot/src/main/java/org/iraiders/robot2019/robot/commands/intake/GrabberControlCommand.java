package org.iraiders.robot2019.robot.commands.intake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;

public class GrabberControlCommand extends InstantCommand {
  private IntakeSubsystem intakeSubsystem;
  private IntakeSubsystem.GrabberPosition position;

 public GrabberControlCommand(IntakeSubsystem intakeSubsystem, IntakeSubsystem.GrabberPosition position){
   this.intakeSubsystem = intakeSubsystem;
   this.position = position;
 }

 @Override
 protected void initialize(){
   if (position.equals(IntakeSubsystem.GrabberPosition.OPEN)) {
     //intakeSubsystem.grabberSolenoid.set(DoubleSolenoid.Value.kForward);
   } else if (position.equals(IntakeSubsystem.GrabberPosition.CLOSE)) {
     //intakeSubsystem.grabberSolenoid.set(DoubleSolenoid.Value.kReverse);
   }
 }
}
