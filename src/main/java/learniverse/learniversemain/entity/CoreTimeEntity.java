package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.CoreTimeDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "core_time")
public class CoreTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "core_time_id")
    private long coreTimeId;
    @Column(name = "room_id")
    private long roomId;
    @Column(name = "core_start_time", nullable = false)
    private LocalDateTime coreStartTime;
    @Column(name = "core_end_time", nullable = false)
    private LocalDateTime coreEndTime;
    @Column(name = "capture_num", nullable = false)
    private Integer captureNum;

    public CoreTimeEntity(CoreTimeDTO coreTimeDTO){
        this.roomId = coreTimeDTO.getRoomId();
        this.coreStartTime = coreTimeDTO.getCoreStartTime().plusHours(9);
        System.out.println(coreStartTime);
        int min = coreTimeDTO.getCoreHour()*60 + coreTimeDTO.getCoreMinute();
        this.coreEndTime = coreTimeDTO.getCoreStartTime().plusMinutes(min).plusHours(9);
        this.captureNum = coreTimeDTO.getCaptureNum();
    }
}
