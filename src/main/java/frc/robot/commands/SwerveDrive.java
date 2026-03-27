package frc.robot.commands;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveDriveState;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.FieldCentricFacingAngle;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.geometry.Translation2dPlus;
import frc.robot.Constants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.util.AutoUtilsHub;




public class SwerveDrive extends Command {    
    
    private CommandSwerveDrivetrain m_swerveDrivetrain;    
    private DoubleSupplier m_translationSup;
    private DoubleSupplier m_strafeSup;
    private DoubleSupplier m_rotationSup;
    private BooleanSupplier m_robotCentricSup;
    private BooleanSupplier m_isEvading;
    public BooleanSupplier m_isCrawling;
    private BooleanSupplier m_isAligning;
    public SlewRateLimiter xAxisLimiter;
    public SlewRateLimiter yAxisLimiter;
    
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    private final SwerveRequest.FieldCentricFacingAngle aligneddrive = new FieldCentricFacingAngle()
        .withHeadingPID(Constants.AutoAlignConstants.ALIGN_P, Constants.AutoAlignConstants.ALIGN_I, Constants.AutoAlignConstants.ALIGN_D);

    
    private static final Translation2d[] WHEEL_POSITIONS =
        Arrays.copyOf(Constants.moduleTranslations, Constants.moduleTranslations.length);
        

    public SwerveDrive(CommandSwerveDrivetrain swerveDrivetrain, 
                       DoubleSupplier translationSup, 
                       DoubleSupplier strafeSup, 
                       DoubleSupplier rotationSup, 
                       BooleanSupplier robotCentricSup, 
                       BooleanSupplier isEvading,
                       Limelight ll,
                       BooleanSupplier isAligning) {

        m_swerveDrivetrain = swerveDrivetrain;
        addRequirements(m_swerveDrivetrain);

        m_translationSup = translationSup;
        m_strafeSup = strafeSup;
        m_rotationSup = rotationSup;
        m_robotCentricSup = robotCentricSup;
        m_isEvading = isEvading;
        m_isAligning = isAligning;
     
        xAxisLimiter = new SlewRateLimiter(Constants.slewRate);
        yAxisLimiter = new SlewRateLimiter(Constants.slewRate);

        aligneddrive.HeadingController.enableContinuousInput(-Math.PI, Math.PI);
        aligneddrive.HeadingController.setTolerance(Math.toRadians(2));
    }

    private Translation2d getCenterOfRotation(final Rotation2d direction, final double rotation) {

        SwerveDriveState m_swerveState = new SwerveDriveState();
        Rotation2d yaw = m_swerveState.RawHeading;

        final var here = new Translation2dPlus(1.0, direction.minus(yaw));

        var cwCenter = WHEEL_POSITIONS[0];
        var ccwCenter = WHEEL_POSITIONS[WHEEL_POSITIONS.length - 1];

        for (int i = 0; i < WHEEL_POSITIONS.length - 1; i++) {
            final var cw = WHEEL_POSITIONS[i];
            final var ccw = WHEEL_POSITIONS[i + 1];

            if (here.isWithinAngle(cw, ccw)) {
                cwCenter = ccw;
                ccwCenter = cw;
            }
        }

        // if clockwise
        if (Math.signum(rotation) == 1.0) {
            return cwCenter;
        } else if (Math.signum(rotation) == -1.0) {
            return ccwCenter;
        } else {
            return new Translation2d();
        }
    }

    @Override
    public void execute() {

        double xAxis = MathUtil.applyDeadband(m_translationSup.getAsDouble(), Constants.stickDeadband);
        double yAxis = MathUtil.applyDeadband(m_strafeSup.getAsDouble(), Constants.stickDeadband);
        double rAxis = MathUtil.applyDeadband(m_rotationSup.getAsDouble(), Constants.stickDeadband);

        double xAxisSquared = xAxis * xAxis * Math.signum(xAxis);
        double yAxisSquared = yAxis * yAxis * Math.signum(yAxis);
        double rAxisSquared = rAxis * rAxis * Math.signum(rAxis);

        


        double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
        double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

        Translation2d newCenterOfRotation = new Translation2d();
        Translation2d translation = new Translation2d(m_translationSup.getAsDouble(), m_strafeSup.getAsDouble()).times(MaxSpeed);

       

         if(m_isEvading.getAsBoolean() && m_robotCentricSup.getAsBoolean()) {
             newCenterOfRotation = getCenterOfRotation(translation.getAngle(), m_rotationSup.getAsDouble());
         } else {
             newCenterOfRotation = new Translation2d();
         }
            
            // Use open-loop control for drive motors

        
            double rAxisActual = rAxisSquared * MaxAngularRate * -1;
       
        if(!m_isAligning.getAsBoolean()) {
            m_swerveDrivetrain.setControl(
                drive.withVelocityX( xAxisSquared * MaxSpeed ) // Drive forward with negative Y (forward)
                    .withVelocityY( yAxisSquared * MaxSpeed ) // Drive left with negative X (left)
                    .withRotationalRate(rAxisActual )
                    .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
                    .withCenterOfRotation(newCenterOfRotation));

                    SmartDashboard.putNumber("rotationamount", rAxisActual);
        } else {
            Translation2d velocity = AutoUtilsHub.getOrbitTranslation(m_swerveDrivetrain, yAxisSquared, xAxisSquared, MaxSpeed);
            m_swerveDrivetrain.setControl(
                aligneddrive
                    .withForwardPerspective(SwerveRequest.ForwardPerspectiveValue.BlueAlliance) //TODO: see if this fixes zero issue
                    .withVelocityX(velocity.getX()) // Drive forward with negative Y (forward)
                    .withVelocityY(velocity.getY()) // Drive left with negative X (left)
                    .withTargetDirection(AutoUtilsHub.getOrbitRotation(m_swerveDrivetrain)
          //              .plus(AutoUtilsHub.calculateOrbitRotationOffset(m_swerveDrivetrain, Units.degreesToRadians(Constants.ShooterConstants.SHOOTERANGLE), velocity)) //shoot while moving???
                    )); 
        }
    

                    
                    
    

                    

    }
}