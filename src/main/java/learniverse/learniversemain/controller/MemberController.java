package learniverse.learniversemain.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.*;
import learniverse.learniversemain.entity.HashtagEntity;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.entity.RoomMemberEntity;
import learniverse.learniversemain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "member", description = "member 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Validated
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/profile/update")
    public ResponseEntity<Response> updateProfile(@Valid @RequestBody ProfileDTO profileDTO){
        Response response = new Response();

        memberService.updateProfile(profileDTO);

        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("프로필 정보 저장 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/moon/add")
    public ResponseEntity<Response> saveMoon(@Valid @RequestBody MoonDTO moonDTO) {
        Response response = new Response();

        memberService.saveMoon(moonDTO);

        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("달 추가 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/moon/add/core")
    public ResponseEntity<Response> saveCoreMoon(@Valid @RequestBody MoonDTO moonDTO) {
        Response response = new Response();

        memberService.saveCoreMoon(moonDTO);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 추가 성공(코어타입 접속)");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Hidden
    @PostMapping("/moon/minus")
    public ResponseEntity<Response> deleteMoon(@Valid @RequestBody MoonDTO moonDTO) {
        Response response = new Response();

        memberService.deleteMoon(moonDTO);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 삭제 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/moon/list")
    public ResponseEntity<Response> getMoon(@NotNull long memberId) {
        Response response = new Response();

        Map<String, List<ResMoonDTO>> data = new HashMap<>();
        data.put("moons", memberService.getMoon(memberId));
        response.setData(data);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 리스트 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/room/list")
    public ResponseEntity<Response> getRooms(@NotNull long memberId) {
        Response response = new Response();
        response.setData(memberService.getRooms(memberId));
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("참여 스터디룸 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/room/list/apply")
    public ResponseEntity<Response> getApplyRooms(@NotNull long memberId) {
        Response response = new Response();
        List<RoomCardDTO> roomCardDTOS = memberService.getRoomsIs(memberId, false);
        Map<String, List<RoomCardDTO>> data = new HashMap<>();
        data.put("rooms", roomCardDTOS);
        response.setData(data);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("신청한 스터디룸 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/room/list/leader")
    public ResponseEntity<Response> getLeaderRooms(@NotNull long memberId) {
        Response response = new Response();
        List<RoomCardDTO> roomCardDTOS = memberService.getRoomsIs(memberId, true);
        Map<String, List<RoomCardDTO>> data = new HashMap<>();
        data.put("rooms", roomCardDTOS);
        response.setData(data);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("내가 만든 스터디룸 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/pin")
    public ResponseEntity<Response> addPin(@Valid @RequestBody RoomMemberID roomMemberID) {
        Response response = new Response();

        boolean change = memberService.updatePin(roomMemberID);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("스터디룸 고정 \'" + change + "\' 로 변경 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Response> getMember(@Valid @RequestParam Long memberId) {
        Response response = new Response();
        Map<String, Map<String, String>> data = new HashMap<>();
        data.put("member", memberService.getMember(memberId));
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("멤버 프로필 정보 출력 완료");
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/first")
    public ResponseEntity<Response> getMemberFirst(@Valid @RequestParam long memberId) {
        Response response = new Response();
        Map<String, String>   data = new HashMap<>();
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("최초 사용자 확인 정보 출력 완료");
        data.put("memberFirst", memberService.isMemberFirst(memberId));
        data.put("refreshToken", memberService.getRefreshToken(memberId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/repoList")
    public ResponseEntity<Response> getGitRepo(@Valid @RequestParam long memberId) {
        Response response = new Response();
        Map<String, String>   data = new HashMap<>();
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("레포리스트 출력 확인");
        data.put("repoList", memberService.getRepoList(memberId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> addPin(@Valid @RequestBody long memberId) {
        Response response = new Response();

        memberService.login(memberId);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("최근 로그인 정보 업데이트 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*@GetMapping()
    public ResponseEntity<Response> getMemberByToken(Principal principal) { //token으로 멤버 조회
        Response response = new Response();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("issue 출력 성공");
        Map<String, Object> data = new HashMap<>();
        data.put("issue", memberService.getMemberById(Long.valueOf(principal.getName())));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }*/
}
