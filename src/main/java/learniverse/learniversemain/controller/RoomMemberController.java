package learniverse.learniversemain.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.MemberDTO;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.service.RoomMainService;
import learniverse.learniversemain.service.RoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "roomMember", description = "스터디룸에 참여한 멤버와 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/room/member")
@Validated
public class RoomMemberController {
    private final RoomMemberService roomMemberService;

    @PostMapping("/apply")
    public ResponseEntity<Response> application(@Valid @RequestBody RoomMemberID roomMemberID){
        Response response = new Response();

        if(roomMemberService.apply(roomMemberID)){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("참여 신청 성공");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else throw new RuntimeException();
    }

    @PostMapping("/join")
    public ResponseEntity<Response>  join(@Valid @RequestBody RoomMemberID roomMemberID){
        Response response = new Response();

        if(roomMemberService.join(roomMemberID)){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("참여 성공");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else throw new RuntimeException();
    }

    @PostMapping("/reject")
    public ResponseEntity<Response> reject(@Valid @RequestBody RoomMemberID roomMemberID){
        Response response = new Response();
        roomMemberService.reject(roomMemberID);
        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("참여 거절 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Hidden
    @GetMapping("/isLeader")
    public ResponseEntity<Response> leaderMembers(@NotNull @RequestParam long roomId,
                                                  @NotNull @RequestParam long memberId){
        Response response = new Response();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("팀장 여부 출력 성공");
        Map<String,Boolean> data = new HashMap<>();
        data.put("isLeader", roomMemberService.getIsLeader(roomId, memberId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Response> members(@NotNull @RequestParam long roomId){
        Response response = new Response();

        List<MemberDTO> member_list = roomMemberService.getMembers(roomId, false);
        if(member_list == null) throw new RuntimeException();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("멤버 리스트 출력 성공");
        Map<String,List<MemberDTO>> data = new HashMap<>();
        data.put("members", member_list);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list/isWait")
    public ResponseEntity<Response> waitMembers(@NotNull @RequestParam long roomId){
        Response response = new Response();

        List<MemberDTO> member_list = roomMemberService.getMembers(roomId, true);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("대기 멤버 리스트 출력 성공");
        Map<String,List<MemberDTO>> data = new HashMap<>();
        data.put("members", member_list);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping("/quit")
//    public  ResponseEntity<Response> quit(@Valid @RequestBody RoomMemberID roomMemberID){
//        Response response = new Response();
//
//        roomMemberService.quitRoom(roomMemberID);
//
//        response.setStatus(Response.StatusEnum.OK);
//        response.setMessage("스터디룸 탈퇴 성공");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
