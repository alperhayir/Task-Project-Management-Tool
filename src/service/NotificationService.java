package service;

import model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private List<Notification> notifications;

    public NotificationService() {
        this.notifications = new ArrayList<>();
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public List<Notification> getAllNotifications() {
        return notifications;
    }

    public boolean hasNotifications() {
        return !notifications.isEmpty();
    }
}
