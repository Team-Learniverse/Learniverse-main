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
    @Column(name = "core_start_date", nullable = false)
    private LocalDateTime coreStartDate;
    @Column(name = "core_end_date", nullable = false)
    private LocalDateTime coreEndDate;

    public CoreTimeEntity(CoreTimeDTO coreTimeDTO){
        this.roomId = coreTimeDTO.getRoomId();
        this.coreStartDate = coreTimeDTO.getCoreStartDate();
        this.coreEndDate = coreTimeDTO.getCoreEndDate();
    }
}
