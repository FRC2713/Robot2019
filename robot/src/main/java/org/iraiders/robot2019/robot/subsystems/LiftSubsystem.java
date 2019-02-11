package org.iraiders.robot2019.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.SimpleMotorCommand;

import static org.iraiders.robot2019.robot.RobotMap.leftLiftForwardButton;
import static org.iraiders.robot2019.robot.RobotMap.leftLiftReverseButton;

//Subsystem for elevator arm
public class LiftSubsystem extends Subsystem {
  //establishes motors (upper and lower)
  public final WPI_TalonSRX leftLift = new WPI_TalonSRX(RobotMap.leftLiftTalonPort);
  private final WPI_TalonSRX rightLift = new WPI_TalonSRX(RobotMap.rightLiftTalonPort);

  public LiftSubsystem(){
    //sets right lift as follower of left lift
    rightLift.set(ControlMode.Follower, RobotMap.leftLiftTalonPort);
    initControls();
  }

  private void initControls(){
    leftLiftForwardButton.whileHeld(new SimpleMotorCommand(leftLift,.5));
    leftLiftReverseButton.whileHeld(new SimpleMotorCommand(leftLift,-.5));
  }

  @Override
  protected void initDefaultCommand() {

  }


  public enum LiftPosition {
    STARTING, ROCKET_LOW, ROCKET_MID, ROCKET_HIGH
  }
}
