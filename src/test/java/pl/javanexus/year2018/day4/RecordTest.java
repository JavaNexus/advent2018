package pl.javanexus.year2018.day4;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecordTest {

    public static final String[] RECORD = {
            "[1518-11-01 00:00] Guard #10 begins shift",
            "[1518-11-01 00:05] falls asleep",
            "[1518-11-01 00:25] wakes up",
            "[1518-11-01 00:30] falls asleep",
            "[1518-11-01 00:55] wakes up",
            "[1518-11-01 23:58] Guard #99 begins shift",
            "[1518-11-02 00:40] falls asleep",
            "[1518-11-02 00:50] wakes up",
            "[1518-11-03 00:05] Guard #10 begins shift",
            "[1518-11-03 00:24] falls asleep",
            "[1518-11-03 00:29] wakes up",
            "[1518-11-04 00:02] Guard #99 begins shift",
            "[1518-11-04 00:36] falls asleep",
            "[1518-11-04 00:46] wakes up",
            "[1518-11-05 00:03] Guard #99 begins shift",
            "[1518-11-05 00:45] falls asleep",
            "[1518-11-05 00:55] wakes up",
    };

    @Test
    public void testGetSleepyGuard() {
        Record reposeRecord = new Record();
        reposeRecord.buildRecord(Arrays.stream(RECORD).collect(Collectors.toList()));
        Guard sleepyGuard = reposeRecord.getSleepyGuard();
        System.out.println(sleepyGuard.getId() * sleepyGuard.getTimeMostAsleep().getMinute());
        System.out.println(reposeRecord.getGuardMostOftenAsleepAtTheSameMinute());
    }

    @Test
    public void testGetSleepyGuardFromFile() throws IOException {
        List<String> entries = new InputReader().readStringValues("day4_guards.input");
        Collections.sort(entries);

        Record reposeRecord = new Record();
        reposeRecord.buildRecord(entries);
        Guard sleepyGuard = reposeRecord.getSleepyGuard();
        System.out.println(sleepyGuard.getId() * sleepyGuard.getTimeMostAsleep().getMinute());
        System.out.println(reposeRecord.getGuardMostOftenAsleepAtTheSameMinute());
    }
}
