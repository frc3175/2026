
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.AutoLeft;
import frc.robot.commands.AutoRight;
import frc.robot.commands.SwerveDrive;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Hopper;


public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed

    /* Setting up bindings for necessary control of the swerve drive platform */
    

     private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController driverController = new CommandXboxController(Constants.DRIVER_CONTROLER);
    private final CommandXboxController opController  = new CommandXboxController(Constants.OPERATOR_CONTROLER);
   
    
    public final Limelight m_ll = new Limelight();
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();



    public final Shooter m_elevator = new Shooter();
    public final Hopper m_wrist = new Hopper();
    public final Intake m_intake = new Intake();
    
    
    public final Climber m_climber = new Climber();
   

    

    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Path follower */
    private final SendableChooser<Command> autoChooser;



    
    

    public RobotContainer() {
        /*NamedCommands.registerCommand("Intake",  new SetIntake(m_intake, m_robotState, "INTAKE")
            .alongWith(new SetElevator(m_elevator, m_robotState, "INTAKE"))
            .andThen(new SetWrist(m_wrist, m_robotState, "INTAKE")));

        NamedCommands.registerCommand("Outtake", new SetIntake(m_intake, m_robotState, "OUTTAKE"));

        NamedCommands.registerCommand("L4", new SetIntake(m_intake, m_robotState, "L4")
            .alongWith(new SetWrist(m_wrist, m_robotState, "HOME"))
            .alongWith(new SetElevator(m_elevator, m_robotState, "L4"))
            .andThen(new SetWrist(m_wrist, m_robotState, "L4")));

        NamedCommands.registerCommand("HOME", new SetIntake(m_intake, m_robotState, "HOME")
            .alongWith(new SetWrist(m_wrist, m_robotState, "HOME"))
            .andThen(new SetElevator(m_elevator, m_robotState, "HOME")));*/

        
        autoChooser = AutoBuilder.buildAutoChooser("Red 2 Piece Left");

      
        

       
    
 
       
        SmartDashboard.putData("Auto Mode", autoChooser);
        SmartDashboard.putNumber("set elevator", 0);

        configureBindings();
        
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            new SwerveDrive(
                drivetrain, 
                () -> -driverController.getRawAxis(translationAxis), 
                () -> -driverController.getRawAxis(strafeAxis), 
                () -> -driverController.getRawAxis(rotationAxis), 
                () -> true, 
                () -> driverController.rightBumper().getAsBoolean(),
                () -> SmartDashboard.getBoolean("Max speed", false))
        );

        
         drivetrain.registerTelemetry(logger::telemeterize);

         driverController.x().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        

        
        


        driverController.rightTrigger().onTrue(new AutoRight(m_ll));
        driverController.leftTrigger().onTrue(new AutoLeft(m_ll));

        
         //TODO drive: llshoot, setpointshoot, llautoalign, autotrack, 
    
        
        opController.leftBumper().onTrue(new InstantCommand(() -> m_intake.intakerun(Constants.IntakeConstants.INTAKEIN)));
        opController.leftBumper().onFalse(new InstantCommand(() -> m_intake.intakerun(Constants.IntakeConstants.STOP)));

        //TODO: op: hopper controls, climber controls, outtake, passing
        
        

drivetrain.registerTelemetry(logger::telemeterize);

         

        
        
        
        }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
       // return null;
    }
}
