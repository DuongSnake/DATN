package com.example.bloodbankmanagement.common.security;


import com.example.bloodbankmanagement.common.untils.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private final MessageSource messageSource;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String) request.getAttribute("exception");
        String token = request.getHeader("token");
        if(null == token){
            String errorCode = "NotFoundToken";
            setResponse(response, errorCode, HttpServletResponse.SC_UNAUTHORIZED, token);
            return;
        }
        if(exception == null) {
            String errorCode = "UNAUTHORIZEDException";
            setResponse(response, errorCode, HttpServletResponse.SC_FORBIDDEN, token);
            return;
        }
        if(exception.equals("ExpiredJwtException")) {
            String errorCode = "ExpiredJwtException";
            setResponse(response, errorCode, HttpServletResponse.SC_BAD_REQUEST, token);
            return;
        }
        if(exception.equals("MalformedJwtException")) {
            String errorCode = "MalformedJwtException";
            setResponse(response, errorCode, HttpServletResponse.SC_BAD_REQUEST, token);
            return;
        }
    }

    private void setResponse(HttpServletResponse response, String errorCode, int statusCode, String token) throws IOException {

        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(statusCode);


        json.put("responseCd", CommonUtil.getMessage(messageSource, "Error", "en"));
        json.put("responseMsg", CommonUtil.getMessage(messageSource, errorCode + ".msg", "en"));
        json.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS")));
        json.put("token", token);
        response.getWriter().print(json);
    }
}
