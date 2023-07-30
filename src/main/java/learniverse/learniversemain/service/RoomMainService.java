package learniverse.learniversemain.service;

import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.repository.CoreTimeRepository;
import learniverse.learniversemain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void getSchedules(){
    }

    public void createCore(CoreTimeEntity coreTimeEntity){
        coreTimeRepository.save(coreTimeEntity);

    }

    public void dropCore(Long coreId){
        coreTimeRepository.deleteById(coreId);

    }

    public void getCores(){

    }
}
