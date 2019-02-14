package org.iraiders.robot2019.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // Motors
  public static final int frontLeftTalonPort = 2;
  public static final int backLeftTalonPort = 4;
  public static final int frontRightTalonPort = 1;
  public static final int backRightTalonPort = 3;
  public static final int leftLiftTalonPort = 5;
  public static final int rightLiftTalonPort = 6;
  public static final int backWheelTalonPort =  7;
  public static final int ballIntakeMotorPort = 8;

  // Pneumatics
  public static int ballIntakeUpNodeId = 0;
  public static int ballIntakeDownNodeId = 1;
  public static int hatchInNodeId = 2;
  public static int hatchOutNodeId = 3;
  public static int pistonCloseNodeId = 4;
  public static int pistonOpenNodeId = 5;

  // Sensors
  public static int ballIntakeLimitSwitchPort = 1;

  // Button Box
  public static int leftLiftForward = 1;
  public static int leftLiftReverse = 2;
  public static int climberLevelUp = 3;
  public static int climberLevelDown = 4;
  // Xbox Buttons
  public static int ballIntakeMotorIn = 7;
  public static int ballIntakeMotorOut = 8;
  public static int ballIntakeJointUp = 5;
  public static int ballIntakeJointDown = 6;
  public static int pistonOut = 9;
  public static int pistonIn = 10;
  public static int plateOut = 11;
  public static int plateIn = 12;


}
