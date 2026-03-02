// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
public class Intake extends SubsystemBase {
  
  TalonFX m_rackMotor;
  TalonFX m_intakeLeftMotor;
  TalonFX m_intakeRightMotor;

  DutyCycleOut intakePercentOutput;
  DutyCycleOut intakeRackOutput;

  public Intake() {

    m_rackMotor = new TalonFX(Constants.IntakeConstants.RACKMOTORID , Constants.CANIVORE);
    m_intakeLeftMotor = new TalonFX(Constants.IntakeConstants.LEFTMOTORID, Constants.CANIVORE);
    m_intakeRightMotor = new TalonFX(Constants.IntakeConstants.RIGHTMOTORID, Constants.CANIVORE);
     
    final TalonFXConfiguration rackConfiguration = new TalonFXConfiguration();
    rackConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.RACKCURRENTLIMIT);
    rackConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rackConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

    final TalonFXConfiguration rollerConfiguration = new TalonFXConfiguration();
    rollerConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.ROLLERCURRENTLIMIT);
    rollerConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rackConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast));

   

    m_rackMotor.getConfigurator().apply(rackConfiguration, 0.050);
    m_intakeLeftMotor.getConfigurator().apply(rollerConfiguration, 0.050);
    m_intakeRightMotor.getConfigurator().apply(rollerConfiguration, 0.050);

    intakePercentOutput = new DutyCycleOut(0);
    intakeRackOutput = new DutyCycleOut(0);

    m_intakeRightMotor.setControl(new Follower(Constants.IntakeConstants.LEFTMOTORID, MotorAlignmentValue.Opposed));
    // periodic, run Motion Magic with slot 0 configs,
  }
  
  @Override
  public void periodic() {
    SmartDashboard.putNumber("rackpose", 0);
    SmartDashboard.putNumber("Roller vel", m_intakeLeftMotor.getVelocity().getValueAsDouble());
  }

  public void runIntake(double percentOutput){
    intakePercentOutput.Output = percentOutput * 5;
        m_intakeLeftMotor.setControl(intakePercentOutput);
  }

 public void moverack(double vel){
  m_rackMotor.setControl(new VelocityVoltage(vel));
 }

  public void stopRack(){
    intakeRackOutput.Output = Constants.IntakeConstants.RACKHOLD;
    m_rackMotor.setControl(intakeRackOutput);
  }

  public double getRackStatorCurrent() {
    return m_rackMotor.getStatorCurrent().getValueAsDouble();
  }

  public void extendintake(){
    m_rackMotor.setControl(new PositionDutyCycle(Constants.IntakeConstants.RACKMAX));
  }

  public void retractintake(){
    m_rackMotor.setControl(new PositionDutyCycle(Constants.IntakeConstants.RACKHOME));
  }

}





