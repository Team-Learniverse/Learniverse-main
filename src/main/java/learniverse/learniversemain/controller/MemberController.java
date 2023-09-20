package learniverse.learniversemain.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.MoonDTO;
import learniverse.learniversemain.dto.ResMoonDTO;
import learniverse.learniversemain.dto.RoomCardDTO;
import learniverse.learniversemain.entity.HashtagEntity;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.RoomMemberEntity;
import learniverse.learniversemain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "member", description = "member 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Validated
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/moon/add")
    public ResponseEntity<Response> saveMoon(@Valid @RequestBody MoonDTO moonDTO){
        Response response = new Response();

        memberService.saveMoon(moonDTO);

        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("달 추가 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @Hidden
    @PostMapping("/moon/minus")
    public ResponseEntity<Response> deleteMoon(@Valid @RequestBody MoonDTO moonDTO){
        Response response = new Response();

        memberService.deleteMoon(moonDTO);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 삭제 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/moon/list")
    public ResponseEntity<Response> getMoon(@NotNull long memberId){
        Response response = new Response();

        Map<String,List<ResMoonDTO>> data = new HashMap<>();
        data.put("moons", memberService.getMoon(memberId));
        response.setData(data);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 리스트 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/room/list")
    public ResponseEntity<Response> getRooms(@NotNull long memberId){
        Response response = new Response();
        response.setData(memberService.getRooms(memberId));
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("참여 스터디룸 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/room/list/apply")
    public ResponseEntity<Response> getApplyRooms(@NotNull long memberId){
        Response response = new Response();
        List<RoomCardDTO>  roomCardDTOS = memberService.getRoomsIs(memberId, false);
        Map<String, List<RoomCardDTO>> data = new HashMap<>();
        data.put("rooms", roomCardDTOS);
        response.setData(data);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("신청한 스터디룸 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/room/list/leader")
    public ResponseEntity<Response> getLeaderRooms(@NotNull long memberId){
        Response response = new Response();
        List<RoomCardDTO>  roomCardDTOS = memberService.getRoomsIs(memberId, true);
        Map<String, List<RoomCardDTO>> data = new HashMap<>();
        data.put("rooms", roomCardDTOS);
        response.setData(data);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("내가 만든 스터디룸 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/pin")
    public ResponseEntity<Response> addPin(@Valid @RequestBody RoomMemberID roomMemberID){
        Response response = new Response();

        boolean change = memberService.updatePin(roomMemberID);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("스터디룸 고정 \'"+ change +"\' 로 변경 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Response> getMember(@Valid @RequestParam Long memberId){
        Response response = new Response();
        Map<String, Map<String, String>> data = new HashMap<>();
        data.put("member", memberService.getMember(memberId));
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("멤버 프로필 정보 출력 완료");
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}