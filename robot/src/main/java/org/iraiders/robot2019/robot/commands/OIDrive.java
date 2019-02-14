package org.iraiders.robot2019.robot.commands;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import org.iraiders.robot2019.robot.OI;
import org.iraiders.robot2019.robot.Robot;
import org.iraiders.robot2019.robot.subsystems.DriveSubsystem;

public class OIDrive extends Command {
  private DriveSubsystem driveSubsystem;
  private boolean useTankInsteadOfBradford = false;
  private double lastLeftStickVal = 0;
  private double lastRightStickVal = 0;
  private XboxController xbox = OI.xBoxController;

  private double joystickChangeLimit;

  Ultrasonic ultra = new Ultrasonic(0, 1);

  AnalogInput ai = new AnalogInput(0);

  public OIDrive(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    requires(driveSubsystem);
  }

  @Override
  protected void initialize() {
    driveSubsystem.roboDrive.setMaxOutput(Robot.prefs.getFloat("OIMaxSpeed", 1));
    joystickChangeLimit = Robot.prefs.getDouble("JoystickChangeLimit", 1f);

    AnalogInput.setGlobalSampleRate(62500);
  }

  @Override
  protected void execute() {
    double measuredLeft;
    double measuredRight;

    if (xbox.getRawButtonPressed(8)) {
      useTankInsteadOfBradford = !useTankInsteadOfBradford;
      lastRightStickVal = 0;
      lastLeftStickVal = 0;
      OI.rumbleController(xbox, .5, 500);
    }

    if (useTankInsteadOfBradford) {
      measuredLeft = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredRight = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      driveSubsystem.roboDrive.tankDrive(measuredLeft, measuredRight, true);
    } else {
      measuredLeft = DriveSubsystem.slewLimit(-xbox.getY(GenericHID.Hand.kLeft), lastLeftStickVal, joystickChangeLimit);
      measuredRight = DriveSubsystem.slewLimit(xbox.getX(GenericHID.Hand.kRight), lastRightStickVal, joystickChangeLimit);
      driveSubsystem.roboDrive.arcadeDrive(measuredLeft, measuredRight, true);
    }
    System.out.printf("Voltage: %f Distance: %f Average Voltage: %f \n",ai.getVoltage(), distanceCalc(ai.getVoltage()), ai.getAverageVoltage());
    System.out.printf("This is the response of the ping %f /n", ultra.getRangeInches());
  }
  private double distanceCalc(double Voltage) {
    // voltage / 2 / 0.00666667 = distance
    return Voltage / 4 / 0.00666667;
  }



  @Override
  protected void end() {
    driveSubsystem.roboDrive.stopMotor();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
