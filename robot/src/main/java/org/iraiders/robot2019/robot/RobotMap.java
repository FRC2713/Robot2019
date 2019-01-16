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
  public static int leftLiftTalonPort = 1;
  public static int rightLiftTalonPort = 2;
  public static int grabberIntakeLeftTalonPort = 3;
  public static int grabberIntakeRightTalonPort = 4;

  // Sensors
  public static int ballIntakeLimitSwitchPort = 1;
}
