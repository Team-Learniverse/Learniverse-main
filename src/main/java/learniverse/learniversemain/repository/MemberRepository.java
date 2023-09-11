package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByMemberEmail(String email);

    Optional<MemberEntity> getByMemberEmail(String email);

}
