package org.iraiders.robot2019.robot.commands.feedback;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;


public class LineTrackingReporter extends Command {
  private DriveSubsystem driveSubsystem;
  public LineTrackingReporter(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
  }

  @Override
  protected void execute() {
    SmartDashboard.putBoolean("LeftLine", driveSubsystem.leftLine.getValue());
    SmartDashboard.putBoolean("midLine", driveSubsystem.midLine.getValue());
    SmartDashboard.putBoolean("rightLine", driveSubsystem.rightLine.getValue());
  }

  @Override
  protected boolean isFinished() {
     return false;
  }
}
