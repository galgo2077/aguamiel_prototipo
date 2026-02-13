// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import java.io.File;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Filesystem;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.networktables.StructPublisher;


//swerve modules

import static edu.wpi.first.units.Units.Meters;


public class swerve_subsystem extends SubsystemBase {
 
  
  //we make a directory so we can acces my values
  File directory = new File(Filesystem.getDeployDirectory(),"swerve");
  SwerveDrive  swerveDrive;

  // AdvantageScope publishers
  private final StructArrayPublisher<SwerveModuleState> measuredStatesPublisher;
  private final StructArrayPublisher<SwerveModuleState> desiredStatesPublisher;
  private final StructPublisher<Pose2d> posePublisher;
  private final StructPublisher<ChassisSpeeds> chassisSpeedsPublisher;

  
  public swerve_subsystem() {
    //use the max speed whit those two values
    try{
      //we create de swerve value
    swerveDrive = new SwerveParser(directory).createSwerveDrive(Constants.constants.MAXSPEED, new Pose2d(new Translation2d(Meters.of(1),Meters.of(4)),Rotation2d.fromDegrees(0)));
    }catch (Exception e){

      throw new RuntimeException(e);
    }

    // Initialize AdvantageScope publishers
    NetworkTableInstance inst = NetworkTableInstance.getDefault();

    measuredStatesPublisher = inst
      .getStructArrayTopic("Swerve/MeasuredStates", SwerveModuleState.struct)
      .publish();

    desiredStatesPublisher = inst
      .getStructArrayTopic("Swerve/DesiredStates", SwerveModuleState.struct)
      .publish();

    posePublisher = inst
      .getStructTopic("Swerve/RobotPose", Pose2d.struct)
      .publish();

    chassisSpeedsPublisher = inst
      .getStructTopic("Swerve/ChassisSpeeds", ChassisSpeeds.struct)
      .publish();
  }

  //you get the velocity
  public void driveFieldOriented(ChassisSpeeds velocity){

    swerveDrive.driveFieldOriented(velocity);
  }

  //what you dio whit the velocity
  public Command driveFieldOriented(Supplier<ChassisSpeeds> velocity) {

    return run(() -> {
      swerveDrive.driveFieldOriented(velocity.get());
    });

  }
  //what you return
  public SwerveDrive getSwerveDrive() {

  return swerveDrive;
}

  public void zeroGyro() {
    swerveDrive.zeroGyro();
}

  public void stop() {// detiene el robot
    swerveDrive.driveFieldOriented(new ChassisSpeeds(0, 0, 0));//we cancel speeds
    swerveDrive.lockPose();//we lock it double security

  }

  @Override
  public void periodic() {
    // Publish swerve data for AdvantageScope using struct format

    // Publish measured module states
    measuredStatesPublisher.set(swerveDrive.getStates());

    // Publish desired module states
    // Note: YAGSL stores these internally, we publish the same as measured for now
    desiredStatesPublisher.set(swerveDrive.getStates());

    // Publish robot pose
    posePublisher.set(swerveDrive.getPose());

    // Publish chassis speeds
    chassisSpeedsPublisher.set(swerveDrive.getRobotVelocity());
  }


}