import model.*;
import service.ProjectService;
import service.TaskService;
import service.UserService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();

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
            System.out.println("8 - Kullanıcı Ekle");
            System.out.println("9 - Proje Ekle");
            System.out.println("0 - Çıkış");
            System.out.print("Seçiminiz: ");

            int secim = scanner.nextInt();
            scanner.nextLine();

            switch (secim) {

                // 1️⃣ Görev oluştur
                case 1 -> {

                    String id;
                    while (true) {
                        System.out.print("Görev ID (0: Ana Menü): ");
                        id = scanner.nextLine();

                        if (id.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        if (taskService.taskExists(id)) {
                            System.out.println("✖ Bu ID ile görev zaten var.");
                        } else break;
                    }

                    System.out.print("Başlık: ");
                    String title = scanner.nextLine();

                    System.out.print("Açıklama: ");
                    String desc = scanner.nextLine();

                    Priority priority = readPriority(scanner);

                    Task task = taskService.createTask(id, title, desc);
                    task.setPriority(priority);

                    System.out.println("✔ Görev oluşturuldu.");
                }

                // 2️⃣ Süreli görev oluştur
                case 2 -> {

                    String id;
                    while (true) {
                        System.out.print("Görev ID (0: Ana Menü): ");
                        id = scanner.nextLine();

                        if (id.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        if (taskService.taskExists(id)) {
                            System.out.println("✖ Bu ID ile görev zaten var.");
                        } else break;
                    }

                    System.out.print("Başlık: ");
                    String title = scanner.nextLine();

                    System.out.print("Açıklama: ");
                    String desc = scanner.nextLine();

                    Priority priority = readPriority(scanner);

                    LocalDate deadline = readValidDate(scanner);
                    if (deadline == null) {
                        returnToMainMenu(scanner);
                        return;
                    }

                    TimedTask task = taskService.createTimedTask(id, title, desc, deadline);
                    task.setPriority(priority);

                    System.out.println("✔ Süreli görev oluşturuldu.");
                }

                // 3️⃣ Görev tamamla
                case 3 -> {

                    if (taskService.getAllTasks().isEmpty()) {
                        System.out.println("✖ Hiç görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Tamamlanacak Görev ID (0): ");
                        String id = scanner.nextLine();

                        if (id.equals("0")) {
                            returnToMainMenu(scanner);
                            break;
                        }

                        if (taskService.completeTask(id)) {
                            System.out.println("✔ Görev tamamlandı.");
                            break;
                        } else {
                            System.out.println("✖ Görev bulunamadı.");
                        }
                    }
                }

                // 4️⃣ Görevi kullanıcıya ata
                case 4 -> {

                    if (taskService.getAllTasks().isEmpty()
                            || userService.getAllUsers().isEmpty()) {
                        System.out.println("✖ Görev veya kullanıcı yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    User user;
                    while (true) {
                        printAllUsers(userService);
                        System.out.print("Kullanıcı ID (0): ");
                        String uid = scanner.nextLine();

                        if (uid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        user = userService.findUserById(uid);
                        if (user != null) break;

                        System.out.println("✖ Kullanıcı bulunamadı.");
                    }

                    Task task;
                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Görev ID (0): ");
                        String tid = scanner.nextLine();

                        if (tid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        task = taskService.findTaskById(tid);
                        if (task != null) break;

                        System.out.println("✖ Görev bulunamadı.");
                    }

                    taskService.assignTaskToUser(task, user);
                    System.out.println("✔ Görev kullanıcıya atandı.");
                }

                // 5️⃣ Görevi projeye ata
                case 5 -> {

                    if (taskService.getAllTasks().isEmpty()
                            || projectService.getAllProjects().isEmpty()) {
                        System.out.println("✖ Görev veya proje yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    Project project;
                    while (true) {
                        printAllProjects(projectService);
                        System.out.print("Proje ID (0): ");
                        String pid = scanner.nextLine();

                        if (pid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        project = projectService.findProjectById(pid);
                        if (project != null) break;

                        System.out.println("✖ Proje bulunamadı.");
                    }

                    Task task;
                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Görev ID (0): ");
                        String tid = scanner.nextLine();

                        if (tid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        task = taskService.findTaskById(tid);
                        if (task != null) break;

                        System.out.println("✖ Görev bulunamadı.");
                    }

                    taskService.assignTaskToProject(task, project);
                    System.out.println("✔ Görev projeye atandı.");
                }

                // 6️⃣ Yaklaşan görevler
                case 6 -> {

                    List<TimedTask> upcoming = taskService.getUpcomingTasks();

                    if (upcoming.isEmpty()) {
                        System.out.println("Yaklaşan görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    for (TimedTask t : upcoming) {

                        LocalDate due = t.getDeadline().getDueDate();
                        long remaining = ChronoUnit.DAYS.between(LocalDate.now(), due);

                        String projectName = "Yok";
                        for (Project p : projectService.getAllProjects()) {
                            if (p.getTasks().contains(t)) {
                                projectName = p.getName();
                                break;
                            }
                        }

                        System.out.println("ID: " + t.getId());
                        System.out.println("Ad: " + t.getTitle());
                        System.out.println("Öncelik: " + t.getPriority());
                        System.out.println("Proje: " + projectName);
                        System.out.println("Deadline: " + due);
                        System.out.println("Kalan Gün: " + remaining);
                        System.out.println("------------------");
                    }

                    returnToMainMenu(scanner);
                }

                // 7️⃣ Tüm görevleri listele
                case 7 -> {

                    if (taskService.getAllTasks().isEmpty()) {
                        System.out.println("Görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    for (Task t : taskService.getAllTasks()) {

                        System.out.println("ID: " + t.getId());
                        System.out.println("Ad: " + t.getTitle());
                        System.out.println("Öncelik: " + t.getPriority());

                        if (t instanceof TimedTask tt) {
                            LocalDate due = tt.getDeadline().getDueDate();
                            long remaining = ChronoUnit.DAYS.between(LocalDate.now(), due);
                            System.out.println("Deadline: " + due);
                            System.out.println("Kalan Gün: " + remaining);
                        } else {
                            System.out.println("Deadline: Yok");
                        }

                        String userName = "Yok";
                        for (User u : userService.getAllUsers()) {
                            if (u.getTasks().contains(t)) {
                                userName = u.getName();
                                break;
                            }
                        }

                        String projectName = "Yok";
                        for (Project p : projectService.getAllProjects()) {
                            if (p.getTasks().contains(t)) {
                                projectName = p.getName();
                                break;
                            }
                        }

                        System.out.println("Kullanıcı: " + userName);
                        System.out.println("Proje: " + projectName);
                        System.out.println("----------------------------");
                    }

                    returnToMainMenu(scanner);
                }

                case 0 -> {
                    running = false;
                    System.out.println("Programdan çıkılıyor...");
                }
            }

            System.out.println();
        }

        scanner.close();
    }

    // -------- YARDIMCI METODLAR --------

    private static Priority readPriority(Scanner scanner) {
        while (true) {
            System.out.println("Öncelik Seç:");
            System.out.println("1 - LOW");
            System.out.println("2 - MEDIUM");
            System.out.println("3 - HIGH");
            System.out.print("Seçim: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1": return Priority.LOW;
                case "2": return Priority.MEDIUM;
                case "3": return Priority.HIGH;
                default:
                    System.out.println("✖ Geçersiz seçim, tekrar deneyin.");
            }
        }
    }

    private static LocalDate readValidDate(Scanner scanner) {
        while (true) {
            System.out.print("Deadline (YYYY-MM-DD) (0): ");
            String input = scanner.nextLine();

            if (input.equals("0")) return null;

            try {
                LocalDate date = LocalDate.parse(input);

                if (date.isBefore(LocalDate.now())) {
                    System.out.println("✖ Geçmiş tarih girilemez.");
                    continue;
                }

                return date;

            } catch (Exception e) {
                System.out.println("✖ Geçersiz tarih formatı. Örnek: 2024-05-12");
            }
        }
    }

    private static void returnToMainMenu(Scanner scanner) {
        System.out.print("Ana menüye dönmek için 0'a basın: ");
        while (!scanner.nextLine().equals("0")) {
            System.out.print("Lütfen 0'a basın: ");
        }
    }

    private static void printAllTasksSimple(TaskService ts) {
        for (Task t : ts.getAllTasks()) {
            System.out.println(t.getId() + " - " + t.getTitle()
                    + " (" + t.getPriority() + ")");
        }
    }

    private static void printAllUsers(UserService us) {
        for (User u : us.getAllUsers()) {
            System.out.println(u.getId() + " - " + u.getName());
        }
    }

    private static void printAllProjects(ProjectService ps) {
        for (Project p : ps.getAllProjects()) {
            System.out.println(p.getId() + " - " + p.getName());
        }
    }
}
