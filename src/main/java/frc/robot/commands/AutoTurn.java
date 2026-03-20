
// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands;

// import java.util.ResourceBundle.Control;

// import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveRequest.FieldCentric;
// import com.ctre.phoenix6.swerve.SwerveDrivetrain;
// import com.ctre.phoenix6.swerve.SwerveRequest;

// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
// import frc.robot.subsystems.CommandSwerveDrivetrain;
// import frc.robot.subsystems.Limelight;


// /* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
// public class AutoTurn extends Command {

//   private CommandSwerveDrivetrain m_drivetrain;
//   private Limelight m_limeLight;
//   private CommandXboxController m_controller;
  
 
//   private PIDController turnController = new PIDController(.04, 0, 0 ); //.2  .01good enough
//   boolean ready = false;


  

//     SwerveRequest.FieldCentric motion = new SwerveRequest.FieldCentric();


//   public AutoTurn(CommandSwerveDrivetrain drive, Limelight limelight) {
//     m_drivetrain = drive;
//     m_limeLight = limelight;

    
//     turnController.setTolerance(1);
    
//   }

//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {
  
     

//   }

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {

//     Pose2d botpose = m_drivetrain.getState().Pose;

//         double yaw = m_drivetrain.get360gyro();
//         double angle = m_limeLight.AimToTarget(botpose.getX(), botpose.getY(), 12, 4);
//         turnController.setSetpoint(angle); //maybe -
//         // xController.setSetpoint(2);
        
//         // yController.setSetpoint(7);


        
        
//         m_drivetrain.setControl(
//           motion// Drive left with negative X (left)
//           .withRotationalRate(turnController.calculate(yaw)));

//             if(turnController.atSetpoint()){
//               end(true);
//             }
          

//        }
    
  

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {
//     // drivetrain.setControl(
//     //   motion/*.withVelocityX(0)*/ // Drive forward with negative Y (forward)
//     //   // .withVelocityY(0) // Drive left with negative X (left)
//     //   .withRotationalRate(0));
//     //   // new Autotranslate(drivetrain, camera, m_controller, true);
//   }

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }

