package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;
//import frc.robot.commands.Autos;
import frc.robot.subsystems.swerve_subsystem; // Asegúrate de que coincida con tu archivo
import swervelib.SwerveInputStream;

public class RobotContainer {

  // Instanciamos el Subistema (Con mayúscula al inicio)
  private final swerve_subsystem drivebase = new swerve_subsystem();

  // Controladores (Driver y Operador)
  // Puerto 0 suele ser el Driver
  private final CommandXboxController m_driverController_chasis = new CommandXboxController(0); 

// how it moves
SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                  () -> m_driverController_chasis.getLeftY() * -1,
                  () -> m_driverController_chasis.getLeftY() * -1)
                  .withControllerRotationAxis(m_driverController_chasis:: getRightX)
                  .deadband(OperatorConstants.DEADBAND)
                  .scaleTranslation(0.8)
                  .allianceRelativeControl(true);

//how it turns
SwerveInputStream driveDirectAngle = driveAngularVelocity.copy()
                  .withControllerHeadingAxis(m_driverController_chasis::getRightX, m_driverController_chasis::getRightY)
                  .headingWhile(true);



Command driveFieldOrientededDirectAngle = drivebase.driveFieldOriented(driveDirectAngle);

Command driveFieldOrientededAngularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);

//aca se llama todo
  public RobotContainer() {
    configureBindings();
    drivebase.setDefaultCommand(driveFieldOrientededAngularVelocity);
  }

  private void configureBindings() {
    
  }

  public Command getAutonomousCommand() {
    // Por ahora retornamos null o un comando vacío hasta que configures PathPlanner
    return null; 
    // return Autos.exampleAuto(drivebase); // Descomentar cuando tengas el auto listo
  }
}