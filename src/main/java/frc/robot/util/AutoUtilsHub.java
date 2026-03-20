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

@SuppressWarnings("all")

public class AutoUtilsHub {

    public AutoUtilsHub() {
        // Constructor
    }

    private static double getAngleToGoal(Pose2d currentPosition, Translation2d goalPosition) {

        double dx = goalPosition.getX() - currentPosition.getX();
        double dy = goalPosition.getY()- currentPosition.getY();

        double angle = Math.toDegrees(Math.atan2(dy, dx)); //dy could be negative

        angle = ((angle % (2*Math.PI)) + (2*Math.PI)) % (2*Math.PI);

        return angle;
    }

    private static Translation2d getGoalPose(boolean isBlueAlliance) {
        if (isBlueAlliance) {
            return new Translation2d(4.6269, 4.034663); //TODO: move to constants
        } else {
            return new Translation2d(11.91409, 4.034663);
        }
    }

    private static boolean getIsBlueAlliance() {
        Optional<Alliance> ally = DriverStation.getAlliance();
        return ally.get() == Alliance.Blue;
    }

    public static Rotation2d getOrbitRotation(Limelight m_limelight,CommandSwerveDrivetrain drivetrain) {
        Pose2d botpose = drivetrain.getState().Pose;      
        Rotation2d goalRotation = new Rotation2d(getAngleToGoal(botpose, getGoalPose(getIsBlueAlliance())));
        return goalRotation;

       
        /* TODO: implement the following:
        1. Get current pose of robot
        2. Calculate closest point along designated shooting arc (make sure to account for the fact that its a semicircle not just a full circle)
        3. Make sure no conflicts
        */

    } 
    public static Translation2d getOrbitVelocity(CommandSwerveDrivetrain drivetrain, double yAxisJoystick, double xAxisJoystick, double maxSpeed) {
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
}

