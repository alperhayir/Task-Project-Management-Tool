package service;

import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExportServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void tumGorevlerDosyayaAktariliyorMu() throws IOException {
        // Arrange
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();
        ExportService exportService = new ExportService();

        Task task1 = taskService.createTask("t1", "Görev 1", "Açıklama 1");
        Task task2 = taskService.createTask("t2", "Görev 2", "Açıklama 2");

        File testFile = tempDir.resolve("test_tasks.txt").toFile();

        // Act
        boolean result = exportService.exportAllTasksToFile(
                taskService, projectService, userService, testFile.getAbsolutePath());

        // Assert
        assertTrue(result);
        assertTrue(testFile.exists());
        assertTrue(testFile.length() > 0);

        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("Görev 1"));
        assertTrue(content.contains("Görev 2"));
        assertTrue(content.contains("TÜM GÖREVLER RAPORU"));
    }

    @Test
    void bosGorevListesiDosyayaAktariliyorMu() throws IOException {
        // Arrange
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();
        ExportService exportService = new ExportService();

        File testFile = tempDir.resolve("test_empty.txt").toFile();

        // Act
        boolean result = exportService.exportAllTasksToFile(
                taskService, projectService, userService, testFile.getAbsolutePath());

        // Assert
        assertTrue(result);
        assertTrue(testFile.exists());

        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("Hiç görev bulunmamaktadır"));
    }

    @Test
    void sureliGorevDosyayaAktariliyorMu() throws IOException {
        // Arrange
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();
        ExportService exportService = new ExportService();

        TimedTask timedTask = taskService.createTimedTask(
                "t1", "Süreli Görev", "Test", LocalDate.now().plusDays(5));

        File testFile = tempDir.resolve("test_timed.txt").toFile();

        // Act
        boolean result = exportService.exportAllTasksToFile(
                taskService, projectService, userService, testFile.getAbsolutePath());

        // Assert
        assertTrue(result);
        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("Süreli Görev"));
        assertTrue(content.contains("Deadline:"));
    }

    @Test
    void tumProjelerDosyayaAktariliyorMu() throws IOException {
        // Arrange
        ProjectService projectService = new ProjectService();
        ExportService exportService = new ExportService();

        projectService.createProject("p1", "Proje 1");
        projectService.createProject("p2", "Proje 2");

        File testFile = tempDir.resolve("test_projects.txt").toFile();

        // Act
        boolean result = exportService.exportAllProjectsToFile(
                projectService, testFile.getAbsolutePath());

        // Assert
        assertTrue(result);
        assertTrue(testFile.exists());

        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("Proje 1"));
        assertTrue(content.contains("Proje 2"));
        assertTrue(content.contains("TÜM PROJELER RAPORU"));
    }

    @Test
    void tumKullanicilarDosyayaAktariliyorMu() throws IOException {
        // Arrange
        UserService userService = new UserService();
        ExportService exportService = new ExportService();

        userService.addUser("u1", "Kullanıcı 1");
        userService.addUser("u2", "Kullanıcı 2");

        File testFile = tempDir.resolve("test_users.txt").toFile();

        // Act
        boolean result = exportService.exportAllUsersToFile(
                userService, testFile.getAbsolutePath());

        // Assert
        assertTrue(result);
        assertTrue(testFile.exists());

        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("Kullanıcı 1"));
        assertTrue(content.contains("Kullanıcı 2"));
        assertTrue(content.contains("TÜM KULLANICILAR RAPORU"));
    }

    @Test
    void tumVerilerDosyayaAktariliyorMu() throws IOException {
        // Arrange
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();
        ExportService exportService = new ExportService();

        taskService.createTask("t1", "Görev", "Test");
        projectService.createProject("p1", "Proje");
        userService.addUser("u1", "Kullanıcı");

        File testFile = tempDir.resolve("test_all.txt").toFile();

        // Act
        boolean result = exportService.exportAllToFile(
                taskService, projectService, userService, testFile.getAbsolutePath());

        // Assert
        assertTrue(result);
        assertTrue(testFile.exists());

        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("Görev"));
        assertTrue(content.contains("Proje"));
        assertTrue(content.contains("Kullanıcı"));
        assertTrue(content.contains("TAM VERİ RAPORU"));
        assertTrue(content.contains("Toplam Kullanıcı: 1"));
        assertTrue(content.contains("Toplam Proje: 1"));
        assertTrue(content.contains("Toplam Görev: 1"));
    }

    @Test
    void gorevKullaniciBilgisiDosyayaAktariliyorMu() throws IOException {
        // Arrange
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();
        ExportService exportService = new ExportService();

        userService.addUser("u1", "Test Kullanıcı");
        User user = userService.findUserById("u1"); // UserService'den al

        Task task = taskService.createTask("t1", "Görev", "Test");
        taskService.assignTaskToUser(task, user);

        File testFile = tempDir.resolve("test_assigned.txt").toFile();

        // Act
        boolean result = exportService.exportAllTasksToFile(
                taskService, projectService, userService, testFile.getAbsolutePath());

        // Assert
        assertTrue(result);
        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("Test Kullanıcı"));
        assertTrue(content.contains("Atanan Kullanıcı:"));
    }

    @Test
    void gorevProjeBilgisiDosyayaAktariliyorMu() throws IOException {
        // Arrange
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();
        ExportService exportService = new ExportService();

        Project project = projectService.createProject("p1", "Test Proje");
        Task task = taskService.createTask("t1", "Görev", "Test");
        taskService.assignTaskToProject(task, project);

        File testFile = tempDir.resolve("test_project.txt").toFile();

        // Act
        boolean result = exportService.exportAllTasksToFile(
                taskService, projectService, userService, testFile.getAbsolutePath());

        // Assert
        assertTrue(result);
        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("Test Proje"));
        assertTrue(content.contains("Proje:"));
    }

    @Test
    void tamamlanmisGorevDosyayaAktariliyorMu() throws IOException {
        // Arrange
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();
        ExportService exportService = new ExportService();

        Task task = taskService.createTask("t1", "Görev", "Test");
        taskService.completeTask("t1");

        File testFile = tempDir.resolve("test_completed.txt").toFile();

        // Act
        boolean result = exportService.exportAllTasksToFile(
                taskService, projectService, userService, testFile.getAbsolutePath());

        // Assert
        assertTrue(result);
        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("Tamamlandı"));
    }
}

