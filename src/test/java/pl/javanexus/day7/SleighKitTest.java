package pl.javanexus.day7;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SleighKitTest {

    public static final String[] STEPS = {
            "Step C must be finished before step A can begin.",
            "Step C must be finished before step F can begin.",
            "Step A must be finished before step B can begin.",
            "Step A must be finished before step D can begin.",
            "Step B must be finished before step E can begin.",
            "Step D must be finished before step E can begin.",
            "Step F must be finished before step E can begin.",
    };
    public static final int BASE_STEP_TIME = 60;
    public static final int NUMBER_OF_WORKERS = 5;

    @Test
    public void testAssemble() {
        AssembledSleigh assembled = new SleighKit(0, 1, STEPS).assemble();
        assertEquals("CABDFE", assembled.getAssemblyOrder());
        assertEquals(22, assembled.getAssemblyTime());

        assembled = new SleighKit(0, 2, STEPS).assemble();
        assertEquals("CABFDE", assembled.getAssemblyOrder());
        assertEquals(16, assembled.getAssemblyTime());
    }

    /**
     * 1) GKTVCNPHIRYDUJMSXFBQLOAEWZ    //Wrong
     * 2) GKCNPTVHIRYDUJMSXFBQLOAEWZ
     * ...
     * 3) GKTVCNPYHRDIUJMSXFBQLOAEWZ 1265
     *
     * @throws IOException
     */
    @Test
    public void testAssembleFromFile() throws IOException {
        List<String> steps = new InputReader().readStringValues("day7_sleigh.input");

        SleighKit sleighKit = new SleighKit(BASE_STEP_TIME, NUMBER_OF_WORKERS, steps.toArray(new String[steps.size()]));
        System.out.println(sleighKit.assemble());
    }

    @Test
    public void testGetVertexTime() {
        assertEquals(61, new Vertex("A", 0).getTime());
        assertEquals(86, new Vertex("Z", 0).getTime());
    }

    @Test
    public void testIsWorkerFinished() {
        Worker worker = new Worker();
        assertTrue(worker.isFinished());

        worker.setCurrentTask(new Vertex("C", 0));
        assertFalse(worker.isFinished());

        worker.updateTimeSpent();//0 -> 1
        assertFalse(worker.isFinished());

        worker.updateTimeSpent();//1 -> 2
        assertFalse(worker.isFinished());

        worker.updateTimeSpent();//2 -> 3
        assertTrue(worker.isFinished());
    }
}
