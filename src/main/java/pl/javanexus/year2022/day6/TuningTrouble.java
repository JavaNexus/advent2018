package pl.javanexus.year2022.day6;

import java.util.HashMap;
import java.util.Map;

public class TuningTrouble {

    private static final int START_OF_PACKET_MARKER_LENGTH = 4;
    private static final int START_OF_MESSAGE_MARKER_LENGTH = 14;

    public int findStartOfPacketPosition(String buffer) {
        return findMarkerPosition(buffer, START_OF_PACKET_MARKER_LENGTH);
    }

    public int findStartOfMessagePosition(String buffer) {
        return findMarkerPosition(buffer, START_OF_MESSAGE_MARKER_LENGTH);
    }

    public int findMarkerPosition(String buffer, int markerLength) {
        Map<Character, Integer> marker = new HashMap<>();

        int i = 0;
        while (i < buffer.length()) {
            marker.compute(buffer.charAt(i++), (character, count) -> count == null ? 1 : count + 1);

            if (marker.size() == markerLength) {
                return i;
            }
            if (i - markerLength >= 0) {
                char firstCharacter = buffer.charAt(i - markerLength);
                int count = marker.get(firstCharacter);
                if (count == 1) {
                    marker.remove(firstCharacter);
                } else {
                    marker.put(firstCharacter, count - 1);
                }
            }
        }

        throw new IllegalStateException("Marker not found");
    }
}
