package org.iraiders.robot2019.robot.commands.lift;

import edu.wpi.first.wpilibj.command.PIDCommand;
import org.iraiders.robot2019.robot.subsystems.LiftSubsystem;

import static org.iraiders.robot2019.robot.Robot.liftSubsystem;

public class LiftControlCommand extends PIDCommand {
  //private final WPI_TalonSRX motor;

  public LiftControlCommand(LiftSubsystem liftSubsystem, LiftSubsystem.LiftPosition position) {
    super(0,0,0);
   // motor = liftSubsystem.leftLift;

  /**  // TODO Properly configure PID targets and ranges
    this.setSetpoint(0);
    this.getPIDController().setContinuous(false);
    this.getPIDController().setInputRange(0,1000);
    this.getPIDController().setPercentTolerance(5);
   **/
  }


  @Override
  protected void initialize() {
    liftSubsystem.laserRange.init();
    //liftSubsystem.laserRange.startRangeFinding();
    System.out.println("Bradford works");
//  motor.configSelectedFeedbackSensor(FeedbackDevice.Analog);

  }

  @Override
  protected void execute () {

    System.out.printf("Lift Range: %d \n", liftSubsystem.laserRange.getDistance());
    //liftSubsystem.laserRange.stopRangeFinding();
    //System.out.printf("Channel 6 of DIO: %b \n", liftSubsystem.digitalInput.get());
  }

  @Override
  protected double returnPIDInput() {
  //  return motor.getSelectedSensorPosition();
    return 0;
  }

  @Override
  protected void usePIDOutput(double output) {
  //  motor.set(output);
  }

  @Override
   protected boolean isFinished() {
     // return this.getPIDController().onTarget();
    return false;
   }

}
