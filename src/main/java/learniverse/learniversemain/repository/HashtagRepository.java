package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {
    boolean existsByRoomId(long roomId);
    List<HashtagEntity> findByRoomId(Long roomId);
}
