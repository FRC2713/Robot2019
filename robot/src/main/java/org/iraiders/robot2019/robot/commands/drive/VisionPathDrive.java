package org.iraiders.robot2019.robot.commands.drive;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class VisionPathDrive extends Command {
  private DriveSubsystem driveSubsystem;
  public NetworkTable vt = NetworkTableInstance.getDefault().getTable("ChickenVision");
  
  public boolean tapeDetected;
  
  public VisionPathDrive(DriveSubsystem driveSubsystem){
    this.driveSubsystem = driveSubsystem;
    requires(driveSubsystem);
  }
  
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  
  }
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double speed;
    double turn;
    boolean targetFound = vt.getEntry("tapeDetected").getBoolean(false);
    double distance = vt.getEntry("distance").getDouble(-1);
    //speed = ;
    
    //this.driveSubsystem.roboDrive.arcadeDrive(speed,turn);
  }
  
  // Called once after isFinished returns true
  @Override
  protected void end() {
  
  }
  
  @Override
  protected boolean isFinished() {
    return false;
  }
}
