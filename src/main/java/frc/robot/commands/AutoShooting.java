// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;



import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Tower;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoShooting extends Command {
  public Shooter m_Shooter;
  public Tower m_tower;
  public Hopper m_hopper;
  public CommandSwerveDrivetrain m_drivetrain;
  public Limelight m_ll;
  public boolean isblue;
  /** Creates a new AutoShooting. */
  public AutoShooting(Shooter shooter, Tower tower, Hopper hopper, CommandSwerveDrivetrain drivetrain, Limelight ll) {
    if(DriverStation.getAlliance().get() == DriverStation.Alliance.Blue){
      isblue = true;
    }
    else{
      isblue = false;
    }
    m_Shooter = shooter;
    m_tower = tower;
    m_hopper = hopper;
    m_drivetrain = drivetrain;

    addRequirements(m_Shooter, m_tower, m_hopper, m_drivetrain);

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(!isblue){
     new SwerveDrive(
                m_drivetrain, 
                () -> 0, 
                () ->0, 
                () -> 0, 
                () -> true, 
                () -> false,
                () -> m_ll.getXOffset(),
                () -> m_ll.getDistanceToTarget(),
                () -> true
            ).schedule();
    }
    m_Shooter.setShooterVelocity(Constants.ShooterConstants.SPINSPEED);
    m_hopper.runFloor(Constants.HopperConstants.FLOORSPEED);
    m_tower.towerrun(Constants.TowerConstants.RUNSPEED);
  } 

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
