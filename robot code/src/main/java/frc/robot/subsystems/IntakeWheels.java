package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DynamicConstants;
import frc.robot.Constants.StaticConstants;
import frc.robot.util.ControllerRumble;

public class IntakeWheels extends SubsystemBase {
    // create all necessary object instances
    private TalonFX intakeWheels;

    private AnalogInput irSensorLeft;
    private AnalogInput irSensorRight;

    // Constructor
    public IntakeWheels() {
        // Configure motors and sensor
        intakeWheels = new TalonFX(StaticConstants.IntakeWheels.ID);
        intakeWheels.getConfigurator().apply(new TalonFXConfiguration());
        intakeWheels.getConfigurator().apply(new VoltageConfigs()
        .withPeakForwardVoltage(12)
        .withPeakReverseVoltage(12));
        intakeWheels.setNeutralMode(NeutralModeValue.Brake);
        intakeWheels.setInverted(false);
        intakeWheels.getConfigurator().apply(new CurrentLimitsConfigs().withSupplyCurrentLimitEnable(true)
        .withSupplyCurrentLimit(StaticConstants.IntakeWheels.supplyCurrentLimit));
        intakeWheels.getConfigurator().apply(new CurrentLimitsConfigs().withSupplyCurrentLimitEnable(true)
        .withSupplyCurrentLimit(StaticConstants.IntakeWheels.statorCurrentLimit));

        irSensorLeft = new AnalogInput(StaticConstants.IntakeWheels.leftIrID);
        irSensorLeft.setAverageBits(4);
        irSensorLeft.setOversampleBits(4);

        irSensorRight = new AnalogInput(StaticConstants.IntakeWheels.rightIrID);
        irSensorRight.setAverageBits(4);
        irSensorRight.setOversampleBits(4);
    }

    // IR Methods

    public double getIrReading() {
        return Math.max(irSensorLeft.getAverageVoltage(), irSensorRight.getAverageVoltage());
    }

    private int confirm;
    public boolean pieceConfirmed() {
        if (confirm < DynamicConstants.Intake.irSensorThresholdShoot) {
            confirm++;
        } else{
            confirm = 0;
        } return (confirm > 5);
    } 

    public void periodic() {
        SmartDashboard.putNumber("Left Sensor Reading", irSensorLeft.getAverageVoltage());
        SmartDashboard.putNumber("Right Sensor Reading", irSensorRight.getAverageVoltage());
        SmartDashboard.putBoolean("IR Sensor Boolean", getIrReading() > DynamicConstants.Intake.irSensorThresholdShoot);
    }

    // Motor Action Methods

    public Command runVoltage(double v) {
        return runEnd(() -> {
            intakeWheels.setVoltage(v);
        }, () -> {
            intakeWheels.setVoltage(0);
        });
    }

    public void setVoltage(double d){
        intakeWheels.setVoltage(d);
    }

    public double getVoltage(){
        return intakeWheels.getMotorVoltage().getValueAsDouble();
    }

    // Advance Motor Methods

    public Command runVoltUntilGivenReading(double voltage, double IR) {
        return runEnd(() -> {
            if(getIrReading() > IR){
                intakeWheels.setVoltage(voltage);
            }
            else{
                intakeWheels.setVoltage(0);
            }
        },
        () -> {
            intakeWheels.setVoltage(0);
        });
    }


    public Command outtakeCommand() {
        return runEnd(() -> {
            intakeWheels.setVoltage(DynamicConstants.Intake.outtakeVoltage);
        }, () -> {
            intakeWheels.setVoltage(0);
        });
    }

    public Command intakeCommand(){
        return runEnd(() -> {
            if(getIrReading() < DynamicConstants.Intake.irSensorThresholdIntake){
                intakeWheels.setVoltage(DynamicConstants.Intake.intakeVoltage);
            }
            else{
                intakeWheels.setVoltage(0);
            }
        },
        () -> {
            intakeWheels.setVoltage(0);
            // Adding a controller rumble on confirmation of a piece allows for faster driving.
            ControllerRumble.getInstance().rumbleController1(1);
        });
    }

}
