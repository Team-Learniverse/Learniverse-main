package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    public List<ScheduleEntity> findByRoomId(Long roomId);
}
