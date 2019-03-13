package org.iraiders.robot2019.robot.commands.drive;

import edu.wpi.first.hal.sim.mockdata.DriverStationDataJNI;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class VisionLineTrack extends Command {
  DriveSubsystem driveSubsystem;
  LineTrackingCommand lineTrackingCommand;
  VisionDrive visionDrive;
  private byte visionTrackerByte;

  @Override
  protected void  initialize() {
    visionDrive.start();


  }


  @Override
  protected void execute() {
    //if(lineTrackingCommand.lineSensorByte == 7){
    //visionDrive.start();
    if (visionDrive.cv.getEntry("tapeDetected").getBoolean(false) && lineTrackingCommand.lineSensorByte == 7) {
      visionTrackerByte |= 1;

    }

    if (visionDrive.cv.getEntry("tapeDetected").getBoolean(false) == false && lineTrackingCommand.lineSensorByte != 7) {
      visionTrackerByte |= 2;

    }

    if (visionDrive.cv.getEntry("tapeDetected").getBoolean(false) && lineTrackingCommand.lineSensorByte != 7) {
      visionTrackerByte |= 4;

    }

    if (visionDrive.cv.getEntry("tapeDetected").getBoolean(false) == false && lineTrackingCommand.lineSensorByte == 7) {
      visionTrackerByte |= 8;

    }

    switch (visionTrackerByte) {

      case 0:
        break;
      case 1:
        visionDrive.start();
        lineTrackingCommand.end();
      case 2:
        lineTrackingCommand.start();
        visionDrive.isFinished();
      case 3:
        lineTrackingCommand.start();
      default: break;

    }


    }




  protected void end() {
    //
  }

  @Override
  protected boolean isFinished() {
    return false;
  }


  }

