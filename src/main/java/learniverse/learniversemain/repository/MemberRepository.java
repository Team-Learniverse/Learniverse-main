package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByMemberId(long memberId);
}
