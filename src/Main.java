import model.Project;
import model.Task;
import model.TimedTask;
import model.User;
import service.ProjectService;
import service.TaskService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        List<User> users = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println("====================================");
            System.out.println("   GÖREV & PROJE YÖNETİM SİSTEMİ");
            System.out.println("====================================");
            System.out.println("1 - Görev Oluştur");
            System.out.println("2 - Süreli Görev Oluştur");
            System.out.println("3 - Görev Tamamla");
            System.out.println("4 - Görevi Kullanıcıya Ata");
            System.out.println("5 - Görevi Projeye Ata");
            System.out.println("6 - Yaklaşan Görevleri Listele");
            System.out.println("7 - Tüm Görevleri Listele");
            System.out.println("0 - Çıkış");
            System.out.print("Seçiminiz: ");

            int secim = scanner.nextInt();
            scanner.nextLine(); // buffer temizle

            switch (secim) {

                // 1 - Görev Oluştur
                case 1:
                    System.out.print("Görev ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Görev Başlığı: ");
                    String title = scanner.nextLine();

                    System.out.print("Görev Açıklaması: ");
                    String description = scanner.nextLine();

                    taskService.createTask(id, title, description);
                    System.out.println("✔ Görev oluşturuldu.");
                    break;

                // 2 - Süreli Görev Oluştur
                case 2:
                    System.out.print("Süreli Görev ID: ");
                    String tid = scanner.nextLine();

                    System.out.print("Görev Başlığı: ");
                    String ttitle = scanner.nextLine();

                    System.out.print("Görev Açıklaması: ");
                    String tdesc = scanner.nextLine();

                    System.out.print("Deadline (YYYY-MM-DD): ");
                    LocalDate dueDate = LocalDate.parse(scanner.nextLine());

                    taskService.createTimedTask(tid, ttitle, tdesc, dueDate);
                    System.out.println("✔ Süreli görev oluşturuldu.");
                    break;

                // 3 - Görev Tamamla
                case 3:
                    System.out.print("Tamamlanacak Görev ID: ");
                    String completeId = scanner.nextLine();

                    taskService.completeTask(completeId);
                    System.out.println("✔ Görev tamamlandı.");
                    break;

                // 4 - Görevi Kullanıcıya Ata
                case 4:
                    System.out.print("Kullanıcı ID: ");
                    String userId = scanner.nextLine();

                    User user = findUserById(users, userId);
                    if (user == null) {
                        System.out.print("Kullanıcı Adı: ");
                        String userName = scanner.nextLine();
                        user = new User(userId, userName);
                        users.add(user);
                    }

                    System.out.print("Atanacak Görev ID: ");
                    String taskIdForUser = scanner.nextLine();

                    Task taskForUser = findTaskById(taskService.getAllTasks(), taskIdForUser);
                    if (taskForUser != null) {
                        taskService.assignTaskToUser(taskForUser, user);
                        System.out.println("✔ Görev kullanıcıya atandı.");
                    } else {
                        System.out.println("✖ Görev bulunamadı.");
                    }
                    break;

                // 5 - Görevi Projeye Ata
                case 5:
                    System.out.print("Proje ID: ");
                    String projectId = scanner.nextLine();

                    Project project = projectService.findProjectById(projectId);
                    if (project == null) {
                        System.out.print("Proje Adı: ");
                        String projectName = scanner.nextLine();
                        project = projectService.createProject(projectId, projectName);
                    }

                    System.out.print("Atanacak Görev ID: ");
                    String taskIdForProject = scanner.nextLine();

                    Task taskForProject = findTaskById(taskService.getAllTasks(), taskIdForProject);
                    if (taskForProject != null) {
                        taskService.assignTaskToProject(taskForProject, project);
                        System.out.println("✔ Görev projeye atandı.");
                    } else {
                        System.out.println("✖ Görev bulunamadı.");
                    }
                    break;

                // 6 - Yaklaşan Görevleri Listele
                case 6:
                    List<TimedTask> upcomingTasks = taskService.getUpcomingTasks();

                    if (upcomingTasks.isEmpty()) {
                        System.out.println("Yaklaşan görev bulunmamaktadır.");
                    } else {
                        System.out.println("===== YAKLAŞAN GÖREVLER =====");
                        for (TimedTask t : upcomingTasks) {
                            System.out.println(
                                    "ID: " + t.getId() +
                                            " | " + t.getTitle() +
                                            " | Deadline: " + t.getDeadline().getDueDate()
                            );
                        }
                    }
                    break;

                // 7 - Tüm Görevleri Listele
                case 7:
                    System.out.println("===== TÜM GÖREVLER =====");
                    for (Task t : taskService.getAllTasks()) {
                        if (t instanceof TimedTask) {
                            TimedTask tt = (TimedTask) t;
                            System.out.println(
                                    "ID: " + tt.getId() +
                                            " | " + tt.getTitle() +
                                            " | Tamamlandı: " + tt.isCompleted() +
                                            " | Deadline: " + tt.getDeadline().getDueDate()
                            );
                        } else {
                            System.out.println(
                                    "ID: " + t.getId() +
                                            " | " + t.getTitle() +
                                            " | Tamamlandı: " + t.isCompleted()
                            );
                        }
                    }
                    break;

                // 0 - Çıkış
                case 0:
                    running = false;
                    System.out.println("Programdan çıkılıyor...");
                    break;

                default:
                    System.out.println("Geçersiz seçim!");
            }

            System.out.println();
        }

        scanner.close();
    }



    private static User findUserById(List<User> users, String id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    private static Task findTaskById(List<Task> tasks, String id) {
        for (Task t : tasks) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }
}
