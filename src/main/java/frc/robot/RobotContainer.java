package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.Constants.OperatorConstants;

import frc.robot.commands.intake_shooter_command;
import frc.robot.commands.shoot;
import frc.robot.commands.entry;

import frc.robot.commands.selector_command;

import frc.robot.subsystems.intake_shooter;
import frc.robot.subsystems.selector;
import frc.robot.subsystems.swerve_subsystem;

import swervelib.SwerveInputStream;

public class RobotContainer {

  /* ================= SUBSYSTEMS ================= */
  private final swerve_subsystem drivebase = new swerve_subsystem();
  private final intake_shooter m_IntakeShooter = new intake_shooter();
  private final selector m_Selector = new selector();

  /* ================= CONTROLLERS ================= */
  private final CommandXboxController m_driverController_chasis =
      new CommandXboxController(0);

  private final CommandXboxController m_driverController_mecanismos =
      new CommandXboxController(1);

  /* ================= DRIVE CONFIG ================= */
  private boolean fieldRelative = false;

  SwerveInputStream driveAngularVelocity =
      SwerveInputStream.of(
              drivebase.getSwerveDrive(),
              () -> -m_driverController_chasis.getLeftY(),
              () -> -m_driverController_chasis.getLeftX())
          .withControllerRotationAxis(m_driverController_chasis::getRightX)
          .deadband(OperatorConstants.DEADBAND)
          .scaleTranslation(0.8)
          .allianceRelativeControl(false);

  SwerveInputStream driveDirectAngle =
      driveAngularVelocity.copy()
          .withControllerHeadingAxis(
              m_driverController_chasis::getRightX,
              m_driverController_chasis::getRightY)
          .headingWhile(() -> fieldRelative);

  Command driveFieldOrientedAngularVelocity =
      drivebase.driveFieldOriented(driveAngularVelocity);

  /* ================= CONSTRUCTOR ================= */
  public RobotContainer() {
    configureBindings();
    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity);
  }

  /* ================= BINDINGS ================= */
  private void configureBindings() {

    /* ----- CHASIS ----- */

    // Toggle field oriented
    m_driverController_chasis.start().onTrue(
        new InstantCommand(() -> {
          fieldRelative = !fieldRelative;
          System.out.println("Field Relative: " + fieldRelative);
        })
    );

    // Reset gyro
    m_driverController_chasis.b().onTrue(
        new InstantCommand(drivebase::zeroGyro)
    );

    /* ----- MECANISMOS ----- */

    m_driverController_mecanismos.a().whileTrue(new intake_shooter_command(m_IntakeShooter, ()-> 0, () -> true, () -> false));
    m_driverController_mecanismos.b().whileTrue(new intake_shooter_command(m_IntakeShooter, ()-> 0, () -> false, () -> true));

    m_driverController_mecanismos.x().whileTrue(new selector_command(m_Selector, () -> true, () -> false));
    m_driverController_mecanismos.y().whileTrue(new selector_command(m_Selector, () -> false, () -> true));


    m_driverController_mecanismos.leftTrigger().whileTrue(
    new shoot(m_IntakeShooter, m_Selector, () -> m_driverController_mecanismos.getRightTriggerAxis(), () -> true));

    m_driverController_mecanismos.rightTrigger().whileTrue(new entry(m_IntakeShooter, m_Selector, () -> true, () -> true));
  }

  /* ================= AUTONOMOUS ================= */
  public Command getAutonomousCommand() {
    return null;
  }
}
