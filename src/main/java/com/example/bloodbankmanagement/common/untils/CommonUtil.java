package com.example.bloodbankmanagement.common.untils;

import com.example.bloodbankmanagement.common.exception.AuthenticationException;
import com.example.bloodbankmanagement.service.authorization.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class CommonUtil {

    public static final String YES_VALUE = "Y";
    public static final String NO_VALUE = "N";
    public static final String USER_NAME = "username";
    public static final String STATUS_USE = "1";
    private static final String URL_REGEX = "^((((https?|http?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)([).!';/?:,][[:blank:]])?$";
    private static final Pattern URL_PATTERN = Pattern.compile("^((((https?|http?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)([).!';/?:,][[:blank:]])?$");
    private static final String[] IP_HEADER_CANDIDATES = new String[]{"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};
    public static final String insertSuccess =  "InsertSuccess";
    public static final String updateSuccess =  "UpdateSuccess";
    public static final String querySuccess =  "QuerySuccess";
    public static final String deleteSuccess =  "DeleteSuccess";
    public static final String sendMAilSuccess =  "SendMAilSuccess";
    public static final String STATUS_EXPIRE = "99";
    public static final String userValue =  "User";
    public static final String successValue =  "success";
    public static final String failValue =  "fail";
    public static final String errorValue =  "error";
    public static final String updatedAt =  "updated_at";
    public static final String createdAt =  "created_at";
    public static final String trueValue =  "true";
    public static final String SendToSingleMailYes =  "SendToSingleMailYes";
    public static final String EMAIL = "email";
    public static final String NOT_FOUND_DATA_USER = "NotFoundDataInDb";
    public static final String EXIST_DATA_IN_DB = "ExistDataInDb";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private CommonUtil() {
    }

    public static String increaseLoggingNumber() {
        if (MDC.get("log.cnt") == null) {
            return "1";
        } else {
            int cnt = Integer.parseInt(MDC.get("log.cnt").replace(" ", ""));
            ++cnt;
            String strCnt = String.valueOf(cnt);
            if (strCnt.length() == 1) {
                strCnt = "  " + strCnt;
            } else if (strCnt.length() == 2) {
                strCnt = " " + strCnt;
            }

            MDC.put("log.cnt", strCnt);
            return MDC.get("log.cnt");
        }
    }

    public static String getMessage(MessageSource messageSource, String code, String locale) {
        return messageSource.getMessage(code, (Object[])null, Locale.forLanguageTag(locale));
    }

    public static String getMessage(MessageSource messageSource, String code, Object[] args, String locale) {
        return messageSource.getMessage(code, args, Locale.forLanguageTag(locale));
    }

    public static String getMessage(MessageSource messageSource, String code) {
        return messageSource.getMessage(code, (Object[])null, Locale.forLanguageTag("vi"));
    }
    public static UserDetailsImpl getThisUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null
                || (authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal().toString()))) {
            throw new AuthenticationException("User is not logined");
        }
        return (UserDetailsImpl) authentication.getPrincipal();
    }
    public static String getUsernameByToken(){
        UserDetailsImpl user = CommonUtil.getThisUser();
        return user.getUsername();
    }

    public static String getJsonStringFromObject(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        try {
            String jsonString = mapper.writeValueAsString(obj);
            return jsonString;
        } catch (JsonProcessingException var4) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, (String)null, var4);
            return "";
        }
    }
}
