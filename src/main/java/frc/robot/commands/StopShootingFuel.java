package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Tower;

public class StopShootingFuel extends Command {
  public Tower m_tower;
  public Hopper m_hopper;
  public Intake m_intake;
  // public double m_floorvel;
  /** Creates a new TowerRun. */
  public StopShootingFuel(Tower tower, Hopper hopper, Intake intake) {
    this.m_tower = tower;
    this.m_intake = intake;
    this.m_hopper = hopper;
    // m_hopper = hopper;
    // m_floorvel = floorvel;
    addRequirements(this.m_tower, this.m_hopper, this.m_intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_tower.towerrun(0);
    m_hopper.runFloor(0);
    m_intake.runIntake(0);
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
