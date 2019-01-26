package org.iraiders.robot2019.robot.commands.intake;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.PIDCommand;
import org.iraiders.robot2019.robot.subsystems.IntakeSubsystem;

public class IntakeJointCommand extends PIDCommand {
  private final WPI_TalonSRX joint;

  public IntakeJointCommand(IntakeSubsystem intakeSubsystem, IntakeSubsystem.IntakePosition position) {
    super(0,0,0);
    joint = intakeSubsystem.grabberIntakeJoint;

    // TODO Properly configure PID targets and ranges
    this.setSetpoint(0);
    this.getPIDController().setContinuous(false);
    this.getPIDController().setInputRange(0,1000);
    this.getPIDController().setPercentTolerance(5);
  }

  @Override
  protected void initialize() {
    joint.configSelectedFeedbackSensor(FeedbackDevice.Analog);
  }

  @Override
  protected double returnPIDInput() {
    return joint.getSelectedSensorPosition();
  }

  @Override
  protected void usePIDOutput(double output) {
    joint.set(output);
  }

  @Override
  protected boolean isFinished() {
    return this.getPIDController().onTarget();
  }
}
