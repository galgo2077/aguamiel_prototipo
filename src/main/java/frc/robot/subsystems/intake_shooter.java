package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.id;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class intake_shooter extends SubsystemBase {
  
  // High-level motor controller definition
  private final SparkMax m_intakeMotor = new SparkMax(id.intake_shooter, MotorType.kBrushless);

  public intake_shooter() {
    // Factory default or specific configuration can go here
  }

  public void setSpeed(double speed) {
    m_intakeMotor.set(speed);
  }

  public void setIntakeBoolean(boolean isOn) {
    m_intakeMotor.set(isOn ? 1.0 : 0.0);
  }

  public void setIntakeBooleanout(boolean out) {
    m_intakeMotor.set(out ? 1.0 : 0.0);
  }

  public void stop() {
    m_intakeMotor.set(0.0);
  }
}