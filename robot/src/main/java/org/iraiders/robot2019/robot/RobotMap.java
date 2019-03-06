package org.iraiders.robot2019.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // Motors
  public static final int frontLeftTalonPort = 2;
  public static final int backLeftTalonPort = 1;
  public static final int frontRightTalonPort = 3;
  public static final int backRightTalonPort = 4;
  public static final int liftOneTalonPort = 5;
  public static final int liftTwoTalonPort = 6;
  public static final int liftThreeTalonPort = 7;
  public static final int liftFourTalonPort = 9;
  public static final int backWheelTalonPort = 11;
  public static final int ballIntakeMotorPort = 10;

  // Pneumatics
  // PCM1
  public static int ballIntakeUpNodeId = 0;
  public static int ballIntakeDownNodeId = 1;
  public static int hatchInNodeId = 2;
  public static int hatchOutNodeId = 3;
  public static int plateCloseNodeId = 4;
  public static int plateOpenNodeId = 5;
  
  // PCM2
  public static int climbFrontOpenNodeId = 1 + 7; // PCM2, Slot 0
  public static int climbFrontCloseNodeId = 2 + 7;
  public static int climbBackOpenNodeId = 3 + 7;
  public static int climbBackCloseNodeId = 4 + 7;


  // Sensors

  public static int ballIntakeLimitSwitchPort = 1;
  public static int leftLineSensorPort = 2;
  public static int midLineSensorPort = 3;
  public static int rightLineSensorPort = 4;
  public static int ultraSonicPing = 5;
  public static int ultraSonicEcho = 6;

  // Buttons
  private static GenericHID buttonBox = OI.arcadeController;
  private static XboxController xboxController = OI.xBoxController;
  private static Joystick joystick = OI.leftAttack;

  //public static JoystickButton liftUpButton = new JoystickButton(buttonBox, 7);
  //public static JoystickButton liftDownButton = new JoystickButton(buttonBox, 3);
  public static JoystickButton climberLevelUpButton = new JoystickButton(joystick, 5);
  public static JoystickButton climberLevelDownButton = new JoystickButton(joystick, 4);

  public static JoystickButton ballIntakeMotorOutButton = new JoystickButton(joystick, 1);
  public static JoystickButton ballIntakeJointToggleButton = new JoystickButton(joystick, 3);

  public static JoystickButton hatchToggleButton = new JoystickButton(joystick, 6);
  public static JoystickButton plateToggleButton = new JoystickButton(joystick, 7);
}
