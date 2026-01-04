package model;

import java.time.LocalDateTime;

public class Notification {

    private String message;
    private LocalDateTime createdAt;
    private Task relatedTask;

    public Notification(String message, Task relatedTask) {
        this.message = message;
        this.relatedTask = relatedTask;
        this.createdAt = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Task getRelatedTask() {
        return relatedTask;
    }
}
