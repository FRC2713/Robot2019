package org.iraiders.robot2019.robot.commands.climb;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.iraiders.robot2019.robot.subsystems.ClimbSubsystem;

public class BackClimbCommand extends InstantCommand {
  private final ClimbSubsystem.ClimberLevel climberPosition;
  private ClimbSubsystem climbSubsystem;

  public BackClimbCommand(ClimbSubsystem climbSubsystem, ClimbSubsystem.ClimberLevel position) {
    this.climbSubsystem = climbSubsystem;
    this.climberPosition = position;
    requires(climbSubsystem);
  }

  protected void initialize() {
    if(climberPosition.equals(ClimbSubsystem.ClimberLevel.UP)) {
      climbSubsystem.backPistons.set(DoubleSolenoid.Value.kForward);
    } else if (climberPosition.equals(ClimbSubsystem.ClimberLevel.DOWN)) {
      climbSubsystem.frontPistons.set(DoubleSolenoid.Value.kReverse);
    }
  }

}
