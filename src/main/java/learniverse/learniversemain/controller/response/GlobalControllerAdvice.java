package learniverse.learniversemain.controller.response;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.controller.Exception.CustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute
    public HttpHeaders jsonHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }

    @ExceptionHandler(value = Exception.class) // 500
    public ResponseEntity exception(Exception e){
        Response response = new Response();
        response.setMessage("서버 내 오류");
        response.setStatus(Response.StatusEnum.INTERNAL_SERVER_ERROR);

        List<Error> errorList = new ArrayList<>();
        Error errorMessage = new Error();
        errorMessage.setMessage(e.getMessage()); // error message
        errorList.add(errorMessage);
        response.setErrorList(errorList);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidException(MethodArgumentNotValidException e){
        Response response = new Response();

        List<Error> errorList = new ArrayList<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error; //형 변환
            Error errorMessage = new Error();
            errorMessage.setField(fieldError.getField()); // field name
            errorMessage.setMessage(fieldError.getDefaultMessage()); // error message
            errorList.add(errorMessage);
        });

        response.setErrorList(errorList);
        response.setMessage("잘못된 값을 입력했습니다. (errorList 확인 필요)");
        response.setStatus(Response.StatusEnum.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Response> constraintViolationException(ConstraintViolationException e){
        Response response = new Response();
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
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class) //아무값도 입력안하면 나오는 애러 (GET)
    public ResponseEntity<Response> missingServletRequestParameterException(MissingServletRequestParameterException e){
        Response response = new Response();

        response.setMessage(e.getParameterName()+" 파라미터가 필요합니다."); // error message
        response.setStatus(Response.StatusEnum.BAD_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Response> httpMessageNotReadableException(HttpMessageNotReadableException e){
        Response response = new Response();
          List<Error> errorList = new ArrayList<>();

        response.setMessage("올바르지 않은 형식의 값이 포함되어있습니다");
        Error errorMessage = new Error();
        errorMessage.setMessage(e.getMessage()); // error message
        errorList.add(errorMessage);

        response.setStatus(Response.StatusEnum.BAD_REQUEST);
        response.setErrorList(errorList);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        Response response = new Response();
        List<Error> errorList = new ArrayList<>();

        response.setMessage("올바르지 않은 형식입니다.");
        Error errorMessage = new Error();
        errorMessage.setField(e.getName());
        errorMessage.setMessage(e.getErrorCode()); // error message
        errorMessage.setInvalidValue(e.getValue().toString());
        errorList.add(errorMessage);

        response.setStatus(Response.StatusEnum.BAD_REQUEST);
        response.setErrorList(errorList);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CannotFindRoomException.class)
    public ResponseEntity<Response> cannotFindRoomException(CannotFindRoomException e){
        Response response = new Response();

        response.setMessage(e.getMessage());
        response.setStatus(Response.StatusEnum.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CustomBadRequestException.class)
    public ResponseEntity<Response> customBadRequestException(CustomBadRequestException e){
        Response response = new Response();
        response.setMessage(e.getMessage());
        response.setStatus(Response.StatusEnum.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<Response> customException(CustomException e){
        Response response = new Response();
        response.setMessage(e.getMessage());
        response.setStatus(Response.StatusEnum.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
