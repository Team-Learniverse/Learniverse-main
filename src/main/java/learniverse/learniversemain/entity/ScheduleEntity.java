package learniverse.learniversemain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "schedules")
public class ScheduleEntity {
    @Id
    private int room_id;
    @Column(nullable = false)
    private String schedule_date;
    @Column(nullable = false)
    private String schedule_task;
    @Column(nullable = false)
    private int schedule_is_done;
}
