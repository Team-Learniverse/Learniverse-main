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
    @Column(name = "member_id")
    private Long memberId;
    @Column(name = "member_email", nullable = false)
    private String memberEmail;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "member_message", nullable = false)
    private String memberMessage;
}
