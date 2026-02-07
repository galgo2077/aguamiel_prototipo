// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.constants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import java.io.File;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Filesystem;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import swervelib.SwerveInputStream;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.kinematics.ChassisSpeeds;


//swerve modules

import static edu.wpi.first.units.Units.Meters;


public class swerve_subsystem extends SubsystemBase {
 
  
  //we make a directory so we can acces my values
  File directory = new File(Filesystem.getDeployDirectory(),"swerve");
  SwerveDrive  swerveDrive;

  
  public swerve_subsystem() {
    //use the max speed whit those two values
    try{
      //we create de swerve value
    swerveDrive = new SwerveParser(directory).createSwerveDrive(Constants.constants.MAXSPEED, new Pose2d(new Translation2d(Meters.of(1),Meters.of(4)),Rotation2d.fromDegrees(0)));
    }catch (Exception e){

      throw new RuntimeException(e);
    }
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



  public void stop() {// detiene el robot
    swerveDrive.driveFieldOriented(new ChassisSpeeds(0, 0, 0));//we cancel speeds
    swerveDrive.lockPose();//we lock it double security

  }



}