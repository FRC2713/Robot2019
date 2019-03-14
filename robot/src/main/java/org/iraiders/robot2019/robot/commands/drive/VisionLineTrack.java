package org.iraiders.robot2019.robot.commands.drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class VisionLineTrack extends Command {
  private DriveSubsystem driveSubsystem;
  private byte visionTrackerByte;

  public VisionLineTrack(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
  }

  @Override
  protected void initialize() {
    driveSubsystem.visionDrive.start();
    driveSubsystem.lineTracking.start();
  }

  @Override
  protected void execute() {
    boolean tapeDetected = driveSubsystem.visionDrive.cv.getEntry("tapeDetected").getBoolean(false);
    byte lineSensorByte = driveSubsystem.lineTracking.lineSensorByte;

    // Tape detected & Line Sensors all detected or all not detected
    if (tapeDetected && (lineSensorByte == 7 || lineSensorByte == 0) ) {
      visionTrackerByte |= 1;
    }

    //Tape not detected & 1 or 2 Line Sensors detected
    if (!tapeDetected && !(lineSensorByte == 7 || lineSensorByte == 0)) {
      visionTrackerByte |= 2;
    }

    //Tape detected & 1 or 2 Line Sensors detected
    if (tapeDetected && !(lineSensorByte == 7 || lineSensorByte == 0)) {
      visionTrackerByte |= 4;
    }

    //Tape & Line Sensors not detected
    if (!driveSubsystem.visionDrive.cv.getEntry("tapeDetected").getBoolean(false) && driveSubsystem.lineTracking.lineSensorByte == 7) {
      visionTrackerByte |= 0;
    }

    switch (visionTrackerByte) {
      default:
      case 0:
        DriverStation.reportWarning("Smart align requested but no sensors are ready", false);
        break;
      case 1:
        driveSubsystem.lineTracking.cancel();
        driveSubsystem.visionDrive.start();
        break;
      case 2:
        driveSubsystem.visionDrive.cancel();
        driveSubsystem.lineTracking.start();
        break;
      case 4:
        driveSubsystem.lineTracking.start();
        break;
    }
  }

  @Override
  protected void end() {
    driveSubsystem.visionDrive.cancel();
    driveSubsystem.lineTracking.cancel();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}

