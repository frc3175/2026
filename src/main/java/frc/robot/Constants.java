package frc.robot;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import static edu.wpi.first.units.Units.Feet;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;


public class Constants {
    public static final double TRACK_WIDTH = Units.inchesToMeters(23.5);
    public static final double WHEEL_BASE = Units.inchesToMeters(23.5);

     public static final Translation2d[] moduleTranslations = new Translation2d[]{
            new Translation2d(WHEEL_BASE / 2.0, TRACK_WIDTH / 2.0),
            new Translation2d(WHEEL_BASE / 2.0, -TRACK_WIDTH / 2.0),
            new Translation2d(-WHEEL_BASE / 2.0, TRACK_WIDTH / 2.0),
            new Translation2d(-WHEEL_BASE / 2.0, -TRACK_WIDTH / 2.0)};

            public static final double stickDeadband = 0.1;


    public static final String CANIVORE = "elevatoryiboi";
    public static final String RIO = "rio";

    public static final CommandXboxController DRIVER_CONTROLER = new CommandXboxController(0);
    
    public static final double slewRate = 1.5;

    public class IntakeConstants{

        public static final int LEFTMOTORID = 35;
        public static final int RIGHTMOTORID = 25;
        public static final int RACKMOTORID = 23;
        // CANcoder/encoder ID for the intake pivot
        public static final int PIVOTENCODERID = 33;
        public static final double PIVOTENCODEROFFSET = 176.923828125 / 360; //TODO: TUNE

        public static final double PIVOTCURRENTLIMIT = 40.0; 
        public static final double ROLLERCURRENTLIMIT = 70.0;

        public static final double INTAKEIN = 1;
        public static final double OUTTAKE = -0.5;
        public static final double STOP = 0;
        public static final double PIVOTHOME = 15;
        public static final double PIVOTOUT = 75; //TODO: TUNE
       
        public static final double PIVOT_P = 5;
        public static final double PIVOT_I = 0;
        public static final double PIVOT_D = 0;

        public static final double PIVOT_S = 0;
        public static final double PIVOT_V = 0;
        public static final double PIVOT_G = 0;
        public static final double PIVOT_A = 0;


        public static final double ROLLER_P = 0.2;
        public static final double ROLLER_I = 0;
        public static final double ROLLER_D = 0;

        public static final double ROLLER_V = 0.12;
        public static final double ROLLER_S = 0.02;
    // Intake roller / pivot presets (tune these values for your robot)
    public static final double INTAKE_ROLLER_VELOCITY = -1;
    public static final double SPINUP_ROLLER_VELOCITY = -0.3;
    public static final double SHOOT_ROLLER_VELOCITY = 0;
    public static final double CARRY_ROLLER_VELOCITY = 0.0;
    public static final double RESET_ROLLER_VELOCITY = 0.0;
    public static final double UNCLOG_ROLLER_VELOCITY = 0.0;
    public static final double AGITATE_ROLLER_PERCENT = -0.5;

    public static final double INTAKE_PIVOT_POSITION = PIVOTOUT; // default: folded
    public static final double SPINUP_PIVOT_POSIITON = 65; //TODO: tune
    public static final double SHOOT_PIVOT_POSITION = 56;     //TODO: tune// example value (degrees/encoder units)
    public static final double CARRY_PIVOT_POSITION = 65; //TODO: tune
    public static final double RESET_PIVOT_POSITION = PIVOTHOME;
    public static final double UNCLOG_PIVOT_POSITION = 70; //TODO: tune
    public static final double AGITATE_PIVOT_POSITION = 20;
        
    }
    
    public class ShooterConstants{
        public static final int FRONTLEFTMOTORID = 13;
        public static final int FRONTRIGHTMOTORID = 11;
        public static final int BACKRIGHTMOTORID = 32;

        public static final double SHOOTERANGLE = 67; //TODO: FIX THIS ANGLE
        public static final double ANGLECOEFFICIENT = 1.0; //TODO: TUNE FOR SHOOT ON MOVE (PROBOBALY LOWER)
        public static final double WHEELRADIUS = 2.0;

        public static final double SHOOTERCURRENTLIMIT = 45.0; 

        public static final double TOWERSPINSPEED = -47.5;
        public static final double TRENCHSPINSPEED = -49.9;
        public static final double SHOOTER_P = 4.25;
        public static final double SHOOTER_I = 0;
        public static final double SHOOTER_D = 0;
        public static final double SHOOTER_V = 0.12;
        public static final double SHOOTER_S = 0;
        public static final double SHOOTER_A = 1;

