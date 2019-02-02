package org.iraiders.robot2019.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.LaserRangeFinder;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.commands.lift.LiftControlCommand;

//Subsystem for elevator arm
public class LiftSubsystem extends Subsystem {
  //establishes motors (upper and lower)

/**  public final WPI_TalonSRX leftLift = new WPI_TalonSRX(RobotMap.leftLiftTalonPort);
  private final WPI_TalonSRX rightLift = new WPI_TalonSRX(RobotMap.rightLiftTalonPort);
**/
  public final LaserRangeFinder laserRange = new LaserRangeFinder(0x29);
  public final DigitalInput digitalInput = new DigitalInput(6);

  public LiftControlCommand liftControlCommand;

  public LiftSubsystem(){
    //sets right lift as follower of left lift
    //rightLift.set(ControlMode.Follower, RobotMap.leftLiftTalonPort);
    initControls();
  }

  private void initControls(){
    GenericHID buttonBox = OI.arcadeController;

    JoystickButton btn1 = new JoystickButton(buttonBox, 1);
    JoystickButton btn2 = new JoystickButton(buttonBox, 2);

  //  btn1.whileHeld(new SimpleMotorCommand(leftLift,.5));
   // btn2.whileHeld(new SimpleMotorCommand(leftLift,-.5));
  }
  public void initTeleop() {
    liftControlCommand = new LiftControlCommand(this, LiftPosition.STARTING);
    liftControlCommand.start();
  }

  @Override
  protected void initDefaultCommand() {

  }


  public enum LiftPosition {
    STARTING, ROCKET_LOW, ROCKET_MID, ROCKET_HIGH
  }
}
