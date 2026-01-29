// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VelocityDutyCycle;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix6.hardware.CANrange;

public class  Intake extends SubsystemBase {
  


TalonFX m_rackMotor;
TalonFX m_intakeleftMotor;
TalonFX m_intakerightMotor;
VelocityDutyCycle intakeVelocity; 

DutyCycleOut intakePercentOutput;



public Intake() {
    m_rackMotor = new TalonFX(Constants.IntakeConstants.RACKMOTORID , Constants.CANIVORE);
    m_intakeleftMotor = new TalonFX(Constants.IntakeConstants.LEFTMOTORID, Constants.CANIVORE);
    m_intakerightMotor = new TalonFX(Constants.IntakeConstants.RIGHTMOTORID, Constants.CANIVORE);

    //TODO: add motion magic for rack
    //TODO: add neutral mode
     
    final TalonFXConfiguration rackConfiguration = new TalonFXConfiguration();
    rackConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rackConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.CurrentLimit);
    final TalonFXConfiguration rollerConfiguration = new TalonFXConfiguration();
    rollerConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rollerConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.CurrentLimit);
   

    m_rackMotor.getConfigurator().apply(rackConfiguration, 0.050);
    m_intakeleftMotor.getConfigurator().apply(rollerConfiguration, 0.050);
    m_intakerightMotor.getConfigurator().apply(rollerConfiguration, 0.050);

    intakeVelocity = new VelocityDutyCycle(0);

    // periodic, run Motion Magic with slot 0 configs,
  }
  
  @Override
  public void periodic() {
    
    SmartDashboard.putNumber("rackpose", 0);
    SmartDashboard.putNumber("Roller vel", m_intakeleftMotor.getVelocity().getValueAsDouble());
  
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



    

}





/*// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import static edu.wpi.first.units.Units.Rotations; 
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Hopper extends SubsystemBase {
  

MotionMagicVoltage m_motmag;

PositionDutyCycle m_PositionDutyCycle;
TalonFX m_motor;
CANcoder m_canCoder;
double nudge = 0;
double cancoderzero = 0.5;
double cancoderoffset = 0;


public Hopper() {
    m_motor = new TalonFX(Constants.WristConstants.MOTORID , Constants.CANIVORE);
    // m_canCoder = new CANcoder(Constants.WristConstants.CANCODERID, Constants.CANIVORE);

    // cancoderoffset = m_canCoder.getAbsolutePosition().getValueAsDouble() - cancoderoffset;


    

    m_motmag = new MotionMagicVoltage(0);

    var talonFXConfigs = new TalonFXConfiguration();
    
    // var canCoderConfigs = new CANcoderConfiguration();

    var slot0Configs = talonFXConfigs.Slot0;
    

    slot0Configs.kP = 2; // change as needed
    slot0Configs.kI = 0;
    slot0Configs.kD = 0;

    var motionMagicConfigs = talonFXConfigs.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = 160;//160; // 80 rps cruise velocity
    motionMagicConfigs.MotionMagicAcceleration = 600;//240; // 160 rps/s acceleration (0.5 seconds)
    motionMagicConfigs.MotionMagicJerk = 1750;
    
     // 1600 rps/s^2 jerk (0.1 seconds)

    m_motmag.EnableFOC = true;



    // talonFXConfigs.Feedback.FeedbackRemoteSensorID = m_canCoder.getDeviceID();
    // talonFXConfigs.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.SyncCANcoder;
    // talonFXConfigs.Feedback.SensorToMechanismRatio = 1.0;
    // talonFXConfigs.Feedback.RotorToSensorRatio = -60.7639;

    // canCoderConfigs.MagnetSensor.withAbsoluteSensorDiscontinuityPoint(1);
    // canCoderConfigs.MagnetSensor.withSensorDirection(SensorDirectionValue.CounterClockwise_Positive);
    // canCoderConfigs.MagnetSensor.withMagnetOffset(Rotations.of(0.1));

    
    m_motor.getConfigurator().apply(talonFXConfigs, 0.050);
    // m_canCoder.getConfigurator().apply(canCoderConfigs, 0.050);
    m_motor.setNeutralMode(NeutralModeValue.Brake);
    // periodic, run Motion Magic with slot 0 configs,
  }
  
  @Override
  public void periodic() {
    m_motmag.Slot = 0;
    SmartDashboard.putNumber("wrist angle", getpose());
  }

  public Double getpose(){ // if you need negative pos, you need to change this
  return  m_motor.getPosition().getValueAsDouble();

 

  }

  public void setangle(double angle){

    m_motor.setControl(m_motmag.withPosition(angle+nudge));


  }
  public void setnudge(double change){
    nudge = nudge + change;
  }

}




 */