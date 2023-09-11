package learniverse.learniversemain.entity;


import jakarta.persistence.*;
import lombok.Builder;
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
    private long memberId;
    @Column(name = "member_email", nullable = false)
    private String memberEmail;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "member_message", nullable = true)
    private String memberMessage;

    @Builder
    public MemberEntity(String nickname, String memberEmail){
        this.nickname=nickname;
        this.memberEmail=memberEmail;
    }
}
