package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "core_time")
public class CoreTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "core_time_id")
    private long coreTimeId;
    @Column(name = "room_id")
    private long roomId;
    @Column(name = "core_date", nullable = false)
    private String coreDate;
}
