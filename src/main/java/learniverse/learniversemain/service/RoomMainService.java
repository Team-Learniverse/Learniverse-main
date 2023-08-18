package learniverse.learniversemain.service;

import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.repository.CoreTimeRepository;
import learniverse.learniversemain.repository.RoomRepository;
import learniverse.learniversemain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomMainService {
    private final ScheduleRepository scheduleRepository;
    private final CoreTimeRepository coreTimeRepository;

    public void createSchedule(ScheduleEntity scheduleEntity){
        scheduleEntity.setScheduleIsDone(0);
        scheduleRepository.save(scheduleEntity);
    }

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

    public void deleteCore(Long coreId){
        coreTimeRepository.deleteById(coreId);

    }

    public List<CoreTimeEntity> getCores(Long roomId){
        List<CoreTimeEntity> coreTimeEntities = coreTimeRepository.findByRoomId(roomId);
        return coreTimeEntities;

    }
}
