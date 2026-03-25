package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class Agitate extends Command {

  private Intake m_intake;
  private Timer m_timer;
  private boolean isFinished;

  public Agitate(Intake intake) {

    m_intake = intake;
    m_timer = new Timer();
    isFinished = false;

    addRequirements(m_intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    isFinished = false;
    m_timer.reset();
    m_timer.start();
    m_intake.setIntakePivotPose(Constants.IntakeConstants.RESET_PIVOT_POSITION);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    while (m_timer.get() < 1) {
      // Wait for 1 seconds
    }

    m_intake.setIntakePivotPose(Constants.IntakeConstants.CARRY_PIVOT_POSITION);
    isFinished = true;


  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
