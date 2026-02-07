// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RotationsPerSecond;


import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.CoastOut;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.setshootvel;

public class Shooter extends SubsystemBase {

  private final TalonFX m_leftmotor = new TalonFX(Constants.ShooterConstants.LEFTMOTORID, Constants.CANIVORE);
  private final TalonFX m_rightmotor = new TalonFX(Constants.ShooterConstants.RIGHTMOTORID, Constants.CANIVORE);
  
  private final CoastOut coastreq = new CoastOut();

  public  boolean m_running = false;

  private final VelocityVoltage leftsetpointreq = new VelocityVoltage(0);

  private static final TalonFXConfiguration intialconfig = new TalonFXConfiguration()
    .withMotorOutput(
      new MotorOutputConfigs()
        .withNeutralMode(NeutralModeValue.Coast)
    ).withCurrentLimits(new CurrentLimitsConfigs().withStatorCurrentLimit(Amps.of(Constants.ShooterConstants.CurrentLimit))
    .withStatorCurrentLimitEnable(true));

    public final TalonFXConfiguration shooterconfig = intialconfig.clone()
    .withMotorOutput(intialconfig.MotorOutput.clone().withInverted(InvertedValue.CounterClockwise_Positive))
    .withFeedback(intialconfig.Feedback.clone().withSensorToMechanismRatio(1))
    .withSlot0(intialconfig.Slot0.clone()
    .withKP(0.8)
    .withKI(0)
    .withKD(0)
    .withKS(0)
    .withKV(0.12)
    .withKA(0));
  /** Creates a new Shooter. */
  public Shooter() {
    
    
    m_rightmotor.setControl(new Follower(Constants.ShooterConstants.LEFTMOTORID, MotorAlignmentValue.Opposed));

    setDefaultCommand(new setshootvel(this, Constants.ShooterConstants.SPINSPEED));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("shooter velocity", getVelocity());
    SmartDashboard.putBoolean("shooterruning", m_running);

    
  
  }

  public void setshootvel(double velocity){
    if(Constants.Buttons.SPINUP.getAsBoolean()){
      m_leftmotor.setControl(new DutyCycleOut(velocity));
      m_running = true;      
    }
    else{
      m_running = false;
      m_leftmotor.setControl(new DutyCycleOut(0));
    }
  }

  public Command coastshooter(){
    return runOnce(() -> m_leftmotor.setControl(coastreq));
  }

  public double getVelocity(){
    return m_leftmotor.getVelocity().getValueAsDouble();
  }
}
