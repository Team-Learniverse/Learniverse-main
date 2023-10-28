package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {
    public FcmTokenEntity findByMemberId(Long memberId);
}

