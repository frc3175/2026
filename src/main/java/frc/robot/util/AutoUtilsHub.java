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

        angle = ((angle % 360) + 360) % 360;

        return angle;
    }

    private static Translation2d getGoalPose(boolean isBlueAlliance) {
        if (isBlueAlliance) {
            return new Translation2d(4.6269, 4.034663); //TODO: move to constants
        } else {
            return new Translation2d(11.91409, 4.034663);
        }
    }

    public static Rotation2d getNewRotation(Limelight m_limelight,CommandSwerveDrivetrain drivetrain) {
        Pose2d botpose = drivetrain.getState().Pose;         
        PathConstraints constraints = new PathConstraints(4, 2, 2 * Math.PI, 4 * Math.PI); 
        Optional<Alliance> ally = DriverStation.getAlliance();
        Rotation2d goalRotation = new Rotation2d(getAngleToGoal(botpose, getGoalPose(ally.get() == Alliance.Blue)));
        return goalRotation;

       
        /* TODO: implement the following:
        1. Get current pose of robot
        2. Calculate closest point along designated shooting arc (make sure to account for the fact that its a semicircle not just a full circle)
        3. Make sure no conflicts
        */

    } 
}

