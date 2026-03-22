// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.RobotState;
import frc.robot.subsystems.Tower;
import frc.robot.subsystems.RobotState.BotState;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SetBotState extends Command {

  private RobotState m_robotState;
  private BotState m_botState;
  private Hopper m_hopper;
  private Intake m_intake;
  private Tower m_tower;
  public SetBotState(RobotState robotState, Hopper hopper, Intake intake, Tower tower, BotState desiredState) {

    m_robotState = robotState;
    m_hopper = hopper;
    m_intake = intake;
    m_tower = tower;
    m_botState = desiredState;

    addRequirements(m_robotState, m_hopper, m_intake, m_tower);

  }

  @Override
  public void initialize() {

    m_robotState.setRobotState(m_botState);
    m_hopper.setHopperVelocity(m_robotState.getRobotState().hopperState.hopperVelocity);
    m_intake.setIntakeVelocity(m_robotState.getRobotState().intakeState.intakeVelocity);
    m_intake.setIntakePivotPose(m_robotState.getRobotState().intakeState.pivotPosition);
    m_tower.setTowerVelocity(m_robotState.getRobotState().towerState.towerVelocity);

  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean isFinished) {}

  @Override
  public boolean isFinished() {

    return true;

  }

}