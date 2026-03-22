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
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Tower extends SubsystemBase {
  
  private TalonFX m_towerMotor;
  private TalonFX m_opposedTowerMotor;

  private VelocityTorqueCurrentFOC towerVelocity = new VelocityTorqueCurrentFOC(0);
  private DutyCycleOut towerPercentOutput = new DutyCycleOut(0);

  public Tower(){

      m_towerMotor = new TalonFX(Constants.TowerConstants.TOWER_ID , Constants.CANIVORE);
      m_opposedTowerMotor = new TalonFX(Constants.TowerConstants.OPPOSITE_TOWER_ID, Constants.CANIVORE);

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
    

      m_towerMotor.getConfigurator().apply(kickConfiguration, 0.050);
      m_opposedTowerMotor.getConfigurator().apply(kickConfiguration, 0.050);
      
      m_opposedTowerMotor.setControl(new Follower(Constants.TowerConstants.TOWER_ID, MotorAlignmentValue.Opposed));

      // periodic, run Motion Magic with slot 0 configs,
  }
    
  @Override
  public void periodic() {}

  public void setTowerVelocity(double velocity){
  
    towerVelocity.Velocity = velocity;
    m_towerMotor.setControl(towerVelocity);
      
  }

  public void setTowerPercentOutput(double percentOutput){

    towerPercentOutput.Output = percentOutput;
    m_towerMotor.setControl(towerPercentOutput);

  }

  public enum TowerState {
    INTAKE(Constants.TowerConstants.INTAKE_TOWER_VELOCITY),
    SHOOT(Constants.TowerConstants.SHOOT_TOWER_VELOCITY),
    CARRY(Constants.TowerConstants.CARRY_TOWER_VELOCITY),
    RESET(Constants.TowerConstants.RESET_TOWER_VELOCITY),
    UNCLOG(Constants.TowerConstants.UNCLOG_TOWER_VELOCITY);

    public double towerVelocity;
    private TowerState(double towerVelocity) {
      this.towerVelocity = towerVelocity;
    }
    
  }

}



    







