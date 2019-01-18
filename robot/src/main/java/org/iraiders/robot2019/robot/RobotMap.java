package org.iraiders.robot2019.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // Motors
  public static int leftLiftTalonPort = 1;
  public static int rightLiftTalonPort = 2;
  public static int grabberIntakeLeftTalonPort = 3;
  public static int grabberIntakeRightTalonPort = 4;
  public static int grabberIntakeJointTalonPort = 5;
  public static final int frontLeftTalonPort = 1;
  public static final int backLeftTalonPort = 2;
  public static final int frontRightTalonPort = 3;
  public static final int backRightTalonPort = 4;

  // Pneumatics
  public static int grabberOpenNodeId = 0;
  public static int grabberCloseNodeId = 1;

  // Sensors
  public static int ballIntakeLimitSwitchPort = 1;


}
