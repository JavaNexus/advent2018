package pl.javanexus.day4;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Record {

    enum GuardState {
        START("Guard #[0-9]+ begins shift"),
        FALLS_ASLEEP("falls asleep"),
        WAKES_UP("wakes up");

        private final String regex;

        GuardState(String regex) {
            this.regex = regex;
        }

        public static GuardState getState(String text) {
            for (GuardState state : GuardState.values()) {
                if (text.matches(state.regex)) {
                    return state;
                }
            }

            throw new IllegalArgumentException("Unknown state: " + text);
        }
    }

    public static final Pattern RECORD_PATTERN =
            Pattern.compile("\\[.+ [0-9]{2}:([0-9]{2})] (Guard #([0-9]+) begins shift|falls asleep|wakes up)");

    private final Map<Integer, Guard> guards = new HashMap<>();

    public void buildRecord(List<String> record) {
        Nap.NapBuilder napBuilder = null;
        Guard guard = null;

        for (String entry : record) {
            Matcher matcher = RECORD_PATTERN.matcher(entry);
            if (matcher.find()) {
                int time = Integer.parseInt(matcher.group(1));
                GuardState state = GuardState.getState(matcher.group(2));
                switch (state) {
                    case START:
                        napBuilder = Nap.builder();
                        guard = guards.computeIfAbsent(Integer.parseInt(matcher.group(3)), key -> new Guard(key));
                        break;
                    case FALLS_ASLEEP:
                        napBuilder = napBuilder.from(time);
                        break;
                    case WAKES_UP:
                        napBuilder = napBuilder.to(time);
                        guard.addNap(napBuilder.build());
                        break;
                }
            }
        }
    }

    public int getGuardMostOftenAsleepAtTheSameMinute() {
        Map.Entry<Integer, Guard> guardEntry = guards.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().getTimeMostAsleep().getNumberOfTimesAsleep()))
                .get();

        return guardEntry.getKey() * guardEntry.getValue().getTimeMostAsleep().getMinute();
    }

    public Guard getSleepyGuard() {
        return guards.entrySet().stream()
                .max(Comparator.comparingInt(left -> left.getValue().getTotalTimeAsleep()))
                .get().getValue();
    }
}
