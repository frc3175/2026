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
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityDutyCycle;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.CANcoder;

public class Tower extends SubsystemBase {
  


TalonFX m_kickMotor;
TalonFX m_leftRoller;
TalonFX m_rightRoller;
VelocityDutyCycle towerVelocity;



DutyCycleOut intakePercentOutput;






public Tower() {
    m_kickMotor = new TalonFX(Constants.TowerConstants.KICKERID , Constants.CANIVORE);
    m_leftRoller = new TalonFX(Constants.TowerConstants.LEFTROLLERID, Constants.CANIVORE);
    m_rightRoller = new TalonFX(Constants.TowerConstants.RIGHTROLLERID, Constants.CANIVORE);

    
  
     
    final TalonFXConfiguration rackConfiguration = new TalonFXConfiguration();
    rackConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rackConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.CurrentLimit);
    rackConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));
    final TalonFXConfiguration rollerConfiguration = new TalonFXConfiguration();
    rollerConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rollerConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.CurrentLimit);
    rackConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast));

   

    m_kickMotor.getConfigurator().apply(rackConfiguration, 0.050);
    m_leftRoller.getConfigurator().apply(rollerConfiguration, 0.050);
    m_rightRoller.getConfigurator().apply(rollerConfiguration, 0.050);

    towerVelocity = new VelocityDutyCycle(0);

    m_rightRoller.setControl(new Follower(Constants.TowerConstants.LEFTROLLERID, MotorAlignmentValue.Opposed));


   

    

    // periodic, run Motion Magic with slot 0 configs,
  }
  
  @Override
  public void periodic() {
    
    
    
    
  
  }

  public void towerrun(double velocity){
    towerVelocity.Velocity = velocity * 5;
        m_kickMotor.setControl(towerVelocity);
        m_leftRoller.setControl(towerVelocity);
       
  }


   public void outake(double velocity){
    towerVelocity.Velocity = -velocity * 5;
        m_leftRoller.setControl(towerVelocity);
        
  }


  }



    







