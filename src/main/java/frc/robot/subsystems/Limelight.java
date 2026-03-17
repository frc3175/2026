// package frc.robot.subsystems;

// import org.opencv.core.Mat;

// import com.ctre.phoenix6.hardware.CANcoder;
// import com.ctre.phoenix6.hardware.TalonFX;
// import com.ctre.phoenix6.swerve.SwerveDrivetrain;

// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.networktables.NetworkTableInstance;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.Constants;

// public class Limelight extends SubsystemBase {

//     private final NetworkTable limelightShooter = NetworkTableInstance.getDefault().getTable("limelight-shooter");
//     private final NetworkTable limelightLeft = NetworkTableInstance.getDefault().getTable("limelight-left");
//     private final NetworkTable limelightRight = NetworkTableInstance.getDefault().getTable("limelight-right");

//     NetworkTableEntry tx;
//     NetworkTableEntry ty;
//     NetworkTableEntry ta;
//     NetworkTableEntry tid;
//     NetworkTable table;
//     CommandSwerveDrivetrain m_drivetrain;

//     double priorityID;


//     public Limelight(/*CommandSwerveDrivetrain drivetrain*/) {
//         // Limelight setup, if any

//         table = NetworkTableInstance.getDefault().getTable("limelight-shooter");
//         tx = table.getEntry("tx");
//         ty = table.getEntry("ty");
//         ta = table.getEntry("ta");
//         tid = table.getEntry("tid");

//         var alliance = DriverStation.getAlliance();
//         // if (alliance.isPresent()) {
//         //     if(alliance.get() == DriverStation.Alliance.Red) {
//         //         priorityID = Constants.AutoAlignConstants.REDHUBID;
//         //     } else {
//         //         priorityID = Constants.AutoAlignConstants.BLUEHUBID;
//         //     }
//         // } else {
//         //     priorityID = Constants.AutoAlignConstants.BLUEHUBID;
//         // }

//         // NetworkTableInstance.getDefault().getTable("limelight-shooter").getEntry("priorityid").setNumber(priorityID);
//         // m_drivetrain = drivetrain;
//     }

//     /** 
//      * 
//      * @return distance from robot to target (inches)
//      * 
//      */

//     public double getDistanceToTarget()  {

//         double targetOffsetAngle_Vertical = ty.getDouble(0.0);

//         // how many degrees back is your limelight rotated from perfectly vertical?
//         double limelightMountAngleDegrees = Constants.LimelightConstants.LIMELIGHT_MOUNTING_ANGLE; 

//         // distance from the center of the Limelight lens to the floor
//         double limelightLensHeightInches = Constants.LimelightConstants.LIMELIGHT_LENS_HEIGHT; 

//         // distance from the target to the floor
//         double goalHeightInches = Constants.AutoAlignConstants.TARGET_HEIGHT; 

//         double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
//         double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);

//         //calculate distance
//         double distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);

//         return distanceFromLimelightToGoalInches - Constants.LimelightConstants.LIMELIGHT_BACK_OFFSET;

//     }

//     // public double aimToTarget() {

//     //     Pose2d botpose = m_drivetrain.getState().Pose;

//     //     // double xdiff = 12 - botpose.getX();
//     //     // double ydiff = 4 -botpose.getY();
//     //     double xdiff = 12 - botpose.getX() ;
//     //     double ydiff = botpose.getY() - 4;

//     //     double targetang = Math.atan2(ydiff, xdiff);

 

//     //     // tx ranges from (-hfov/2) to (hfov/2) in degrees. If your target is on the rightmost edge of 
//     //     // // your limelight 3 feed, tx should return roughly 31 degrees.
//     //     // double targetingAngularVelocity = targetang * Constants.AutoAlignConstants.LIMELIGHT_ANGLE_P;

//     //     // // convert to radians per second for our drive method
//     //     // targetingAngularVelocity *= (Constants.AutoAlignConstants.MAX_ANGULAR_VELOCITY);

//     //     //invert since tx is positive when the target is to the right of the crosshair
//     //     //targetingAngularVelocity *= -1.0;

//     //     return Math.toDegrees(targetang) -75;
//     //     // return Math.toDegrees(targetang);


//     // }

//     public double AimToTarget(double currentX, double currentY, double targetX, double targetY) {

//         double dx = targetX - currentX;
//         double dy = targetY - currentY;

//         double angle = Math.toDegrees(Math.atan2(dy, dx)); //dy could be negative

//         if (angle < 0) angle += 360; 

