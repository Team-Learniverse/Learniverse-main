package learniverse.learniversemain.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.*;
import learniverse.learniversemain.entity.HashtagEntity;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "room", description = "스터디룸 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@Validated
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/setting/category")
    public ResponseEntity<Response> getCategories(){
        Response response = new Response();

        Map<String, List<RoomSettingDTO>> data = new HashMap<>();
        data.put("category",roomService.getSetting("category"));
        response.setData(data);
        response.setStatus(Response.StatusEnum.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/setting/language")
    public ResponseEntity<Response> getLanguages(){
        Response response = new Response();

        Map<String, List<RoomSettingDTO>> data = new HashMap<>();
        data.put("language",roomService.getSetting("language"));
        response.setData(data);
        response.setStatus(Response.StatusEnum.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Response> create(@Valid @RequestBody RoomDTO roomDTO) throws GeneralSecurityException, UnsupportedEncodingException {
        Response response = new Response();

        long roomId = roomService.createRoom(roomDTO);
        response.setStatus(Response.StatusEnum.CREATED);

        Map<String,String> data = new HashMap<>();
        data.put("encoded", roomService.getRoomEncoding(roomId));
        response.setData(data);
        response.setMessage("방 생성 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Hidden
    @PostMapping("/update")
    public void update(@RequestBody RoomDTO roomDTO){
        roomService.updateRoom(roomDTO);
    }

    @Hidden
    @GetMapping("/hashtags")
    public  ResponseEntity<Response> getHashtags(@NotNull @RequestParam Long roomId){
        Response response = new Response();

        List<HashtagEntity> hashtagEntities = roomService.getHashtags(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("해시태그 리스트 출력 성공");
        Map<String,List<HashtagEntity>> data = new HashMap<>();
        data.put("hashtags", hashtagEntities);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/hashtags/add")
    public ResponseEntity<Response> saveHashtags(@Valid @RequestBody HashtagDTO hashtagDTO){
        Response response = new Response();

        boolean existRoom = roomService.saveHashtags(hashtagDTO.getRoomId(), hashtagDTO.getHashtags());
        if(existRoom){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("해시태그 저장 성공");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else throw new RuntimeException();
    }

    @Hidden
    @DeleteMapping("/hashtags/delete")
    public ResponseEntity<Response> deleteHashtags(@Valid @RequestParam @NotEmpty Long[] hashtagIds){
        Response response = new Response();

        boolean result = roomService.deleteHashtags(hashtagIds);
        if(result){
            response.setStatus(Response.StatusEnum.OK);
            response.setMessage("해시태그 삭제 성공");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("해당 해시태그가 존재하지 않습니다. 해시태그 확인 필요");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @Hidden
    @GetMapping("/info")
    public ResponseEntity<Response> getRoomInfo(@NotNull @RequestParam long roomId){
        Response response = new Response();
        RoomEntity roomEntity = roomService.getRoomInfo(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("스터디룸 정보 출력 성공");
        Map<String, RoomEntity> data = new HashMap<>();
        data.put("info", roomEntity);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/info/{path}")
    public ResponseEntity<Response> getRoomInfo(@PathVariable String path,
                                                @NotNull @RequestParam long roomId){
        Response response = new Response();
        RoomEntity roomEntity = roomService.getRoomInfo(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("스터디룸 정보 출력 성공");
        Map<String, String> data = roomEntity.getPath(path);
        if(data == null){
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("잘못된 path 입력입니다. (roomName, workspace, roomIntro만 가능)");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/encode")
    public ResponseEntity<Response> getRoomEncoding(@NotNull @RequestParam long roomId) throws GeneralSecurityException, UnsupportedEncodingException {
        Response response = new Response();

        String encoded = roomService.getRoomEncoding(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("roomId 인코딩 성공");
        Map<String, String> data = new HashMap<>();
        data.put("encoded", encoded);
        response.setData(data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/decode")
    public ResponseEntity<Response> getRoomEncoding(@NotNull @RequestParam String encoded) throws GeneralSecurityException, UnsupportedEncodingException {
        Response response = new Response();

        long roomId = roomService.getRoomDecoding(encoded);
        if(roomId == 0){
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("해당 방 정보 없음. encoded 확인 필요");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("roomId 디코딩 성공");
        Map<String, Long> data = new HashMap<>();
        data.put("roomId", roomId);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/search")
    public ResponseEntity<Response> getSearch(@NotNull @RequestParam String str){
        Response response = new Response();
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("검색");
        response.setData(roomService.getSearch(str, 0));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Response> getRooms(){
        Response response = new Response();
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("전체 방 조회");
        Map<String, List<RoomCardDTO>> data = new HashMap<>();
        data.put("rooms", roomService.getRooms());
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
