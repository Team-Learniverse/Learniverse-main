package learniverse.learniversemain.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.controller.Exception.CustomException;
import learniverse.learniversemain.dto.MemberDTO;
import learniverse.learniversemain.dto.RoomDTO;
import learniverse.learniversemain.dto.RoomSettingDTO;
import learniverse.learniversemain.entity.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static learniverse.learniversemain.dto.MemberDTO.toMemberDTO;

@Service
@RequiredArgsConstructor
public class RoomService {
    @PersistenceContext
    private EntityManager entityManager;

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final RoomSettingRepository roomSettingRepository;

    public List<RoomSettingDTO> getSetting(String type){
        List<RoomSettingEntity> roomSettingEntities = roomSettingRepository.findByType(type);
        List<RoomSettingDTO> roomSettingDTOS = new ArrayList<>();
        for(RoomSettingEntity roomSettingEntity : roomSettingEntities){
            RoomSettingDTO roomSettingDTO = new RoomSettingDTO(roomSettingEntity);
            roomSettingDTOS.add(roomSettingDTO);
        }
        return roomSettingDTOS;
    }

    @Transactional
    public long createRoom(RoomDTO roomDTO){
        //방 생성
        RoomEntity roomEntity = RoomEntity.toRoomEntity(roomDTO);
        roomRepository.save(roomEntity);
        //해시태그 처리
        saveHashtag(roomEntity.getRoomId(), roomDTO.getRoomHashtags());
        //방장 처리
        RoomMemberEntity roomMemberEntity
                = new RoomMemberEntity(roomEntity.getRoomId(), roomDTO.getMemberId(), true);
        roomMemberRepository.save(roomMemberEntity);
        return roomEntity.getRoomId();
    }

    @Transactional
    public void updateRoom(RoomDTO roomDTO){
        RoomEntity oldRoom = roomRepository.findById(roomDTO.getRoomId())
                .orElseThrow(()-> new CannotFindRoomException());
        RoomEntity newRoom = RoomDTO.toRoomEntity(roomDTO);
        oldRoom.update(newRoom);
    }

    public RoomEntity getRoomInfo(Long roomId){
        RoomEntity findRoom = roomRepository.findById(roomId)
                .orElseThrow(()-> new CannotFindRoomException());
        return roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException());
    }

    public boolean saveHashtags(long roomId, List<String> hashtags){
        //roomId 확인
        boolean existRoom = hashtagRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        for (String hashtag : hashtags){
            HashtagEntity hashtagEntity = new HashtagEntity(roomId, hashtag);
            hashtagRepository.save(hashtagEntity);
        }
        return true;
    }

    public void saveHashtag(long room_id, String[] hashtags){
        for(String hashtag : hashtags){
            HashtagEntity hashtagEntity = new HashtagEntity(room_id, hashtag);
            hashtagRepository.save(hashtagEntity);
        }
    }

    @Transactional
    public boolean deleteHashtags(Long[] hashtagIds) {
        List<HashtagEntity> hashtagEntityList = new ArrayList<>();
        //해시태그 존재여부 확인
        for (Long hashtagId : hashtagIds){
            Optional<HashtagEntity> hashtagEntity = hashtagRepository.findById(hashtagId);
            if(hashtagEntity.isEmpty()) {return false;}
            hashtagEntityList.add(hashtagEntity.get());
        }
        //삭제
        for (HashtagEntity hashtagEntity : hashtagEntityList){
            hashtagRepository.delete(hashtagEntity);
        }
        return true;
    }

    @Transactional
    public List<HashtagEntity> getHashtags(long roomId){
        return hashtagRepository.findByRoomId(roomId);
    }

    public void deleteHashtag(long hashtagId){
        hashtagRepository.deleteById(hashtagId);
    }

    public String getRoomEncoding (long roomId) throws NoSuchAlgorithmException,
            GeneralSecurityException, UnsupportedEncodingException {
        //roomId 확인
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        AES256Util aes = new AES256Util();
        return aes.encrypt(String.valueOf(roomId));
    }
    
    public long getRoomDecoding (String encoded) throws UnsupportedEncodingException, GeneralSecurityException {
        AES256Util aes = new AES256Util();
        String roomStr = aes.decrypt(encoded);
        long roomId = Integer.parseInt(roomStr);
        //roomId 확인
        boolean existRoom = hashtagRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();
        return roomId;
    }

    public Page<RoomEntity> getSearch (String str, int page){
        //List<RoomDTO> result = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page,10);
        Page<RoomEntity> roomList1 = roomRepository.findByRoomNameContainingOrRoomIntroContaining(str, str, pageRequest);
        //List<HashtagEntity> roomList3 = hashtagRepository.findByHashtagContaining(str);
        return roomList1;
    }
}