//         return angle;
//     }

//     // public double getXOffset() {
//     //     return limelightLeft.getEntry("tx").getDouble(0.0); // Horizontal offset (degrees)
//     // }

//     // public double getRightXOffset() {
//     //     return limelightRight.getEntry("tx").getDouble(0.0); // Horizontal offset (degrees)
//     // }

//     // public double getYOffset() {
//     //     return limelightLeft.getEntry("ty").getDouble(0.0); // Horizontal offset (degrees)
//     // }

//     // public double getRightYOffset() {
//     //     return limelightRight.getEntry("ty").getDouble(0.0); // Horizontal offset (degrees)
//     // }

//     public double shooterGetHorizontalOffset() {
//         return limelightShooter.getEntry("tx").getDouble(0.0); // Horizontal offset (degrees)
//     }

//     // public boolean leftHasTarget() {
//     //     return limelightLeft.getEntry("ta").getDouble(0.0) > 0.0; // If target area is > 0
//     // }

//     public boolean shooterHasTarget() {
//         return limelightShooter.getEntry("ta").getDouble(0.0) > 0.0; // If target area is > 0

//     }

//     // public boolean rightHasTarget() {
//     //     return limelightRight.getEntry("ta").getDouble(0.0) > 0.0; // If target area is > 0

//     // }

//     public int getTargetid() {
//         return ((int)limelightLeft.getEntry("tid").getDouble(0)); // If target area is > 0

//     }

//     public double shooterGetTargetid() {
//         return limelightShooter.getEntry("tid").getDouble(0.0); // If target area is > 0

//     }

//      @Override
//   public void periodic() {
//     // SmartDashboard.putNumber("x offset",getXOffset());
//     // SmartDashboard.putNumber("Y offset",getYOffset());
//     // SmartDashboard.putBoolean("left has tag",leftHasTarget());
//     // SmartDashboard.putBoolean("right has tag",rightHasTarget());
//     SmartDashboard.putNumber("shooter horizontal offset",shooterGetHorizontalOffset());
//     SmartDashboard.putBoolean("shooter has tag",shooterHasTarget());
//     SmartDashboard.putNumber("targetid", getTargetid());
//     SmartDashboard.putNumber("shootertargetid", shooterGetTargetid());
//     SmartDashboard.putNumber("rotation from distance", Math.toDegrees(Math.atan2(12 - m_drivetrain.getState().Pose.getX() , 4 - m_drivetrain.getState().Pose.getY() )));
    
//   }
// }

