package frc.robot.commands;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.selector; // Asegúrate de que la clase empiece con Mayúscula en su archivo

public class selector_command extends Command {

    private final selector m_subsystem;
    private final BooleanSupplier m_intakein;
    private final BooleanSupplier m_intakeout;

    public selector_command(selector subsystem, BooleanSupplier intakein, BooleanSupplier intakeout) {
        m_subsystem = subsystem;
        m_intakein = intakein;
        m_intakeout = intakeout;

        // Es vital registrar el subsistema para que dos comandos no lo usen a la vez
        addRequirements(m_subsystem);
    }

    @Override
    public void execute() {
        // Le pasamos el valor booleano del botón a la lógica del subsistema
        m_subsystem.mecanism_logic_selector_positive(m_intakein.getAsBoolean());
        m_subsystem.mecanism_logic_selector_negative(m_intakeout.getAsBoolean());
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