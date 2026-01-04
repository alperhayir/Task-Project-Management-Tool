package service;

import model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private List<Task> tasks;

    public TaskService() {
        this.tasks = new ArrayList<>();
    }

    public Task createTask(String id, String title, String description) {
        Task task = new Task(id, title, description);
        tasks.add(task);
        return task;
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void completeTask(String taskId) {
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                task.complete();
                break;
            }
        }
    }

    public TimedTask createTimedTask(String id, String title, String description, LocalDate dueDate ) {

        Deadline deadline = new Deadline(dueDate);
        TimedTask timedTask = new TimedTask(id, title, description, deadline);
        tasks.add(timedTask);
        return timedTask;
    }

    public void assignTaskToUser(Task task, User user) {
        user.addTask(task);
    }

    public void assignTaskToProject(Task task, Project project) {
        project.addTask(task);
    }

    public List<TimedTask> getUpcomingTasks() {
        List<TimedTask> upcomingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task instanceof TimedTask) {
                TimedTask timedTask = (TimedTask) task;
                Deadline deadline = timedTask.getDeadline();

                if (deadline != null) {
                    LocalDate dueDate = deadline.getDueDate();
                    LocalDate today = LocalDate.now();

                    if (!dueDate.isBefore(today) &&
                            !dueDate.isAfter(today.plusDays(3))) {
                        upcomingTasks.add(timedTask);
                    }
                }
            }
        }
        return upcomingTasks;
    }
}
