package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

@SuppressWarnings("all")
public class RetractIntake extends Command {
    
    private final Intake m_intake;
    private boolean isFinished;
    private final Timer m_timer;
    
    public RetractIntake(Intake intake) {
        m_intake = intake;
        m_timer = new Timer();
        addRequirements(m_intake);

    }

    @Override 
    public void initialize() {
        m_intake.retractIntake();
        m_timer.reset();
        m_timer.start();
        isFinished = false;
    }

    @Override
    public void execute() {
        if(m_intake.getRackStatorCurrent() > Constants.IntakeConstants.HARDSTOPCURRENTLIMIT && m_timer.get() > 0.35) {
            isFinished = true;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_intake.stopRack();
        m_timer.stop();
    }


    @Override
    public boolean isFinished() {
        return isFinished;
    }

}
