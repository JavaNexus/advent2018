package pl.javanexus.day4;

import java.util.LinkedList;
import java.util.List;

public class Guard {

    public static final int MINUTES_IN_HOUR = 60;

    private final int id;
    private final List<Nap> naps = new LinkedList();

    private MinuteAsleep timeMostAsleep;

    public Guard(int id) {
        this.id = id;
    }

    public void addNap(Nap nap) {
        naps.add(nap);
    }

    public int getId() {
        return id;
    }

    public int getTotalTimeAsleep() {
        return naps.stream().mapToInt(Nap::getDuration).sum();
    }

    public MinuteAsleep getTimeMostAsleep() {
        if (timeMostAsleep == null) {
            this.timeMostAsleep = calculateTimeMostAsleep();
        }

        return timeMostAsleep;
    }

    private MinuteAsleep calculateTimeMostAsleep() {
        int[] minutesInHour = new int[MINUTES_IN_HOUR];
        for (Nap nap : naps) {
            for (int i = nap.getFrom(); i < nap.getTo(); i++) {
                minutesInHour[i]++;
            }
        }

        int minuteMostAsleep = 0;
        for (int minute = 0; minute < minutesInHour.length; minute++) {
            if (minutesInHour[minute] > minutesInHour[minuteMostAsleep]) {
                minuteMostAsleep = minute;
            }
        }

        return new MinuteAsleep(minuteMostAsleep, minutesInHour[minuteMostAsleep]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Guard guard = (Guard) o;

        return id == guard.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Guard{" +
                "id='" + id + '\'' +
                ", naps=" + naps +
                '}';
    }
}
