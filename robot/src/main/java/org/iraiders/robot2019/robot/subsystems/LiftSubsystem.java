package org.iraiders.robot2019.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.Robot;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.lift.LiftJoystickControl;

//Subsystem for elevator arm
public class LiftSubsystem extends Subsystem {
  //establishes motors (upper and lower)
  public final WPI_TalonSRX liftTalon = new WPI_TalonSRX(RobotMap.liftOneTalonPort);
  private final WPI_TalonSRX liftTalonTwo = new WPI_TalonSRX(RobotMap.liftTwoTalonPort);
  private final WPI_TalonSRX liftTalonThree = new WPI_TalonSRX(RobotMap.liftThreeTalonPort);
  private final WPI_TalonSRX liftTalonFour = new WPI_TalonSRX(RobotMap.liftFourTalonPort);
  
  private final LiftJoystickControl liftJoystickControl = new LiftJoystickControl(this);

  public LiftSubsystem() {
    Robot.initializeTalonDefaults(liftTalon, liftTalonTwo, liftTalonThree, liftTalonFour);
    liftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    liftTalonTwo.set(ControlMode.Follower, RobotMap.liftOneTalonPort);
    liftTalonThree.set(ControlMode.Follower, RobotMap.liftOneTalonPort);
    liftTalonFour.set(ControlMode.Follower, RobotMap.liftOneTalonPort);
    
    liftTalon.setName("LiftMaster");
  }

  public void initControls(){
    //liftUpButton.whileHeld(new SimpleMotorCommand(liftTalon,-.25));
    //liftDownButton.whileHeld(new SimpleMotorCommand(liftTalon,.25));
  }

  public void initTeleop() {
    liftJoystickControl.start();
    //new EncoderReporter(FeedbackDevice.CTRE_MagEncoder_Relative, liftTalon).start();
    //new LaserArduinoInterface().start();
  }


  @Override
  protected void initDefaultCommand() {
  }


  public enum LiftPosition {
    STARTING, ROCKET_LOW, ROCKET_MID, ROCKET_HIGH
  }
}
