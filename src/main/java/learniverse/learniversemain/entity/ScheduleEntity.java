package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "schedules")
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private long scheduleId;
    @Column(name = "room_id", nullable = false)
    private int roomId;
    @Column(name = "schedule_date", nullable = false)
    private String scheduleDate;
    @Column(name = "schedule_task", nullable = false)
    private String scheduleTask;
    @Column(name = "schedule_is_done", nullable = false)
    private int scheduleIsDone;
}
