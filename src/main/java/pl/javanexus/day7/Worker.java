package pl.javanexus.day7;

import lombok.Getter;

public class Worker {

    private int timeSpentOnCurrentTask;

    @Getter
    private Vertex currentTask;

    public void updateTimeSpent() {
        timeSpentOnCurrentTask++;
    }

    public boolean isFinished() {
        return timeSpentOnCurrentTask >= getTimeToFinishCurrentTask();
    }

    private int getTimeToFinishCurrentTask() {
        return currentTask == null ? 0 : currentTask.getTime();
    }

    public void setCurrentTask(Vertex currentTask) {
        this.currentTask = currentTask;
        this.timeSpentOnCurrentTask = 0;
    }
}
