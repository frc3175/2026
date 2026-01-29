// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityDutyCycle;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.CANcoder;

public class  Intake extends SubsystemBase {
  


TalonFX m_rackMotor;
TalonFX m_intakeleftMotor;
TalonFX m_intakerightMotor;
VelocityDutyCycle intakeVelocity;



DutyCycleOut intakePercentOutput;

MotionMagicVoltage m_motmag;

PositionDutyCycle m_PositionDutyCycle;
CANcoder m_canCoder;
double cancoderzero = 0.5;
double cancoderoffset = 0;




public Intake() {
    m_rackMotor = new TalonFX(Constants.IntakeConstants.RACKMOTORID , Constants.CANIVORE);
    m_intakeleftMotor = new TalonFX(Constants.IntakeConstants.LEFTMOTORID, Constants.CANIVORE);
    m_intakerightMotor = new TalonFX(Constants.IntakeConstants.RIGHTMOTORID, Constants.CANIVORE);

    
  
     
    final TalonFXConfiguration rackConfiguration = new TalonFXConfiguration();
    rackConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rackConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.CurrentLimit);
    rackConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));
    final TalonFXConfiguration rollerConfiguration = new TalonFXConfiguration();
    rollerConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rollerConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.CurrentLimit);
    rackConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast));

   

    m_rackMotor.getConfigurator().apply(rackConfiguration, 0.050);
    m_intakeleftMotor.getConfigurator().apply(rollerConfiguration, 0.050);
    m_intakerightMotor.getConfigurator().apply(rollerConfiguration, 0.050);

    intakeVelocity = new VelocityDutyCycle(0);


    m_motmag = new MotionMagicVoltage(0);

    configure();

    

    // periodic, run Motion Magic with slot 0 configs,
  }
  
  @Override
  public void periodic() {
    
    SmartDashboard.putNumber("rackpose", 0);
    SmartDashboard.putNumber("Roller vel", m_intakeleftMotor.getVelocity().getValueAsDouble());
    
    m_motmag.Slot = 0;
  
  }

  public void intakerun(double velocity){
    intakeVelocity.Velocity = velocity * 5;
        m_intakeleftMotor.setControl(intakeVelocity);
        m_intakerightMotor.setControl(intakeVelocity);
  }


   public void outake(double velocity){
    intakeVelocity.Velocity = -velocity * 5;
        m_intakeleftMotor.setControl(intakeVelocity);
        m_intakerightMotor.setControl(intakeVelocity);
  }

public void setpos(double postion){

    m_rackMotor.setControl(m_motmag.withPosition(postion));


  }

  public void extendintake(){
    m_rackMotor.setControl(m_motmag.withPosition(Constants.IntakeConstants.RACKMAX));
  }

   public void retractintake(){
    m_rackMotor.setControl(m_motmag.withPosition(Constants.IntakeConstants.RACKHOME));
  }







  public void configure(){
    var talonFXConfigs = new TalonFXConfiguration();
    
    // var canCoderConfigs = new CANcoderConfiguration();

    var slot0Configs = talonFXConfigs.Slot0;
    

    slot0Configs.kP = 0.2; // change as needed
    slot0Configs.kI = 0;
    slot0Configs.kD = 0;

    var motionMagicConfigs = talonFXConfigs.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = 160;//160; // 80 rps cruise velocity
    motionMagicConfigs.MotionMagicAcceleration = 600;//240; // 160 rps/s acceleration (0.5 seconds)
    motionMagicConfigs.MotionMagicJerk = 1750;
    
     // 1600 rps/s^2 jerk (0.1 seconds)

    m_motmag.EnableFOC = true;

  }



    

}





