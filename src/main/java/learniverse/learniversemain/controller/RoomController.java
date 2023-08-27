package learniverse.learniversemain.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.HashtagDTO;
import learniverse.learniversemain.dto.MemberDTO;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }

    @PostMapping("/application")
    public ResponseEntity<Response> application(@Valid @RequestBody RoomMemberID roomMemberID){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        if(roomService.application(roomMemberID)){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("가입 신청 성공");
            return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
        }
        else throw new RuntimeException();
    }

    @PostMapping("/join")
    public ResponseEntity<Response>  join(@Valid @RequestBody RoomMemberID roomMemberID){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));


        roomService.join(roomMemberID);

        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("가입 성공");
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public void update(@RequestBody RoomDTO roomDTO){
        roomService.updateRoom(roomDTO);
    }

    @GetMapping("/hashtags")
    public  ResponseEntity<Response> getHashtags(@NotNull @RequestParam Long roomId){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        List<HashtagEntity> hashtagEntities = roomService.getHashtags(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("해시태그 리스트 출력 성공");
        Map<String,List<HashtagEntity>> data = new HashMap<>();
        data.put("hashtags", hashtagEntities);
        response.setData(data);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/hashtags/add")
    public ResponseEntity<Response> saveHashtags(@Valid @RequestBody HashtagDTO hashtagDTO){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        boolean existRoom = roomService.saveHashtags(hashtagDTO.getRoomId(), hashtagDTO.getHashtags());
        if(existRoom){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("해시태그 저장 성공");

            return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
        }
        else throw new RuntimeException();
    }

    @DeleteMapping("/hashtags/delete")
    public ResponseEntity<Response> deleteHashtags(@Valid @RequestParam @NotEmpty Long[] hashtagIds){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        boolean result = roomService.deleteHashtags(hashtagIds);
        if(result){
            response.setStatus(Response.StatusEnum.OK);
            response.setMessage("해시태그 삭제 성공");

            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
        else{
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("해당 해시태그가 존재하지 않습니다. 해시태그 확인 필요");

            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/pin")
    public void pin(@RequestBody RoomMemberID roomMemberID){
        //null 처리
        roomService.pin(roomMemberID);
    }

    @GetMapping("/members")
    public ResponseEntity<Response> members(@NotNull @RequestParam long roomId){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        List<MemberDTO> member_list = roomService.getMembers(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("멤버 리스트 출력 성공");
        Map<String,List<MemberDTO>> data = new HashMap<>();
        data.put("members", member_list);
        response.setData(data);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/info")
    public RoomEntity getRoomInfo(@RequestParam long roomId){
        return roomService.getRoomInfo(roomId);

        //return new ResponseEntity(roomService.getRoomInfo(roomId), HttpStatus.OK);
    }

    @GetMapping("/encode")
    public ResponseEntity<Response> getRoomEncoding(@NotNull @RequestParam long roomId) throws GeneralSecurityException, UnsupportedEncodingException {
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        String encoded = roomService.getRoomEncoding(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("roomId 인코딩 성공");
        Map<String, String> data = new HashMap<>();
        data.put("encoded", encoded);
        response.setData(data);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/decode")
    public ResponseEntity<Response> getRoomEncoding(@NotNull @RequestParam String encoded) throws GeneralSecurityException, UnsupportedEncodingException {
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        long roomId = roomService.getRoomDecoding(encoded);
        if(roomId == 0){
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("해당 방 정보 없음. encoded 확인 필요");

            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("roomId 디코딩 성공");
        Map<String, Long> data = new HashMap<>();
        data.put("roomId", roomId);
        response.setData(data);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


}
