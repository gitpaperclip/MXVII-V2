package frc.robot.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import edu.wpi.first.wpilibj.Preferences;
import frc.robot.util.Special;

public final class RioConstants {

    public static void sendSwerveZeroes() {
        Preferences.setDouble("FRONT_LEFT_MODULE_STEER_OFFSET", Drive.FRONT_LEFT_MODULE_STEER_OFFSET);
        Preferences.setDouble("FRONT_RIGHT_MODULE_STEER_OFFSET", Drive.FRONT_RIGHT_MODULE_STEER_OFFSET);
        Preferences.setDouble("BACK_LEFT_MODULE_STEER_OFFSET", Drive.BACK_LEFT_MODULE_STEER_OFFSET);
        Preferences.setDouble("BACK_RIGHT_MODULE_STEER_OFFSET", Drive.BACK_RIGHT_MODULE_STEER_OFFSET);
    }

    public static void writeZeroes(double fl, double fr, double bl, double br) {
        Drive.FRONT_LEFT_MODULE_STEER_OFFSET = fl;
        Drive.FRONT_RIGHT_MODULE_STEER_OFFSET = fr;
        Drive.BACK_LEFT_MODULE_STEER_OFFSET = bl;
        Drive.BACK_RIGHT_MODULE_STEER_OFFSET = br;

        sendSwerveZeroes();
    }

    public static double getSwerveZeroes(int index) {
        double[] swerveZeroArray = new double[4];
        swerveZeroArray[0] = Preferences.getDouble("FRONT_LEFT_MODULE_STEER_OFFSET", Drive.FRONT_LEFT_MODULE_STEER_OFFSET);
        swerveZeroArray[1] = Preferences.getDouble("FRONT_RIGHT_MODULE_STEER_OFFSET", Drive.FRONT_RIGHT_MODULE_STEER_OFFSET);
        swerveZeroArray[2] = Preferences.getDouble("BACK_LEFT_MODULE_STEER_OFFSET", Drive.BACK_LEFT_MODULE_STEER_OFFSET);
        swerveZeroArray[3] = Preferences.getDouble("BACK_RIGHT_MODULE_STEER_OFFSET", Drive.BACK_RIGHT_MODULE_STEER_OFFSET);

        return swerveZeroArray[index];
    }

    public static class Drive {
        public static double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toDegrees(0.0);
        public static double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toDegrees(0.0);
        public static double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toDegrees(0.0);
        public static double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toDegrees(0.0);
    }
}
