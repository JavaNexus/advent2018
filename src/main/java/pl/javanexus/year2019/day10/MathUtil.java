package pl.javanexus.year2019.day10;

public class MathUtil {

    public static int findGreatestCommonDivisor(int a, int b) {
        while(b != 0) {
            int c = a % b;
            a = b;
            b = c;
        }

        return a;
    }
}
