package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.RoomSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomSettingRepository extends JpaRepository<RoomSettingEntity, Integer> {
    List<RoomSettingEntity> findByType(String type);

    RoomSettingEntity findFirstByTypeOrderBySettingIdAsc(String type);
}
