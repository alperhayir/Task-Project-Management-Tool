package service;

import model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Görev, proje ve kullanıcı verilerini dosyaya aktarma ve dosyadan yükleme işlemlerini yöneten servis sınıfı.
 */
public class ExportService {

    /**
     * Tüm görevleri dosyaya aktarır.
     *
     * @param taskService Görev servisi
     * @param projectService Proje servisi
     * @param userService Kullanıcı servisi
     * @param fileName Dosya adı
     * @return İşlem başarılı ise true, aksi halde false
     */
    public boolean exportAllTasksToFile(TaskService taskService,
                                        ProjectService projectService,
                                        UserService userService,
                                        String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("====================================\n");
            writer.write("   TÜM GÖREVLER RAPORU\n");
            writer.write("====================================\n\n");

            List<Task> tasks = taskService.getAllTasks();

            if (tasks.isEmpty()) {
                writer.write("Hiç görev bulunmamaktadır.\n");
                return true;
            }

            for (Task task : tasks) {
                writer.write("Görev ID: " + task.getId() + "\n");
                writer.write("Başlık: " + task.getTitle() + "\n");
                writer.write("Açıklama: " + task.getDescription() + "\n");
                writer.write("Öncelik: " + task.getPriority() + "\n");
                writer.write("Durum: " + (task.isCompleted() ? "Tamamlandı" : "Devam Ediyor") + "\n");

                if (task instanceof TimedTask timedTask) {
                    LocalDate due = timedTask.getDeadline().getDueDate();
                    long remaining = ChronoUnit.DAYS.between(LocalDate.now(), due);
                    writer.write("Deadline: " + due + "\n");
                    writer.write("Kalan Gün: " + remaining + "\n");
                } else {
                    writer.write("Deadline: Yok\n");
                }

                // Kullanıcı bilgisi
                String userName = "Yok";
                for (User user : userService.getAllUsers()) {
                    if (user.getTasks().contains(task)) {
                        userName = user.getName() + " (ID: " + user.getId() + ")";
                        break;
                    }
                }
                writer.write("Atanan Kullanıcı: " + userName + "\n");

                // Proje bilgisi
                String projectName = "Yok";
                for (Project project : projectService.getAllProjects()) {
                    if (project.getTasks().contains(task)) {
                        projectName = project.getName() + " (ID: " + project.getId() + ")";
                        break;
                    }
                }
                writer.write("Proje: " + projectName + "\n");
                writer.write("------------------------------------\n");
            }

            writer.write("\nToplam Görev Sayısı: " + tasks.size() + "\n");
            return true;

        } catch (IOException e) {
            System.err.println("Dosyaya yazma hatası: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tüm projeleri dosyaya aktarır.
     *
     * @param projectService Proje servisi
     * @param fileName Dosya adı
     * @return İşlem başarılı ise true, aksi halde false
     */
    public boolean exportAllProjectsToFile(ProjectService projectService, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("====================================\n");
            writer.write("   TÜM PROJELER RAPORU\n");
            writer.write("====================================\n\n");

            List<Project> projects = projectService.getAllProjects();

            if (projects.isEmpty()) {
                writer.write("Hiç proje bulunmamaktadır.\n");
                return true;
            }

            for (Project project : projects) {
                writer.write("Proje ID: " + project.getId() + "\n");
                writer.write("Proje Adı: " + project.getName() + "\n");
                writer.write("Görev Sayısı: " + project.getTasks().size() + "\n");

                if (!project.getTasks().isEmpty()) {
                    writer.write("Görevler:\n");
                    for (Task task : project.getTasks()) {
                        writer.write("  - " + task.getId() + ": " + task.getTitle() +
                                " (" + task.getPriority() + ")\n");
                    }
                }
                writer.write("------------------------------------\n");
            }

            writer.write("\nToplam Proje Sayısı: " + projects.size() + "\n");
            return true;

        } catch (IOException e) {
            System.err.println("Dosyaya yazma hatası: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tüm kullanıcıları dosyaya aktarır.
     *
     * @param userService Kullanıcı servisi
     * @param fileName Dosya adı
     * @return İşlem başarılı ise true, aksi halde false
     */
    public boolean exportAllUsersToFile(UserService userService, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("====================================\n");
            writer.write("   TÜM KULLANICILAR RAPORU\n");
            writer.write("====================================\n\n");

            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                writer.write("Hiç kullanıcı bulunmamaktadır.\n");
                return true;
            }

            for (User user : users) {
                writer.write("Kullanıcı ID: " + user.getId() + "\n");
                writer.write("Kullanıcı Adı: " + user.getName() + "\n");
                writer.write("Görev Sayısı: " + user.getTasks().size() + "\n");

                if (!user.getTasks().isEmpty()) {
                    writer.write("Görevler:\n");
                    for (Task task : user.getTasks()) {
                        writer.write("  - " + task.getId() + ": " + task.getTitle() +
                                " (" + task.getPriority() + ")\n");
                    }
                }
                writer.write("------------------------------------\n");
            }

            writer.write("\nToplam Kullanıcı Sayısı: " + users.size() + "\n");
            return true;

        } catch (IOException e) {
            System.err.println("Dosyaya yazma hatası: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tüm verileri (görevler, projeler, kullanıcılar) tek bir dosyaya aktarır.
     *
     * @param taskService Görev servisi
     * @param projectService Proje servisi
     * @param userService Kullanıcı servisi
     * @param fileName Dosya adı
     * @return İşlem başarılı ise true, aksi halde false
     */
    public boolean exportAllToFile(TaskService taskService,
                                   ProjectService projectService,
                                   UserService userService,
                                   String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("====================================\n");
            writer.write("   GÖREV & PROJE YÖNETİM SİSTEMİ\n");
            writer.write("   TAM VERİ RAPORU\n");
            writer.write("====================================\n\n");

            // Kullanıcılar
            writer.write("=== KULLANICILAR ===\n");
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                writer.write("Hiç kullanıcı bulunmamaktadır.\n");
            } else {
                for (User user : users) {
                    writer.write("ID: " + user.getId() + " | Ad: " + user.getName() +
                            " | Görev Sayısı: " + user.getTasks().size() + "\n");
                }
            }
            writer.write("\n");

            // Projeler
            writer.write("=== PROJELER ===\n");
            List<Project> projects = projectService.getAllProjects();
            if (projects.isEmpty()) {
                writer.write("Hiç proje bulunmamaktadır.\n");
            } else {
                for (Project project : projects) {
                    writer.write("ID: " + project.getId() + " | Ad: " + project.getName() +
                            " | Görev Sayısı: " + project.getTasks().size() + "\n");
                }
            }
            writer.write("\n");

            // Görevler
            writer.write("=== GÖREVLER ===\n");
            List<Task> tasks = taskService.getAllTasks();
            if (tasks.isEmpty()) {
                writer.write("Hiç görev bulunmamaktadır.\n");
            } else {
                for (Task task : tasks) {
                    writer.write("ID: " + task.getId() + " | Başlık: " + task.getTitle() +
                            " | Öncelik: " + task.getPriority() +
                            " | Durum: " + (task.isCompleted() ? "Tamamlandı" : "Devam Ediyor"));

                    if (task instanceof TimedTask timedTask) {
                        LocalDate due = timedTask.getDeadline().getDueDate();
                        long remaining = ChronoUnit.DAYS.between(LocalDate.now(), due);
                        writer.write(" | Deadline: " + due + " (Kalan: " + remaining + " gün)");
                    }

                    // Kullanıcı bilgisi
                    String userName = "Yok";
                    String userId = "";
                    for (User user : userService.getAllUsers()) {
                        if (user.getTasks().contains(task)) {
                            userName = user.getName();
                            userId = user.getId();
                            break;
                        }
                    }
                    writer.write(" | Kullanıcı: " + userName + (userId.isEmpty() ? "" : " (ID: " + userId + ")"));

                    // Proje bilgisi
                    String projectName = "Yok";
                    String projectId = "";
                    for (Project project : projectService.getAllProjects()) {
                        if (project.getTasks().contains(task)) {
                            projectName = project.getName();
                            projectId = project.getId();
                            break;
                        }
                    }
                    writer.write(" | Proje: " + projectName + (projectId.isEmpty() ? "" : " (ID: " + projectId + ")"));

                    writer.write("\n");
                }
            }

            writer.write("\n====================================\n");
            writer.write("Özet:\n");
            writer.write("Toplam Kullanıcı: " + users.size() + "\n");
            writer.write("Toplam Proje: " + projects.size() + "\n");
            writer.write("Toplam Görev: " + tasks.size() + "\n");
            writer.write("====================================\n");

            return true;

        } catch (IOException e) {
            System.err.println("Dosyaya yazma hatası: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tüm verileri basit formatta dosyaya kaydeder (yükleme için).
     * Format: Her satır bir kayıt
     * USER|id|name
     * PROJECT|id|name
     * TASK|id|title|description|priority|deadline|userId|projectId|completed
     * TIMEDTASK|id|title|description|priority|deadline|userId|projectId|completed
     *
     * @param taskService Görev servisi
     * @param projectService Proje servisi
     * @param userService Kullanıcı servisi
     * @param fileName Dosya adı
     * @return İşlem başarılı ise true, aksi halde false
     */
    public boolean saveDataToFile(TaskService taskService,
                                ProjectService projectService,
                                UserService userService,
                                String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Kullanıcıları kaydet
            for (User user : userService.getAllUsers()) {
                writer.write("USER|" + user.getId() + "|" + user.getName() + "\n");
            }

            // Projeleri kaydet
            for (Project project : projectService.getAllProjects()) {
                writer.write("PROJECT|" + project.getId() + "|" + project.getName() + "\n");
            }

            // Görevleri kaydet
            for (Task task : taskService.getAllTasks()) {
                // Kullanıcı ID'sini bul
                String userId = "";
                for (User user : userService.getAllUsers()) {
                    if (user.getTasks().contains(task)) {
                        userId = user.getId();
                        break;
                    }
                }

                // Proje ID'sini bul
                String projectId = "";
                for (Project project : projectService.getAllProjects()) {
                    if (project.getTasks().contains(task)) {
                        projectId = project.getId();
                        break;
                    }
                }

                if (task instanceof TimedTask timedTask) {
                    LocalDate due = timedTask.getDeadline().getDueDate();
                    writer.write("TIMEDTASK|" + task.getId() + "|" + task.getTitle() + "|" +
                            task.getDescription() + "|" + task.getPriority() + "|" +
                            due + "|" + userId + "|" + projectId + "|" + task.isCompleted() + "\n");
                } else {
                    writer.write("TASK|" + task.getId() + "|" + task.getTitle() + "|" +
                            task.getDescription() + "|" + task.getPriority() + "||" +
                            userId + "|" + projectId + "|" + task.isCompleted() + "\n");
                }
            }

            return true;
        } catch (IOException e) {
            System.err.println("Dosyaya yazma hatası: " + e.getMessage());
            return false;
        }
    }

    /**
     * Basit format dosyadan veri yükler.
     * Format: Her satır bir kayıt
     * USER|id|name
     * PROJECT|id|name
     * TASK|id|title|description|priority|deadline|userId|projectId|completed
     * TIMEDTASK|id|title|description|priority|deadline|userId|projectId|completed
     *
     * @param taskService Görev servisi
     * @param projectService Proje servisi
     * @param userService Kullanıcı servisi
     * @param fileName Dosya adı
     * @return İşlem başarılı ise true, aksi halde false
     */
    public boolean importFromSimpleFormat(TaskService taskService,
                                         ProjectService projectService,
                                         UserService userService,
                                         String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;

                String type = parts[0].trim();

                switch (type) {
                    case "USER":
                        if (parts.length >= 3) {
                            String userId = parts[1].trim();
                            String userName = parts[2].trim();
                            if (!userService.userExists(userId)) {
                                userService.addUser(userId, userName);
                            }
                        }
                        break;

                    case "PROJECT":
                        if (parts.length >= 3) {
                            String projectId = parts[1].trim();
                            String projectName = parts[2].trim();
                            if (!projectService.projectExists(projectId)) {
                                projectService.createProject(projectId, projectName);
                            }
                        }
                        break;

                    case "TASK":
                        if (parts.length >= 4) {
                            String taskId = parts[1].trim();
                            String title = parts[2].trim();
                            String description = parts[3].trim();
                            Priority priority = parts.length > 4 ? Priority.valueOf(parts[4].trim()) : Priority.MEDIUM;
                            boolean completed = parts.length > 8 && Boolean.parseBoolean(parts[8].trim());

                            if (!taskService.taskExists(taskId)) {
                                Task task = taskService.createTask(taskId, title, description);
                                task.setPriority(priority);
                                if (completed) task.complete();

                                // Kullanıcıya ata
                                if (parts.length > 6 && !parts[6].trim().isEmpty()) {
                                    User user = userService.findUserById(parts[6].trim());
                                    if (user != null) {
                                        taskService.assignTaskToUser(task, user);
                                    }
                                }

                                // Projeye ata
                                if (parts.length > 7 && !parts[7].trim().isEmpty()) {
                                    Project project = projectService.findProjectById(parts[7].trim());
                                    if (project != null) {
                                        taskService.assignTaskToProject(task, project);
                                    }
                                }
                            }
                        }
                        break;

                    case "TIMEDTASK":
                        if (parts.length >= 5) {
                            String taskId = parts[1].trim();
                            String title = parts[2].trim();
                            String description = parts[3].trim();
                            Priority priority = parts.length > 4 ? Priority.valueOf(parts[4].trim()) : Priority.MEDIUM;
                            LocalDate deadline = null;
                            if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                                try {
                                    deadline = LocalDate.parse(parts[5].trim());
                                } catch (DateTimeParseException e) {
                                    // Tarih parse edilemezse atla
                                }
                            }
                            boolean completed = parts.length > 8 && Boolean.parseBoolean(parts[8].trim());

                            if (!taskService.taskExists(taskId) && deadline != null) {
                                TimedTask task = taskService.createTimedTask(taskId, title, description, deadline);
                                task.setPriority(priority);
                                if (completed) task.complete();

                                // Kullanıcıya ata
                                if (parts.length > 6 && !parts[6].trim().isEmpty()) {
                                    User user = userService.findUserById(parts[6].trim());
                                    if (user != null) {
                                        taskService.assignTaskToUser(task, user);
                                    }
                                }

                                // Projeye ata
                                if (parts.length > 7 && !parts[7].trim().isEmpty()) {
                                    Project project = projectService.findProjectById(parts[7].trim());
                                    if (project != null) {
                                        taskService.assignTaskToProject(task, project);
                                    }
                                }
                            }
                        }
                        break;
                }
            }

            return true;
        } catch (IOException e) {
            System.err.println("Dosyadan okuma hatası: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Veri yükleme hatası: " + e.getMessage());
            return false;
        }
    }
}

