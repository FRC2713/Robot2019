package org.iraiders.robot2019.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;

public class LineSensorControl extends Command {
  private OIDrive driveCommand;

  public LineSensorControl(OIDrive driveCommand) {
    this.driveCommand = driveCommand;
  }

  @Override
  protected void initialize() {
    driveCommand.isLineSensing = true;
  }

  @Override
  protected void end() {
    driveCommand.isLineSensing = false;
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
