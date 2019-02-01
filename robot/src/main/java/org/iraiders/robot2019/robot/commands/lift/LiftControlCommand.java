package org.iraiders.robot2019.robot.commands.lift;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.PIDCommand;
import org.iraiders.robot2019.robot.subsystems.LiftSubsystem;

import static org.iraiders.robot2019.robot.Robot.liftSubsystem;

public class LiftControlCommand extends PIDCommand {
  private final WPI_TalonSRX motor;

  public LiftControlCommand(LiftSubsystem liftSubsystem, LiftSubsystem.LiftPosition position) {
    super(0,0,0);
    motor = liftSubsystem.leftLift;

    // TODO Properly configure PID targets and ranges
    this.setSetpoint(0);
    this.getPIDController().setContinuous(false);
    this.getPIDController().setInputRange(0,1000);
    this.getPIDController().setPercentTolerance(5);
  }

  @Override
  protected void initialize() {
    motor.configSelectedFeedbackSensor(FeedbackDevice.Analog);

    liftSubsystem.laserRange.startRangeFinding();
    System.out.printf("Lift Range: %d \n", liftSubsystem.laserRange.getDistance());
    liftSubsystem.laserRange.stopRangeFinding();


  }

  @Override
  protected double returnPIDInput() {
    return motor.getSelectedSensorPosition();
  }

  @Override
  protected void usePIDOutput(double output) {
    motor.set(output);
  }

  @Override
  protected boolean isFinished() {
    return this.getPIDController().onTarget();
  }
}
