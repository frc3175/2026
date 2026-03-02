package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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

    double priorityID;


    public Limelight() {
        // Limelight setup, if any

        table = NetworkTableInstance.getDefault().getTable("limelight-shooter");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");
        tid = table.getEntry("tid");

        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            if(alliance.get() == DriverStation.Alliance.Red) {
                priorityID = Constants.AutoAlignConstants.REDHUBID;
            } else {
                priorityID = Constants.AutoAlignConstants.BLUEHUBID;
            }
        } else {
            priorityID = Constants.AutoAlignConstants.BLUEHUBID;
        }

        NetworkTableInstance.getDefault().getTable("limelight-shooter").getEntry("priorityid").setNumber(priorityID);
    }

        /** 
     * 
     * @return distance from robot to target (inches)
     * 
     */

    public double getDistanceToTarget()  {

        double targetOffsetAngle_Vertical = ty.getDouble(0.0);

        // how many degrees back is your limelight rotated from perfectly vertical?
        double limelightMountAngleDegrees = Constants.LimelightConstants.LIMELIGHT_MOUNTING_ANGLE; 

        // distance from the center of the Limelight lens to the floor
        double limelightLensHeightInches = Constants.LimelightConstants.LIMELIGHT_LENS_HEIGHT; 

        // distance from the target to the floor
        double goalHeightInches = Constants.AutoAlignConstants.TARGET_HEIGHT; 

        double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
        double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);

        //calculate distance
        double distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);

        return distanceFromLimelightToGoalInches - Constants.LimelightConstants.LIMELIGHT_BACK_OFFSET;

    }

    // public double aimToTarget() {

    //     double kP = Constants.AutoAlignConstants.LIMELIGHT_ANGLE_P;

    //     double distanceToGoal = getDistanceToTarget();
    //     double cameraOffset = Constants.LimelightConstants.LIMELIGHT_OFFSET;
    //     double errorRadians = Math.asin(cameraOffset/distanceToGoal);
    //     double errorDegrees = errorRadians * (180/3.14159);

    //     double cameraToTargetDegrees = tx.getDouble(0.0);
    //     double centerToTargetDegrees = cameraToTargetDegrees + errorDegrees;

    //     // tx ranges from (-hfov/2) to (hfov/2) in degrees. If your target is on the rightmost edge of 
    //     // your limelight 3 feed, tx should return roughly 31 degrees.
    //     double targetingAngularVelocity = centerToTargetDegrees * kP;

    //     // convert to radians per second for our drive method
    //     targetingAngularVelocity *= (Constants.AutoAlignConstants.MAX_ANGULAR_VELOCITY);

    //     //invert since tx is positive when the target is to the right of the crosshair
    //     targetingAngularVelocity *= -1.0;

    //     return targetingAngularVelocity;


    // }

    public double getXOffset() {
        return limelightLeft.getEntry("tx").getDouble(0.0); // Horizontal offset (degrees)
    }

    public double getRightXOffset() {
        return limelightRight.getEntry("tx").getDouble(0.0); // Horizontal offset (degrees)
    }

    public double getYOffset() {
        return limelightLeft.getEntry("ty").getDouble(0.0); // Horizontal offset (degrees)
    }

    public double getRightYOffset() {
        return limelightRight.getEntry("ty").getDouble(0.0); // Horizontal offset (degrees)
    }

    public double shooterGetHorizontalOffset() {
        return limelightShooter.getEntry("tx").getDouble(0.0); // Horizontal offset (degrees)
    }

    public boolean leftHasTarget() {
        return limelightLeft.getEntry("ta").getDouble(0.0) > 0.0; // If target area is > 0
    }

    public boolean shooterHasTarget() {
        return limelightShooter.getEntry("ta").getDouble(0.0) > 0.0; // If target area is > 0

    }

    public boolean rightHasTarget() {
        return limelightRight.getEntry("ta").getDouble(0.0) > 0.0; // If target area is > 0

    }

    public int getTargetid() {
        return ((int)limelightLeft.getEntry("tid").getDouble(0)); // If target area is > 0

    }

    public double shooterGetTargetid() {
        return limelightShooter.getEntry("tid").getDouble(0.0); // If target area is > 0

    }

     @Override
  public void periodic() {
    SmartDashboard.putNumber("x offset",getXOffset());
    SmartDashboard.putNumber("Y offset",getYOffset());
    SmartDashboard.putBoolean("left has tag",leftHasTarget());
    SmartDashboard.putBoolean("right has tag",rightHasTarget());
    SmartDashboard.putNumber("shooter horizontal offset",shooterGetHorizontalOffset());
    SmartDashboard.putBoolean("shooter has tag",shooterHasTarget());
    SmartDashboard.putNumber("targetid", getTargetid());
    SmartDashboard.putNumber("shootertargetid", shooterGetTargetid());
  }
}