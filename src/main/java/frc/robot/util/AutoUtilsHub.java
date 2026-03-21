package frc.robot.util;

import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.util.ShooterLookup;

@SuppressWarnings("all")

public class AutoUtilsHub {

    public AutoUtilsHub() {
        // Constructor
    }

    private static double getAngleToGoal(Pose2d currentPosition, Translation2d goalPosition) {

        double dx = goalPosition.getX() - currentPosition.getX();
        double dy = goalPosition.getY()- currentPosition.getY();

        double angle = Math.toDegrees(Math.atan2(dy, dx)); //dy could be negative

        return angle;
    }

    private static double getDistanceToGoal(Pose2d currentPosition, Translation2d goalPosition) {
        Translation2d diff = goalPosition.minus(currentPosition.getTranslation());
        return diff.getNorm();
    }

    private static Translation2d getDifferenceToGoal(Pose2d currentPosition, Translation2d goalPosition) {
        return goalPosition.minus(currentPosition.getTranslation());
    }

    private static Translation2d getGoalPose(boolean isBlueAlliance) {
        if (isBlueAlliance) {
            return new Translation2d(Constants.FieldConstants.BLUE_HUB.getX(), Constants.FieldConstants.BLUE_HUB.getY());
        } else {
            return new Translation2d(Constants.FieldConstants.RED_HUB.getX(), Constants.FieldConstants.RED_HUB.getY());
        }
    }

    private static boolean getIsBlueAlliance() {
        Optional<Alliance> ally = DriverStation.getAlliance();
        return ally.get() == Alliance.Blue;
    }

    public static Rotation2d getOrbitRotation(CommandSwerveDrivetrain drivetrain) {
        Pose2d botpose = drivetrain.getState().Pose;      
        Rotation2d goalRotation = new Rotation2d(getAngleToGoal(botpose, getGoalPose(getIsBlueAlliance())));
        return goalRotation;

    } 
    public static Translation2d getOrbitTranslation(CommandSwerveDrivetrain drivetrain, double yAxisJoystick, double xAxisJoystick, double maxSpeed) {
        Pose2d robotPose = drivetrain.getState().Pose;
        Translation2d goalPose = getGoalPose(getIsBlueAlliance());

        Translation2d delta = goalPose.minus(robotPose.getTranslation());
        double distance = delta.getNorm();
        if (distance < 0.01) {
            distance = 0.01;
        }

        Translation2d radial = delta.times(1.0 / distance);
        Translation2d tangent = new Translation2d(-radial.getY(), radial.getX());
        Translation2d velocity = radial.times(yAxisJoystick * maxSpeed)
                                 .plus(tangent.times(xAxisJoystick * maxSpeed));

        return velocity;
    }

    public static Rotation2d calculateOrbitRotationOffset(CommandSwerveDrivetrain drivetrain, double shooterAngle, Translation2d velocity) {
        double distanceToGoal = getDistanceToGoal(drivetrain.getState().Pose, getGoalPose(getIsBlueAlliance()));
        Translation2d delta = getDifferenceToGoal(drivetrain.getState().Pose, getGoalPose(getIsBlueAlliance()));
        Translation2d radial = delta.times(1 / distanceToGoal);
        Translation2d tangent = new Translation2d(-radial.getY(), radial.getX());
        double tangentSpeed = velocity.getX() * tangent.getX() + velocity.getY() * tangent.getY();
        double shooterRPM = ShooterLookup.calculateFlywheelVelocity(distanceToGoal);
        double shooterVelocity = (shooterRPM * 2 * Math.PI / 60.0) * Constants.ShooterConstants.WHEELRADIUS;

        double flightTime = distanceToGoal / (shooterVelocity * Math.cos(shooterAngle));

        double lateralOffset = tangentSpeed * flightTime;

        double angleOffset = Math.atan2(lateralOffset, distanceToGoal) * Constants.ShooterConstants.ANGLECOEFFICIENT;

        return new Rotation2d(angleOffset);
    }
}

