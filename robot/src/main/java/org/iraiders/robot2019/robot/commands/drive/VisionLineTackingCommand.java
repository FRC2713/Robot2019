package org.iraiders.robot2019.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class VisionLineTackingCommand extends Command {
  private DriveSubsystem driveSubsystem;
  private LineTrackingCommand lineTrackingCommand;
  private VisionDrive visionDrive;
  private OIDrive oiDrive;

  public VisionLineTackingCommand(DriveSubsystem driveSubsystem, LineTrackingCommand linetracking, VisionDrive visiondrive) {
    this.driveSubsystem = driveSubsystem;
    this.lineTrackingCommand = linetracking;
    this.visionDrive = visiondrive;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    visionDrive.start();
   if(visionDrive.isFinished()) {
     if(!driveSubsystem.leftLine.get()
       || !driveSubsystem.midLine.get()
       || !driveSubsystem.rightLine.get()) {
       lineTrackingCommand.start();

     }
   }

  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
