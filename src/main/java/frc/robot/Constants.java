package frc.robot;


import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
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
    public static final CommandXboxController OPERATOR_CONTROLER = new CommandXboxController(1);

    public class IntakeConstants{

        public static final int LEFTMOTORID = 37;
        public static final int RIGHTMOTORID = 25;
        public static final int RACKMOTORID = 23;

        public static final double CurrentLimit = 80.0; 

        public static final double HARDSTOPCURRENTLIMIT = 40.0;
        

        public static final double INTAKEIN = 1;
        public static final double OUTTAKE = -0.5;
        public static final double STOP = 0;
        public static final double RACKHOME = 0.25;
        public static final double RACKMAX = -0.25;
        public static final double RACKHOLD = 0;
       
    }
    
    public class ShooterConstants{
        public static final int FRONTLEFTMOTORID = 13;
        public static final int FRONTRIGHTMOTORID = 11;
        public static final int BACKRIGHTMOTORID = 32;

        public static final double SHOOTERCURRENTLIMIT = 80.0; 
        // public static final double SPINSPEED = -48.75;
        // public static final double SHOOTER_P = 2;
        // public static final double SHOOTER_I = 0;
        // public static final double SHOOTER_D = 2;
        // public static final double SHOOTER_V = 8;
        // public static final double SHOOTER_S = 0;
        // public static final double SHOOTER_A = 80;

        public static final double SPINSPEED = -48.5;
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

    public class HopperConstants{
        public static final int HOPPERFLOORMOTORID = 44;
        public static final double HOPPERCURRENTLIMIT = 40.0;
        
        public static final double HOPPER_P = 0.8;
        public static final double HOPPER_I = 0;
        public static final double HOPPER_D = 0;
        
        public static final double HOPPER_V = 0.12;
        public static final double HOPPER_S = 0;
        public static final double HOPPER_A = 0;
        
        public static final double FLOORSPEED = -1; 
    }

    public class TowerConstants{

        public static final int KICKERID = 12;
        public static final int OPKICKID = 21;

        public static final double TOWERCURRENTLIMIT = 80.0; 
        public static final double RUNSPEED = -60;
        public static final double TOWER_P = 0.9;
        public static final double TOWER_I = 0;
        public static final double TOWER_D = 0.2;
        public static final double TOWER_V = 0.12;
        public static final double TOWER_S = 0;
        public static final double TOWER_A = 15;
       
        public static final double STOP = 0;
        

    }

    public class LimelightConstants {
        public static final double LIMELIGHT_BACK_OFFSET = -11.25;
        public static final double LIMELIGHT_MOUNTING_ANGLE = 31.24; 
        public static final double LIMELIGHT_LENS_HEIGHT = 29.0;
        public static final double LIMELIGHT_OFFSET = 11.5;
    }

    public class AutoAlignConstants { 
        public static final int REDHUBID = 10;
        public static final int BLUEHUBID = 25;
        public static final double TARGET_HEIGHT = 52;
        public static final double LIMELIGHT_ANGLE_P = 0.011;
        public static final double MAX_ANGULAR_VELOCITY = Math.PI * 4.12 * 0.5;
    }

    /** Meters per Second */
    public static final double MAX_SPEED = Units.feetToMeters(17.94);
}
