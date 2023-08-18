package learniverse.learniversemain.service;


import jakarta.transaction.Transactional;
import learniverse.learniversemain.dto.MemberDTO;
import learniverse.learniversemain.dto.RoomDTO;
import learniverse.learniversemain.entity.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static learniverse.learniversemain.dto.MemberDTO.toMemberDTO;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;
    private final MemberStatusRepository memberStatusRepository;


    public void createRoom(RoomDTO roomDTO){
        //방 생성
        RoomEntity roomEntity = RoomEntity.toRoomEntity(roomDTO);
        roomRepository.save(roomEntity);
        //해시태그 처리
        saveHashtag(roomEntity.getRoomId(), roomDTO.getRoomHashtags());
        //방장 처리
        RoomMemberEntity roomMemberEntity
                = new RoomMemberEntity(roomEntity.getRoomId(), roomDTO.getMemberId(), 1);
        roomMemberRepository.save(roomMemberEntity);
    }

    @Transactional
    public void updateRoom(RoomDTO roomDTO){
        RoomEntity oldRoom = roomRepository.findById(roomDTO.getRoomId())
                .orElseThrow(()-> new IllegalArgumentException("해당 방이 없습니다."));
        RoomEntity newRoom = RoomDTO.toRoomEntity(roomDTO);
        oldRoom.update(newRoom);
    }

    public RoomEntity getRoomInfo(Long roomId){
        return roomRepository.findById(roomId).orElseThrow();
    }

    public void saveHashtag(long roomId, String[] hashtags){
        for(String hashtag : hashtags){
            HashtagEntity hashtagEntity = new HashtagEntity(roomId, hashtag);
            hashtagRepository.save(hashtagEntity);
        }
    }

    public void saveHashtags(List<HashtagEntity> hashtagEntities){
        for (HashtagEntity hashtagEntity : hashtagEntities){
        saveHashtag(roomEntity.getRoom_id(), roomDTO.getRoom_hashtags());
        //방장 처리
        RoomMemberEntity roomMemberEntity
                = new RoomMemberEntity(roomEntity.getRoom_id(), roomDTO.getMember_id(), 1);
        roomMemberRepository.save(roomMemberEntity);
    }

    public void saveHashtag(long room_id, String[] hashtags){
        for(String hashtag : hashtags){
            HashtagEntity hashtagEntity = new HashtagEntity(room_id, hashtag);
            hashtagRepository.save(hashtagEntity);
        }
    }


    public void deleteHashtags(List<HashtagEntity> hashtagEntities) {
        for(HashtagEntity hashtagEntity : hashtagEntities){
            hashtagRepository.deleteById(hashtagEntity.getHashtagId());
        }
    }

    public void deleteHashtag(long hashtagId){
        hashtagRepository.deleteById(hashtagId);
    }

    public void application(RoomMemberID roomMemberID){
        RoomMemberEntity roomMemberEntity = new RoomMemberEntity(roomMemberID,0);
        roomMemberRepository.save(roomMemberEntity);
    }

    public void join(RoomMemberID roomMemberID){
        RoomMemberEntity roomMemberEntity = roomMemberRepository.findById(roomMemberID).get();
        roomMemberEntity.setIsWait(0);
        roomMemberEntity.setIs_wait(0);
        roomMemberRepository.save(roomMemberEntity);
    }

    public void pin(RoomMemberID roomMemberID){
        RoomMemberEntity roomMemberEntity = roomMemberRepository.findById(roomMemberID).get();
        roomMemberEntity.changePin();
        roomMemberRepository.save(roomMemberEntity);
    }

    public List<MemberDTO> getMembers(long roomId){
        List<MemberDTO> memberDTOS = new ArrayList<>();
        List<RoomMemberEntity> roomMemberEntities = roomMemberRepository.findByRoomId(roomId);
        for(RoomMemberEntity roomMemberEntity : roomMemberEntities){
            long memberId = roomMemberEntity.getMemberId();
            Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
            Optional<MemberStatusEntity> memberStatusEntity = memberStatusRepository.findById(memberId);
            MemberDTO memberDTO = toMemberDTO(memberEntity.get(), memberStatusEntity.get());
            memberDTOS.add(memberDTO);
        }
        return memberDTOS;
    }
}
