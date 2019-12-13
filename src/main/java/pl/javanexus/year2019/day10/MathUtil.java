package pl.javanexus.year2019.day10;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MathUtil {

    public static long findGreatestCommonDivisor(long a, long b) {
        while(b != 0) {
            long c = a % b;
            a = b;
            b = c;
        }

        return a;
    }

    public static BigInteger findGreatestCommonDivisor(BigInteger a, BigInteger b) {
        while(!b.equals(BigInteger.ZERO)) {
            BigInteger c = a.mod(b);
            a = b;
            b = c;
        }

        return a;
    }

    public static long findLeastCommonMultiple(long a, long b) {
        return a * b / findGreatestCommonDivisor(a, b);
    }

    public static BigInteger findLeastCommonMultiple(BigInteger a, BigInteger b) {
        return a.multiply(b).divide(findGreatestCommonDivisor(a, b));
    }

    public static BigInteger findCommonPeriod(Set<Long> periods) {
        Set<BigInteger> result = periods.stream().map(p -> new BigInteger(Long.toString(p))).collect(Collectors.toSet());
        while (periods.size() > 1) {
            System.out.println(result);
            result = findCommonPeriods(result);
        }

        return result.iterator().next();
    }

    public static Set<BigInteger> findCommonPeriods(Set<BigInteger> periods) {
        Set<BigInteger> commonPeriods = new HashSet<>();

        for (BigInteger left : periods) {
            for (BigInteger right : periods) {
                if (!left.equals(right)) {
                    commonPeriods.add(MathUtil.findLeastCommonMultiple(left, right));
                }
            }
        }

        return commonPeriods;
    }
}
