package learniverse.learniversemain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "members")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long member_id;
    @Column(nullable = false)
    private String member_email;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String member_message;
}
