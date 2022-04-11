package frc.robot.subsystems;

import static frc.robot.Constants.*;

//ctre
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
//wpilib
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Flywheel extends SubsystemBase {

  private TalonFX flywheelMotor;
  private double flywheelControllerKP = 0.16;
  private double flywheelControllerKI = 0.001;
  private double flywheelControllerKD = 0;

  public Flywheel() {
    flywheelMotor = new TalonFX(KFlywheelMotorTalon);
    flywheelMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    flywheelMotor.setInverted(true);

    flywheelMotor.config_kP(0, flywheelControllerKP);
    flywheelMotor.config_kI(0, flywheelControllerKI);
    flywheelMotor.config_kD(0, flywheelControllerKD);
    
    SmartDashboard.putBoolean("Flywheel Spinning", false);

    //shuffleboard flywheel pid tuning fields
    // SmartDashboard.putNumber("Flywheel kP", flywheelControllerKP);
    // SmartDashboard.putNumber("Flywheel kI", flywheelControllerKI);
    // SmartDashboard.putNumber("Flywheel kD", flywheelControllerKD);
    
    //shuffleboard flywheel shooting speed tuning fields
    // SmartDashboard.putNumber("95", 1800);
    // SmartDashboard.putNumber("100", 1650);
    // SmartDashboard.putNumber("130", 1850);
    
  }
  
  //requires input in RPM!
  public void move(double RPMOutput) {
    //display on shuffleboard to let the driver know whether flywheel is at desired RPM or not
    if (Math.abs(getVelocity() - RPMOutput) <= 50) {
      SmartDashboard.putBoolean("FlywheelAtRPM", true);
    }
    else {
      SmartDashboard.putBoolean("FlywheelAtRPM", false);
    }

    //convert to encoder unit 
    double rawOutput = RPMOutput * (512.0 / 75.0); 

    if (rawOutput != 0) {
      //Falcons (TalonFX) has built-in encoder and built-in pid for velocity and position, so we just plug and use
      SmartDashboard.putBoolean("Flywheel Spinning", true);
      flywheelMotor.set(ControlMode.Velocity, rawOutput);
    }
    else {
      //when we want the flywheel to stop, cut power and let it coast to stop instead of using pid (pid will apply reverse power to make flywheel stop faster, but requires tuning and we don't really need that)
      SmartDashboard.putBoolean("Flywheel Spinning", false);
      flywheelMotor.set(ControlMode.PercentOutput, 0); 
    }
  }
  
  public void moveRawPercent(double speed) {
    if (speed != 0) {
      SmartDashboard.putBoolean("Flywheel Spinning", true);
    }
    else {
      SmartDashboard.putBoolean("Flywheel Spinning", false);
    }
    flywheelMotor.set(ControlMode.PercentOutput, speed);
  }
  
  public double getVelocity() //max velocity = 19776u / 100ms, which is 2896.875 rotations/min, conversion factor is 600/4096 (75/512)
  {
    double factor = 75.0 / 512.0;
    return flywheelMotor.getSelectedSensorVelocity() * factor;
  }

  public double getRawVelocity() {
    return flywheelMotor.getSelectedSensorVelocity();
  }
  
  public void setFlywheelGains(double kP, double kI, double kD){
      // flywheelController.setPID(kP, kI, kD);
      flywheelMotor.config_kP(0, kP);
      flywheelMotor.config_kI(0, kI);
      flywheelMotor.config_kD(0, kD);
  }

  public double calculateFlywheelSpeedFromDist(double distanceFromHub) {
    double flywheelOutput;
    // if (distanceFromHub > 60) {
    //   // if (distanceFromHub < 95) {
    //   //   flywheelOutput = 1750 + distanceFromHub * 4.077;
    //   //   // flywheelOutput = SmartDashboard.getNumber("95", 1750) + distanceFromHub * 4.077;
    //   // }
    //   if (distanceFromHub < 100) {
    //     flywheelOutput = 1750 + distanceFromHub * 3.5;
    //     // flywheelOutput = SmartDashboard.getNumber("100", 1800) + distanceFromHub * 4.077;
    //   }
    //   else if (distanceFromHub < 130) {
    //     flywheelOutput = 1750 + distanceFromHub * 4.077;
    //     // flywheelOutput = SmartDashboard.getNumber("130", 1750) + distanceFromHub * 4.077;
    //   }
    //   else if (distanceFromHub < 150) {
    //     flywheelOutput = 2200 + distanceFromHub * 4.077;
    //   }
    //   else {
    //     flywheelOutput = 2450 + distanceFromHub * 4.077;
    //   }
    // }
    // else {
    //   flywheelOutput = 1900;
    // }
      
    // polynomial curve from testing: 0.157 x^2 - 19.84 x + 2592.2
    flywheelOutput = 0.157 * Math.pow(distanceFromHub, 2) - 19.84 * distanceFromHub + 2600;

    return flywheelOutput;
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    //used to tune pid using shuffleboard
    // setFlywheelGains(SmartDashboard.getNumber("Flywheel kP", 0.0), 
    //   SmartDashboard.getNumber("Flywheel kI", 0.0), 
    //   SmartDashboard.getNumber("Flywheel kD", 0.0));
    SmartDashboard.putNumber("flywheel RPM", getVelocity());
  }
}
