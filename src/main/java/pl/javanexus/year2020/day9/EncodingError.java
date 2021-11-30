package pl.javanexus.year2020.day9;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EncodingError {

    public BigInteger findFirstInvalidValue(List<BigInteger> values, int preambleLength) {
        Set<BigInteger> preamble = new HashSet<>();

        for (int i = 0; i < values.size(); i++) {
            BigInteger nextValue = values.get(i);
            if (i >= preambleLength) {
                if (!isValid(nextValue, preamble)) {
                    return nextValue;
                }
                preamble.remove(values.get(i - preambleLength));
            }
            preamble.add(nextValue);
        }

        throw new IllegalStateException("Invalid value not found!");
    }

    public List<BigInteger> findSubsetWithSum(List<BigInteger> values, BigInteger sum) {
        int fromIndex = 0;

        BigInteger subSum = BigInteger.ZERO;
        for (int i = 0; i < values.size(); i++) {
            BigInteger value = values.get(i);
            subSum = subSum.add(value);

            while (subSum.compareTo(sum) > 0) {
                subSum = subSum.subtract(values.get(fromIndex++));
            }

            if (subSum.compareTo(sum) == 0) {
                return values.subList(fromIndex, i + 1);
            }
        }

        throw new IllegalStateException("Could not find subset for sum: " + sum);
    }

    public BigInteger sumMinAndMax(List<BigInteger> values) {
        BigInteger min = values.get(0), max = BigInteger.ZERO;

        for (BigInteger value : values) {
            if (value.compareTo(min) < 0) {
                min = value;
            }
            if (value.compareTo(max) > 0) {
                max = value;
            }
        }

        return min.add(max);
    }

    private boolean isValid(BigInteger sum, Set<BigInteger> preamble) {
        for (BigInteger value : preamble) {
            BigInteger otherValue = sum.subtract(value);
            if (!otherValue.equals(value) && preamble.contains(otherValue)) {
                return true;
            }
        }
        return false;
    }
}
