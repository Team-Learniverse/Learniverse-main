package learniverse.learniversemain.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.service.RoomMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@Validated
public class RoomMainController {
    private final RoomMainService roomMainService;
    @PostMapping("/schedule/create")
    public void createSchedule(@Valid @RequestBody ScheduleEntity scheduleEntity){
        roomMainService.createSchedule(scheduleEntity);
    }

    @DeleteMapping("/schedule/delete")
    public void deleteSchedule(@NotNull @RequestParam long scheduleId){
        roomMainService.deleteSchedule(scheduleId);
    }

    @GetMapping("/schedules")
    public List<ScheduleEntity> getSchedules(@RequestParam Long roomId){
        return roomMainService.getSchedules(roomId);
    }

    @PostMapping("/core/create")
    public void createCoreTime(@RequestBody CoreTimeEntity coreTimeEntity){
        roomMainService.createCore(coreTimeEntity);
    }

    @DeleteMapping("/core/delete")
    public void deleteCoreTime(@NotNull @RequestParam long coreTimeId){
        roomMainService.deleteCore(coreTimeId);
    }

    @GetMapping("/cores")
    public List<CoreTimeEntity> getCores(@RequestParam Long roomId){
        return roomMainService.getCores(roomId);
    }


}
