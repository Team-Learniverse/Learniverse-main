package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByMemberId(long memberId);
    //boolean existsByMemberEmail(String email);
    boolean existsByGithubId(String githubId);

    //Optional<MemberEntity> getByMemberEmail(String email);
    Optional<MemberEntity> getByGithubId(String githubId);

}
