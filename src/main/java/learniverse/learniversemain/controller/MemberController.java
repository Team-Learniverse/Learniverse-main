package learniverse.learniversemain.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.MoonDTO;
import learniverse.learniversemain.dto.ResMoonDTO;
import learniverse.learniversemain.entity.HashtagEntity;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Validated
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/moon")
    public ResponseEntity<Response> saveMoon(@Valid @RequestBody MoonDTO moonDTO){
        Response response = new Response();

        memberService.saveMoon(moonDTO);

        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("달 추가 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/moon/minus")
    public ResponseEntity<Response> deleteMoon(@Valid @RequestBody MoonDTO moonDTO){
        Response response = new Response();

        memberService.deleteMoon(moonDTO);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 삭제 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/moon")
    public ResponseEntity<Response> getMoon(@NotNull @RequestParam Long memberId){
        Response response = new Response();

        List<ResMoonDTO> resMoonList= memberService.getMoon(memberId);

        Map<String,List<ResMoonDTO>> data = new HashMap<>();
        data.put("moons", resMoonList);
        response.setData(data);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 리스트 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
