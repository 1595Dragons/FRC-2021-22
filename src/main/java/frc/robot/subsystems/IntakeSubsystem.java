// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {

  CANSparkMax intakeMotor, indexerMotor;
  DoubleSolenoid intakeSolenoid;
  DigitalInput intakeSensor, ballOneSensor, ballTwoSensor;
  DeployState intakeDeployState;

  public enum DeployState {
    DEPLOYING,
    DEPLOYED,
    STOWED
  }
  public IntakeSubsystem() {
    intakeMotor = new CANSparkMax(Constants.intakeMotorID, MotorType.kBrushless);
    intakeSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.intakeSolenoidIn, Constants.intakeSolenoidOut);
    intakeSolenoid.set(Value.kReverse);
    indexerMotor = new CANSparkMax(Constants.indexerMotorID, MotorType.kBrushless);
    intakeSensor = new DigitalInput(Constants.intakeSensorID);
    ballOneSensor = new DigitalInput(Constants.ballOneSensorID);
    ballTwoSensor = new DigitalInput(Constants.ballTwoSensorID);
  }

  public void intakeForward(double intakeSpeed) {
    intakeMotor.set(intakeSpeed);
    intakeSolenoid.set(Value.kForward);
    intakeDeployState = DeployState.DEPLOYED;
    pullInBall();
  }

  public void intakeBackward(double intakeSpeed) {
    intakeMotor.set(intakeSpeed);
    intakeSolenoid.set(Value.kForward);
    intakeDeployState = DeployState.DEPLOYED;
  }

  public void intakeUp() {
    intakeMotor.set(0);
    intakeSolenoid.set(Value.kReverse);
    intakeDeployState = DeployState.STOWED;
    pullInBall();
  }

  public void stopIndexerMotor() {
    indexerMotor.set(0);
  }
  public void indexBallsForward(double indexSpeed) {
    indexerMotor.set(indexSpeed);
  }

  public void indexBallsBack(double indexSpeed) {
    indexerMotor.set(indexSpeed);
  }

  public void ejectBallsToShoot() {
    indexerMotor.set(Constants.indexSpeedForward);
  }

  public void ejectWrongBallOut() {
    indexerMotor.set(Constants.indexSpeedBack);
    intakeMotor.set(Constants.intakeBack);
  }

  public DeployState intakeDeployState() {
    return intakeDeployState;
  }

  public boolean pullInBall() {
    boolean exitNow = false;
    while (exitNow == false) {
      // Create off conditions
      if (ballOneSensor.get() && ballTwoSensor.get()) {
        stopIndexerMotor();
        exitNow = true;
      }
      else if (!intakeSensor.get()  && !ballOneSensor.get() && !ballTwoSensor.get()) {
        stopIndexerMotor();
      }
      else if (!intakeSensor.get() && !ballOneSensor.get() && ballTwoSensor.get()) {
        indexBallsBack(Constants.indexSpeedBack);
      }
      else if (!intakeSensor.get() && ballOneSensor.get() && !ballTwoSensor.get()) {
        stopIndexerMotor();
      }
      else if (intakeSensor.get() && !ballOneSensor.get() && !ballTwoSensor.get()) {
        indexBallsForward(Constants.indexSpeedForward);
      }
      else if (intakeSensor.get() && ballOneSensor.get() && !ballTwoSensor.get()) {
        indexBallsForward(Constants.indexSpeedForward);
      }
    }
    
    return true;
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
