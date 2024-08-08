package frc.robot.util;

public class Special {
    public static double floorMod(double x, double y){
        return (x - Math.floor(x/y) * y);
    }
}
