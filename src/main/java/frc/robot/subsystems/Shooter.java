// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.CoastOut;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

  private final TalonFX m_leftmotor = new TalonFX(Constants.ShooterConstants.LEFTMOTORID, Constants.CANIVORE);
  private final TalonFX m_rightmotor = new TalonFX(Constants.ShooterConstants.RIGHTMOTORID, Constants.CANIVORE);
  
  private final CoastOut coastreq = new CoastOut();

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
    //TODO: add tower code 
    for(int i = 0; i < 2; ++i){
      var status = m_leftmotor.getConfigurator().apply(shooterconfig);
      if(status.isOK()) break;
    }

    setDefaultCommand(coastshooter());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("shooter velocity", getVelocity());
  
  }

  public Command SetTarget(double velocity){
    return run(() -> { 
      leftsetpointreq.withVelocity(RotationsPerSecond.of(velocity));
      m_leftmotor.setControl(leftsetpointreq);
    });
  }

  public Command coastshooter(){
    return runOnce(() -> m_leftmotor.setControl(coastreq));
  }

  public double getVelocity(){
    return m_leftmotor.getVelocity().getValueAsDouble();
  }
}
