package org.iraiders.robot2019.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.RobotMap;
import org.iraiders.robot2019.robot.commands.feedback.EncoderReporter;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeJointCommand;
import org.iraiders.robot2019.robot.commands.intake.BallIntakeMonitor;
import org.iraiders.robot2019.robot.commands.intake.hatch.GenericHatch;

import static org.iraiders.robot2019.robot.RobotMap.*;
import static org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand.BallIntakeState.OUT;
import static org.iraiders.robot2019.robot.commands.intake.BallIntakeControlCommand.BallIntakeState.STOPPED;
import static org.iraiders.robot2019.robot.subsystems.IntakeSubsystem.IntakeJointPosition.DOWN;
import static org.iraiders.robot2019.robot.subsystems.IntakeSubsystem.IntakeJointPosition.UP;

public class IntakeSubsystem extends Subsystem {
  public WPI_TalonSRX intakeTalon = new WPI_TalonSRX(RobotMap.ballIntakeMotorPort);
  public final DoubleSolenoid ballIntakeSolenoid = OI.getDoubleSolenoid(RobotMap.ballIntakeUpNodeId, RobotMap.ballIntakeDownNodeId);
  public final DoubleSolenoid hatchSolenoid = OI.getDoubleSolenoid(RobotMap.hatchInNodeId, RobotMap.hatchOutNodeId);
  public final DoubleSolenoid plateSolenoid = OI.getDoubleSolenoid(RobotMap.plateOpenNodeId, RobotMap.plateCloseNodeId);
  public final DigitalInput ballIntakeLimitSwitch = new DigitalInput(RobotMap.ballIntakeLimitSwitchPort);

  private final BallIntakeMonitor ballIntakeMonitor = new BallIntakeMonitor(this);
  public final BallIntakeControlCommand ballIntakeControlCommand = new BallIntakeControlCommand(this);
  public final BallIntakeJointCommand ballIntakeJointCommand = new BallIntakeJointCommand(this);
  public final GenericHatch plateExtendCommand = new GenericHatch(this, hatchSolenoid);
  public final GenericHatch hatchExtendCommand = new GenericHatch(this, plateSolenoid);

  public IntakeSubsystem() {
    ballIntakeSolenoid.setName("BallIntakeSolenoid");
    hatchSolenoid.setName("HatchExtendSolenoid");
    plateSolenoid.setName("PlateExtendSolenoid");

    initControls();
  }

  private void initControls() {
    ballIntakeJointToggleButton.whenPressed(new InstantCommand(() -> this.ballIntakeJointCommand.setIntakeJointPosition(this.ballIntakeJointCommand.getIntakeJointPosition() == UP ? DOWN : UP)));

    ballIntakeMotorOutButton.whenPressed(new InstantCommand(() -> this.ballIntakeControlCommand.setBallIntakeState(OUT)));
    ballIntakeMotorOutButton.whenReleased(new InstantCommand(() -> this.ballIntakeControlCommand.setBallIntakeState(STOPPED)));

    pistonToggleButton.whenPressed(new InstantCommand(() -> this.plateExtendCommand.setPosition(HatchPosition.EXTENDED)));
    pistonToggleButton.whenReleased(new InstantCommand(()-> this.plateExtendCommand.setPosition(HatchPosition.RETRACTED)));

    plateToggleButton.whenPressed(new InstantCommand(()-> this.hatchExtendCommand.setPosition(HatchPosition.EXTENDED)));
    plateToggleButton.whenReleased(new InstantCommand(()-> this.hatchExtendCommand.setPosition(HatchPosition.RETRACTED)));
  }

  @Override
  protected void initDefaultCommand() {
    ballIntakeControlCommand.start();
    ballIntakeMonitor.start();
    new EncoderReporter(intakeTalon).start();
  }

  public enum IntakeJointPosition {
    UP, DOWN
  }

  public enum HatchPosition {
    EXTENDED, RETRACTED
  }
}
