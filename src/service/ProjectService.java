package service;

import model.Project;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    private List<Project> projects;

    public ProjectService() {
        this.projects = new ArrayList<>();
    }

    public Project createProject(String id ,String name){
        if (projectExists(id)) {
            throw new IllegalArgumentException("Bu ID ile proje zaten var");
        }
        Project project = new Project(id,name);
        projects.add(project);
        return project;
    }

    /**
     * Proje ID'sinin mevcut olup olmadığını kontrol eder.
     *
     * @param projectId Kontrol edilecek proje ID'si
     * @return Proje mevcut ise true, aksi halde false
     */
    public boolean projectExists(String projectId) {
        return findProjectById(projectId) != null;
    }

    /**
     * Proje listesinin boş olup olmadığını kontrol eder.
     *
     * @return Proje listesi boş ise true, aksi halde false
     */
    public boolean hasProjects() {
        return !projects.isEmpty();
    }
    public List<Project> getAllProjects() {
        return projects;
    }

    public Project findProjectById(String  projectId){
        for(Project project : projects){
            if(project.getId().equals(projectId)){
                return project;
            }
        }
        return null;
    }

    public List<Task> getProjectTasks(String projectId){
        Project project = findProjectById(projectId);
        if (project !=null){
            return project.getTasks();
        }
        return null;
    }

    /**
     * Belirtilen ID'ye sahip projeyi siler.
     *
     * @param projectId Silinecek projenin ID'si
     * @return Proje bulunup silindi ise true, aksi halde false
     */
    public boolean deleteProject(String projectId) {
        Project project = findProjectById(projectId);
        if (project == null) {
            return false;
        }
        projects.remove(project);
        return true;
    }

    /**
     * Tüm projeleri siler.
     */
    public void deleteAllProjects() {
        projects.clear();
    }
}
