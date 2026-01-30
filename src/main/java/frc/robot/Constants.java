package frc.robot;

import java.security.PublicKey;

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

        public static final int LEFTMOTORID = 46;
        public static final int RIGHTMOTORID = 98;
        public static final int RACKMOTORID = 99;

        public static final double CurrentLimit = 40.0; 
        

        public static final double INTAKEIN = 12;
        public static final double OUTTAKE = -6;
        public static final double STOP = 0;
        public static final double RACKHOME = -5;
        public static final double RACKMAX = 0;
       
    
    }
      
    public class ClimberConstants{

        public static final int MOTORID = 22;

        public static final int SERVOPORT = 0;
        
        public static final double HOME = 0;
    }
    
    public class ShooterConstants{
        public static final int LEFTMOTORID = 100;
        public static final int RIGHTMOTORID = 101;

        public static final double CurrentLimit = 40.0; 
        public static final double SPINSPEED = 12;
    }

    public class HopperConstants{
        public static final int FLOORMOTORID = 102;

        public static final double CurrentLimit = 40.0; 

        
    }

    public class TowerConstants{

        public static final int LEFTROLLERID = 103;
        public static final int RIGHTROLLERID = 104;
        public static final int KICKERID = 105;

        public static final double CurrentLimit = 40.0; 
        

        public static final double RUNSPEED = 12;
        public static final double STOP = 0;
        

    }

    public class Buttons{
        public static final Trigger FIELDRESET = DRIVER_CONTROLER.x();
        public static final Trigger AUTOALIGNLEFT = DRIVER_CONTROLER.leftTrigger();
        public static final Trigger AUTOALIGNRIGHT = DRIVER_CONTROLER.rightTrigger();

        public static final Trigger SHOOT = DRIVER_CONTROLER.a();

        public static final Trigger INTAKE =  OPERATOR_CONTROLER.rightBumper();

        public static final Trigger SPINUP = OPERATOR_CONTROLER.leftBumper();
        
        public static final Trigger EXTENDHOP = OPERATOR_CONTROLER.x();
        public static final Trigger RETRACTHOP = OPERATOR_CONTROLER.b();
        

    }

   

   
    

     
    
}
