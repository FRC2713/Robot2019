package org.iraiders.robot2019.robot.subsystems;


import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.Robot;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.SimpleMotorCommand;
import org.iraiders.robot2019.robot.commands.climb.ClimbArmControl;
import org.iraiders.robot2019.robot.commands.climb.ClimbFollowDrive;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kReverse;

public class ClimbSubsystem extends Subsystem {
  public CANSparkMax leftLArm = new CANSparkMax(RobotMap.leftLArmPort, CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightLArm = new CANSparkMax(RobotMap.rightLArmPort, CANSparkMaxLowLevel.MotorType.kBrushless);
  public WPI_TalonSRX climberPistonMotor = new WPI_TalonSRX(RobotMap.climberPistonMotorPort);

  private final DoubleSolenoid climbPiston = OI.getDoubleSolenoid(RobotMap.climbFrontOpenNodeId, RobotMap.climbFrontCloseNodeId);

  public void initTeleop() {
    //DriverStation.reportWarning("Default SparkMax Stall & Free current limit:" + leftLArm.getParameterInt(CANSparkMaxLowLevel.ConfigParameter.kSmartCurrentStallLimit) + " & " + leftLArm.getParameterInt(CANSparkMaxLowLevel.ConfigParameter.kSmartCurrentFreeLimit), false);
    //TalonSRXConfiguration cf = new TalonSRXConfiguration();
    //climberPistonMotor.getAllConfigs(cf);
    //DriverStation.reportWarning("Default Talon Stall limits : " + cf.continuousCurrentLimit + " continuous, " + cf.peakCurrentLimit + " peak (" + cf.peakCurrentDuration + ")", false);
  
    //Robot.initializeSparkDefaults(leftLArm);
    //Robot.initializeSparkDefaults(rightLArm);
    //Robot.initializeTalonDefaults(climberPistonMotor);

    //rightLArm.setInverted(true);
    //rightLArm.follow(leftLArm);
    rightLArm.setIdleMode(CANSparkMax.IdleMode.kBrake);
    leftLArm.setIdleMode(CANSparkMax.IdleMode.kBrake);
    
    RobotMap.climberArmManualDown.whileHeld(new SimpleMotorCommand(leftLArm, .5));
    RobotMap.climberArmManualDown.whileHeld(new SimpleMotorCommand(rightLArm, -.5));
    RobotMap.climberArmManualUp.whileHeld(new SimpleMotorCommand(leftLArm, -.5));
    RobotMap.climberArmManualUp.whileHeld(new SimpleMotorCommand(rightLArm, .5));

    //new ClimbArmControl(this).start();
    ClimbFollowDrive climbFollowDrive = new ClimbFollowDrive(Robot.driveSubsystem, this);

    RobotMap.climberLevelButton.whenPressed(new InstantCommand(() -> this.climbPiston.set(this.climbPiston.get() == kForward ? kReverse : kForward)));
    RobotMap.climberLevelButton.toggleWhenPressed(climbFollowDrive);
  }

  @Override
  protected void initDefaultCommand() {

  }
}
