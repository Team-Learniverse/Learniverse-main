package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long room_id;
    @Column(nullable = false)
    private String room_name;
    @Column(nullable = true)
    private String room_intro;
    @Column(nullable = false)
    private int room_limit;
    @Column(nullable = true)
    private String room_git_org;
    @Column(nullable = true)
    private String room_notion;
    @Column(nullable = true)
    private String room_google_drive;
    @Column(nullable = true)
    private String room_figma;


}
