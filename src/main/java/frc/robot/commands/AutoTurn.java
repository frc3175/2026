
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.ResourceBundle.Control;

import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveRequest.FieldCentric;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;


/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoTurn extends Command {

  private CommandSwerveDrivetrain drivetrain;
  private Limelight camera;
  private CommandXboxController m_controller;
  private double yaw;
  private PIDController turnController = new PIDController(.04, 0, 0 ); //.2  .01good enough
  boolean ready = false;


  

    SwerveRequest.FieldCentric motion = new SwerveRequest.FieldCentric();


  public AutoTurn(CommandSwerveDrivetrain drive, Limelight limelight) {
    drivetrain = drive;
    camera = limelight;
    

    
    turnController.setTolerance(.0001);
    
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    
        // double yaw = drivetrain.get360gyro();
        // double angle = camera.aimToTarget();
        
        // xController.setSetpoint(2);
        
        // yController.setSetpoint(7);

        Pose2d botpose = drivetrain.getState().Pose;

       
        
        if(DriverStation.getAlliance().get() == Alliance.Red){
          yaw = drivetrain.get360gyro();
         double angle = camera.AimToTarget(botpose.getX(), botpose.getY(), Constants.FieldConstants.RED_HUB.getX(), Constants.FieldConstants.RED_HUB.getY());
         turnController.setSetpoint(angle + Constants.AutoAlignConstants.RED_ANGLE_OFFSET);
        }
        else{
          yaw = drivetrain.get360gyro() ;
           double angle = camera.AimToTarget(botpose.getX(), botpose.getY(), Constants.FieldConstants.BLUE_HUB.getX(), Constants.FieldConstants.BLUE_HUB.getY());
           turnController.setSetpoint(Math.abs(angle + Constants.AutoAlignConstants.BLUE_ANGLE_OFFSET) %360);
          
        }

         //maybe - +90 or -20
        
        drivetrain.setControl(
          motion// Drive left with negative X (left)
          .withRotationalRate(turnController.calculate(yaw)));

            if(turnController.atSetpoint()){
              end(true);
            }
       }
    
  @Override
  public void end(boolean interrupted) {
    
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

