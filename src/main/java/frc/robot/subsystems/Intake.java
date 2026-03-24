// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
//import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicTorqueCurrentFOC;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  
  private TalonFX m_pivotMotor;
  private TalonFX m_intakeLeftMotor;
  private TalonFX m_intakeRightMotor;
  //private CANcoder m_pivotEncoder;

  private PositionTorqueCurrentFOC pivotMotionMagic = new PositionTorqueCurrentFOC(0);
  private VelocityTorqueCurrentFOC intakeVelocity = new VelocityTorqueCurrentFOC(0);
  private DutyCycleOut intakePercentOutput = new DutyCycleOut(0);
  private DutyCycleOut intakePivotPercentOutput = new DutyCycleOut(0) ;

  public Intake() {

    m_pivotMotor = new TalonFX(Constants.IntakeConstants.RACKMOTORID , Constants.CANIVORE);
    m_intakeLeftMotor = new TalonFX(Constants.IntakeConstants.LEFTMOTORID, Constants.CANIVORE);
    m_intakeRightMotor = new TalonFX(Constants.IntakeConstants.RIGHTMOTORID, Constants.CANIVORE);
    //m_pivotEncoder = new CANcoder(Constants.IntakeConstants.PIVOTENCODERID, Constants.CANIVORE);
     
    final TalonFXConfiguration pivotConfiguration = new TalonFXConfiguration();
    pivotConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.PIVOTCURRENTLIMIT);
    pivotConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    pivotConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

    pivotConfiguration.Slot0.kP = Constants.IntakeConstants.PIVOT_P;
    pivotConfiguration.Slot0.kI = Constants.IntakeConstants.PIVOT_I;
    pivotConfiguration.Slot0.kD = Constants.IntakeConstants.PIVOT_D;
    pivotConfiguration.Slot0.kS = Constants.IntakeConstants.PIVOT_S;
    pivotConfiguration.Slot0.kV = Constants.IntakeConstants.PIVOT_V;
    pivotConfiguration.Slot0.kG = Constants.IntakeConstants.PIVOT_G;

    final TalonFXConfiguration rollerConfiguration = new TalonFXConfiguration();

    rollerConfiguration.Slot0.kP = Constants.IntakeConstants.ROLLER_P;
    rollerConfiguration.Slot0.kI = Constants.IntakeConstants.ROLLER_I;
    rollerConfiguration.Slot0.kD = Constants.IntakeConstants.ROLLER_D;

    rollerConfiguration.Slot0.kV = Constants.IntakeConstants.ROLLER_V;
    rollerConfiguration.Slot0.kS = Constants.IntakeConstants.ROLLER_S;

    
    rollerConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.ROLLERCURRENTLIMIT);
    rollerConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);
    rollerConfiguration.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

    m_pivotMotor.getConfigurator().apply(pivotConfiguration, 0.050);
    m_intakeLeftMotor.getConfigurator().apply(rollerConfiguration, 0.050);
    m_intakeRightMotor.getConfigurator().apply(rollerConfiguration, 0.050);

    m_intakeRightMotor.setControl(new Follower(Constants.IntakeConstants.LEFTMOTORID, MotorAlignmentValue.Opposed));

  }
  
  @Override
  public void periodic() {

    SmartDashboard.putNumber("Pivot pose", getPivotPose());
    SmartDashboard.putNumber("Roller vel", m_intakeLeftMotor.getVelocity().getValueAsDouble());
  }

  public void setIntakePercentOutput(double percentOutput) {

    intakePercentOutput.Output = percentOutput;
    m_intakeLeftMotor.setControl(intakePercentOutput);

  }

  public void setIntakeVelocity(double velocity) {

    intakeVelocity.Velocity = velocity;
    m_intakeLeftMotor.setControl(intakeVelocity);

  }

  public void setIntakePivotPercentOutput(double percentOutput) {

    intakePivotPercentOutput.Output = percentOutput;
    m_pivotMotor.setControl(intakePivotPercentOutput);

  }

  public void setIntakePivotPose(double position) {

    pivotMotionMagic.withPosition(position);
    m_pivotMotor.setControl(pivotMotionMagic);

  }

  public double getPivotPose() {

    return m_pivotMotor.getPosition().getValueAsDouble();

  }

  public enum IntakeState {
    INTAKE(Constants.IntakeConstants.INTAKE_ROLLER_VELOCITY, Constants.IntakeConstants.INTAKE_PIVOT_POSITION),
    SPINUP(Constants.IntakeConstants.SPINUP_ROLLER_VELOCITY, Constants.IntakeConstants.SPINUP_PIVOT_POSIITON),
    SHOOT(Constants.IntakeConstants.SHOOT_ROLLER_VELOCITY, Constants.IntakeConstants.SHOOT_PIVOT_POSITION),
    CARRY(Constants.IntakeConstants.CARRY_ROLLER_VELOCITY, Constants.IntakeConstants.CARRY_PIVOT_POSITION),
    RESET(Constants.IntakeConstants.STOP, Constants.IntakeConstants.RESET_PIVOT_POSITION),
    UNCLOG(Constants.IntakeConstants.UNCLOG_ROLLER_VELOCITY, Constants.IntakeConstants.UNCLOG_PIVOT_POSITION);

    public double intakeVelocity;
    public double pivotPosition;
    private IntakeState(double intakeVelocity, double pivotPosition) {
      this.intakeVelocity = intakeVelocity;
      this.pivotPosition = pivotPosition;
    }
  }

  

}





