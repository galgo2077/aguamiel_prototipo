package frc.robot.commands;

import java.util.function.BooleanSupplier;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake_shooter; // Asegúrate de que la clase empiece con Mayúscula en su archivo

public class intake_shooter_command extends Command {

    private final intake_shooter m_subsystem;
    private final BooleanSupplier m_selectorin;
    private final DoubleSupplier m_analog;
    private final BooleanSupplier m_selector_out;

    public intake_shooter_command(intake_shooter subsystem, DoubleSupplier analog_trigger, BooleanSupplier selectorin, BooleanSupplier selectorout) {
        m_subsystem = subsystem;
        m_analog = analog_trigger;
        m_selectorin = selectorin;
        m_selector_out = selectorout;

        // Es vital registrar el subsistema para que dos comandos no lo usen a la vez
        addRequirements(m_subsystem);
    }

    @Override
    public void execute() {
        // Le pasamos el valor booleano del botón a la lógica del subsistema
        
        m_subsystem.setSpeed(m_analog.getAsDouble());

        m_subsystem.setIntakeBoolean(m_selectorin.getAsBoolean());
        m_subsystem.setIntakeBooleanout(m_selector_out.getAsBoolean());
    }

    @Override
    public void end(boolean interrupted) {
        // Cuando el comando se detiene o se interrumpe, apagamos el motor
        m_subsystem.stop();
    }

    @Override
    public boolean isFinished() {
        // Normalmente devuelve false para que el comando corra mientras el botón esté presionado
        return false;
    }
}