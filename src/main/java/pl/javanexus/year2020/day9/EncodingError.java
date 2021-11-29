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
