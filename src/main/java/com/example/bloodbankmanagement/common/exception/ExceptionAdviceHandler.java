package com.example.bloodbankmanagement.common.exception;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.ErrorCode;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdviceHandler{

        private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

        private final ResponseCommon responseService;
        private static final String HANDLE_EXCEPTION_CODE = "[{}] [Exception] - handleIOException Code : {}";
        private static final String HANDLE_EXCEPTION_MSG = "[{}] [Exception] - handleIOException Message : {}";
        private static final String HANDLE_EXCEPTION_ENTITY = "[{}] [Exception] - handleIOException Entity : {}";
        private static final String HANDLE_EXCEPTION_STACK_TRANCE = "[{}] [Exception] - handleIOException StackTrace : {}";

        @ExceptionHandler({Exception.class})
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<BasicResponseDto> handleException(Exception exception) {

            BasicResponseDto basicResponseDto = responseService.getFailResult("FAIL");

            logger.error("[{}] [Exception] - handleException Code : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseCd());
            logger.error("[{}] [Exception] - handleException Message : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseMsg());
            logger.debug("[{}] [Exception] - handleException Entity : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.toString());
            logger.error("[{}] [Exception] - handleException StackTrace :", CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(basicResponseDto, HttpStatus.BAD_REQUEST);

        }

        @ExceptionHandler({NullPointerException.class})
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<BasicResponseDto> handleNullPointerException(NullPointerException exception) {

            BasicResponseDto basicResponseDto = responseService.getFailResult("FAIL");

            logger.error("[{}] [Exception] - handleNullPointerException Code : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseCd());
            logger.error("[{}] [Exception] - handleNullPointerException Message : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseMsg());
            logger.debug("[{}] [Exception] - handleNullPointerException Entity : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.toString());
            logger.error("[{}] [Exception] - handleNullPointerException StackTrace :", CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(basicResponseDto, HttpStatus.BAD_REQUEST);

        }

        @ExceptionHandler({MethodArgumentNotValidException.class})
        @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
        public ResponseEntity<ExceptionEntity> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

            final ExceptionEntity infoPlusExceptionEntity = ExceptionEntity.of(ErrorCode.FAIL, exception.getBindingResult());

            logger.error("[{}] [Exception] - HandleMethodArgumentNotValid Message : {}", CommonUtil.increaseLoggingNumber(), exception.getBindingResult());
            logger.debug("[{}] [Exception] - HandleMethodArgumentNotValid Entity : {}", CommonUtil.increaseLoggingNumber(), infoPlusExceptionEntity.toString());
            logger.error("[{}] [Exception] - HandleMethodArgumentNotValid StackTrace :", CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(infoPlusExceptionEntity, HttpStatus.UNPROCESSABLE_ENTITY);

        }

        @ExceptionHandler({BindException.class})
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ExceptionEntity> handleBindException(BindException exception) {

            final ExceptionEntity infoPlusExceptionEntity = ExceptionEntity.of(ErrorCode.FAIL, exception.getBindingResult());

            logger.error("[{}] [Exception] - HandleBindException Message : {}", CommonUtil.increaseLoggingNumber(), exception.getMessage());
            logger.debug("[{}] [Exception] - HandleBindException Entity : {}", CommonUtil.increaseLoggingNumber(), infoPlusExceptionEntity.toString());
            logger.error("[{}] [Exception] - HandleBindException StackTrace :", CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(infoPlusExceptionEntity, HttpStatus.BAD_REQUEST);

        }

        @ExceptionHandler({AuthenticationException.class})
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<BasicResponseDto> handleAuthenticationException(AuthenticationException exception) {

            BasicResponseDto basicResponseDto = responseService.getFailResult(exception.getErrorCode());

            logger.error("[{}] [Exception] - HandleAuthenticationException Code : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseCd());
            logger.error("[{}] [Exception] - HandleAuthenticationException Message : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseMsg());
            logger.debug("[{}] [Exception] - HandleAuthenticationException Entity : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.toString());
            logger.error("[{}] [Exception] - HandleAuthenticationException StackTrace :", CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);

        }

        @ExceptionHandler({CustomException.class})
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<BasicResponseDto> handleCustomException(CustomException exception) {

            BasicResponseDto basicResponseDto = responseService.getFailResult(exception.getErrorCode());

            logger.error("[{}] [Exception] - HandleCustomException Code : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseCd());
            logger.error("[{}] [Exception] - HandleCustomException Message : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseMsg());
            logger.debug("[{}] [Exception] - HandleCustomException Entity : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.toString());
            logger.error("[{}] [Exception] - HandleCustomException StackTrace :", CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(basicResponseDto, HttpStatus.BAD_REQUEST);

        }

        @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
        @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
        public ResponseEntity<BasicResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {

            BasicResponseDto basicResponseDto = responseService.getFailResult("MethodNotAllowed");

            logger.error("[{}] [Exception] - handleHttpRequestMethodNotSupportedException Code : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseCd());
            logger.error("[{}] [Exception] - handleHttpRequestMethodNotSupportedException Message : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseMsg());
            logger.debug("[{}] [Exception] - handleHttpRequestMethodNotSupportedException Entity : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.toString());
            logger.error("[{}] [Exception] - handleHttpRequestMethodNotSupportedException StackTrace :", CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(basicResponseDto, HttpStatus.METHOD_NOT_ALLOWED);

        }

        @ExceptionHandler({HttpMessageNotReadableException.class})
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<BasicResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {

            BasicResponseDto basicResponseDto = responseService.getFailResult("EntityNotFound");

            logger.error("[{}] [Exception] - handleHttpMessageNotReadableException Code : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseCd());
            logger.error("[{}] [Exception] - handleHttpMessageNotReadableException Message : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseMsg());
            logger.debug("[{}] [Exception] - handleHttpMessageNotReadableException Entity : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.toString());
            logger.error("[{}] [Exception] - handleHttpMessageNotReadableException StackTrace :", CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);

        }

        @ExceptionHandler({DuplicateKeyException.class})
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<BasicResponseDto> handleDuplicateKeyException(DuplicateKeyException exception) {

            BasicResponseDto basicResponseDto = responseService.getFailResult("DuplicateKeyException");

            logger.error("[{}] [Exception] - handleDuplicateKeyException Code : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseCd());
            logger.error("[{}] [Exception] - handleDuplicateKeyException Message : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseMsg());
            logger.debug("[{}] [Exception] - handleDuplicateKeyException Entity : {}", CommonUtil.increaseLoggingNumber(), basicResponseDto.toString());
            logger.error("[{}] [Exception] - handleDuplicateKeyException StackTrace :", CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);

        }

        @ExceptionHandler({IOException.class})
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<BasicResponseDto> handleIOException(IOException exception) {

            BasicResponseDto basicResponseDto = responseService.getFailResult("FAIL");

            logger.error(HANDLE_EXCEPTION_CODE, CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseCd());
            logger.error(HANDLE_EXCEPTION_MSG, CommonUtil.increaseLoggingNumber(), basicResponseDto.getResponseMsg());
            logger.debug(HANDLE_EXCEPTION_ENTITY, CommonUtil.increaseLoggingNumber(), basicResponseDto.toString());
            logger.error(HANDLE_EXCEPTION_STACK_TRANCE, CommonUtil.increaseLoggingNumber(), exception);
            return new ResponseEntity<>(basicResponseDto, HttpStatus.BAD_REQUEST);

        }

        @ExceptionHandler({ValidateException.class})
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ExceptionEntity> handleMethodArgumentNotValid(ValidateException exception) {
            List<ExceptionEntity.FieldError> listError = new ArrayList<>();
            if (exception.getErrors() != null) {
                for (ExceptionEntity.FieldError error : exception.getErrors()) {
                    BasicResponseDto basicResponseDto = responseService.getFailResult(error.getReason(), exception.getLocale());
                    listError.addAll(ExceptionEntity.FieldError.of(error.getField(), error.getValue(), basicResponseDto.getResponseMsg()));
                }
            }
            final ExceptionEntity infoPlusExceptionEntity = ExceptionEntity.of(ErrorCode.FAIL, listError);

            logger.error("HandleMethodArgumentNotValid Message : {}", exception.getMessage());
            logger.error("HandleMethodArgumentNotValid Entity : {}", infoPlusExceptionEntity.toString());
            logger.error("HandleMethodArgumentNotValid StackTrace :", exception);
            return new ResponseEntity<>(infoPlusExceptionEntity, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler({ValidateDuplicateException.class})
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<ExceptionEntity> handleMethodArgumentNotValid(ValidateDuplicateException exception) {
            List<ExceptionEntity.FieldError> listError = new ArrayList<>();
            if (exception.getErrors() != null) {
                for (ExceptionEntity.FieldError error : exception.getErrors()) {
                    BasicResponseDto basicResponseDto = responseService.getFailResult(error.getReason(), exception.getLocale());
                    listError.addAll(ExceptionEntity.FieldError.of(error.getField(), error.getValue(), basicResponseDto.getResponseMsg()));
                }
            }
            final ExceptionEntity infoPlusExceptionEntity = ExceptionEntity.of(ErrorCode.DUPLICATE_INPUT_VALUE, listError);

            logger.error("HandleMethodArgumentNotValid Message : {}", exception.getMessage());
            logger.error("HandleMethodArgumentNotValid Entity : {}", infoPlusExceptionEntity.toString());
            logger.error("HandleMethodArgumentNotValid StackTrace :", exception);
            return new ResponseEntity<>(infoPlusExceptionEntity, HttpStatus.OK);
        }

        @ExceptionHandler({NoResourceFoundException.class})
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<BasicResponseDto> handleNoResourceFoundException(NoResourceFoundException exception) {
            BasicResponseDto basicResponseDto = responseService.getFailResult("FAIL");
            basicResponseDto.setResponseMsg("Rousce not found "+exception.getResourcePath());
            logger.error("HandleNoResourceFoundException Message : {}", exception.getMessage());
            logger.error("HandleNoResourceFoundException Entity : {}", exception.getResourcePath());
            logger.error("HandleNoResourceFoundException StackTrace :", exception);
            return new ResponseEntity<>(basicResponseDto, HttpStatus.NOT_FOUND);
    }
}
