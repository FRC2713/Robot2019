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
  public static final int backWheelTalonPort =  11;
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
//buttonbox
  public static int leftLiftForward = 1;
  public static int LeftLiftReverse = 2;
  public static int climberLeverUp = 3;
  public static int climberLevelDown = 4;
  //Xbox buttons
  public static int ballIntakeMotorIn = 5;
  public static int ballIntakeMotorOut = 6;
  public static int ballIntakeJointUp = 7;
  public static int ballIntakeJointDown = 8;
  public static int pistonOut = 9;
  public static int pistonIn = 10;
  public static int plateOut = 11;
  public static int plateIn = 12;



 static GenericHID buttonBox = OI.arcadeController;
 static XboxController XboxController =  OI.xBoxController;

  public static JoystickButton leftLiftForwardButton = new JoystickButton(buttonBox, 1);
  public static JoystickButton LeftLiftReverseButton = new JoystickButton(buttonBox, 2);
  public static JoystickButton climberLevelUpButton = new JoystickButton(buttonBox, 3);
  public static JoystickButton climberLevelDownButton = new JoystickButton(buttonBox, 4);
  public static JoystickButton ballIntakeMotorInButton = new JoystickButton(buttonBox, 5);
  public static JoystickButton ballIntakeMotorOutButton = new JoystickButton(buttonBox, 6);
  public static  JoystickButton ballIntakeJointUpButton = new JoystickButton(XboxController, 7);
  public static JoystickButton ballIntakeJointDownButton = new JoystickButton(XboxController, 8);
  public static JoystickButton pistonOutButton = new JoystickButton(XboxController, 9);
  public static JoystickButton pistonInButton = new JoystickButton(XboxController, 10);
  public static JoystickButton plateOutButton = new JoystickButton(XboxController, 11);
  public static JoystickButton plateInButton = new JoystickButton(XboxController, 12);








