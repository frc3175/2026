package frc.robot.subsystems;

import org.opencv.core.Mat;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.geometry.Translation2dPlus;
import frc.robot.Constants;

public class Limelight extends SubsystemBase {

    private final NetworkTable limelightShooter = NetworkTableInstance.getDefault().getTable("limelight-shooter");
    private final NetworkTable limelightLeft = NetworkTableInstance.getDefault().getTable("limelight-left");
    private final NetworkTable limelightRight = NetworkTableInstance.getDefault().getTable("limelight-right");

    NetworkTableEntry tx;
    NetworkTableEntry ty;
    NetworkTableEntry ta;
    NetworkTableEntry tid;
    NetworkTable table;
    CommandSwerveDrivetrain m_drivetrain;

    double priorityID;


    public Limelight(CommandSwerveDrivetrain drivetrain) {
        // Limelight setup, if any

        table = NetworkTableInstance.getDefault().getTable("limelight-shooter");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");
        tid = table.getEntry("tid");

        var alliance = DriverStation.getAlliance();
        // if (alliance.isPresent()) {
        //     if(alliance.get() == DriverStation.Alliance.Red) {
        //         priorityID = Constants.AutoAlignConstants.REDHUBID;
        //     } else {
        //         priorityID = Constants.AutoAlignConstants.BLUEHUBID;
        //     }
        // } else {
        //     priorityID = Constants.AutoAlignConstants.BLUEHUBID;
        // }

        // NetworkTableInstance.getDefault().getTable("limelight-shooter").getEntry("priorityid").setNumber(priorityID);
        m_drivetrain = drivetrain;
    }

        /** 
     * 
     * @return distance from robot to target (inches)
     * 
     */

    // public double getDistanceToTarget()  {

    //     double targetOffsetAngle_Vertical = ty.getDouble(0.0);

    //     // how many degrees back is your limelight rotated from perfectly vertical?
    //     double limelightMountAngleDegrees = Constants.LimelightConstants.LIMELIGHT_MOUNTING_ANGLE; 

    //     // distance from the center of the Limelight lens to the floor
    //     double limelightLensHeightInches = Constants.LimelightConstants.LIMELIGHT_LENS_HEIGHT; 

    //     // distance from the target to the floor
    //     double goalHeightInches = Constants.AutoAlignConstants.TARGET_HEIGHT; 

    //     double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
    //     double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);

    //     //calculate distance
    //     double distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);

    //     return distanceFromLimelightToGoalInches - Constants.LimelightConstants.LIMELIGHT_BACK_OFFSET;

  //  }

    public double aimToTarget() {

        Pose2d botpose = m_drivetrain.getState().Pose;


        // double xdiff = 12 - botpose.getX();
        // double ydiff = 4 -botpose.getY();
        //double xdiff = 12 - botpose.getX() ;
        //double ydiff = botpose.getY() - 4;

        //double targetang = Math.atan2(ydiff, xdiff);

        Rotation2d targetang = Translation2dPlus.getAngle(new Translation2d(12, 4), botpose.getTranslation());
 

        // tx ranges from (-hfov/2) to (hfov/2) in degrees. If your target is on the rightmost edge of 
        // // your limelight 3 feed, tx should return roughly 31 degrees.
        // double targetingAngularVelocity = targetang * Constants.AutoAlignConstants.LIMELIGHT_ANGLE_P;

        // // convert to radians per second for our drive method
        // targetingAngularVelocity *= (Constants.AutoAlignConstants.MAX_ANGULAR_VELOCITY);

        //invert since tx is positive when the target is to the right of the crosshair
        //targetingAngularVelocity *= -1.0;

        return targetang.getDegrees() + 180;
        // return Math.toDegrees(targetang);


    }

     public double AimToTarget(double currentX, double currentY, double targetX, double targetY) {

        double dx = targetX - currentX;
        double dy = targetY - currentY;

        double angle = Math.toDegrees(Math.atan2(dy, dx)); //dy could be negative

        if (angle < 0) angle += 360; 
        SmartDashboard.putNumber("rotation to align", Math.abs(angle + Constants.AutoAlignConstants.BLUE_ANGLE_OFFSET));
        return angle;
    }

    // public double getXOffset() {
    //     return limelightLeft.getEntry("tx").getDouble(0.0); // Horizontal offset (degrees)
    // }

    // public double getRightXOffset() {
    //     return limelightRight.getEntry("tx").getDouble(0.0); // Horizontal offset (degrees)
    // }

    // public double getYOffset() {
    //     return limelightLeft.getEntry("ty").getDouble(0.0); // Horizontal offset (degrees)
    // }

    // public double getRightYOffset() {
    //     return limelightRight.getEntry("ty").getDouble(0.0); // Horizontal offset (degrees)
    // }

    public double shooterGetHorizontalOffset() {
        return limelightShooter.getEntry("tx").getDouble(0.0); // Horizontal offset (degrees)
    }

    // public boolean leftHasTarget() {
    //     return limelightLeft.getEntry("ta").getDouble(0.0) > 0.0; // If target area is > 0
    // }

    public boolean shooterHasTarget() {
        return limelightShooter.getEntry("ta").getDouble(0.0) > 0.0; // If target area is > 0

    }

    // public boolean rightHasTarget() {
    //     return limelightRight.getEntry("ta").getDouble(0.0) > 0.0; // If target area is > 0

    // }

    public int getTargetid() {
        return ((int)limelightLeft.getEntry("tid").getDouble(0)); // If target area is > 0

    }

    public double shooterGetTargetid() {
        return limelightShooter.getEntry("tid").getDouble(0.0); // If target area is > 0

    }

     @Override
  public void periodic() {
    // SmartDashboard.putNumber("x offset",getXOffset());
    // SmartDashboard.putNumber("Y offset",getYOffset());
    // SmartDashboard.putBoolean("left has tag",leftHasTarget());
    // SmartDashboard.putBoolean("right has tag",rightHasTarget());
    SmartDashboard.putNumber("shooter horizontal offset",shooterGetHorizontalOffset());
    SmartDashboard.putBoolean("shooter has tag",shooterHasTarget());
    SmartDashboard.putNumber("targetid", getTargetid());
    SmartDashboard.putNumber("shootertargetid", shooterGetTargetid());
    // SmartDashboard.putNumber("rotation to align", aimToTarget());
    SmartDashboard.putNumber("to ang", aimToTarget() - m_drivetrain.getState().Pose.getRotation().getRadians());
  }
}