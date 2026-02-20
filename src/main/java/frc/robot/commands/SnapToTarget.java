package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;

public class SnapToTarget extends Command {
    
    private Limelight m_limelight;
    private CommandSwerveDrivetrain m_drivetrain;

    public SnapToTarget(Limelight limelight, CommandSwerveDrivetrain drivetrain) {

        m_drivetrain = drivetrain;
        m_limelight = limelight;

        addRequirements(m_limelight, m_drivetrain);

    }

    @Override
    public void execute() {
        //TODO: implement
    }
    
}