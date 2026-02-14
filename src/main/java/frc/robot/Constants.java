package frc.robot;


import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Constants {
    public static final double TRACK_WIDTH = Units.inchesToMeters(22.875);
    public static final double WHEEL_BASE = Units.inchesToMeters(22.875);

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
        public static final int RACKMOTORID = 26;

        public static final double CurrentLimit = 40.0; 
        

        public static final double INTAKEIN = 1;
        public static final double OUTTAKE = -0.5;
        public static final double STOP = 0;
        public static final double RACKHOME = 0;
        public static final double RACKMAX = 0.1;

        public static final double INTAKE_P = 0.8;
        public static final double INTAKE_I = 0;
        public static final double INTAKE_D = 0;
        public static final double INTAKE_V = 0.12;
        public static final double INTAKE_S = 0;
        public static final double INTAKE_A = 0;

        public static final double RACK_P = 0.8;
        public static final double RACK_I = 0;
        public static final double RACK_D = 0;
        public static final double RACK_V = 0.12;
        public static final double RACK_S = 0;
        public static final double RACK_A = 0;
       
    
    }
      
    public class ClimberConstants{

        // public static final int MOTORID = 22;

        public static final int SERVOPORT = 0;
        
        public static final double HOME = 0;
    }
    
    public class ShooterConstants{
        public static final int LEFTMOTORID = 13;
        public static final int RIGHTMOTORID = 11;

        public static final double CurrentLimit = 40.0; 
        public static final double SPINSPEED = -0.57;
        public static final double SHOOTER_P = 1.2;
        public static final double SHOOTER_I = 0;
        public static final double SHOOTER_D = 0;
        public static final double SHOOTER_V = 0.12;
        public static final double SHOOTER_S = 0;
        public static final double SHOOTER_A = 0;
    

    }

    public class HopperConstants{
        public static final int FLOORMOTORID = 32;

        public static final double CurrentLimit = 40.0; 
        public static final double FLOORSPEED = -1;

        public static final double HOPPER_P = 0.8;
        public static final double HOPPER_I = 0;
        public static final double HOPPER_D = 0;
        public static final double HOPPER_V = 0.12;
        public static final double HOPPER_S = 0;
        public static final double HOPPER_A = 0;

        
    }

    public class TowerConstants{

        public static final int LEFTROLLERID = 22;
        public static final int RIGHTROLLERID = 23;
        public static final int KICKERID = 12;
        public static final int OPKICKID = 21;

        public static final double CurrentLimit = 40.0; 
        public static final double TOWER_P = 0.9;
        public static final double TOWER_I = 0;
        public static final double TOWER_D = 0.2;
        public static final double TOWER_V = 0.12;
        public static final double TOWER_S = 0;
        public static final double TOWER_A = 0;
        

        public static final double RUNSPEED = -1;
        public static final double STOP = 0;
        

    }

    public class Buttons{
        public static final Trigger FIELDRESET = DRIVER_CONTROLER.x();
        public static final Trigger AUTOALIGNLEFT = DRIVER_CONTROLER.leftTrigger();
        public static final Trigger AUTOALIGNRIGHT = DRIVER_CONTROLER.rightTrigger();

        public static final Trigger SHOOT = OPERATOR_CONTROLER.a();

      

        public static final Trigger SPINUP = OPERATOR_CONTROLER.leftBumper();
        
        public static final Trigger EXTENDHOP = OPERATOR_CONTROLER.x();
        public static final Trigger RETRACTHOP = OPERATOR_CONTROLER.b();
        

    }

   

   
    

     
    
}
