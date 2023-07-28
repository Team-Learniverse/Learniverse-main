package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.MemberStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStatusRepository extends JpaRepository<MemberStatusEntity, Long> {
}
