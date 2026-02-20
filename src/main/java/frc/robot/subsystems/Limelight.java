package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

    private final NetworkTable limelightShooter = NetworkTableInstance.getDefault().getTable("limelight-shooter");
    private final NetworkTable limelightLeft = NetworkTableInstance.getDefault().getTable("limelight-left");
    private final NetworkTable limelightRight = NetworkTableInstance.getDefault().getTable("limelight-right");

    public Limelight() {
        // Limelight setup, if any
    }

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