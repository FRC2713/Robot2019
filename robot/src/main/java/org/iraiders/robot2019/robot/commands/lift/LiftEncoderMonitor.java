package org.iraiders.robot2019.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.subsystems.LiftSubsystem;

public class LiftEncoderMonitor extends Command {
  private final LiftSubsystem subsystem;
  
  public LiftEncoderMonitor(LiftSubsystem subsystem) {
    this.subsystem = subsystem;
  }
  
  @Override
  protected void execute() {
    if (subsystem.liftTalon.getSensorCollection().isFwdLimitSwitchClosed())
      subsystem.liftTalon.setSelectedSensorPosition(0);
  }
  
  @Override
  protected boolean isFinished() {
    return false;
  }
}
