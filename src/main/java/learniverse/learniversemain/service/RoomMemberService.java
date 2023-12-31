package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.dto.MemberDTO;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.entity.MemberStatusEntity;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.entity.RoomMemberEntity;
import learniverse.learniversemain.entity.mongoDB.JoinsEntity;
import learniverse.learniversemain.entity.mongoDB.RoomsEntity;
import learniverse.learniversemain.repository.MemberRepository;
import learniverse.learniversemain.repository.MemberStatusRepository;
import learniverse.learniversemain.repository.RoomMemberRepository;
import learniverse.learniversemain.repository.RoomRepository;
import learniverse.learniversemain.repository.mongoDB.JoinsMongoDBRepository;
import learniverse.learniversemain.repository.mongoDB.RoomsMongoDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static learniverse.learniversemain.dto.MemberDTO.toMemberDTO;

@Service
@RequiredArgsConstructor
public class RoomMemberService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberRepository memberRepository;
    //private final MemberStatusRepository memberStatusRepository;
    private final JoinsMongoDBRepository joinsMongoDBRepository;
    private final RoomsMongoDBRepository roomsMongoDBRepository;
    private final RoomService roomService;

    public boolean apply(RoomMemberID roomMemberID){
        //memberId 확인
        boolean existMember = memberRepository.existsByMemberId(roomMemberID.getMemberId());
        if(!existMember) throw new CustomBadRequestException("해당 멤버Id와 매칭되는 멤버를 찾을 수 없습니다.");

        //roomId 확인
        boolean existRoom = roomRepository.existsByRoomId(roomMemberID.getRoomId());
        if(!existRoom) throw new CannotFindRoomException();

        //이미 참여했거나 참여 신청한 경우
        boolean existRoomMember = roomMemberRepository.existsById(roomMemberID);
        if(existRoomMember) throw new CustomBadRequestException("이미 참여 신청했거나 참여한 상태입니다.");



        RoomMemberEntity roomMemberEntity = new RoomMemberEntity(roomMemberID,false);
        roomMemberRepository.save(roomMemberEntity);
        return true;
    }

    @Transactional
    public boolean join(RoomMemberID roomMemberID){
        long roomId = roomMemberID.getRoomId();
        //roomId 확인
        Optional<RoomEntity> roomEntity = roomRepository.findById(roomId);
        if(roomEntity.isEmpty()) throw new CannotFindRoomException();
        //대기여부
        Optional<RoomMemberEntity> findRoomMemberEntity = roomMemberRepository.findById(roomMemberID);
        if(findRoomMemberEntity.isEmpty()) throw new CustomBadRequestException("해당 방 대기 리스트에 입력한 memberId가 존재하지 않습니다.");
        //count
        if(roomEntity.get().getRoomCount()==roomEntity.get().getRoomLimit())
            throw new CustomBadRequestException("제한 인원이 초과되는 요청입니다.");
        RoomMemberEntity roomMemberEntity = findRoomMemberEntity.get();
        if(roomMemberEntity.isReject()) throw new CustomBadRequestException("참여 요청이 거절된 멤버입니다.");
        if(!roomMemberEntity.isWait()) throw new CustomBadRequestException("이미 참여하고있는 팀원입니다.");

        roomMemberEntity.setWait(false);
        roomMemberEntity.setJoinTime(LocalDateTime.now());
        roomMemberRepository.save(roomMemberEntity);

        roomEntity.get().setRoomCount(roomEntity.get().getRoomCount()+1);
        //방이 다차면
        if(roomEntity.get().getRoomCount() == roomEntity.get().getRoomLimit()) {
            fullRoom(roomId);
            roomEntity.get().setFull(true);}
        roomRepository.save(roomEntity.get());

        joinsMongoDBRepository.save(new JoinsEntity(roomMemberEntity.getMemberId(), roomMemberEntity.getRoomId(), false));
        return true;
    }

    public void fullRoom(long roomId){
        List<RoomsEntity> roomsEntity = roomsMongoDBRepository.findByRoomId(roomId);
        roomsEntity.get(0).setFull(true);
        roomsMongoDBRepository.save(roomsEntity.get(0));
    }

    public void reject(RoomMemberID roomMemberID){
        RoomMemberEntity roomMemberEntity = roomMemberRepository.findById(roomMemberID)
                .orElseThrow(() -> new CustomBadRequestException("입력한 roomId, memberId 관련 정보를 찾을 수 없습니다."));

        if(roomMemberEntity.isReject()) throw new CustomBadRequestException("이미 거절된 멤버입니다.");
        if(roomMemberEntity.isWait()){
            roomMemberEntity.setReject(true);
            roomMemberRepository.save(roomMemberEntity);
        }
        else throw new CustomBadRequestException("대기하고 있지 않은 멤버에 대한 요청입니다.");
    }

    public boolean getIsLeader(long roomId, long memberId){
        RoomMemberID roomMemberID = new RoomMemberID(roomId, memberId);
        RoomMemberEntity roomMemberEntity = roomMemberRepository.findById(roomMemberID)
                .orElseThrow(() -> new CustomBadRequestException("입력한 roomId, memberId 관련 정보를 찾을 수 없습니다."));

        return roomMemberEntity.isLeader();
    }

    public List<MemberDTO> getMembers(long roomId, boolean isWait){
        List<MemberDTO> memberDTOS = new ArrayList<>();
        List<RoomMemberEntity> roomMemberEntities = roomMemberRepository.findByRoomId(roomId);
        if (roomMemberEntities.isEmpty()) throw new CannotFindRoomException();

        if(isWait){
            roomMemberEntities.removeIf(entity -> entity.isLeader() == true);
        }else{
            roomMemberEntities.removeIf(entity -> entity.isWait() == true);
        }


        for(RoomMemberEntity roomMemberEntity : roomMemberEntities){
            long memberId = roomMemberEntity.getMemberId();
            MemberEntity memberEntity = memberRepository.findById(memberId)
                    .orElseThrow(()->new CustomBadRequestException("존재하지 않는 memberId"));
            //MemberStatusEntity memberStatusEntity = memberStatusRepository.findById(memberId)
            //        .orElseThrow(()->new CustomBadRequestException("memberId \'"+memberId+"\'의 memberStauts가 존재하지 않습니다"));
            MemberDTO memberDTO = toMemberDTO(memberEntity, roomMemberEntity);
            memberDTOS.add(memberDTO);
        }
        return memberDTOS;
    }

    @Transactional
    public void quitRoom(RoomMemberID roomMemberID){
        Optional<RoomMemberEntity> roomMemberEntity = roomMemberRepository.findById(roomMemberID);
        if(roomMemberEntity.isEmpty())
            throw new CustomBadRequestException("해당 roomId, memberId 조합에 대한 결과가 존재하지 않습니다.");
        //리더인지 확인
        if (roomMemberEntity.get().isLeader())
            throw new CustomBadRequestException("리더는 탈퇴할 수 없습니다.");
        //대기자인지 확인
        if (roomMemberEntity.get().isWait())
            throw new CustomBadRequestException("대기 중인 팀원입니다.");
        roomMemberRepository.delete(roomMemberEntity.get());
    }
}
