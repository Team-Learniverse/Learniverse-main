package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.entity.BoardEntity;
import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.repository.BoardRepository;
import learniverse.learniversemain.repository.CoreTimeRepository;
import learniverse.learniversemain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomMainService {
    private final ScheduleRepository scheduleRepository;
    private final CoreTimeRepository coreTimeRepository;
    private final BoardRepository boardRepository;

    public void createSchedule(ScheduleEntity scheduleEntity){
        scheduleEntity.setScheduleIsDone(0);
        scheduleRepository.save(scheduleEntity);
    }

    //bool로수정
    public void deleteSchedule(Long scheduleId){
        scheduleRepository.deleteById(scheduleId);
    }

    public List<ScheduleEntity> getSchedules(Long roomId){
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByRoomId(roomId);
        return scheduleEntities;
    }

    public void createCore(CoreTimeEntity coreTimeEntity){
        coreTimeRepository.save(coreTimeEntity);
    }

    //bool로수정
    public void deleteCore(Long coreId){
        coreTimeRepository.deleteById(coreId);

    }

    public List<CoreTimeEntity> getCores(Long roomId){
        List<CoreTimeEntity> coreTimeEntities = coreTimeRepository.findByRoomId(roomId);
        return coreTimeEntities;

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
