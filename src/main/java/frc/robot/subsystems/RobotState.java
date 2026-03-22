// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hopper.HopperState;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Tower.TowerState;


public class RobotState extends SubsystemBase {
  
  private BotState m_botState;

  public RobotState() {}
    
  public void setRobotState(BotState state) {

    m_botState = state;

  }

  public BotState getRobotState() {

    return m_botState;

  }

  public enum BotState {
    INTAKE(HopperState.INTAKE, IntakeState.INTAKE, TowerState.INTAKE),
    SPINUP(HopperState.SPINUP, IntakeState.SPINUP, TowerState.SPINUP),
    SHOOT(HopperState.SHOOT, IntakeState.SHOOT, TowerState.SHOOT),
    CARRY(HopperState.CARRY, IntakeState.CARRY, TowerState.CARRY),
    RESET(HopperState.RESET, IntakeState.RESET, TowerState.RESET),
    UNCLOG(HopperState.UNCLOG, IntakeState.UNCLOG, TowerState.UNCLOG);

    public HopperState hopperState;
    public IntakeState intakeState;
    public TowerState towerState;
    private BotState(HopperState hopperState, IntakeState intakeState, TowerState towerState) {
      this.hopperState = hopperState;
      this.intakeState = intakeState;
      this.towerState = towerState;

    }

  }

}





