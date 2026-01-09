package com.example.bloodbankmanagement.service;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final UserRepository userRepository;

    public String isUserHaveRoleAdmin(String userName){
        boolean isTypeAdmin = false;
        if(ObjectUtils.isEmpty(userName)){
            return "";
        }
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(ObjectUtils.isEmpty(userInfo.get())){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        if(CommonUtil.ROLE_ADMIN.equals(userInfo.get().getRoleInfo().getName().toString())){
            isTypeAdmin = true;
        }
        if(isTypeAdmin){
            return "";
        }
        return userInfo.get().getUsername();
    }

    public String checkExistUser(String userName){
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(ObjectUtils.isEmpty(userInfo.get())){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo.get().getUsername();
    }
}
