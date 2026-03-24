package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.epilogue.Logged.Strategy;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import static edu.wpi.first.units.Units.Meters;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;

@Logged(strategy = Strategy.OPT_IN)
public class Limelight extends SubsystemBase {

    // ===== Vision Measurement (kept but simplified usage) =====
    public static class VisionMeasurement {
        public final Pose2d pose;
        public final double timestampSeconds;
        public final int tagCount;
        public final double avgTagDistanceMeters;
        public final boolean isMegaTag2;

        public VisionMeasurement(
            Pose2d pose,
            double timestampSeconds,
            int tagCount,
            double avgTagDistanceMeters,
            boolean isMegaTag2
        ) {
            this.pose = pose;
            this.timestampSeconds = timestampSeconds;
            this.tagCount = tagCount;
            this.avgTagDistanceMeters = avgTagDistanceMeters;
            this.isMegaTag2 = isMegaTag2;
        }
    }

    // ===== Constants =====
    private static final double XY_STD_DEV_COEFFICIENT = 0.333;
    private static final double ROTATION_STD_DEV_COEFFICIENT = 1.5;
    private static final double MEGATAG2_ROTATION_STD_DEV = Double.POSITIVE_INFINITY;

    private static final double MAX_EFFECTIVE_TAG_COUNT = 2.5;

    private static final double MAX_AMBIGUITY = 0.3;
    private static final double FIELD_BORDER_MARGIN_METERS = 0.5;
    private static final double MAX_ANGULAR_VELOCITY_MT1_DEG_PER_SEC = 360;
    private static final double MAX_ANGULAR_VELOCITY_MT2_DEG_PER_SEC = 200;

    private final String m_limelightName;
    private final CommandSwerveDrivetrain m_drivetrain;
    private final double m_stdDevFactor;

    private PoseEstimate lastPoseEstimate = new PoseEstimate();

    public Limelight(String limelightName, CommandSwerveDrivetrain drivetrain, double stdDevFactor) {
        m_limelightName = limelightName;
        m_drivetrain = drivetrain;
        m_stdDevFactor = stdDevFactor;
    }

    public Limelight(String limelightName, CommandSwerveDrivetrain drivetrain) {
        this(limelightName, drivetrain, 1.0);
    }

    @Override
    public void periodic() {
        updateRobotOrientation();

        PoseEstimate poseEstimate = getValidPoseEstimate();
        if (poseEstimate == null) return;

        lastPoseEstimate = poseEstimate;
        addVisionMeasurement(poseEstimate);
    }

    // ===== Single-camera measurement (clean replacement) =====
    public List<VisionMeasurement> getAcceptedVisionMeasurements(Pose2d currentOdometryPose) {
        List<VisionMeasurement> list = new ArrayList<>();

        PoseEstimate estimate = getValidPoseEstimate();
        if (estimate == null) return list;

        list.add(new VisionMeasurement(
            estimate.pose,
            estimate.timestampSeconds,
            estimate.tagCount,
            estimate.avgTagDist,
            estimate.isMegaTag2
        ));

        return list;
    }

    private void updateRobotOrientation() {
        LimelightHelpers.SetRobotOrientation(
            m_limelightName,
            m_drivetrain.getPose().getRotation().getDegrees(),
            Math.toDegrees(m_drivetrain.getRobotSpeeds().omegaRadiansPerSecond),
            0, 0, 0, 0
        );
    }

    private PoseEstimate getValidPoseEstimate() {
        PoseEstimate poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue(m_limelightName);

        if (!LimelightHelpers.validPoseEstimate(poseEstimate)) return null;

        if (poseEstimate.rawFiducials[0].ambiguity > MAX_AMBIGUITY) return null;

        // ===== MT2 switching =====
        if (poseEstimate.tagCount == 1 && !DriverStation.isDisabled()) {
            if (isRotatingTooFastForMT2()) return null;

            PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(m_limelightName);
            if (!LimelightHelpers.validPoseEstimate(mt2)) return null;

            poseEstimate = mt2;
        }

        if (!poseEstimate.isMegaTag2 && isRotatingTooFastForMT1()) return null;

        if (!isPoseOnField(poseEstimate.pose)) return null;

        return poseEstimate;
    }

    private boolean isPoseOnField(Pose2d pose) {
        return pose.getX() >= -FIELD_BORDER_MARGIN_METERS
            && pose.getX() <= Constants.FieldConstants.FIELD_LENGTH.in(Meters) + FIELD_BORDER_MARGIN_METERS
            && pose.getY() >= -FIELD_BORDER_MARGIN_METERS
            && pose.getY() <= Constants.FieldConstants.FIELD_WIDTH.in(Meters) + FIELD_BORDER_MARGIN_METERS;
    }

    private boolean isRotatingTooFastForMT1() {
        double omega = Math.toDegrees(m_drivetrain.getRobotSpeeds().omegaRadiansPerSecond);
        return Math.abs(omega) > MAX_ANGULAR_VELOCITY_MT1_DEG_PER_SEC;
    }

    private boolean isRotatingTooFastForMT2() {
        double omega = Math.toDegrees(m_drivetrain.getRobotSpeeds().omegaRadiansPerSecond);
        return Math.abs(omega) > MAX_ANGULAR_VELOCITY_MT2_DEG_PER_SEC;
    }

    private void addVisionMeasurement(PoseEstimate poseEstimate) {
        double distanceFactor = Math.pow(poseEstimate.avgTagDist, 1.2);
        double effectiveTags = Math.min(MAX_EFFECTIVE_TAG_COUNT, poseEstimate.tagCount);
        double tagFactor = Math.pow(effectiveTags, 2.0);

        double xyStdDev = XY_STD_DEV_COEFFICIENT * distanceFactor / tagFactor * m_stdDevFactor;

        double rotationStdDev = poseEstimate.isMegaTag2
            ? MEGATAG2_ROTATION_STD_DEV
            : ROTATION_STD_DEV_COEFFICIENT * distanceFactor / tagFactor * m_stdDevFactor;

        m_drivetrain.addVisionMeasurement(
            poseEstimate.pose,
            poseEstimate.timestampSeconds,
            VecBuilder.fill(xyStdDev, xyStdDev, rotationStdDev)
        );
    }

    public Matrix<N3, N1> getStdDevsForMeasurement(VisionMeasurement measurement) {
        double distanceFactor = Math.pow(measurement.avgTagDistanceMeters, 1.2);

        double effectiveTags = Math.min(MAX_EFFECTIVE_TAG_COUNT, measurement.tagCount);
        double tagFactor = Math.pow(effectiveTags, 2.0);

        double xyStdDev =
            XY_STD_DEV_COEFFICIENT * distanceFactor / tagFactor * m_stdDevFactor;

        double rotationStdDev =
            measurement.isMegaTag2
                ? MEGATAG2_ROTATION_STD_DEV
                : ROTATION_STD_DEV_COEFFICIENT * distanceFactor / tagFactor * m_stdDevFactor;

        return VecBuilder.fill(xyStdDev, xyStdDev, rotationStdDev);
    }

    // ===== Logging =====
    @Logged public Pose2d getPose() { return lastPoseEstimate.pose; }
    @Logged public double getTimestampSeconds() { return lastPoseEstimate.timestampSeconds; }
    @Logged public double getAvgTagDist() { return lastPoseEstimate.avgTagDist; }
    @Logged public int getTagCount() { return lastPoseEstimate.tagCount; }
}