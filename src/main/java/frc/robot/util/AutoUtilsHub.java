package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants;
import frc.robot.subsystems.Limelight;

@SuppressWarnings("all")

public class AutoUtilsHub {

    public AutoUtilsHub() {
        // Constructor
    }
    
    public static Pose2d getNewPose(Limelight m_limelight, double input) {
        Double doubleInput = new Double(input);
        int intInput = doubleInput.intValue();
        Pose2d goalpose = new Pose2d();
       
        /* TODO: implement the following:
        1. Get current pose of robot
        2. Calculate closest point along designated shooting arc (make sure to account for the fact that its a semicircle not just a full circle)
        3. Make sure no conflicts
        */

        switch(intInput) {
            case Constants.AutoAlignConstants.REDHUBID:
                goalpose = new Pose2d(13.048 ,2.913,Rotation2d.fromDegrees(120));
                break;

            case Constants.AutoAlignConstants.BLUEHUBID:
                goalpose = new Pose2d(13.846 ,3.973 ,Rotation2d.fromDegrees(180));
                break; 
            default:
                goalpose = new Pose2d(); // Default case if input is not in the range
                break;
        }

        return goalpose;
    }

}

