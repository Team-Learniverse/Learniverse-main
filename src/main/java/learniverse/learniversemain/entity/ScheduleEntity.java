package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.ScheduleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "schedules")
@NoArgsConstructor
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private long scheduleId;
    @Column(name = "room_id", nullable = false)
    private long roomId;
    @Column(name = "schedule_date", nullable = false)
    private LocalDateTime scheduleDate;
    @Column(name = "schedule_task", nullable = false)
    private String scheduleTask;
    @Column(name = "schedule_is_done", nullable = false)
    private boolean scheduleIsDone;

    public ScheduleEntity(ScheduleDTO scheduleDTO) {
        this.roomId = scheduleDTO.getRoomId();
        this.scheduleDate = scheduleDTO.getScheduleDate();
        this.scheduleTask = scheduleDTO.getScheduleTask();
        this.scheduleIsDone = false;
    }
}
