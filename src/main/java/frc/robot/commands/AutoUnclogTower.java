// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Tower;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoUnclogTower extends Command {
  public Tower m_tower;
  public Hopper m_hopper;
  // public double m_floorvel;
  /** Creates a new TowerRun. */
  public AutoUnclogTower(Tower tower, Hopper hopper) {
    m_tower = tower;
    m_hopper = hopper;
    // m_hopper = hopper;
    // m_floorvel = floorvel;
    addRequirements(m_tower, m_hopper);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_tower.towerrun(Constants.TowerConstants.REVERSERUNSPEED);
    m_hopper.runFloor(Constants.HopperConstants.REVERSEFLOORSPEED);
    //m_intake.runIntake(Constants.IntakeConstants.INTAKEIN);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
