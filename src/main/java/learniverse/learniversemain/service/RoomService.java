package learniverse.learniversemain.service;

import learniverse.learniversemain.dto.RoomDTO;
import learniverse.learniversemain.entity.HashtagEntity;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.entity.RoomMemberEntity;
import learniverse.learniversemain.repository.HashtagRepository;
import learniverse.learniversemain.repository.RoomMemberRepository;
import learniverse.learniversemain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final HashtagRepository hashtagRepository;

    public void createRoom(RoomDTO roomDTO){
        //방 생성
        RoomEntity roomEntity = RoomEntity.toRoomEntity(roomDTO);
        roomRepository.save(roomEntity);
        //해시태그 처리
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

    public void application(RoomMemberID roomMemberID){
        RoomMemberEntity roomMemberEntity = new RoomMemberEntity(roomMemberID,0);
        roomMemberRepository.save(roomMemberEntity);
    }

    public void join(RoomMemberID roomMemberID){
        RoomMemberEntity roomMemberEntity = roomMemberRepository.findById(roomMemberID).get();
        roomMemberEntity.setIs_wait(0);
        roomMemberRepository.save(roomMemberEntity);
    }

    public void pin(RoomMemberID roomMemberID){
        RoomMemberEntity roomMemberEntity = roomMemberRepository.findById(roomMemberID).get();
        roomMemberEntity.changePin();
        roomMemberRepository.save(roomMemberEntity);
    }

}
