package service;

import model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @Test
    void gorevOlusturuluyorMu(){
        TaskService taskService = new TaskService();

        Task task = taskService.createTask(
                "1","Test Deneme Task","Junit ogren"
        );


        assertNotNull(task);
        assertEquals("Test Deneme Task",task.getTitle());
        assertFalse(task.isCompleted());

    }

    @Test
    void gorevTamamlanincaCompletedTrueMu(){
        TaskService taskService = new TaskService();

        Task task = taskService.createTask(
                "2","Bitirilecek Görev","Junit test yaz"
        );

        taskService.completeTask("2");
        assertTrue(task.isCompleted());
    }

    @Test
    void kulaniciyaGorevAtanabiliyorMu(){

        TaskService taskService = new TaskService();

        User user = new User("a1","Alper");
        Task task = taskService.createTask(
                "3","Kullanici Görevi" , "Görev atama testi"
        );
        taskService.assignTaskToUser(task,user);
        assertEquals(1,user.getTasks().size());
        assertTrue(user.getTasks().contains(task));
    }

    @Test
    void projeyeGorevAtanabiliyorMu(){
        TaskService taskService = new TaskService();

        Project project = new Project("p1","OOP Projesi");
        Task task = taskService.createTask(
                "4",
                "Proje Görevi",
                "Projeye atama testi"

        );

        taskService.assignTaskToProject(task , project);

        assertEquals(1,project.getTasks().size());
        assertTrue(project.getTasks().contains(task));
    }

    @Test
    void bugunkuGorevListeleniyorMu(){
        TaskService taskService = new TaskService();

        Deadline deadline = new Deadline(LocalDate.now());
        TimedTask task = new TimedTask(
                "5",
                "Bugünkü Görev",
                "Deadline bugün",
                deadline
        );

        taskService.getAllTasks().add(task);


        List<TimedTask> upcomingTasks =taskService.getUpcomingTasks();

        assertEquals(1,upcomingTasks.size());
        assertTrue(upcomingTasks.contains(task));
    }
}
