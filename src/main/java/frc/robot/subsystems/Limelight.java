package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.LimelightHelpers.RawFiducial;

public class Limelight extends SubsystemBase {

    public static class VisionMeasurement {
        public final String cameraName;
        public final Pose2d pose;
        public final double timestampSeconds;
        public final double avgTagArea;
        public final int tagCount;
        public final double avgTagDistanceMeters;
        public final double poseErrorMeters;
        public final boolean isMegaTag2;

        public VisionMeasurement(
            String cameraName,
            Pose2d pose,
            double timestampSeconds,
            double avgTagArea,
            int tagCount,
            double avgTagDistanceMeters,
            double poseErrorMeters,
            boolean isMegaTag2
        ) {
            this.cameraName = cameraName;
            this.pose = pose;
            this.timestampSeconds = timestampSeconds;
            this.avgTagArea = avgTagArea;
            this.tagCount = tagCount;
            this.avgTagDistanceMeters = avgTagDistanceMeters;
            this.poseErrorMeters = poseErrorMeters;
            this.isMegaTag2 = isMegaTag2;
        }
    }

    private static class CameraConfig {
        public final String name;

        public CameraConfig(String name) {
            this.name = name;
        }
    }

    private final List<CameraConfig> cameras = List.of(
        new CameraConfig("limelight-shooter"),
        new CameraConfig("limelight-left"),
        new CameraConfig("limelight-right")
    );

    private Pose2d lastAcceptedVisionPose = null;

    public Limelight() {}

    public List<VisionMeasurement> getAcceptedVisionMeasurements(Pose2d currentOdometryPose) {
        List<VisionMeasurement> accepted = new ArrayList<>();

        for (CameraConfig camera : cameras) {
            Optional<VisionMeasurement> measurement = getMeasurementFromCamera(camera, currentOdometryPose);
            measurement.ifPresent(accepted::add);
        }

        accepted.sort(
            Comparator.comparingInt((VisionMeasurement m) -> -m.tagCount)
                .thenComparingDouble(m -> m.avgTagDistanceMeters)
                .thenComparingDouble(m -> m.poseErrorMeters)
        );

        return accepted;
    }

    private Optional<VisionMeasurement> getMeasurementFromCamera(CameraConfig camera, Pose2d currentOdometryPose) {
        PoseEstimate estimate = getPoseEstimate(camera.name);
        if (estimate == null) return Optional.empty();
        if (!LimelightHelpers.validPoseEstimate(estimate)) return Optional.empty();
        if (estimate.pose == null) return Optional.empty();

        Pose2d pose = estimate.pose;

        if (!Double.isFinite(pose.getX()) || !Double.isFinite(pose.getY()) || !Double.isFinite(pose.getRotation().getRadians())) {
            return Optional.empty();
        }

        if (!isPoseInsideField(pose)) return Optional.empty();

        if (estimate.tagCount < 1) return Optional.empty();

        if (estimate.avgTagArea < 0.1) return Optional.empty();

        if (estimate.tagCount == 1 && estimate.avgTagDist > 4.0) return Optional.empty();

        if (!passesAmbiguityCheck(estimate.rawFiducials, estimate.tagCount)) return Optional.empty();

        double poseErrorMeters = pose.getTranslation().getDistance(currentOdometryPose.getTranslation());
        if (poseErrorMeters > 2.0) return Optional.empty();

        if (lastAcceptedVisionPose != null) {
            double deltaFromLastVision = pose.getTranslation().getDistance(lastAcceptedVisionPose.getTranslation());
            if (deltaFromLastVision > 5.0) return Optional.empty();
        }

        lastAcceptedVisionPose = pose;

        return Optional.of(new VisionMeasurement(
            camera.name,
            pose,
            estimate.timestampSeconds,
            estimate.avgTagArea,
            estimate.tagCount,
            estimate.avgTagDist,
            poseErrorMeters,
            estimate.isMegaTag2
        ));
    }

    private PoseEstimate getPoseEstimate(String limelightName) {
        if (useMegaTag2()) {
            return LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(limelightName);
        }
        return LimelightHelpers.getBotPoseEstimate_wpiBlue(limelightName);
    }

    private boolean useMegaTag2() {
        return Constants.LimelightConstants.USES_MT2;
    }

    private boolean passesAmbiguityCheck(RawFiducial[] rawFiducials, int tagCount) {
        if (tagCount >= 2) return true;
        if (rawFiducials == null || rawFiducials.length == 0) return false;

        double bestAmbiguity = Double.MAX_VALUE;
        for (RawFiducial fid : rawFiducials) {
            if (fid != null) {
                bestAmbiguity = Math.min(bestAmbiguity, fid.ambiguity);
            }
        }

        return bestAmbiguity <= 0.1;
    }

    private boolean isPoseInsideField(Pose2d pose) {
        return pose.getX() > -0.1
            && pose.getX() < 17.0
            && pose.getY() > -0.1
            && pose.getY() < 9.0;
    }

    public Matrix<N3, N1> getStdDevsForMeasurement(VisionMeasurement measurement) {
        double xy;
        double theta;

        if (measurement.tagCount >= 2 && measurement.avgTagDistanceMeters < 3.0) {
            xy = 0.12;
            theta = Math.toRadians(6.0);
        } else if (measurement.tagCount >= 2) {
            xy = 0.20;
            theta = Math.toRadians(10.0);
        } else if (measurement.avgTagDistanceMeters < 2.5) {
            xy = 0.35;
            theta = Math.toRadians(18.0);
        } else {
            xy = 0.75;
            theta = Math.toRadians(999.0); // basically do not trust heading much on weak single-tag
        }

        return VecBuilder.fill(xy, xy, theta);
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("LL Shooter Has Target", LimelightHelpers.getTV("limelight-shooter"));
        SmartDashboard.putBoolean("LL Left Has Target", LimelightHelpers.getTV("limelight-left"));
        SmartDashboard.putBoolean("LL Right Has Target", LimelightHelpers.getTV("limelight-right"));

        SmartDashboard.putNumber("LL Shooter Target Count", LimelightHelpers.getTargetCount("limelight-shooter"));
        SmartDashboard.putNumber("LL Left Target Count", LimelightHelpers.getTargetCount("limelight-left"));
        SmartDashboard.putNumber("LL Right Target Count", LimelightHelpers.getTargetCount("limelight-right"));
    }
}