package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

  private TalonFX m_pivotMotor;
  private TalonFX m_intakeLeftMotor;
  private TalonFX m_intakeRightMotor;
  private CANcoder m_pivotEncoder;

  private PositionDutyCycle pivotPosition = new PositionDutyCycle(0);
  private DutyCycleOut intakePercentOutput = new DutyCycleOut(0);
  private DutyCycleOut intakePivotPercentOutput = new DutyCycleOut(0);

  public Intake() {

    m_pivotMotor = new TalonFX(Constants.IntakeConstants.RACKMOTORID, Constants.CANIVORE);
    m_intakeLeftMotor = new TalonFX(Constants.IntakeConstants.LEFTMOTORID, Constants.CANIVORE);
    m_intakeRightMotor = new TalonFX(Constants.IntakeConstants.RIGHTMOTORID, Constants.CANIVORE);
    m_pivotEncoder = new CANcoder(Constants.IntakeConstants.PIVOTENCODERID, Constants.CANIVORE);

    // ================= CANcoder Config =================
    CANcoderConfiguration cancoderConfig = new CANcoderConfiguration();
    cancoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
    cancoderConfig.MagnetSensor.MagnetOffset = Constants.IntakeConstants.PIVOTENCODEROFFSET; // tune later
    m_pivotEncoder.getConfigurator().apply(cancoderConfig);

    // ================= Pivot Motor Config =================
    TalonFXConfiguration pivotConfiguration = new TalonFXConfiguration();

    pivotConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.PIVOTCURRENTLIMIT);
    pivotConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);

    pivotConfiguration.withMotorOutput(
        new MotorOutputConfigs()
            .withNeutralMode(NeutralModeValue.Brake)
            .withInverted(InvertedValue.Clockwise_Positive)
    );

    pivotConfiguration.Slot0.kP = Constants.IntakeConstants.PIVOT_P;
    pivotConfiguration.Slot0.kI = Constants.IntakeConstants.PIVOT_I;
    pivotConfiguration.Slot0.kD = Constants.IntakeConstants.PIVOT_D;
    pivotConfiguration.Slot0.kS = Constants.IntakeConstants.PIVOT_S;
    pivotConfiguration.Slot0.kV = Constants.IntakeConstants.PIVOT_V;
    pivotConfiguration.Slot0.kG = Constants.IntakeConstants.PIVOT_G;
    pivotConfiguration.Slot0.kA = Constants.IntakeConstants.PIVOT_A;
    pivotConfiguration.Slot0.GravityType = GravityTypeValue.Arm_Cosine;

    // Use CANcoder as feedback
    pivotConfiguration.Feedback.FeedbackRemoteSensorID =
        Constants.IntakeConstants.PIVOTENCODERID;
    pivotConfiguration.Feedback.FeedbackSensorSource =
        FeedbackSensorSourceValue.RemoteCANcoder;

    // Gear ratio (motor rotations per mechanism rotation)
    pivotConfiguration.Feedback.RotorToSensorRatio = 64.516;
    pivotConfiguration.Feedback.SensorToMechanismRatio = 1.0;

    m_pivotMotor.getConfigurator().apply(pivotConfiguration, 0.050);

    // ================= Roller Config =================
    TalonFXConfiguration rollerConfiguration = new TalonFXConfiguration();

    rollerConfiguration.Slot0.kP = Constants.IntakeConstants.ROLLER_P;
    rollerConfiguration.Slot0.kI = Constants.IntakeConstants.ROLLER_I;
    rollerConfiguration.Slot0.kD = Constants.IntakeConstants.ROLLER_D;

    rollerConfiguration.Slot0.kV = Constants.IntakeConstants.ROLLER_V;
    rollerConfiguration.Slot0.kS = Constants.IntakeConstants.ROLLER_S;

    rollerConfiguration.CurrentLimits.withStatorCurrentLimit(Constants.IntakeConstants.ROLLERCURRENTLIMIT);
    rollerConfiguration.CurrentLimits.withStatorCurrentLimitEnable(true);

    rollerConfiguration.withMotorOutput(
        new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

    m_intakeLeftMotor.getConfigurator().apply(rollerConfiguration, 0.050);
    m_intakeRightMotor.getConfigurator().apply(rollerConfiguration, 0.050);

    m_intakeRightMotor.setControl(
        new Follower(Constants.IntakeConstants.LEFTMOTORID, MotorAlignmentValue.Opposed));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Pivot Degrees", getPivotPose());
    SmartDashboard.putNumber("Pivot Raw Rotations", m_pivotMotor.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("Roller Vel", m_intakeLeftMotor.getVelocity().getValueAsDouble());
    SmartDashboard.putString("Pivot Control Mode", 
    m_pivotMotor.getAppliedControl().toString());
SmartDashboard.putNumber("Pivot Target", 
    m_pivotMotor.getClosedLoopReference().getValueAsDouble());
SmartDashboard.putNumber("Pivot CANcoder", 
    m_pivotEncoder.getAbsolutePosition().getValueAsDouble() * 360.0);

    SmartDashboard.putNumber("Pivot Stator Current", m_pivotMotor.getStatorCurrent().getValueAsDouble());
SmartDashboard.putNumber("Pivot Output Voltage", m_pivotMotor.getMotorVoltage().getValueAsDouble());
SmartDashboard.putNumber("Pivot CANcoder Pos", m_pivotEncoder.getAbsolutePosition().getValueAsDouble());
SmartDashboard.putNumber("Pivot Error", m_pivotMotor.getClosedLoopError().getValueAsDouble());
  }

  // ================= Roller =================

  public void setIntakePercentOutput(double percentOutput) {
    intakePercentOutput.Output = percentOutput;
    m_intakeLeftMotor.setControl(intakePercentOutput);
  }

  // ================= Pivot =================

  public void setIntakePivotPercentOutput(double percentOutput) {
    intakePivotPercentOutput.Output = percentOutput;
    m_pivotMotor.setControl(intakePivotPercentOutput);
  }

  public void setIntakePivotPose(double positionDegrees) {
    double rotations = positionDegrees / 360.0;
    pivotPosition = pivotPosition.withPosition(rotations);
    m_pivotMotor.setControl(pivotPosition);
  }

  public double getPivotPose() {
    return m_pivotEncoder.getAbsolutePosition().getValueAsDouble() * 360.0;
}

  // ================= States =================

  public enum IntakeState {
    INTAKE(Constants.IntakeConstants.INTAKE_ROLLER_VELOCITY,
        Constants.IntakeConstants.INTAKE_PIVOT_POSITION),
    SPINUP(Constants.IntakeConstants.SPINUP_ROLLER_VELOCITY,
        Constants.IntakeConstants.SPINUP_PIVOT_POSIITON),
    SHOOT(Constants.IntakeConstants.SHOOT_ROLLER_VELOCITY,
        Constants.IntakeConstants.SHOOT_PIVOT_POSITION),
    CARRY(Constants.IntakeConstants.CARRY_ROLLER_VELOCITY,
        Constants.IntakeConstants.CARRY_PIVOT_POSITION),
    RESET(Constants.IntakeConstants.STOP,
        Constants.IntakeConstants.RESET_PIVOT_POSITION),
    UNCLOG(Constants.IntakeConstants.UNCLOG_ROLLER_VELOCITY,
        Constants.IntakeConstants.UNCLOG_PIVOT_POSITION);

    public double intakeVelocity;
    public double pivotPosition;

    private IntakeState(double intakeVelocity, double pivotPosition) {
      this.intakeVelocity = intakeVelocity;
      this.pivotPosition = pivotPosition;
    }
  }
}
