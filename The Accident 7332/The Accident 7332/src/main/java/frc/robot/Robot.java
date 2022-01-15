// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  private final PWMVictorSPX m_leftFront = new PWMVictorSPX(0);
  private final PWMVictorSPX m_leftBack = new PWMVictorSPX(1);
  private final PWMVictorSPX m_rightFront = new PWMVictorSPX(2);
  private final PWMVictorSPX m_rightBack = new PWMVictorSPX(3);
  private final SpeedControllerGroup leftside = new SpeedControllerGroup(m_leftFront, m_leftBack);
  private final SpeedControllerGroup rightside = new SpeedControllerGroup(m_rightFront, m_rightBack);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(leftside, rightside);
  private final Joystick m_stick = new Joystick(0);
  private final Timer m_timer = new Timer();

  private double[][] instructions1 = {
    // {xSpeed, zRotation, seconds}
    // xSpeed: -1.0 forward --> +1.0 backward
    // zRotation: -1.0 right --> +1.0 left
    {-0.5, 0.0, 2.0}
  };

  private int currentInstruction;

  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture();
  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getX());
  }

  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
    // start at first instruction
    currentInstruction = 0;
  }

  @Override
  public void autonomousPeriodic() {
    
    double[][] instructionSet = instructions1; // change to corresponding challenge number: Barrel Racing Path = 1, Slalom Path = 2, Bounce Path = 3.
    double instructionXSpeed = 0.0;
    double instructionZRotation = 0.0;
    double instructionSeconds = 1.0;
    if (currentInstruction < instructionSet.length) {
     instructionXSpeed = instructionSet[currentInstruction][0]; // set x speed
     instructionZRotation = instructionSet[currentInstruction][1]; // set z rotation
     instructionSeconds = instructionSet[currentInstruction][2]; // set time for instruction
   }

   if (m_timer.get() < instructionSeconds) {
     m_robotDrive.arcadeDrive(instructionXSpeed, instructionZRotation);
   } else {
     m_timer.reset();
     currentInstruction++;
   }
 }
}
