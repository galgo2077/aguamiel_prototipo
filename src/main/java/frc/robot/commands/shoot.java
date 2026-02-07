package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.selector;
 // Asegúrate de que la clase empiece con Mayúscula en su archivo
import frc.robot.subsystems.intake_shooter;

public class shoot extends Command {

    private final intake_shooter m_subsystem_Intake;
    private final selector m_subsystem_shooter;


    private final BooleanSupplier  m_outake;
    private final DoubleSupplier m_intakeActive;


    public shoot(intake_shooter subsystem, selector subsystem2, DoubleSupplier intakeon_trigger, BooleanSupplier outake) {

        this.m_subsystem_Intake = subsystem;
        this.m_subsystem_shooter = subsystem2;
        this.m_intakeActive = intakeon_trigger;
        this.m_outake = outake;

        // Es vital registrar el subsistema para que dos comandos no lo usen a la vez
        addRequirements(m_subsystem_Intake, m_subsystem_shooter);
    }

    @Override
    public void execute() {
        // Le pasamos el valor booleano del botón a la lógica del subsistema
        m_subsystem_Intake.setSpeed(m_intakeActive.getAsDouble());
        m_subsystem_shooter.mecanism_logic_selector_positive(m_outake.getAsBoolean());
    }

    @Override
    public void end(boolean interrupted) {
        // Cuando el comando se detiene o se interrumpe, apagamos el motor
        m_subsystem_Intake.stop();
        m_subsystem_shooter.stop();
    }

    @Override
    public boolean isFinished() {
        // Normalmente devuelve false para que el comando corra mientras el botón esté presionado
        return false;
    }
}