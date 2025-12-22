package model;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String name;

    private List<Task> tasks;

    public User(String id , String name ){
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task){
        this.tasks.add(task);
    }


}
