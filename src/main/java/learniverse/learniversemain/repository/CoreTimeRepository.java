package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.CoreTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoreTimeRepository extends JpaRepository<CoreTimeEntity, Long> {
    public List<CoreTimeEntity> findByRoomId(Long roomId);
}
