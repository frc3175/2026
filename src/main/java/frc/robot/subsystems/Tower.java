// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Tower extends SubsystemBase {
  
  TalonFX m_kickMotor;
  VelocityVoltage towerVelocity;
  TalonFX m_opposedKicker;

  public Tower(){

      m_kickMotor = new TalonFX(Constants.TowerConstants.KICKERID , Constants.CANIVORE);
      m_opposedKicker = new TalonFX(Constants.TowerConstants.OPKICKID, Constants.CANIVORE);

      final TalonFXConfiguration kickConfiguration = new TalonFXConfiguration();
      kickConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
      kickConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.TowerConstants.TOWERCURRENTLIMIT);
      kickConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast));

      var slot0Configs = kickConfiguration.Slot0;

      slot0Configs.kP = Constants.TowerConstants.TOWER_P;
      slot0Configs.kI = Constants.TowerConstants.TOWER_I;
      slot0Configs.kD = Constants.TowerConstants.TOWER_D;

      slot0Configs.kS = Constants.TowerConstants.TOWER_S;
      slot0Configs.kV = Constants.TowerConstants.TOWER_V;
      slot0Configs.kA = Constants.TowerConstants.TOWER_A;
    

      m_kickMotor.getConfigurator().apply(kickConfiguration, 0.050);
      m_opposedKicker.getConfigurator().apply(kickConfiguration, 0.050);
     
      
      towerVelocity = new VelocityVoltage(0);
      
      m_opposedKicker.setControl(new Follower(Constants.TowerConstants.KICKERID, MotorAlignmentValue.Opposed));

      // periodic, run Motion Magic with slot 0 configs,
  }
    
  @Override
  public void periodic() {
    
  }

  public void towerrun(double velocity){
  
    towerVelocity.Velocity = velocity;
        m_kickMotor.setControl(towerVelocity);
      
  }
}



    







