package org.iraiders.robot2019.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class OIDrive extends Command {
  private DriveSubsystem driveSubsystem;
  private XboxController xboxController = new XboxController(0);

  public OIDrive(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    requires(driveSubsystem);
  }

  @Override
  protected void execute() {
    driveSubsystem.roboDrive.tankDrive(-xboxController.getY(GenericHID.Hand.kLeft), -xboxController.getY(GenericHID.Hand.kRight));
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
