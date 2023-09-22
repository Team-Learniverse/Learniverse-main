package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.dto.CoreTimeDTO;
import learniverse.learniversemain.dto.ScheduleDTO;
import learniverse.learniversemain.dto.WorkspaceDTO;
import learniverse.learniversemain.entity.BoardEntity;
import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.repository.BoardRepository;
import learniverse.learniversemain.repository.CoreTimeRepository;
import learniverse.learniversemain.repository.RoomRepository;
import learniverse.learniversemain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomMainService {
    private final ScheduleRepository scheduleRepository;
    private final CoreTimeRepository coreTimeRepository;
    private final RoomRepository roomRepository;
    private final BoardRepository boardRepository;



    public boolean createSchedule(ScheduleDTO scheduleDTO){
        //roomId 확인
        boolean existRoom = roomRepository.existsByRoomId(scheduleDTO.getRoomId());
        if(!existRoom) throw new CannotFindRoomException();

        ScheduleEntity scheduleEntity = new ScheduleEntity(scheduleDTO);
        scheduleRepository.save(scheduleEntity);
        return true;
    }

    public boolean deleteSchedule(Long scheduleId){
        ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new CustomBadRequestException("일정 삭제 실패, scheduleId 확인 필요"));
        scheduleRepository.delete(scheduleEntity);
        return true;
    }

    public List<ScheduleEntity> getSchedules(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByRoomId(roomId);
        return scheduleEntities;
    }

    public boolean createCore(CoreTimeDTO coreTimeDTO){
        CoreTimeEntity coreTimeEntity = new CoreTimeEntity(coreTimeDTO);
        //방체크
        boolean existRoom = roomRepository.existsByRoomId(coreTimeDTO.getRoomId());
        if(!existRoom) throw new CannotFindRoomException();

        //중복체크
        CoreTimeEntity coreTimeEntities = coreTimeRepository
                .findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(coreTimeDTO.getRoomId(), coreTimeDTO.getCoreStartTime(), coreTimeDTO.getCoreStartTime());
        if(coreTimeEntities != null) throw new CustomBadRequestException("해당 시간과 겹치는 코어타임 시간이 이미 존재합니다.");

        //중복체크 end 기준으로도 진행
        coreTimeEntities = coreTimeRepository
                .findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(coreTimeDTO.getRoomId(), coreTimeEntity.getCoreEndTime(), coreTimeEntity.getCoreEndTime());
        if(coreTimeEntities != null) throw new CustomBadRequestException("해당 시간과 겹치는 코어타임 시간이 이미 존재합니다.");

//        if(coreTimeDTO.getCoreEndDate().isBefore(coreTimeDTO.getCoreStartDate()))
//            throw new CustomBadRequestException("coreEndTime은 coreStartTime 이후 datetime이어야 합니다.");
//
//        if(coreTimeDTO.getCoreEndDate().isEqual(coreTimeDTO.getCoreStartDate()))
//            throw new CustomBadRequestException("coreEndTime은 coreStartTime 이후 datetime이어야 합니다.");


        coreTimeRepository.save(coreTimeEntity);
        return true;
    }

    public boolean deleteCore(Long coreId){
        CoreTimeEntity coreTimeEntity = coreTimeRepository.findById(coreId)
                .orElseThrow(()-> new CustomBadRequestException("코어타임 삭제 실패, coreTimeId 확인 필요"));
        coreTimeRepository.delete(coreTimeEntity);
        return true;

    }

    public List<CoreTimeEntity> getCores(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        List<CoreTimeEntity> coreTimeEntities = coreTimeRepository
                .findByRoomIdAndCoreEndTimeGreaterThanOrderByCoreStartTime(roomId, LocalDateTime.now());
        return coreTimeEntities;
    }

    public boolean isCore(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        CoreTimeEntity coreTimeEntity = coreTimeRepository.findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(roomId, LocalDateTime.now(), LocalDateTime.now());
        if(coreTimeEntity == null) return false;
        else return true;
    }

    public LocalDateTime getEndTime(Long coreTimeId){
        CoreTimeEntity coreTimeEntity = coreTimeRepository.findById(coreTimeId)
                .orElseThrow(() -> new CustomBadRequestException("해당 coreTimeId와 매칭되는 코어타임이 존재하지 않습니다."));

        return coreTimeEntity.getCoreEndTime();
    }

    public long getNowId(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        CoreTimeEntity coreTimeEntity = coreTimeRepository.findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(roomId, LocalDateTime.now(), LocalDateTime.now());
        if(coreTimeEntity == null) throw new CustomBadRequestException("현재 코어타임이 아닙니다. 코어타임인 경우 해당 API를 호출해주세요.");
        else return coreTimeEntity.getCoreTimeId();
    }

    public void updateWorkspace(WorkspaceDTO workspaceDTO){
        RoomEntity roomEntity = roomRepository.findById(workspaceDTO.getRoomId())
                .orElseThrow(()-> new CannotFindRoomException());

        if(workspaceDTO.getRoomGitOrg() != null) roomEntity.setRoomGitOrg(workspaceDTO.getRoomGitOrg());
        if(workspaceDTO.getRoomFigma() != null) roomEntity.setRoomFigma(workspaceDTO.getRoomFigma());
        if(workspaceDTO.getRoomNotion() != null) roomEntity.setRoomNotion(workspaceDTO.getRoomNotion());
        if(workspaceDTO.getRoomGoogleDrive() != null) roomEntity.setRoomGoogleDrive(workspaceDTO.getRoomGoogleDrive());

        roomRepository.save(roomEntity);
    }

    public void createBoard(BoardEntity boardEntity){
        boardRepository.save(boardEntity);
    }

    public void deleteBoardPost(Long boardPostId){
        boardRepository.deleteById(boardPostId);
    }

    @Transactional
    public void updateBoard(BoardEntity boardEntity){
        BoardEntity exitedBoard = boardRepository.findById(boardEntity.getBoardId())
                .orElseThrow(()-> new IllegalArgumentException("해당 방이 없습니다."));
        exitedBoard.update(boardEntity);
    }

    public List<BoardEntity> getBoards(Long roomId){
        List<BoardEntity> boardsEntities = boardRepository.findByRoomId(roomId);
        return boardsEntities;
    }

    public Optional<BoardEntity> getBoardById(Long boardId){
        Optional<BoardEntity> boardsEntity = boardRepository.findById(boardId);
        return boardsEntity;
    }
}
