package pl.javanexus.day16;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceInput {

    private final int[] initialRegisterValues;
    private final int[] expectedRegisterValues;
    private final int inputA;
    private final int inputB;
    private final int resultRegisterIndex;

    public boolean isExpectedRegister(int[] actualRegisterValues) {
        boolean isMatch = true;
        for (int i = 0; i < actualRegisterValues.length; i++) {
            isMatch &= (expectedRegisterValues[i] == actualRegisterValues[i]);
        }

        return isMatch;
    }
}
