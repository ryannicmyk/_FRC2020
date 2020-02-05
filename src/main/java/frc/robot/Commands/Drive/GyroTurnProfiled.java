package frc.robot.Commands.Drive;

import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import frc.robot.Robot;
import frc.robot.Constants.DriveConstants;

public class GyroTurnProfiled extends ProfiledPIDCommand 
{
  ProfiledPIDController controller;
  /**
   * Turns to robot to the specified angle using a motion profile.
   *
   * @param targetAngleDegrees The angle to turn to
   */
  public GyroTurnProfiled(double targetAngleDegrees) 
  {
    super(new ProfiledPIDController(DriveConstants.turnP, DriveConstants.turnI,
          DriveConstants.turnD, new TrapezoidProfile.Constraints(
          DriveConstants.maxTurnRateDegPerS,
          DriveConstants.maxTurnAccelerationDegPerSSquared)),
      // Close loop on heading
      Robot.drivetrain::getHeading,
      // Set reference to target
      targetAngleDegrees,
      // Pipe output to turn robot
      (output, setpoint) -> Robot.drivetrain.arcadeDrive(0, output),
      // Require the drive
      Robot.drivetrain);
      addRequirements(Robot.drivetrain);

    
    // Set the controller to be continuous (because it is an angle controller)
    getController().enableContinuousInput(-180, 180);
    // Set the controller tolerance - the delta tolerance ensures the robot is stationary at the
    // setpoint before it is considered as having reached the reference
    getController().setTolerance(DriveConstants.turnToleranceDeg, DriveConstants.turnRateToleranceDegPerS);
  }

  @Override
  public boolean isFinished() 
  {
    // End when the controller is at the reference.
    return getController().atGoal();
  }
}