package model;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String id;
    private String name;

    private List<Task> tasks;


    public Project(String id,String name) {
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public List <Task> getTasks() {
        return tasks;
    }
    public void addTask(Task task) {
        this.tasks.add(task);
    }
    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