/* NEW CODE BELOW
---------------------------------------------------------------------------------------------------------------------------------
*/

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

    public static class VisionMeasurement {
        public final String cameraName;
        public final Pose2d pose;
        public final double timestampSeconds;
        public final double tagArea;
        public final int tagCount;
        public final double primaryTagDistanceMeters;
        public final double poseErrorMeters;

        public VisionMeasurement(
            String cameraName,
            Pose2d pose,
            double timestampSeconds,
            double tagArea,
            int tagCount,
            double primaryTagDistanceMeters,
            double poseErrorMeters
        ) {
            this.cameraName = cameraName;
            this.pose = pose;
            this.timestampSeconds = timestampSeconds;
            this.tagArea = tagArea;
            this.tagCount = tagCount;
            this.primaryTagDistanceMeters = primaryTagDistanceMeters;
            this.poseErrorMeters = poseErrorMeters;
        }
    }

    private static class CameraConfig {
        public final String name;
        public final NetworkTable table;

        public CameraConfig(String name, NetworkTable table) {
            this.name = name;
            this.table = table;
        }
    }

    private final NetworkTable shooterTable = NetworkTableInstance.getDefault().getTable("limelight-shooter");
    private final NetworkTable leftTable = NetworkTableInstance.getDefault().getTable("limelight-left");
    private final NetworkTable rightTable = NetworkTableInstance.getDefault().getTable("limelight-right");
    private Pose2d lastAcceptedVisionPose = null;

    private final List<CameraConfig> cameras = List.of(
        new CameraConfig("shooter", shooterTable),
        new CameraConfig("left", leftTable),
        new CameraConfig("right", rightTable)
    );

    Limelight() {}

    public List<VisionMeasurement> getAcceptedVisionMeasurements(Pose2d currentOdometryPose) {
        List<VisionMeasurement> accepted = new ArrayList<>();

        for (CameraConfig camera : cameras) {
            Optional<VisionMeasurement> measurement = getMeasurementFromCamera(camera, currentOdometryPose);
            measurement.ifPresent(accepted::add);
        }

        accepted.sort(Comparator
            .comparingInt((VisionMeasurement m) -> -m.tagCount)
            .thenComparingDouble(m -> m.poseErrorMeters)
            .thenComparingDouble(m -> m.primaryTagDistanceMeters));

        return accepted;
    }

    private Optional<VisionMeasurement> getMeasurementFromCamera(CameraConfig camera, Pose2d currentOdometryPose) {
        NetworkTable table = camera.table;

        double tv = table.getEntry("tv").getDouble(0.0);
        if (tv < 1.0) return Optional.empty();

        String poseEntry = getBotPoseEntryName();
        double[] botpose = table.getEntry(poseEntry).getDoubleArray(new double[0]);
        if (botpose.length < 7) return Optional.empty();

        double x = botpose[0];
        double y = botpose[1];
        double yawDeg = botpose[5];
        double latencyMs = botpose[6];

        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(yawDeg) || !Double.isFinite(latencyMs)) {
            return Optional.empty();
        }

        Pose2d pose = new Pose2d(x, y, Rotation2d.fromDegrees(yawDeg));
        double timestampSeconds = Timer.getFPGATimestamp() - latencyMs / 1000.0;

        if (!isPoseInsideField(pose)) return Optional.empty();

        double ta = table.getEntry("ta").getDouble(0.0);
        double tid = table.getEntry("tid").getDouble(-1.0);

        int tagCount = tid >= 0 ? 1 : 0;
        double poseErrorMeters = pose.getTranslation().getDistance(currentOdometryPose.getTranslation());

        if (poseErrorMeters > 2.5) return Optional.empty();
        if (ta < 0.1) return Optional.empty();

        if (lastAcceptedVisionPose != null) {
            double delta = pose.getTranslation().getDistance(lastAcceptedVisionPose.getTranslation());
            if (delta > 5.0) return Optional.empty();
        }

        double distanceMeters = estimateDistanceMeters(table);
        
        lastAcceptedVisionPose = pose;

        return Optional.of(new VisionMeasurement(
            camera.name,
            pose,
            timestampSeconds,
            ta,
            tagCount,
            distanceMeters,
            poseErrorMeters
        ));
    }

    private boolean isPoseInsideField(Pose2d pose) {
        return pose.getX() > -0.1
            && pose.getX() < 17.0
            && pose.getY() > -0.1
            && pose.getY() < 9.0;
    }

    private String getBotPoseEntryName() {
        var alliance = DriverStation.getAlliance();

        // comment in if botpose_wpired is needed
        // if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red) {
        //     return "botpose_wpired";
        // }
        return "botpose_wpiblue";
    }

    private double estimateDistanceMeters(NetworkTable table) {
        double ty = table.getEntry("ty").getDouble(0.0);
        double mountAngleDeg = frc.robot.Constants.LimelightConstants.LIMELIGHT_MOUNTING_ANGLE;
        double lensHeightInches = frc.robot.Constants.LimelightConstants.LIMELIGHT_LENS_HEIGHT;
        double goalHeightInches = frc.robot.Constants.AutoAlignConstants.TARGET_HEIGHT;

        double angleDeg = mountAngleDeg + ty;
        double angleRad = Math.toRadians(angleDeg);

        if (Math.abs(Math.tan(angleRad)) < 1e-6) return 999.0;

        double distanceInches = (goalHeightInches - lensHeightInches) / Math.tan(angleRad);
        return distanceInches * 0.0254;
    }

    public Matrix<N3, N1> getStdDevsForMeasurement(VisionMeasurement measurement) {
        double xy;
        double theta;

        if (measurement.tagCount >= 2) {
            xy = 0.15;
            theta = Math.toRadians(8.0);
        } else if (measurement.primaryTagDistanceMeters < 2.5) {
            xy = 0.25;
            theta = Math.toRadians(15.0);
        } else {
            xy = 0.6;
            theta = Math.toRadians(30.0);
        }

        return VecBuilder.fill(xy, xy, theta);
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("LL Shooter Has Target", shooterTable.getEntry("tv").getDouble(0.0) >= 1.0);
        SmartDashboard.putBoolean("LL Left Has Target", leftTable.getEntry("tv").getDouble(0.0) >= 1.0);
        SmartDashboard.putBoolean("LL Right Has Target", rightTable.getEntry("tv").getDouble(0.0) >= 1.0);
    }
}