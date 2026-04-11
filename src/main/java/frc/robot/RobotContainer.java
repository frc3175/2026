
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import static edu.wpi.first.units.Units.MetersPerSecond;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
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
import frc.robot.subsystems.RobotState.BotState;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Tower;
import frc.robot.util.ShooterLookup;



public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed

    public  final CommandXboxController drivecontroller = new CommandXboxController(0);
    public  final CommandXboxController opController = new CommandXboxController(1);

    public final Trigger Spinup = opController.leftBumper();
    public final Trigger ShootButton = new Trigger(() -> drivecontroller.getRawAxis(XboxController.Axis.kRightTrigger.value) >= 0.5);
    public final Trigger IntakeInButton = opController.rightBumper();
    public final Trigger AgitateHop = opController.x();
    public final Trigger FullFieldPass = opController.b();
    public final Trigger UnclogTower = opController.y();
    public final Trigger TowerShot = opController.a();
    public final Trigger AutoAlign = new Trigger(() -> drivecontroller.getRawAxis(XboxController.Axis.kLeftTrigger.value) >= 0.5);
    public final Trigger LockWheels = new Trigger(() -> opController.getRawAxis(XboxController.Axis.kRightTrigger.value) >= 0.5);
    

    /* Setting up bindings for necessary control of the swerve drive platform */
    

    private final Telemetry logger = new Telemetry(MaxSpeed);
    
    
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    public final Limelight m_ll = new Limelight("limelight-shooter", drivetrain);
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
        drivetrain.m_Limelight = m_ll;

        NamedCommands.registerCommand("SPINUPBUMP", new SpinUp(m_shooter, Constants.ShooterConstants.BUMPAUTOSPEED).alongWith(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SPINUP)));
        NamedCommands.registerCommand("SPINUPTOWER", new SpinUp(m_shooter, Constants.ShooterConstants.TOWERSPINSPEED).alongWith(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SPINUP)));
        NamedCommands.registerCommand("SPINUP", new SpinUp(m_shooter, drivetrain::getDesiredShooterVelocity).alongWith(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SPINUP)));
        NamedCommands.registerCommand("SHOOT", new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SHOOT));
        NamedCommands.registerCommand("EXTENDHOP", new InstantCommand(() -> m_intake.setIntakePivotPose(Constants.IntakeConstants.CARRY_PIVOT_POSITION)));
        NamedCommands.registerCommand("AGITATE", new InstantCommand(() -> m_intake.setIntakePivotPose(Constants.IntakeConstants.AGITATE_PIVOT_POSITION)));
        NamedCommands.registerCommand("RESET", new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.RESET));
        NamedCommands.registerCommand("SPINDOWN", new SpinDown(m_shooter));
        NamedCommands.registerCommand("INTAKE", new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.INTAKE));
        NamedCommands.registerCommand("CARRY",new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.CARRY) );
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
                () -> drivecontroller.leftBumper().getAsBoolean(),
                m_ll,
                AutoAlign,
                ShootButton,
                LockWheels
            )
        );

        
        drivetrain.registerTelemetry(logger::telemeterize);

        drivecontroller.x().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        ShootButton.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SHOOT))
            .onFalse(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.CARRY));
          //  .whileTrue(new Agitate(m_intake));

        IntakeInButton.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.INTAKE))
        //        .onFalse(new InstantCommand(() -> m_intake.setIntakeVelocity(0)));
             .onFalse(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.CARRY));

        Spinup.whileTrue(new SpinUp(m_shooter, drivetrain::getDesiredShooterVelocity));
        Spinup.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SPINUP));
        Spinup.onFalse(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, BotState.RESET).alongWith(new SpinDown(m_shooter)));
        

        AgitateHop.onTrue(new InstantCommand(() -> m_intake.setIntakePivotPose(Constants.IntakeConstants.AGITATE_PIVOT_POSITION))).onFalse(new InstantCommand(() -> m_intake.setIntakePivotPose(Constants.IntakeConstants.SHOOT_PIVOT_POSITION)));
        UnclogTower.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.UNCLOG))
            .onFalse(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.RESET));

        
        TowerShot.whileTrue(new SpinUp(m_shooter, Constants.ShooterConstants.TOWERSPINSPEED));
        TowerShot.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SPINUP));
        TowerShot.onFalse(new SpinDown(m_shooter).alongWith(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.RESET)));

        FullFieldPass.whileTrue(new SpinUp(m_shooter, Constants.ShooterConstants.FFPASSSPEED));
        FullFieldPass.onTrue(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, RobotState.BotState.SPINUP));
        Spinup.onFalse(new SetBotState(m_robotState, m_hopper, m_intake, m_tower, BotState.RESET).alongWith(new SpinDown(m_shooter)));
        
        opController.pov(270).onTrue(new InstantCommand(()->m_intake.setIntakePivotPercentOutput(-0.40))).onFalse(new InstantCommand(()->m_intake.setIntakePivotPercentOutput(0)));
        //opController.rightBumper().onTrue(new InstantCommand(()->m_intake.setIntakePivotPercentOutput(-0.70))).onFalse(new InstantCommand(()->m_intake.setIntakePivotPercentOutput(0)));
        opController.pov(90).onTrue(new InstantCommand(()->m_intake.setIntakePivotPercentOutput(0.40))).onFalse(new InstantCommand(()->m_intake.setIntakePivotPercentOutput(0)));
    }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
       // return null;
    }
}
