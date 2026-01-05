package service;

import model.Notification;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceTest {
    @Test
    void baslangictaBildirimYokMu() {

        NotificationService notificationService = new NotificationService();

        assertFalse(notificationService.hasNotifications());
        assertEquals(0, notificationService.getAllNotifications().size());
    }

    @Test
    void bildirimEklenebiliyorMu() {

        NotificationService notificationService = new NotificationService();

        Task task = new Task("t1", "Test Görev", "Test");
        Notification notification =
                new Notification("Test Bildirimi", task);

        notificationService.addNotification(notification);

        assertTrue(notificationService.hasNotifications());
        assertEquals(1, notificationService.getAllNotifications().size());
    }

    @Test
    void eklenenBildirimDogruMu() {

        NotificationService notificationService = new NotificationService();

        Task task = new Task("t2", "Görev", "Açıklama");
        Notification notification =
                new Notification("Bildirim Mesajı", task);

        notificationService.addNotification(notification);

        Notification stored =
                notificationService.getAllNotifications().get(0);

        assertEquals("Bildirim Mesajı", stored.getMessage());
        assertEquals(task, stored.getRelatedTask());
    }

}
