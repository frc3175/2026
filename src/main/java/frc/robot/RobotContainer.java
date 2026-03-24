
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import static edu.wpi.first.units.Units.MetersPerSecond;

import java.util.Optional;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.Agitate;
import frc.robot.commands.SetBotState;
import frc.robot.commands.SpinDown;
import frc.robot.commands.SpinUp;
import frc.robot.commands.SwerveDrive;
import frc.robot.generated.TunerConstants; 
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.RobotState;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Tower;
import frc.robot.util.ShooterLookup;



public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed

    public  final CommandXboxController drivecontroller = new CommandXboxController(0);
    public  final CommandXboxController opController = new CommandXboxController(1);
    public Pose2d botpose;

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

    public final Limelight m_ll = new Limelight();
    public static final Shooter m_shooter = new Shooter();
    public static final Hopper m_hopper = new Hopper();
    public final Intake m_intake = new Intake();
    public static final Tower m_tower  = new Tower();    
    public final ShooterLookup shooterLookup = new ShooterLookup();
    public final RobotState m_robotState = new RobotState();

    // public final Climber m_climber = new Climber();

    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

 

    /* Path follower */
    private final SendableChooser<Command> autoChooser;
    public RobotContainer() {

        m_shooter.m_Limelight = m_ll; // give shooter access to limelight object, dangerous but im lazy

        NamedCommands.registerCommand("SPINUP", new SpinUp(m_shooter, getDesiredShooterVelocity()).alongWith(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SPINUP)));
        NamedCommands.registerCommand("SHOOT", new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SHOOT));
         NamedCommands.registerCommand("EXTENDHOP", new InstantCommand(() -> m_intake.setIntakePivotPose(Constants.IntakeConstants.CARRY_PIVOT_POSITION)));
        NamedCommands.registerCommand("RETRACTHOP", new InstantCommand(() -> m_intake.setIntakePivotPose(Constants.IntakeConstants.RESET_PIVOT_POSITION)));
        NamedCommands.registerCommand("HOME", new InstantCommand(() -> CommandScheduler.getInstance().cancelAll()));
        NamedCommands.registerCommand("STOPSHOOT", new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.RESET));
        NamedCommands.registerCommand("SPINDOWN", new SpinDown(m_shooter));
        NamedCommands.registerCommand("INTAKE", new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.INTAKE));
        NamedCommands.registerCommand("INTAKESTOP", new InstantCommand(() -> m_intake.setIntakePercentOutput(0)));
        NamedCommands.registerCommand("UNCLOGAUTO", new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.UNCLOG));

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

        ShootButton.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SHOOT))
            .onFalse(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.RESET))
            .whileTrue(new Agitate(m_intake));

    
        
        IntakeInButton.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.INTAKE))
            .onFalse(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.CARRY));

        Spinup.whileTrue(new SpinUp(m_shooter, getDesiredShooterVelocity()).alongWith(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SPINUP)))
            .onFalse(new SpinDown(m_shooter).alongWith(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.RESET)));

        Extendhop.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.CARRY));
        Retracthop.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.RESET));
        UnclogTower.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.UNCLOG))
            .onFalse(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.RESET));


        }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
       // return null;
    }
    public void periodic() {

        botpose = drivetrain.getState().Pose;
        SmartDashboard.putNumber("distance to target: ", getDistanceToTargetMeters());
        //SmartDashboard.putNumber("to ang", m_ll.AimToTarget(botpose.getX(), botpose.getY(), 12, 4) - drivetrain.getState().Pose.getRotation().getRadians());
        
    }

    public Pose2d getCurrentHubPose() {
        Optional<Alliance> alliance = DriverStation.getAlliance();

        if (alliance.isPresent() && alliance.get() == Alliance.Red) {
            return Constants.FieldConstants.RED_HUB;
        }

        return Constants.FieldConstants.BLUE_HUB;
    }

    public double getDistanceToTargetMeters() {
        Pose2d robotPose = drivetrain.getState().Pose;
        Pose2d hubPose = getCurrentHubPose();

        return robotPose.getTranslation().getDistance(hubPose.getTranslation());
    }

    public double getDesiredShooterVelocity() {
        return ShooterLookup.calculateFlywheelVelocity(getDistanceToTargetMeters());
    }
}
