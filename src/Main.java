import model.*;
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
            System.out.println("8 - Kullanıcı Ekle");
            System.out.println("9 - Proje Ekle");
            System.out.println("0 - Çıkış");
            System.out.print("Seçiminiz: ");

            int secim = scanner.nextInt();
            scanner.nextLine();

            switch (secim) {


                case 1: {
                    String taskId;

                    while (true) {
                        System.out.print("Görev ID (0: Ana Menü): ");
                        taskId = scanner.nextLine();

                        if (taskId.equals("0")) break;

                        if (taskExists(taskService, taskId)) {
                            System.out.println("Bu ID ile görev zaten mevcut. Tekrar deneyin.");
                            continue;
                        }
                        break;
                    }

                    if (taskId.equals("0")) break;

                    System.out.print("Görev Başlığı: ");
                    String title = scanner.nextLine();

                    System.out.print("Görev Açıklaması: ");
                    String desc = scanner.nextLine();

                    taskService.createTask(taskId, title, desc);
                    System.out.println(" Görev oluşturuldu.");
                    break;
                }


                case 2: {
                    String timedId;

                    while (true) {
                        System.out.print("Süreli Görev ID (0: Ana Menü): ");
                        timedId = scanner.nextLine();

                        if (timedId.equals("0")) break;

                        if (taskExists(taskService, timedId)) {
                            System.out.println(" Bu ID ile görev zaten mevcut. Tekrar deneyin.");
                            continue;
                        }
                        break;
                    }

                    if (timedId.equals("0")) break;

                    System.out.print("Görev Başlığı: ");
                    String tTitle = scanner.nextLine();

                    System.out.print("Görev Açıklaması: ");
                    String tDesc = scanner.nextLine();

                    System.out.print("Deadline (YYYY-MM-DD): ");
                    LocalDate dueDate = LocalDate.parse(scanner.nextLine());

                    taskService.createTimedTask(timedId, tTitle, tDesc, dueDate);
                    System.out.println("✔ Süreli görev oluşturuldu.");
                    break;
                }


                case 3:
                    if (taskService.getAllTasks().isEmpty()) {
                        System.out.println(" Henüz hiç görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Tamamlanacak Görev ID (0: Ana Menü): ");
                        String completeId = scanner.nextLine();

                        if (completeId.equals("0")) break;

                        if (!taskExists(taskService, completeId)) {
                            System.out.println("✖ Geçersiz görev ID. Tekrar deneyin.");
                            continue;
                        }

                        taskService.completeTask(completeId);
                        System.out.println("✔ Görev tamamlandı.");
                        break;
                    }
                    break;


                case 4:
                    if (taskService.getAllTasks().isEmpty()) {
                        System.out.println(" Görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    if (users.isEmpty()) {
                        System.out.println(" Kullanıcı yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    User selectedUser = null;
                    while (true) {
                        printAllUsers(users);
                        System.out.print("Kullanıcı ID (0: Ana Menü): ");
                        String uid = scanner.nextLine();

                        if (uid.equals("0")) break;

                        selectedUser = findUserById(users, uid);
                        if (selectedUser == null) {
                            System.out.println(" Geçersiz kullanıcı ID. Tekrar deneyin.");
                            continue;
                        }
                        break;
                    }

                    if (selectedUser == null) break;

                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Atanacak Görev ID (0: Ana Menü): ");
                        String taskToUser = scanner.nextLine();

                        if (taskToUser.equals("0")) break;

                        Task tUser = findTaskById(taskService.getAllTasks(), taskToUser);
                        if (tUser == null) {
                            System.out.println(" Geçersiz görev ID. Tekrar deneyin.");
                            continue;
                        }

                        taskService.assignTaskToUser(tUser, selectedUser);
                        System.out.println("✔Görev kullanıcıya atandı.");
                        break;
                    }
                    break;


                case 5:
                    if (taskService.getAllTasks().isEmpty()) {
                        System.out.println(" Görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    if (projectService.getAllProjects().isEmpty()) {
                        System.out.println(" Proje yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    Project selectedProject = null;
                    while (true) {
                        printAllProjects(projectService);
                        System.out.print("Proje ID (0: Ana Menü): ");
                        String pid = scanner.nextLine();

                        if (pid.equals("0")) break;

                        selectedProject = projectService.findProjectById(pid);
                        if (selectedProject == null) {
                            System.out.println("✖ Geçersiz proje ID. Tekrar deneyin.");
                            continue;
                        }
                        break;
                    }

                    if (selectedProject == null) break;

                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Atanacak Görev ID (0: Ana Menü): ");
                        String taskToProject = scanner.nextLine();

                        if (taskToProject.equals("0")) break;

                        Task tProj = findTaskById(taskService.getAllTasks(), taskToProject);
                        if (tProj == null) {
                            System.out.println("✖ Geçersiz görev ID. Tekrar deneyin.");
                            continue;
                        }

                        taskService.assignTaskToProject(tProj, selectedProject);
                        System.out.println("✔ Görev projeye atandı.");
                        break;
                    }
                    break;


                case 6:
                    List<TimedTask> upcoming = taskService.getUpcomingTasks();
                    if (upcoming.isEmpty()) {
                        System.out.println("Yaklaşan görev yok.");
                        returnToMainMenu(scanner);
                    } else {
                        for (TimedTask t : upcoming) {
                            System.out.println(t.getId() + " - " + t.getTitle()
                                    + " | Deadline: " + t.getDeadline().getDueDate());
                        }
                    }
                    break;


                case 7:
                    printAllTasksDetailed(taskService, users, projectService);
                    break;


                case 8:
                    System.out.print("Kullanıcı ID: ");
                    String newUid = scanner.nextLine();

                    if (userExists(users, newUid)) {
                        System.out.println("✖ Bu ID ile kullanıcı zaten var.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    System.out.print("Kullanıcı Adı: ");
                    String uname = scanner.nextLine();
                    users.add(new User(newUid, uname));
                    System.out.println("✔ Kullanıcı eklendi.");
                    break;


                case 9:
                    System.out.print("Proje ID: ");
                    String projId = scanner.nextLine();

                    if (projectService.findProjectById(projId) != null) {
                        System.out.println("✖ Bu ID ile proje zaten var.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    System.out.print("Proje Adı: ");
                    String pname = scanner.nextLine();
                    projectService.createProject(projId, pname);
                    System.out.println("✔ Proje eklendi.");
                    break;

                case 0:
                    running = false;
                    System.out.println("Programdan çıkılıyor...");
                    break;

                default:
                    System.out.println("Geçersiz seçim!");
                    returnToMainMenu(scanner);
            }

            System.out.println();
        }

        scanner.close();
    }



    private static void returnToMainMenu(Scanner scanner) {
        System.out.print("Ana menüye dönmek için 0'a basın: ");
        while (true) {
            if (scanner.nextLine().equals("0")) break;
            System.out.print("Lütfen 0'a basın: ");
        }
    }

    private static boolean taskExists(TaskService ts, String id) {
        return findTaskById(ts.getAllTasks(), id) != null;
    }

    private static boolean userExists(List<User> users, String id) {
        return findUserById(users, id) != null;
    }

    private static User findUserById(List<User> users, String id) {
        for (User u : users) {
            if (u.getId().equals(id)) return u;
        }
        return null;
    }

    private static Task findTaskById(List<Task> tasks, String id) {
        for (Task t : tasks) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }

    private static void printAllTasksSimple(TaskService ts) {
        for (Task t : ts.getAllTasks()) {
            System.out.println(t.getId() + " - " + t.getTitle()
                    + " | Tamamlandı: " + t.isCompleted());
        }
    }

    private static void printAllUsers(List<User> users) {
        for (User u : users) {
            System.out.println(u.getId() + " - " + u.getName());
        }
    }

    private static void printAllProjects(ProjectService ps) {
        for (Project p : ps.getAllProjects()) {
            System.out.println(p.getId() + " - " + p.getName());
        }
    }

    private static void printAllTasksDetailed(
            TaskService ts, List<User> users, ProjectService ps) {

        if (ts.getAllTasks().isEmpty()) {
            System.out.println("Görev yok.");
            return;
        }

        for (Task t : ts.getAllTasks()) {

            System.out.println("ID: " + t.getId());
            System.out.println("Ad: " + t.getTitle());
            System.out.println("Tamamlandı: " + t.isCompleted());

            if (t instanceof TimedTask) {
                System.out.println("Deadline: "
                        + ((TimedTask) t).getDeadline().getDueDate());
            } else {
                System.out.println("Deadline: Yok");
            }

            String userName = "Yok";
            for (User u : users) {
                if (u.getTasks().contains(t)) {
                    userName = u.getName();
                    break;
                }
            }
            System.out.println("Kullanıcı: " + userName);

            String projectName = "Yok";
            for (Project p : ps.getAllProjects()) {
                if (p.getTasks().contains(t)) {
                    projectName = p.getName();
                    break;
                }
            }
            System.out.println("Proje: " + projectName);
            System.out.println("----------------------------");
        }
    }
}