        public static final double IDLESPEED = 0;
        /* 
        current good values
        public static final double SPINSPEED = -49.75;
        public static final double SHOOTER_P = 1.2;
        public static final double SHOOTER_I = 0;
        public static final double SHOOTER_D = 0;
        public static final double SHOOTER_V = 0.12;
        public static final double SHOOTER_S = 0;
        public static final double SHOOTER_A = 0;

        another good set 
        public static final double CurrentLimit = 80.0; 
        public static final double SPINSPEED = -48;
        public static final double SHOOTER_P = 1.2;
        public static final double SHOOTER_I = 0;
        public static final double SHOOTER_D = 1;
        public static final double SHOOTER_V = 0.12;
        public static final double SHOOTER_S = 0;
        public static final double SHOOTER_A = 5;
        */


    } 
    public static final class FieldConstants {
        public static final Pose2d BLUE_HUB = new Pose2d(4.625, 4.035, new Rotation2d());
        public static final Pose2d RED_HUB  = new Pose2d(11.92, 4.035, new Rotation2d());
        public static final Distance FIELD_LENGTH = Feet.of(54);
        public static final Distance FIELD_WIDTH = Feet.of(27);
    }


    public class HopperConstants{
        public static final int HOPPERFLOORMOTORID = 44;
        public static final int RIGHTSAUCEID = 14;
        public static final int LEFTSAUCEID = 15;
        public static final double HOPPERCURRENTLIMIT = 30.0;
        
        public static final double HOPPER_P = 0.8;
        public static final double HOPPER_I = 0;
        public static final double HOPPER_D = 0;
        
        public static final double HOPPER_V = 0.12;
        public static final double HOPPER_S = 0;
        public static final double HOPPER_A = 0;
        
        
        public static final double FLOORSPEED = -1; 
        public static final double REVERSEFLOORSPEED = 0.3;
        // Hopper velocity presets (tune for your mechanism)
        public static final double INTAKE_HOPPER_VELOCITY = -15.0;
        public static final double SPINUP_HOPPER_VELOCITY = 15;
        public static final double SHOOT_HOPPER_VELOCITY  = -90.0;
        public static final double CARRY_HOPPER_VELOCITY  = 0;
        public static final double RESET_HOPPER_VELOCITY  = 0.0;
        public static final double UNCLOG_HOPPER_VELOCITY  = 40;

        public static final double INTAKE_SIDEROLLER_PCTOUT = 0.0;
        public static final double SPINUP_SIDEROLLER_PCTOUT = 0.0;
        public static final double SHOOT_SIDEROLLER_PCTOUT  = -0.5;
        public static final double CARRY_SIDEROLLER_PCTOUT  = 0.0;
        public static final double RESET_SIDEROLLER_PCTOUT  = 0.0;
        public static final double UNCLOG_SIDEROLLER_PCTOUT  = 0.0;
    }

    public class TowerConstants{

        public static final int KICKERID = 12;
        public static final int OPKICKID = 21;

        // Legacy/alternate names expected elsewhere in the code
        public static final int TOWER_ID = KICKERID;
        public static final int OPPOSITE_TOWER_ID = OPKICKID;

        public static final double TOWERCURRENTLIMIT = 25.0; 
        public static final double RUNSPEED = -60;
        public static final double REVERSERUNSPEED = 60;
        public static final double TOWER_P = 0.9;
        public static final double TOWER_I = 0;
        public static final double TOWER_D = 0.2;
        public static final double TOWER_V = 0.12;
        public static final double TOWER_S = 0;
        public static final double TOWER_A = 15;
       
        public static final double STOP = 0;
    // Tower velocity presets (tune as needed)
    public static final double INTAKE_TOWER_VELOCITY = STOP;
    public static final double SHOOT_TOWER_VELOCITY  = -70.0;
    public static final double SPINUP_TOWER_VELOCITY = 25.0;
    public static final double CARRY_TOWER_VELOCITY  = STOP;
    public static final double RESET_TOWER_VELOCITY  = STOP;
    public static final double UNCLOG_TOWER_VELOCITY  = REVERSERUNSPEED;
        

    }

    public class LimelightConstants {
       // public static final double LIMELIGHT_BACK_OFFSET = -18.5;
        public static final double LIMELIGHT_MOUNTING_ANGLE = 31.24; 
        public static final double LIMELIGHT_LENS_HEIGHT = 29.0;
        //public static final double LIMELIGHT_OFFSET = 11.5;
        public static final double LIMELIGHT_OFFSET = -18.5;
        public static final double LIMELIGHT_BACK_OFFSET = 11.5;
        public static final boolean USES_MT2 = false;

    }

    public class AutoAlignConstants { 
     public static final double RED_ANGLE_OFFSET = 68;
     public static final double BLUE_ANGLE_OFFSET = 240 ;

     public static final double ALIGN_P = 4;
     public static final double ALIGN_I = 0;
     public static final double ALIGN_D = 0.2;

    }

    /** Meters per Second */
    public static final double MAX_SPEED = Units.feetToMeters(17.94);
}
