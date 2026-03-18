
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;
import com.fasterxml.jackson.databind.util.Named;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import static edu.wpi.first.units.Units.MetersPerSecond;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AutoDrive;
import frc.robot.commands.AutoTurn;
import frc.robot.commands.AutoUnclogTower;
import frc.robot.commands.ExtendIntake;
import frc.robot.commands.IntakeRun;
import frc.robot.commands.RetractIntake;
import frc.robot.commands.ShootFuel;
import frc.robot.commands.SpinDown;
import frc.robot.commands.SpinUp;
import frc.robot.commands.StopShootingFuel;
import frc.robot.commands.SwerveDrive;
import frc.robot.commands.UnclogTower;
import frc.robot.generated.TunerConstants; 
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Tower;


public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed

    public  final CommandXboxController drivecontroller = new CommandXboxController(0);
    public  final CommandXboxController opController = new CommandXboxController(1);

    public final Trigger Spinup = opController.leftBumper();
    public final Trigger ShootButton = drivecontroller.rightBumper();
    public final Trigger IntakeInButton = opController.rightBumper();
    public final Trigger Extendhop = opController.x();
    public final Trigger Retracthop = opController.b();
    public final Trigger UnclogTower = opController.y();
    public final Trigger TrenchShot = opController.a();
    

    /* Setting up bindings for necessary control of the swerve drive platform */
    

    private final Telemetry logger = new Telemetry(MaxSpeed);
    
    
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    public final Limelight m_ll = new Limelight(drivetrain);
    public static final Shooter m_shooter = new Shooter();
    public static final Hopper m_hopper = new Hopper();
    public final Intake m_intake = new Intake();
    public static final Tower m_tower  = new Tower();    
    // public final Climber m_climber = new Climber();

    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

 

    /* Path follower */
    private final SendableChooser<Command> autoChooser;
    public RobotContainer() {

        NamedCommands.registerCommand("SPINUP", new SpinUp(m_shooter, m_tower, Constants.ShooterConstants.SPINSPEED));
        NamedCommands.registerCommand("SHOOT", new ShootFuel(m_tower, m_hopper, m_intake));
         NamedCommands.registerCommand("EXTENDHOP", new InstantCommand(() -> m_intake.extendIntake()));
        NamedCommands.registerCommand("RETRACTHOP", new InstantCommand(() -> m_intake.retractintake()));
        NamedCommands.registerCommand("HOME", new InstantCommand(() -> CommandScheduler.getInstance().cancelAll()));
        NamedCommands.registerCommand("STOPSHOOT", new StopShootingFuel(m_tower, m_hopper, m_intake));
        NamedCommands.registerCommand("SPINDOWN", new SpinDown(m_shooter));
        NamedCommands.registerCommand("INTAKE", new IntakeRun(m_intake));
        NamedCommands.registerCommand("INTAKESTOP", new InstantCommand(() -> m_intake.runIntake(0)));
        NamedCommands.registerCommand("UNCLOGAUTO", new AutoUnclogTower(m_tower, m_hopper));

         autoChooser = AutoBuilder.buildAutoChooser("New Auto");
       

        SmartDashboard.putData("Auto Mode", autoChooser);
        SmartDashboard.putNumber("set elevator", 0);
        

        configureBindings();
        
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            //Drivetrain will execute this command periodically
            new SwerveDrive(
                drivetrain, 
                () -> -Constants.DRIVER_CONTROLER.getRawAxis(translationAxis), 
                () -> -Constants.DRIVER_CONTROLER.getRawAxis(strafeAxis), 
                () -> Constants.DRIVER_CONTROLER.getRawAxis(rotationAxis), 
                () -> true, 
                () -> drivecontroller.leftTrigger().getAsBoolean(),
                m_ll,
                () -> drivecontroller.leftBumper().getAsBoolean()
            )
        );

        
        drivetrain.registerTelemetry(logger::telemeterize);

        drivecontroller.x().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));
        drivecontroller.pov(90).whileTrue(new AutoTurn(drivetrain, m_ll));
        drivecontroller.start().onTrue(new AutoDrive(m_ll, drivetrain));

        ShootButton.onTrue(new ShootFuel(m_tower, m_hopper, m_intake)).onFalse(new StopShootingFuel(m_tower, m_hopper, m_intake));
         //TODO drive: llshoot, setpointshoot, llautoalign, autotrack, 
    
        
        IntakeInButton.onTrue(new IntakeRun(m_intake));
        IntakeInButton.onFalse(new InstantCommand(() -> m_intake.runIntake(0)));

        Spinup.onTrue(new SpinUp(m_shooter, m_tower, Constants.ShooterConstants.SPINSPEED)).onFalse(new SpinDown(m_shooter));
        TrenchShot.onTrue(new SpinUp(m_shooter, m_tower, Constants.ShooterConstants.TRENCHSPEED)).onFalse(new SpinDown(m_shooter));

       Extendhop.onTrue(new ExtendIntake(m_intake));
        Retracthop.onTrue(new RetractIntake(m_intake));
        UnclogTower.onTrue(new UnclogTower(m_tower, m_hopper)).onFalse(new StopShootingFuel(m_tower, m_hopper, m_intake));
       opController.pov(90).onTrue(new InstantCommand(() -> m_intake.moverack(Constants.IntakeConstants.RACKVEL))).onFalse(new InstantCommand(() -> m_intake.moverack(0)));
       opController.pov(270).onTrue(new InstantCommand(() -> m_intake.moverack(-Constants.IntakeConstants.RACKVEL))).onFalse(new InstantCommand(() -> m_intake.moverack(0)));
        opController.pov(180).onTrue(new InstantCommand(() -> m_intake.resetIntakeRackZero()));

        //TODO: op: hopper controls, climber controls, outtake, passing
        }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
       // return null;
    }
}
