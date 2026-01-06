package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimedTask extends Task {

    private Deadline deadline;

    public TimedTask(String id , String title , String description ,Deadline deadline) {
        super(id, title, description);
        this.deadline = deadline;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    /**
     * TimedTask için detaylı bilgileri döndürür.
     * Polymorphism örneği: Task tipindeki bir değişken TimedTask instance'ı tutuyorsa,
     * bu metod çağrıldığında TimedTask'ın versiyonu çalışır.
     *
     * @return TimedTask'ın detaylı bilgisi (deadline bilgisi dahil)
     */
    @Override
    public String getDetails() {
        LocalDate dueDate = deadline.getDueDate();
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        return super.getDetails() + String.format(" | Deadline: %s (Kalan: %d gün)", dueDate, remainingDays);
    }
}
