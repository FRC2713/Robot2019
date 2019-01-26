package org.iraiders.robot2019.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // PORTS
  // Motors
  public static final int frontLeftTalonPort = 2;
  public static final int backLeftTalonPort = 4;
  public static final int frontRightTalonPort = 1;
  public static final int backRightTalonPort = 3;
  public static final int leftLiftTalonPort = 5;
  public static final int rightLiftTalonPort = 6;
  public static final int grabberIntakeLeftTalonPort = 7;
  public static final int grabberIntakeRightTalonPort = 8;
  public static final int grabberIntakeJointTalonPort = 9;

  // Sensors
  public static int ballIntakeLimitSwitchPort = 1;
}
