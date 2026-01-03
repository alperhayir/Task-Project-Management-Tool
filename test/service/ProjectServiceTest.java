package service;

import model.Project;
import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectServiceTest {

    @Test
    void projeOlusturuluyorMu(){
        ProjectService projectService = new ProjectService();
        Project project =projectService.createProject("p1","OOP Projesi");

        assertNotNull(project);
        assertEquals("p1",project.getId());
        assertEquals(1,projectService.getAllProjects().size());

    }

    @Test
    void projeIdIleBulunabiliyorMu(){
        ProjectService projectService = new ProjectService();

        projectService.createProject("p1","OOP Projesi");

        Project foundProject = projectService.findProjectById("p1");
        assertNotNull(foundProject);
        assertEquals("p1",foundProject.getId());

    }

    @Test
    void olmayanProjeNullDonuyorMu(){
        ProjectService projectService = new ProjectService();

        Project project = projectService.findProjectById("p999");

        assertNull(project);
    }

    @Test
    void projeGorevleriGetiriliyorMu(){
        ProjectService projectService = new ProjectService();

        Project project = projectService.createProject("p1","OOP Projesi");

        Task task = new Task ("t1","Proje Gorevi","Test");
        project.addTask(task);

        assertEquals(1,projectService.getProjectTasks("p1").size());

    }

}
