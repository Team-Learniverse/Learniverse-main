package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.controller.Exception.CustomException;
import learniverse.learniversemain.dto.MemberDTO;
import learniverse.learniversemain.dto.RoomDTO;
import learniverse.learniversemain.entity.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
        //meber 존재 유무 체크
        RoomMemberEntity roomMemberEntity
                = new RoomMemberEntity(roomEntity.getRoomId(), roomDTO.getMemberId(), true);
        roomMemberRepository.save(roomMemberEntity);
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

    public boolean application(RoomMemberID roomMemberID){
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

    public boolean join(RoomMemberID roomMemberID){
        //roomId 확인
        boolean existRoom = roomRepository.existsByRoomId(roomMemberID.getRoomId());
        if(!existRoom) throw new CannotFindRoomException();

        Optional<RoomMemberEntity> findRoomMemberEntity = roomMemberRepository.findById(roomMemberID);
        if(findRoomMemberEntity.isEmpty()) throw new CustomBadRequestException("해당 방 대기 리스트에 입력한 memberId가 존재하지 않습니다.");

        RoomMemberEntity roomMemberEntity = findRoomMemberEntity.get();
        if(roomMemberEntity.isWait()==false) throw new CustomBadRequestException("이미 참여하고있는 팀원입니다.");

        roomMemberEntity.setWait(false);
        roomMemberRepository.save(roomMemberEntity);
        return true;
    }

    public void pin(RoomMemberID roomMemberID){
        RoomMemberEntity roomMemberEntity = roomMemberRepository.findById(roomMemberID).get();
        roomMemberEntity.changePin();
        roomMemberRepository.save(roomMemberEntity);
    }

    public List<MemberDTO> getMembers(long roomId, boolean isWait){
        List<MemberDTO> memberDTOS = new ArrayList<>();
        List<RoomMemberEntity> roomMemberEntities = roomMemberRepository.findByRoomId(roomId);
        if (roomMemberEntities.isEmpty()) throw new CannotFindRoomException();

        roomMemberEntities.removeIf(entity -> entity.isWait() != isWait);

        for(RoomMemberEntity roomMemberEntity : roomMemberEntities){
            long memberId = roomMemberEntity.getMemberId();
            MemberEntity memberEntity = memberRepository.findById(memberId)
                    .orElseThrow(()->new CustomBadRequestException("존재하지 않는 memberId"));
            MemberStatusEntity memberStatusEntity = memberStatusRepository.findById(memberId)
                    .orElseThrow(()->new CustomBadRequestException("memberId \'"+memberId+"\'의 memberStauts가 존재하지 않습니다"));
            MemberDTO memberDTO = toMemberDTO(memberEntity, memberStatusEntity);
            memberDTOS.add(memberDTO);
        }
        return memberDTOS;
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
}
