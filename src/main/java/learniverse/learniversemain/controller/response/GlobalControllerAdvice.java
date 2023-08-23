package learniverse.learniversemain.controller.response;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestControllerAdvice
public class GlobalControllerAdvice {

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity exception(Exception e){
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-");
//    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidException(MethodArgumentNotValidException e){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        List<Error> errorList = new ArrayList<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error; //형 변환
//
            Error errorMessage = new Error();
            errorMessage.setField(fieldError.getField()); // field name
            errorMessage.setMessage(fieldError.getDefaultMessage()); // error message
            errorList.add(errorMessage);
        });

        response.setErrorList(errorList);
        response.setMessage("잘못된 값을 입력했습니다. (errorList 확인 필요)");
        response.setStatus(Response.StatusEnum.BAD_REQUEST);
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Response> constraintViolationException(ConstraintViolationException e){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));
        List<Error> errorList = new ArrayList<>();

        e.getConstraintViolations().forEach(error ->{
            Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);
            List<Path.Node> list = stream.collect(Collectors.toList());
            String field = list.get(list.size()-1).getName();

            Error errorMessage = new Error();
            errorMessage.setField(field); // field name
            errorMessage.setMessage(error.getMessage()); // error message
            errorMessage.setInvalidValue(error.getInvalidValue().toString()); // value check
        });

        response.setErrorList(errorList);
        response.setMessage("잘못된 값을 입력했습니다. (errorList 확인 필요)");
        response.setStatus(Response.StatusEnum.BAD_REQUEST);
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class) //아무값도 입력안하면 나오는 애러 (GET)
    public ResponseEntity<Response> missingServletRequestParameterException(MissingServletRequestParameterException e){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        response.setMessage(e.getParameterName()+" 파라미터가 필요합니다."); // error message
        response.setStatus(Response.StatusEnum.BAD_REQUEST);

        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }
}