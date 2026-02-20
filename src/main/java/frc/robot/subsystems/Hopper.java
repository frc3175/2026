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

//TODO: Is this using pid or just duty cycle out??? It looks like we configure a pid and then just... don't use it?
public class Hopper extends SubsystemBase {
  
  private final TalonFX m_floormotor;

  public Hopper() {
    this.m_floormotor = new TalonFX(Constants.HopperConstants.HOPPERFLOORMOTORID , Constants.CANIVORE);

    final TalonFXConfiguration floorConfiguration = new TalonFXConfiguration();
    floorConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    floorConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.HopperConstants.HOPPERCURRENTLIMIT);
    floorConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

    var slot0Configs = floorConfiguration.Slot0;

    slot0Configs.kP = Constants.HopperConstants.HOPPER_P;
    slot0Configs.kI = Constants.HopperConstants.HOPPER_I;
    slot0Configs.kD = Constants.HopperConstants.HOPPER_D;

    slot0Configs.kS = Constants.HopperConstants.HOPPER_S;
    slot0Configs.kA = Constants.HopperConstants.HOPPER_A;
    slot0Configs.kV = Constants.HopperConstants.HOPPER_V;

    //this.m_floormotor.getConfigurator().apply(floorConfiguration); TODO: is this being *used*, if not, delete the pid
  }
  
  @Override
  public void periodic() {
    
  }

  public void runFloor(double speed){
    m_floormotor.setControl(new DutyCycleOut(speed));
  }

}




