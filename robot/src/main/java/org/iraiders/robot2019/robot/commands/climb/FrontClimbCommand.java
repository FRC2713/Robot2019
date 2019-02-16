package org.iraiders.robot2019.robot.commands.climb;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.iraiders.robot2019.robot.subsystems.ClimbSubsystem;

public class FrontClimbCommand extends InstantCommand {
  public final ClimbSubsystem.ClimberLevel climberPosition;
  private ClimbSubsystem climbSubsystem;

  public FrontClimbCommand(ClimbSubsystem climbSubsystem, ClimbSubsystem.ClimberLevel position) {
    this.climbSubsystem = climbSubsystem;
    this.climberPosition = position;
    requires(climbSubsystem);
  }

  protected void initialize() {
    if(climberPosition.equals(ClimbSubsystem.ClimberLevel.UP)) {
      climbSubsystem.frontPistons.set(DoubleSolenoid.Value.kForward);
    } else if (climberPosition.equals(ClimbSubsystem.ClimberLevel.DOWN)) {
      climbSubsystem.frontPistons.set(DoubleSolenoid.Value.kReverse);
    }
  }

}
