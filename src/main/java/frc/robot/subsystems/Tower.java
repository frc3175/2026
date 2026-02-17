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
import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.TowerRun;


public class Tower extends SubsystemBase {
  


TalonFX m_kickMotor;
 TalonFX m_leftRoller;
// TalonFX m_rightRoller;
 VelocityVoltage towerVelocity;
TalonFX m_oppsedkick;



DutyCycleOut intakePercentOutput;






public Tower(){
    m_kickMotor = new TalonFX(Constants.TowerConstants.KICKERID , Constants.CANIVORE);
    m_leftRoller = new TalonFX(Constants.TowerConstants.LEFTROLLERID, Constants.CANIVORE);
    // m_rightRoller = new TalonFX(Constants.TowerConstants.RIGHTROLLERID, Constants.CANIVORE);
    m_oppsedkick = new TalonFX(Constants.TowerConstants.OPKICKID, Constants.CANIVORE);

    
  
     
    final TalonFXConfiguration kickConfiguration = new TalonFXConfiguration();
    kickConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    kickConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.CurrentLimit);
    kickConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));
    kickConfiguration.Slot0.kP = Constants.TowerConstants.TOWER_P;
    kickConfiguration.Slot0.kI = Constants.TowerConstants.TOWER_I;
    kickConfiguration.Slot0.kD = Constants.TowerConstants.TOWER_D;
    kickConfiguration.Slot0.kS = Constants.TowerConstants.TOWER_S;
    kickConfiguration.Slot0.kA = Constants.TowerConstants.TOWER_A;
    kickConfiguration.Slot0.kV = Constants.TowerConstants.TOWER_V;

    final TalonFXConfiguration rollerConfiguration = new TalonFXConfiguration();
    rollerConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rollerConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.CurrentLimit);
    kickConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast));

   

    m_kickMotor.getConfigurator().apply(kickConfiguration, 0.050);
     m_leftRoller.getConfigurator().apply(rollerConfiguration, 0.050);
    // m_rightRoller.getConfigurator().apply(rollerConfiguration, 0.050);

    towerVelocity = new VelocityVoltage(0);
    //m_rightRoller.setControl(new Follower(Constants.TowerConstants.LEFTROLLERID, MotorAlignmentValue.Opposed));
    m_oppsedkick.setControl(new Follower(Constants.TowerConstants.KICKERID, MotorAlignmentValue.Opposed));


   

    

    // periodic, run Motion Magic with slot 0 configs,
  }
  
  @Override
  public void periodic() {
    
    
    
    
    
  
  }

  public void towerrun(double velocity){
   // if(Constants.Buttons.SHOOT.getAsBoolean()){
    towerVelocity.Velocity = velocity;
        m_kickMotor.setControl(towerVelocity);
        m_leftRoller.setControl(towerVelocity);
    //}
    //else{
     
    //}
      
    }
       
  


   

  }



    







