package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

public class ExtendIntake extends Command {

    private Intake m_intake;
    private boolean isFinished;
    private Timer m_timer;
    
    public ExtendIntake(Intake intake) {
        m_intake = intake;
        m_timer = new Timer();
        addRequirements(m_intake);

    }

    @Override 
    public void initialize() {
       
        m_timer.reset();
        m_timer.start();
        isFinished = false;
    }

    @Override
    public void execute() {
         m_intake.extendintake();
       
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
