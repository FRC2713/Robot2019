package org.iraiders.robot2019.robot;


import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iraiders.robot2019.robot.commands.ExampleCommand;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;
import org.iraiders.robot2019.robot.subsystems.LiftSubsystem;

public class Robot extends TimedRobot {
  public static DriveSubsystem driveSubsystem;
  public static LiftSubsystem liftSubsystem;
  public static IntakeSubsystem intakeSubsystem;
  public static Preferences prefs = Preferences.getInstance();

  public static OI m_oi;
  public static final Compressor compressor = new Compressor();
  
  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  
  @Override
  public void robotInit() {
    initCamera();

    m_oi = new OI();
    driveSubsystem = new DriveSubsystem();
    liftSubsystem = new LiftSubsystem();
    intakeSubsystem = new IntakeSubsystem();
    m_chooser.setDefaultOption("Default Auto", new ExampleCommand());
    // chooser.addOption("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", m_chooser);

    compressor.start();
  }

  private void initCamera() {
    CameraServer cs = CameraServer.getInstance();
    UsbCamera u = cs.startAutomaticCapture();
    u.setResolution(640, 480);
    u.setFPS(10);
  }


  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }
  
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }
  
  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    driveSubsystem.initTeleop();
  }
  
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

  }

  @Override
  public void testPeriodic() {
  }
}
