package frc.robot.util;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;

@SuppressWarnings("all")

public class AutoUtilsHub {

    public AutoUtilsHub() {
        // Constructor
    }

    public static Pose2d getNewPose(Limelight m_limelight,CommandSwerveDrivetrain drivetrain ) {
        // Double doubleInput = new Double(input);
        // int intInput = doubleInput.intValue();
        Pose2d botpose = drivetrain.getState().Pose;         
        PathConstraints constraints = new PathConstraints(4, 2, 2 * Math.PI, 4 * Math.PI); 
        Pose2d currbotpose = drivetrain.getState().Pose;
        double xdiff = Math.abs(12 - currbotpose.getX());
        double ydiff = Math.abs(4 - currbotpose.getY());
        double currdistaway = Math.sqrt((xdiff*xdiff) + (ydiff*ydiff));
        double rangedist = Units.inchesToMeters(145) - currdistaway; //distance to get in range 145 in is in range
        double rangex = rangedist*currbotpose.getRotation().getCos();
        double rangey = rangedist*currbotpose.getRotation().getSin();
        Pose2d goalpose = new Pose2d(new Translation2d(currbotpose.getX() + rangex, currbotpose.getY() + rangey), Rotation2d.fromRadians(m_limelight.AimToTarget(botpose.getX(), botpose.getY(), 12, 4)));
        return goalpose;

       
        /* TODO: implement the following:
        1. Get current pose of robot
        2. Calculate closest point along designated shooting arc (make sure to account for the fact that its a semicircle not just a full circle)
        3. Make sure no conflicts
        */

    } 
}

