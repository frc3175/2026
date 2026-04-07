// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Hopper extends SubsystemBase {
  
  private TalonFX m_hopperMotor;
  private TalonFXS m_rightsauce;
  private TalonFXS m_leftsauce;

  private DutyCycleOut hopperPercentOutput = new DutyCycleOut(0);
  private VelocityTorqueCurrentFOC hopperVelocity = new VelocityTorqueCurrentFOC(0);

  public Hopper() {

    m_hopperMotor = new TalonFX(Constants.HopperConstants.HOPPERFLOORMOTORID , Constants.CANIVORE);
    m_leftsauce = new TalonFXS(Constants.HopperConstants.LEFTSAUCEID);
    m_rightsauce = new TalonFXS(Constants.HopperConstants.RIGHTSAUCEID);
    

    final TalonFXConfiguration floorConfiguration = new TalonFXConfiguration();
    floorConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    floorConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.HopperConstants.HOPPERCURRENTLIMIT);
    floorConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

    final TalonFXSConfiguration sauceConfiguration = new TalonFXSConfiguration();
    sauceConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    sauceConfiguration.CurrentLimits.withStatorCurrentLimit(40.0);
    sauceConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast));

    


    
    var slot0Configs = floorConfiguration.Slot0;
    
    slot0Configs.kP = Constants.HopperConstants.HOPPER_P;
    slot0Configs.kI = Constants.HopperConstants.HOPPER_I;
    slot0Configs.kD = Constants.HopperConstants.HOPPER_D;

    slot0Configs.kS = Constants.HopperConstants.HOPPER_S;
    slot0Configs.kV = Constants.HopperConstants.HOPPER_V;
    slot0Configs.kA = Constants.HopperConstants.HOPPER_A;

    var sauceslot0Configs = sauceConfiguration.Slot0;

    sauceslot0Configs.kP = 0.8;
    sauceslot0Configs.kI = 0;
    sauceslot0Configs.kD = 0;
    sauceslot0Configs.kV = 0.12;
    sauceslot0Configs.kA = 0;
    sauceslot0Configs.kS = 0; 
    

    m_hopperMotor.getConfigurator().apply(floorConfiguration); 
    m_leftsauce.getConfigurator().apply(sauceConfiguration);
    m_rightsauce.getConfigurator().apply(sauceConfiguration);

    m_rightsauce.setControl(new Follower(Constants.HopperConstants.LEFTSAUCEID, MotorAlignmentValue.Opposed));

  }
  
  @Override
  public void periodic() {}

  public void setHopperPercentOutput (double percentOutput) {

    hopperPercentOutput.Output = percentOutput;
    m_hopperMotor.setControl(hopperPercentOutput);
    m_leftsauce.setControl(hopperPercentOutput);

  }

  public void setHopperVelocity(double velocity) {

    hopperVelocity.Velocity = velocity;
    m_hopperMotor.setControl(hopperVelocity);
    m_leftsauce.setControl(hopperVelocity);

  }

  public enum HopperState {
    INTAKE(Constants.HopperConstants.INTAKE_HOPPER_VELOCITY),
    SPINUP(Constants.HopperConstants.SPINUP_HOPPER_VELOCITY),
    SHOOT(Constants.HopperConstants.SHOOT_HOPPER_VELOCITY),
    CARRY(Constants.HopperConstants.CARRY_HOPPER_VELOCITY),
    RESET(Constants.HopperConstants.RESET_HOPPER_VELOCITY),
    UNCLOG(Constants.HopperConstants.UNCLOG_HOPPER_VELOCITY);

    public double hopperVelocity;
    private HopperState(double hopperVelocity) {
      this.hopperVelocity = hopperVelocity;
    }

  }

}





