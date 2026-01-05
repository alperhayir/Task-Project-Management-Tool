package service;

import model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Görev, proje ve kullanıcı verilerini dosyaya aktarma işlemlerini yöneten servis sınıfı.
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
}

