package org.iraiders.robot2019.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class enableLineSensors extends Command {
  private OIDrive driveCommand;
  public enableLineSensors (OIDrive driveCommand) {
    this.driveCommand = driveCommand;
  }
  @Override
  protected void execute() {
    driveCommand.isLineSensing = true;
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
