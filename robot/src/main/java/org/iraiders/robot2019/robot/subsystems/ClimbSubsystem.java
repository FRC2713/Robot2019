package org.iraiders.robot2019.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.SimpleMotorCommand;
import org.iraiders.robot2019.robot.commands.climb.ClimbingCommand;

public class ClimbSubsystem extends Subsystem {
  private WPI_TalonSRX backWheel = new WPI_TalonSRX(RobotMap.backWheelTalonPort);
  public final DoubleSolenoid pistons = new DoubleSolenoid(RobotMap.pistonOpenNodeId, RobotMap.pistonCloseNodeId);

  private void initControls() {
    GenericHID buttonBox = OI.arcadeController;
    JoystickButton btn8 = new JoystickButton(buttonBox, 8);
    JoystickButton btn9 = new JoystickButton(buttonBox, 9);

    btn8.whileHeld(new ClimbingCommand(this, ClimberLevel.UP));
    btn8.whileHeld(new SimpleMotorCommand(backWheel, .3));
    btn9.whileHeld(new ClimbingCommand(this, ClimberLevel.DOWN));
  }

  @Override
  protected void initDefaultCommand() {

  }

  public enum ClimberLevel {
    UP, DOWN
  }
}
