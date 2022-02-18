// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.RelativeEncoder;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hang extends SubsystemBase {
  /** Creates a new Hang. */
  
  private TalonFX leftSwingMotor;
  private TalonFX rightSwingMotor;
  private TalonSRX levelHangMotor;
  private Servo rightClawServo;
  private Servo leftClawServo;
  private Servo ratchetServo;

  private DigitalInput bottomLiftLimitSwitch;
  private DigitalInput topLiftLimitSwitch;
  private DigitalInput armLimitSwitch;

  // private double ki, kp, kd;

  public Hang() {
    // hangController = new PIDController(kp, ki, kd);
    leftSwingMotor = new TalonFX(KSwingHangMotor);
    rightSwingMotor = new TalonFX(KSwingHangMotor);
    levelHangMotor = new TalonSRX(KLevelHangMotor);

    leftSwingMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    rightSwingMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    levelHangMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

    rightClawServo = new Servo(KRightClawServo);
    leftClawServo = new Servo(KLeftClawServo);

    bottomLiftLimitSwitch = new DigitalInput(KLiftBottomLimit);
    topLiftLimitSwitch = new DigitalInput(KLiftTopLimit);
    armLimitSwitch = new DigitalInput(KArmsLimit);
  }
  public void move(double swingMotorSpeed, double levelMotorSpeed){
    leftSwingMotor.set(ControlMode.PercentOutput, swingMotorSpeed);
    rightSwingMotor.set(ControlMode.PercentOutput, swingMotorSpeed);
    levelHangMotor.set(ControlMode.PercentOutput, levelMotorSpeed);
  }
  public void moveToPosition(double armPosition, double levelPosition){
    leftSwingMotor.set(ControlMode.Position, armPosition);
    rightSwingMotor.set(ControlMode.Position, armPosition);
    levelHangMotor.set(ControlMode.Position, levelPosition);
  }
  public void moveArmsToPosition(double position) {
    leftSwingMotor.set(ControlMode.Position, position);
    rightSwingMotor.set(ControlMode.Position, position);
  }
  public void moveLevelToPosition(double position) {
    levelHangMotor.set(ControlMode.PercentOutput, position);
  }
  // public void moveArms(double swingMotorSpeed) {
  //   leftSwingMotor.set(ControlMode.PercentOutput, swingMotorSpeed);
  //   rightSwingMotor.set(ControlMode.PercentOutput, swingMotorSpeed);
  // }
  // public void moveLift(double levelMotorSpeed) {
  //   levelHangMotor.set(ControlMode.PercentOutput, levelMotorSpeed);
  // }
  public void moveClaw(double pos) {
    leftClawServo.set(pos);
    rightClawServo.set(pos);
  }
  public void moveRachet(double pos) {
    ratchetServo.set(pos);
  }

  public boolean getBottomLiftLimitSwitch() {
    return bottomLiftLimitSwitch.get();
  }
  public boolean getTopLiftLimitSwitch() {
    return topLiftLimitSwitch.get();
  }
  public boolean getArmsLimitSwitch() {
    return armLimitSwitch.get();
  }

  public double getLeftArmEncoder() {
    return leftSwingMotor.getSelectedSensorPosition();
  }
  public double getRightArmEncoder() {
    return rightSwingMotor.getSelectedSensorPosition();
  }
  public double getLevelEncoder() {
    return levelHangMotor.getSelectedSensorPosition();
  }
  // public void setFlywheelGains(double kP, double kI, double kD){
  //   hangController.setPID(kp, ki, kd);
  // }

  public void resetArmEncders() {
    leftSwingMotor.setSelectedSensorPosition(0);
    rightSwingMotor.setSelectedSensorPosition(0);
  }
  public void resetLevelEncoder() {
    levelHangMotor.setSelectedSensorPosition(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
