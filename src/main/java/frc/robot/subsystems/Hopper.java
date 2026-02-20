// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Hopper extends SubsystemBase {
  
  private final TalonFX m_floormotor;

  public Hopper() {
    this.m_floormotor = new TalonFX(Constants.HopperConstants.HOPPERFLOORMOTORID , Constants.CANIVORE);

    final TalonFXConfiguration floorConfiguration = new TalonFXConfiguration();
    floorConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    floorConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.HopperConstants.HOPPERCURRENTLIMIT);
    floorConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

    var slot0Configs = floorConfiguration.Slot0;

   

    this.m_floormotor.getConfigurator().apply(floorConfiguration); 
  }
  
  @Override
  public void periodic() {
    
  }

  public void runFloor(double speed){
    m_floormotor.setControl(new DutyCycleOut(speed));
  }

}




