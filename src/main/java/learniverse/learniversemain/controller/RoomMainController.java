package learniverse.learniversemain.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.CoreTimeDTO;
import learniverse.learniversemain.dto.ScheduleDTO;
import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.service.RoomMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@Validated
public class RoomMainController {
    private final RoomMainService roomMainService;
    @PostMapping("/schedule/create")
    public ResponseEntity<Response> createSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO){
        Response response = new Response();

        if(roomMainService.createSchedule(scheduleDTO)){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("일정 생성 성공");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else throw new RuntimeException();
    }

    @DeleteMapping("/schedule/delete")
    public ResponseEntity<Response> deleteSchedule(@NotNull @RequestParam long scheduleId){
        Response response = new Response();

        if(roomMainService.deleteSchedule(scheduleId)){
            response.setStatus(Response.StatusEnum.OK);
            response.setMessage("일정 삭제 성공");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else throw new RuntimeException();
    }

    @GetMapping("/schedules")
    public ResponseEntity<Response> getSchedules(@RequestParam Long roomId){
        Response response = new Response();

        List<ScheduleEntity> scheduleEntities = roomMainService.getSchedules(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("일정 리스트 출력 성공");
        Map<String, List<ScheduleEntity>> data = new HashMap<>();
        data.put("schedules", scheduleEntities);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/core/create")
    public ResponseEntity<Response>  createCoreTime(@Valid @RequestBody CoreTimeDTO coreTimeDTO){
        Response response = new Response();

        if(roomMainService.createCore(coreTimeDTO)){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("코어타임 생성 성공");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else throw new RuntimeException();
    }

    @DeleteMapping("/core/delete")
    public ResponseEntity<Response>  deleteCoreTime(@NotNull @RequestParam long coreTimeId){
        Response response = new Response();
        if(roomMainService.deleteCore(coreTimeId)){
            response.setStatus(Response.StatusEnum.OK);
            response.setMessage("코어타임 삭제 성공");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else throw new RuntimeException();
    }

    @GetMapping("/cores")
    public ResponseEntity<Response> getCores(@NotNull @RequestParam Long roomId){
        Response response = new Response();

        List<CoreTimeEntity> coreTimeEntities = roomMainService.getCores(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("코어타임 리스트 출력 성공");
        Map<String, List<CoreTimeEntity>> data = new HashMap<>();
        data.put("cores", coreTimeEntities);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/core/isCore")
    public ResponseEntity<Response> isCore(@NotNull @RequestParam Long roomId){
        Response response = new Response();

        List<CoreTimeEntity> coreTimeEntities = roomMainService.getCores(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("코어타임 여부 출력 성공");
        Map<String, Boolean> data = new HashMap<>();
        data.put("isCore", roomMainService.isCore(roomId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
