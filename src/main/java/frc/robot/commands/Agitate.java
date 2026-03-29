package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

public class Agitate extends Command {
  private Intake m_intake;
  /** Creates a new setshootvel. */
  public Agitate(Intake intake) {

    m_intake = intake;

    addRequirements(m_intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    m_intake.setIntakePivotPose(Constants.IntakeConstants.AGITATE_PIVOT_POSITION);
    m_intake.setIntakePercentOutput(Constants.IntakeConstants.AGITATE_ROLLER_PERCENT);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

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
