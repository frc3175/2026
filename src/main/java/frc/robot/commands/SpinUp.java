// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SpinUp extends Command {
  private Shooter m_shooter;
  // private double m_velocity;
  private DoubleSupplier m_velocity;
  private double d_velocity;
  private boolean isVariable = false;

  /** Creates a new setshootvel. */
  public SpinUp(Shooter shooter,  DoubleSupplier velocitySupplier) {

    m_shooter = shooter;
    m_velocity = velocitySupplier;
    isVariable = true;
    addRequirements(m_shooter);

    // Use addRequirements() here to declare subsystem dependencies.
  }


  /** Creates a new setshootvel. */
  public SpinUp(Shooter shooter,  double velocity) {

    m_shooter = shooter;
    d_velocity = velocity;
    isVariable = false;
    addRequirements(m_shooter);

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (isVariable) {
        m_shooter.setShooterVelocity(m_velocity.getAsDouble());
    } else {
      m_shooter.setShooterVelocity(d_velocity);
    }

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
