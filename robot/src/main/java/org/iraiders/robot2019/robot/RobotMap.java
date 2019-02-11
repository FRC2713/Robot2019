package org.iraiders.robot2019.robot;

import edu.wpi.first.wpilibj.GenericHID;
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
  public static final int backLeftTalonPort = 4;
  public static final int frontRightTalonPort = 1;
  public static final int backRightTalonPort = 3;
  public static final int leftLiftTalonPort = 5;
  public static final int rightLiftTalonPort = 6;
  public static final int backWheelTalonPort = 11;
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

  // Buttons
  private static GenericHID buttonBox = OI.arcadeController;
  private static XboxController xboxController = OI.xBoxController;

  public static JoystickButton leftLiftForwardButton = new JoystickButton(buttonBox, 1);
  public static JoystickButton leftLiftReverseButton = new JoystickButton(buttonBox, 2);
  public static JoystickButton climberLevelUpButton = new JoystickButton(buttonBox, 3);
  public static JoystickButton climberLevelDownButton = new JoystickButton(buttonBox, 4);
  public static JoystickButton ballIntakeMotorInButton = new JoystickButton(buttonBox, 5);
  public static JoystickButton ballIntakeMotorOutButton = new JoystickButton(buttonBox, 6);
  public static JoystickButton ballIntakeJointUpButton = new JoystickButton(xboxController, 1);
  public static JoystickButton ballIntakeJointDownButton = new JoystickButton(xboxController, 2);
  public static JoystickButton pistonOutButton = new JoystickButton(xboxController, 3);
  public static JoystickButton pistonInButton = new JoystickButton(xboxController, 4);
  public static JoystickButton plateOutButton = new JoystickButton(xboxController, 5);
  public static JoystickButton plateInButton = new JoystickButton(xboxController, 6);
}









