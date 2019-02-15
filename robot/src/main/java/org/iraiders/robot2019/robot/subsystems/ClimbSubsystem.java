package org.iraiders.robot2019.robot.subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.SimpleMotorCommand;
import org.iraiders.robot2019.robot.commands.climb.BackClimbCommand;
import org.iraiders.robot2019.robot.commands.climb.FrontClimbCommand;

import static org.iraiders.robot2019.robot.RobotMap.climberLevelDownButton;
import static org.iraiders.robot2019.robot.RobotMap.climberLevelUpButton;

public class ClimbSubsystem extends Subsystem {
  private WPI_TalonSRX backWheel = new WPI_TalonSRX(RobotMap.backWheelTalonPort);

  public final DoubleSolenoid frontPistons = new DoubleSolenoid(RobotMap.climbFrontOpenNodeId, RobotMap.climbFrontCloseNodeId);
  public final DoubleSolenoid backPistons = new DoubleSolenoid(RobotMap.climbBackOpenNodeId, RobotMap.climbBackCloseNodeId);


  public ClimbSubsystem() {
    initControls();
  }

  private void initControls() {
    GenericHID buttonBox = OI.arcadeController;

    climberLevelUpButton.whileHeld(new FrontClimbCommand(this, ClimberLevel.UP));
    climberLevelUpButton.whileHeld(new BackClimbCommand(this, ClimberLevel.UP));
    climberLevelUpButton.whileHeld(new SimpleMotorCommand(backWheel, .3));

    climberLevelDownButton.whileHeld(new FrontClimbCommand(this, ClimberLevel.DOWN));
    climberLevelDownButton.whileHeld(new BackClimbCommand(this, ClimberLevel.DOWN));
  }

  @Override
  protected void initDefaultCommand() {

  }

  public enum ClimberLevel {
    UP, DOWN

  }
}
