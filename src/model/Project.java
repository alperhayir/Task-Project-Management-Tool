package model;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String id;
    private String name;
    private String description;

    private List<Task> tasks;


    public Project(String id,String name) {
        this.id = id;
        this.name = name;
        this.description = "";
        this.tasks = new ArrayList<>();
    }

    public Project(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description != null ? description : "";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    /**
     * Projenin tamamlanıp tamamlanmadığını kontrol eder.
     * Proje, tüm görevleri tamamlandıysa tamamlanmış sayılır.
     * Eğer projede hiç görev yoksa false döner.
     *
     * @return Tüm görevler tamamlandıysa true, aksi halde false
     */
    public boolean isCompleted() {
        if (tasks.isEmpty()) {
            return false;
        }
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                return false;
            }
        }
        return true;
    }
}
