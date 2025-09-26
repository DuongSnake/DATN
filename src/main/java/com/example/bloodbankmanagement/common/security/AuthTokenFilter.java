package com.example.bloodbankmanagement.common.security;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.JwtUtils;
import com.example.bloodbankmanagement.common.untils.ReadableRequestWrapper;
import com.example.bloodbankmanagement.common.untils.ResponseWrapper;
import com.example.bloodbankmanagement.service.authorization.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private long start;
    private long end;
    public static final String LOG_CNT = "log.cnt";
    public static final String TRACE_ID = "trace.id";

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    private String parsJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("token");
        return headerAuth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.put(TRACE_ID, UUID.randomUUID().toString());
        MDC.put(LOG_CNT, "0");
        if(StringUtils.startsWithIgnoreCase(request.getContentType(), "multipart/")) {
            if (request.getParts().isEmpty()) {
                LOGGER.info("Request.getParts() is empty for={}", request.getPathInfo());
            } else {
                LOGGER.info("Request.getParts() is NOT empty for={}", request.getPathInfo());
            }
        }
        if (isAsyncDispatch(request)) {

            filterChain.doFilter(request, response);
        } else {

            doFilterWrapped(new ReadableRequestWrapper(request), new ResponseWrapper(response), filterChain);
        }

        MDC.clear();
    }

    protected void doFilterWrapped(ReadableRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            start = System.currentTimeMillis();
            logRequest(request);
            String jwt = parsJwt(request);
            if(jwt != null && jwtUtils.validateJWTToken(request, jwt)){

                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }finally {
            end = System.currentTimeMillis();
            logResponse(response);
            response.copyBodyToResponse();
        }
    }

    private static void logRequest(ReadableRequestWrapper request) throws IOException {
        String queryString = request.getQueryString();

        LOGGER.info("[{}] [ REQ] - Method: {}", CommonUtil.increaseLoggingNumber(), ((HttpServletRequest) request).getMethod());
        LOGGER.info("[{}] [ REQ] - URI: {}", CommonUtil.increaseLoggingNumber(), ((HttpServletRequest) request).getRequestURI());
        LOGGER.info("[{}] [ REQ] - Headers: {}", CommonUtil.increaseLoggingNumber(), getHeaders(request));
        if(request.getMethod().equals("GET")) {
            LOGGER.info("[{}] [ REQ] - Payload {}", CommonUtil.increaseLoggingNumber(), queryString);
        } else {
            LOGGER.info("[{}] [ REQ] - Payload: {}", CommonUtil.increaseLoggingNumber(), logPayload(request.getContentType(), request.getInputStream()).replace("\\R", "").replace(" ", ""));
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {

        LOGGER.info("[{}] [ RES] - Status: {}", CommonUtil.increaseLoggingNumber(), response.getStatus());
        LOGGER.info("[{}] [ RES] - Processing Time: {}", CommonUtil.increaseLoggingNumber(), (end - start) / 1000.0);
        LOGGER.info("[{}] [ RES] - Payload: {}", CommonUtil.increaseLoggingNumber(), logPayload(response.getContentType(), response.getContentInputStream()).replace("\\R", "").replace(" ", ""));

    }

    private static Map getHeaders(HttpServletRequest request) {
        Map headerMap = new HashMap<>();

        Enumeration headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = (String) headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private static String logPayload(String contentType, InputStream inputStream) throws IOException {
        //get data truyen vao
        String contentString = "";
        boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));
        if (visible) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            if (content.length > 0) {
                contentString = new String(content);
            }
        } else {
            contentString = "Binary Content";
        }

        return contentString;
    }

    private static boolean isVisible(MediaType mediaType) {
        final List<MediaType> visibleTypes = Arrays.asList(
                MediaType.valueOf("text/*"),
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_XML,
                MediaType.valueOf("application/*+json"),
                MediaType.valueOf("application/*+xml"),
                MediaType.MULTIPART_FORM_DATA
        );

        return visibleTypes.stream()
                .anyMatch(visibleType -> visibleType.includes(mediaType));
    }
}
