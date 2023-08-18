package learniverse.learniversemain.controller;

import jakarta.validation.Valid;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.RoomDTO;
import learniverse.learniversemain.entity.HashtagEntity;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@Validated
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<Response> create(@Valid @RequestBody RoomDTO roomDTO){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        roomService.createRoom(roomDTO);

        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("방 생성 성공");
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    public void create(@RequestBody RoomDTO roomDTO){
        //null 처리
        roomService.createRoom(roomDTO);
    }

    @PostMapping("/application")
    public void application(@RequestBody RoomMemberID roomMemberID){
        //null 처리
        roomService.application(roomMemberID);
    }

    @PostMapping("/join")
    public void join(@RequestBody RoomMemberID roomMemberID){
        //null 처리
        roomService.join(roomMemberID);
    }

    @PostMapping("/update")
    public void update(@RequestBody RoomDTO roomDTO){
        roomService.updateRoom(roomDTO);
    }

    @PostMapping("/hashtags/add")
    public void saveHashtags(@RequestBody List<HashtagEntity> hashtagEntities){
        roomService.saveHashtags(hashtagEntities);
    }

    @PostMapping("/hashtags/delete")
    public void deleteHashtags(@RequestBody List<HashtagEntity> hashtagEntities){
        roomService.deleteHashtags(hashtagEntities);
    }

    @PostMapping("/pin")
    public void pin(@RequestBody RoomMemberID roomMemberID){
        //null 처리
        roomService.pin(roomMemberID);
    }

    @GetMapping("/members")
    public ResponseEntity members(@RequestParam long roomId){

        return new ResponseEntity(roomService.getMembers(roomId), HttpStatus.OK);

    }

    @GetMapping("/info")
    public RoomEntity getRoomInfo(@RequestParam long roomId){
        return roomService.getRoomInfo(roomId);

        //return new ResponseEntity(roomService.getRoomInfo(roomId), HttpStatus.OK);
    }


}
