package service;

import model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @Test
    void gorevOlusturuluyorMu() {
        TaskService taskService = new TaskService();

        Task task = taskService.createTask(
                "1", "Test Task", "JUnit öğren"
        );

        assertNotNull(task);
        assertEquals("Test Task", task.getTitle());
        assertFalse(task.isCompleted());
    }

    @Test
    void ayniIdIleGorevOlusturulamaz() {
        TaskService taskService = new TaskService();

        taskService.createTask("1", "Görev", "Açıklama");

        assertThrows(IllegalArgumentException.class, () ->
                taskService.createTask("1", "Tekrar", "Hata")
        );
    }

    @Test
    void gorevTamamlanincaTrueDonuyorMu() {
        TaskService taskService = new TaskService();

        taskService.createTask("2", "Bitirilecek", "Test");

        boolean result = taskService.completeTask("2");

        assertTrue(result);
    }

    @Test
    void olmayanGorevTamamlanamaz() {
        TaskService taskService = new TaskService();

        boolean result = taskService.completeTask("999");

        assertFalse(result);
    }

    @Test
    void kullaniciyaGorevAtanabiliyorMu() {
        TaskService taskService = new TaskService();
        User user = new User("u1", "Alper");

        Task task = taskService.createTask(
                "3", "Kullanıcı Görevi", "Atama testi"
        );

        taskService.assignTaskToUser(task, user);

        assertEquals(1, user.getTasks().size());
        assertTrue(user.getTasks().contains(task));
    }

    @Test
    void projeyeGorevAtanabiliyorMu() {
        TaskService taskService = new TaskService();
        Project project = new Project("p1", "OOP Projesi");

        Task task = taskService.createTask(
                "4", "Proje Görevi", "Atama testi"
        );

        taskService.assignTaskToProject(task, project);

        assertEquals(1, project.getTasks().size());
        assertTrue(project.getTasks().contains(task));
    }

    @Test
    void buguneYakinGorevlerListeleniyorMu() {
        TaskService taskService = new TaskService();

        taskService.createTimedTask(
                "5",
                "Yaklaşan Görev",
                "Deadline bugün",
                LocalDate.now()
        );

        List<TimedTask> upcomingTasks = taskService.getUpcomingTasks();

        assertEquals(1, upcomingTasks.size());
    }

    @Test
    void varsayilanOncelikMediumMu() {

        TaskService taskService = new TaskService(new NotificationService());

        Task task = taskService.createTask("p1", "Test", "Test");

        assertEquals(Priority.MEDIUM, task.getPriority());
    }

    @Test
    void sureliGorevOncelikAlabiliyorMu() {

        TaskService taskService = new TaskService(new NotificationService());

        TimedTask task = taskService.createTimedTask(
                "t1",
                "Deadline Görev",
                "Test",
                LocalDate.now().plusDays(2)
        );

        task.setPriority(Priority.HIGH);

        assertEquals(Priority.HIGH, task.getPriority());
    }

    @Test
    void highOncelikliGorevBildirimUretiyorMu() {

        NotificationService ns = new NotificationService();
        TaskService ts = new TaskService(ns);

        TimedTask task = ts.createTimedTask(
                "t2",
                "Acil Görev",
                "Test",
                LocalDate.now().plusDays(1)
        );

        task.setPriority(Priority.HIGH);

        ts.getUpcomingTasks();

        assertTrue(ns.hasNotifications());
    }



}
