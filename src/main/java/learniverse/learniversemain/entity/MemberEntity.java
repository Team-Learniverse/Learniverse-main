package learniverse.learniversemain.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "members")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    @Column(name = "member_email", nullable = true)
    private String memberEmail;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "github_id", nullable = false)
    private String githubId;
    @Column(name = "member_message", nullable = true)
    private String memberMessage;
    @Column(name = "member_url", nullable = true)
    private String imageUrl;
    @Column(name = "member_isFirst")
    private Boolean memberFirst;
    @Column(name = "last_login_date", nullable = true)
    private LocalDate LastLoginDate;

    @Builder
    public MemberEntity(String nickname, String githubId, String memberEmail, String imageUrl) {
        LocalDate now = LocalDate.now();

        this.nickname = nickname;
        this.githubId = githubId;
        this.memberEmail = memberEmail;
        this.imageUrl = imageUrl;
        this.LastLoginDate = now;

    }
}
