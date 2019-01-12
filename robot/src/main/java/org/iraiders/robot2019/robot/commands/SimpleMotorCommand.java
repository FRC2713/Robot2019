package org.iraiders.robot2019.robot.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SimpleMotorCommand extends Command {
  private WPI_TalonSRX motor;
  private double speed;
  
  public SimpleMotorCommand(WPI_TalonSRX motor, double speed) {
    this(null, motor, speed);
  }
  
  public SimpleMotorCommand(Subsystem subsystem, WPI_TalonSRX motor, double speed) {
    if (subsystem != null) this.requires(subsystem);
    this.motor = motor;
    this.speed = speed;
  }
  
  @Override
  protected void execute() {
    motor.set(speed);
  }
  
  protected void end() {
    motor.set(0);
  }
  
  @Override
  protected boolean isFinished() {
    return false;
  }
}
