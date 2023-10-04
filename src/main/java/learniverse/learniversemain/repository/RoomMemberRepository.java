package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.RoomMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMemberEntity, RoomMemberID> {
    List<RoomMemberEntity> findByRoomId(long room_id);
    List<RoomMemberEntity> findByMemberId(long member_id);
    List<RoomMemberEntity> findByRoomIdAndIsWait(long roomId, boolean isWait);
    List<RoomMemberEntity> findByMemberIdOrderByRoomIdDesc(long memberId);
    List<RoomMemberEntity> findByMemberIdOrderByJoinTimeDesc(long memberId);
    List<RoomMemberEntity> findByMemberIdAndIsWaitAndIsPinOrderByJoinTimeDesc(long memberId, boolean b, boolean b1);
    List<RoomMemberEntity> findByMemberIdAndIsWaitAndIsPinOrderByPinTimeDesc(long memberId, boolean b, boolean b1);

    long countByMemberIdAndIsPin(Long memberId, boolean b);
}
