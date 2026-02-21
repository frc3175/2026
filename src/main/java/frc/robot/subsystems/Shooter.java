// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.CoastOut;
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

public class Shooter extends SubsystemBase {

  private final TalonFX m_frontLeftShooterMotor;
  private final TalonFX m_frontRightShooterMotor;
  private final TalonFX m_backRightShooterMotor;

  private final CoastOut coastRequest = new CoastOut();

  public  boolean m_running = false;

  private VelocityVoltage shooterVelocityVoltage = new VelocityVoltage(0);
   
  /** Creates a new Shooter. */
  public Shooter() {
    m_frontLeftShooterMotor = new TalonFX(Constants.ShooterConstants.FRONTLEFTMOTORID, Constants.CANIVORE);
    m_frontRightShooterMotor = new TalonFX(Constants.ShooterConstants.FRONTRIGHTMOTORID, Constants.CANIVORE);
    m_backRightShooterMotor = new TalonFX(Constants.ShooterConstants.BACKRIGHTMOTORID, Constants.CANIVORE);

    final TalonFXConfiguration shooterConfig = new TalonFXConfiguration()
      .withMotorOutput(new MotorOutputConfigs()
          .withNeutralMode(NeutralModeValue.Coast)
          .withInverted(InvertedValue.CounterClockwise_Positive))
      .withCurrentLimits(new CurrentLimitsConfigs().withStatorCurrentLimit(Amps.of(Constants.ShooterConstants.SHOOTERCURRENTLIMIT))
      .withStatorCurrentLimitEnable(true));
      
    var slot0Configs = shooterConfig.Slot0;
      
    slot0Configs.kP = Constants.ShooterConstants.SHOOTER_P;
    slot0Configs.kI = Constants.ShooterConstants.SHOOTER_I;
    slot0Configs.kD = Constants.ShooterConstants.SHOOTER_D;

    slot0Configs.kS = Constants.ShooterConstants.SHOOTER_S;
    slot0Configs.kV = Constants.ShooterConstants.SHOOTER_V;
    slot0Configs.kA = Constants.ShooterConstants.SHOOTER_A;

    m_frontLeftShooterMotor.getConfigurator().apply(shooterConfig);
    m_frontRightShooterMotor.getConfigurator().apply(shooterConfig);
    m_backRightShooterMotor.getConfigurator().apply(shooterConfig);
    m_frontRightShooterMotor.setControl(new Follower(Constants.ShooterConstants.FRONTLEFTMOTORID, MotorAlignmentValue.Opposed));
    m_backRightShooterMotor.setControl(new Follower(Constants.ShooterConstants.FRONTLEFTMOTORID, MotorAlignmentValue.Opposed));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("shooter velocity", getShooterVelocity());
    SmartDashboard.putBoolean("shooterruning", shooterVelocityVoltage.Velocity != 0);
  }

  public void setShooterVelocity(double velocity) {
    shooterVelocityVoltage.Velocity = velocity;
    m_frontLeftShooterMotor.setControl(shooterVelocityVoltage);      
  }

  public Command coastShooter() {
    return runOnce(() -> m_frontLeftShooterMotor.setControl(coastRequest));
  }

  public double getShooterVelocity() {
    return m_frontLeftShooterMotor.getVelocity().getValueAsDouble();
  }

}
