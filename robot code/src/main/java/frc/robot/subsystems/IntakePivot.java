package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.StaticConstants;

public class IntakePivot {
    // create all necessary object instances
    private TalonFX pivotMotor;
    private double positionCoefficient = 1.0/16.0*15.0/24.0;
    private ProfiledPIDController profiledPIDController;
    private ArmFeedforward armFeedforward;
    private boolean brakeEnabled;
    private boolean softLimitEnabled;
    private DigitalInput reverseLimitSwitch;
    private boolean resetPos = false; 
    
    // Constructor

    public IntakePivot() {
        pivotMotor = new TalonFX(StaticConstants.IntakePivot.ID);
        // Configure PivotMotor
        pivotMotor.getConfigurator().apply(
            new TalonFXConfiguration()
        );
        // Configure Softlimits
        pivotMotor.getConfigurator().apply(new SoftwareLimitSwitchConfigs()
          .withForwardSoftLimitEnable(true)
          .withForwardSoftLimitThreshold(StaticConstants.IntakePivot.forwardLimit / positionCoefficient)
          .withReverseSoftLimitEnable(false)
          .withReverseSoftLimitThreshold(StaticConstants.IntakePivot.reverseLimit / positionCoefficient) 
        );
        // Configure Voltage
        pivotMotor.getConfigurator().apply(new VoltageConfigs()
        .withPeakForwardVoltage(10)
        .withPeakReverseVoltage(-10)
        );
        // Configure Current Limits
        pivotMotor.getConfigurator().apply(new CurrentLimitsConfigs()
        .withSupplyCurrentLimitEnable(true)
        .withSupplyCurrentLimit(StaticConstants.IntakePivot.supplyCurrentLimit)
        .withStatorCurrentLimitEnable(true)
        .withStatorCurrentLimit(StaticConstants.IntakePivot.statorCurrentLimit)
        );
        // Final configs
        pivotMotor.setNeutralMode(NeutralModeValue.Coast);
        brakeEnabled = false;
        softLimitEnabled = true;
        pivotMotor.setInverted(true);
        pivotMotor.setPosition(0);
        // PID
        profiledPIDController = new ProfiledPIDController(3,0,0, new TrapezoidProfile.Constraints(100,60));
        armFeedforward = new ArmFeedforward(0,0.7, 0,0);
        profiledPIDController.setTolerance(0.02 / positionCoefficient);
        // Configure Limit Switch
        reverseLimitSwitch = new DigitalInput(StaticConstants.IntakePivot.reverseLimitSwitchID);
    }

    /*
     * Basic Motor Methods
     */
    public void setDutyCycle(double d) {
        pivotMotor.set(d);
    }
    
    public void setVoltage(double d) {
        pivotMotor.setVoltage(d);
    }
    /*
     * Positional Control Methods
     */
    public void setPosition(double pos) {
       
    }
}
