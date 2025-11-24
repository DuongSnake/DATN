package com.example.bloodbankmanagement.common;


import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.DateUtil;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.ListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ResponseCommon {
    public static final String CODE = ".code";
    public static final String TRACE_ID = "trace.id";

    private final MessageSource messageSource;

    public ResponseCommon(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public <T> SingleResponseDto<T> getSingleResponse(T data) {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    public <T> SingleResponseDto<T> getSingleResponse(T data, Object[] args, String nameMessage) {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setData(data);
        String msg = getConstI18n(nameMessage,args);
        result.setResponseCd(CommonUtil.successValue);
        result.setResponseMsg(msg);
        result.setResponseTs(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS)));
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
        return result;
    }

    public <T> SingleResponseDto<T> getSingleResponseHandleMessage(T data, String responseCode, String nameMessage) {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setData(data);
        result.setResponseCd(responseCode);
        result.setResponseMsg(nameMessage);
        result.setResponseTs(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS)));
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
        return result;
    }


    public <T> SingleResponseDto<T> getFailSingleResponse(T data, String code) {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setData(data);
        setFailResult(result, code);
        return result;
    }


    public BasicResponseDto getSuccessResult() {
        BasicResponseDto result = new BasicResponseDto();
        setSuccessResult(result);
        return result;
    }


    public BasicResponseDto getSuccessResultHaveValueMessage(String responseCode, String responseMessage) {
        BasicResponseDto result = new BasicResponseDto();
        setSuccessResultWithResponseCdAndMsg(result, responseCode, responseMessage);
        return result;
    }

    public BasicResponseDto getFailResult(String code){
        BasicResponseDto result = new BasicResponseDto();
        setFailResult(result, code);
        return result;
    }

    public BasicResponseDto getFailResult2(String code){
        BasicResponseDto result = new BasicResponseDto();
        setFailResult2(result, code);
        return result;
    }

    private void setSuccessResult(BasicResponseDto result) {
        result.setResponseCd("000000");
        result.setResponseMsg("OK.");
        result.setResponseTs(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS)));
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
    }

    private void setSuccessResultWithResponseCdAndMsg(BasicResponseDto result, String responseCd, String responseMsg) {
        result.setResponseCd(responseCd);
        result.setResponseMsg(responseMsg);
        result.setResponseTs(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS)));
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
    }

    private void setFailResult(BasicResponseDto result, String code) {
        result.setResponseCd(CommonUtil.getMessage(messageSource, code + CODE, getLanguageCode()));
        result.setResponseMsg(CommonUtil.getMessage(messageSource, code + ".msg", getLanguageCode()));
        result.setResponseTs(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS)));
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
    }

    private void setFailResult2(BasicResponseDto result, String code) {
        result.setResponseCd(CommonUtil.getMessage(messageSource, code + CODE, getLanguageCode()));
        result.setResponseMsg(CommonUtil.getMessage(messageSource, code + ".msg", getLanguageCode()));
        result.setResponseTs(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS)));
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
    }

    public <T>SingleResponseDto<T> getSingleResponseGateway(SingleResponseDto<T> responseDto) {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setResponseCd(responseDto.getResponseCd());
        result.setResponseMsg(responseDto.getResponseMsg());
        result.setResponseTs(responseDto.getResponseTs());
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
        result.setData(responseDto.getData());
        return result;
    }

    public <T>BasicResponseDto getBasicResponseGateway(SingleResponseDto<T> responseDto) {
        BasicResponseDto result = new BasicResponseDto();
        result.setResponseCd(responseDto.getResponseCd());
        result.setResponseMsg(responseDto.getResponseMsg());
        result.setResponseTs(responseDto.getResponseTs());
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
        return result;
    }

    public <T> ListResponseDto<T> getListResponse(List<T> list) {
        ListResponseDto<T> result = new ListResponseDto<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    public BasicResponseDto getFailResult(String code, String msg){
        BasicResponseDto result = new BasicResponseDto();
        setFailResult(result, code, msg);
        return result;
    }

    public <T> SingleResponseDto<T> getFailSingleResponse(String code, String msg){
        SingleResponseDto<T> result = new SingleResponseDto<T>();
        setFailResult(result, code, msg);
        return result;
    }

    public <T> SingleResponseDto<T> getFailListResponse(T data, String code) throws Exception {
        SingleResponseDto<T> result = new SingleResponseDto<>();
        result.setData(data);
        setFailResult(result, code);
        return result;
    }

    public SingleResponseDto getSingleFailResult(String code, String locale) {
        SingleResponseDto result = new SingleResponseDto();
        result.setResponseCd(CommonUtil.getMessage(messageSource, code + CODE, getLanguageCode()));
        result.setResponseMsg(CommonUtil.getMessage(messageSource, code + ".msg", getLanguageCode()));
        result.setResponseTs(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS)));
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
        return result;
    }

    public <T> ListResponseDto<T> getFailListResponse(String code, String msg){
        ListResponseDto<T> result = new ListResponseDto<T>();
        setFailResult(result, code, msg);
        return result;
    }

    private <T extends BasicResponseDto> void setFailResult(T result, String code, String msg) {
        result.setResponseCd(code);
        result.setResponseMsg(msg);
        result.setResponseTs(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS)));
        result.setResponseTraceId(TRACE_ID);
        result.setRequesterTrId(getRequesterTrId());
    }

    public static String getRequesterTrId() {
        ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpRequest = servletRequestAttribute.getRequest();
        String requestId = httpRequest.getHeader("requestId");
        return requestId == null ? UUID.randomUUID().toString() : requestId;
    }

    public static String getLanguageCode() {
        ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpRequest = servletRequestAttribute.getRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        String lang = "";

        if (httpRequest.getMethod().equals("POST")) {
            Map<String, Object> paramMap = new HashMap();
            ((Map)paramMap).put("lang", httpRequest.getHeader("lang"));

            lang = ((Map)paramMap).get("lang") == null ? "en" : (String)((Map)paramMap).get("lang");
        } else if (httpRequest.getMethod().equals("GET")) {
            lang = httpRequest.getHeader("lang") == null ? "en" : httpRequest.getHeader("lang");
        }

        return lang;
    }

    public String getConstI18n(String paramName) {
        if (paramName == null) return null;
        return CommonUtil.getMessage(messageSource, paramName, getLanguageCode());
    }

    public String getConstI18n(String paramName, Object[] args) {
        if (paramName == null) return null;
        return CommonUtil.getMessage(messageSource, paramName, args, getLanguageCode());
    }
}
