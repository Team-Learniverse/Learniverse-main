package learniverse.learniversemain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "core_time")
public class CoreTimeEntity {
    @Id
    @Column(name = "room_id")
    private long roomId;
    @Column(name = "core_date", nullable = false)
    private String coreDate;
}
