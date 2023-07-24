package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.RoomMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMemberRepository extends JpaRepository<RoomMemberEntity, Long> {
}
