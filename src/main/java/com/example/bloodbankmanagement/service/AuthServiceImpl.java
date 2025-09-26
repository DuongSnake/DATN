package com.example.bloodbankmanagement.service;


import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.AuthenticationException;
import com.example.bloodbankmanagement.common.exception.ExceptionEntity;
import com.example.bloodbankmanagement.common.exception.ValidateException;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.DateUtil;
import com.example.bloodbankmanagement.common.untils.EmailTemplate;
import com.example.bloodbankmanagement.common.untils.JwtUtils;
import com.example.bloodbankmanagement.dto.common.JwtResponseDto;
import com.example.bloodbankmanagement.dto.common.LoginRequestDto;
import com.example.bloodbankmanagement.dto.common.SignupRequestDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.UserRepository;
import com.example.bloodbankmanagement.service.authorization.RoleServiceImpl;
import com.example.bloodbankmanagement.service.authorization.UserDetailsImpl;
import com.example.bloodbankmanagement.service.email.MailServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleServiceImpl roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ResponseCommon responseCommon;

    @Autowired
    MailServiceImpl mailService;

    @Value("${url-login}")
    private String urlLogin;


    @Autowired
    JwtUtils jwtUtils;

    public SingleResponseDto<JwtResponseDto> authenticationUser(@Valid @RequestBody LoginRequestDto.LoginDataRequest loginRequest){
        try {
            SingleResponseDto dataResponse;
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            dataResponse = responseCommon.getSingleResponse(new JwtResponseDto(jwt, userDetails.getId(), userDetails.getUsername(), roles), new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
            return dataResponse;
        }catch (Exception e) {
            throw new AuthenticationException("UsernameOrPasswordNotFoundException");
        }
    }

    @Transactional
    public SingleResponseDto<SignupRequestDto.SignupDataRequest> registerUser(@Valid @RequestBody SignupRequestDto.SignupDataRequest signupRequestDto, @RequestHeader("lang") String lang) throws Exception {
        SingleResponseDto<SignupRequestDto.SignupDataRequest> dataResponse = new SingleResponseDto();
        if(ObjectUtils.isEmpty(signupRequestDto)){
            return responseCommon.getFailListResponse(signupRequestDto,"InvalidDataRequest");
        }
        if (userRepository.existsByUsername(signupRequestDto.getUsername())){
            throw new ValidateException(ExceptionEntity.FieldError.of(CommonUtil.USER_NAME, signupRequestDto.getUsername(), "DuplicateUserAccount"), lang);
        }
        String defaultPassword = "ktx2024";
        //Crate new user's account
        User user = new User(signupRequestDto.getUsername(), encoder.encode(defaultPassword), signupRequestDto.getEmail(), signupRequestDto.getPhone(), signupRequestDto.getFullName());
        Set<String> strRoles = signupRequestDto.getRole();
        Set<Role> roles = roleService.getRole(strRoles);
        user.setRoles(roles);
        user.setStatus(CommonUtil.STATUS_USE);
        user.setCreateAt(DateUtil.strNowDate());
        user.setCreateTm(DateUtil.strNowTime());
        user.setCreateUser(user.getUsername());
        userRepository.save(user);
        dataResponse = responseCommon.getSingleResponse(signupRequestDto, new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.insertSuccess);
        //Send mail anoucement register new user
        Map<String, String> params = new HashMap<>();
        params.put("userNm", signupRequestDto.getUsername());
        params.put("email", signupRequestDto.getEmail());
        params.put("password", defaultPassword);
        params.put("urlLogin", urlLogin);
        params.put(CommonUtil.SendToSingleMailYes, CommonUtil.YES_VALUE);
        boolean sendSuccess = mailService.sendEmailByTemplate(params, EmailTemplate.REG_NEW.getName(), EmailTemplate.REG_NEW.getSubject());
        if (!sendSuccess) {
            return responseCommon.getSingleFailResult("EmailSendFail", lang);
        }
        return dataResponse;
    }

    public String getUsernameByToken(String token){
        return jwtUtils.getUserNameFromJwtToken(token);
    }
}
