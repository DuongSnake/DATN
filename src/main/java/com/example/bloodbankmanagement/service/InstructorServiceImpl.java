package com.example.bloodbankmanagement.service;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.EmailTemplate;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.InstructorDto;
import com.example.bloodbankmanagement.entity.Instructor;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.InstructorRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import com.example.bloodbankmanagement.service.authorization.RoleServiceImpl;
import com.example.bloodbankmanagement.service.email.MailServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final InstructorRepository instructorRepository;
    private final ResponseCommon responseService;
    private final PasswordEncoder encoder;
    private final RoleServiceImpl roleService;
    private final MailServiceImpl mailService;
    private final UserRepository userRepository;
    @Autowired
    ResponseCommon responseCommon;

    @Value("${url-login}")
    String urlLogin;

    @Value("${url-reget-password}")
    String urlRegetPassword;

    @Transactional
    public BasicResponseDto insertInstructor(InstructorDto.InstructorInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdCreate = CommonUtil.getUsernameByToken();
        
        Instructor objectInsert = new Instructor();
        objectInsert.setEmail(request.getEmail());
        objectInsert.setPhone(request.getPhone());
        objectInsert.setFullName(request.getFullName());
        objectInsert.setCreateUser(userIdCreate);
        objectInsert.setStatus(CommonUtil.STATUS_USE);
        objectInsert.setCreateAt(LocalDate.now());
        
        instructorRepository.insertInstructor(objectInsert);

        //Create account with role instructor
        User objectInstructorInsert = new User();
        //Set value default when create new user (0:not paid, 1:paid success)
        String defaultPassword = CommonUtil.DEFAULT_PASSWORD;
        objectInstructorInsert.setPassword(encoder.encode(defaultPassword));
        objectInstructorInsert.setStatus(CommonUtil.STATUS_USE);
        objectInstructorInsert.setCreateAt(LocalDate.now());
        objectInstructorInsert.setCreateUser(CommonUtil.getUsernameByToken());

        objectInstructorInsert.setUsername(request.getEmail());
        objectInstructorInsert.setEmail(request.getEmail());
        objectInstructorInsert.setFullName(request.getFullName());
        //Set role
        Set<Role> roles = roleService.getRole("4");//4:Instructor
        Role userRole = roles.stream().findFirst().get();//Get first role
        objectInstructorInsert.setRoleInfo(userRole);
        userRepository.save(objectInstructorInsert);
        //Send mail announcement register new user
        Map<String, String> params = new HashMap<>();
        params.put("userNm", request.getEmail());
        params.put("email", request.getEmail());
        params.put("password", defaultPassword);
        params.put("urlLogin", urlLogin);
        boolean sendSuccess = mailService.sendEmailByTemplate(params, EmailTemplate.REG_NEW.getName(), EmailTemplate.REG_NEW.getSubject());
        if (!sendSuccess) {
            return responseCommon.getSingleFailResult("EmailSendFail", lang);
        }
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<InstructorDto.InstructorListInfo>> selectListInstructor(InstructorDto.InstructorSelectListRequest request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<InstructorDto.InstructorListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        
        // Select list instructors
        Page<Instructor> listDataInstructor = instructorRepository.findListInstructor(request, pageable);
        pageAmtObject = Instructor.convertListObjectToDto(listDataInstructor.getContent(), listDataInstructor.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<InstructorDto.InstructorSelectInfoResponse> selectInstructor(InstructorDto.InstructorSelectInfo request, String lang){
        if(null == request || null == request.getInstructorId()){
            throw new CustomException("Not found value request param", "en");
        }
        SingleResponseDto<InstructorDto.InstructorSelectInfoResponse> selectObject = new SingleResponseDto<>();
        Instructor dataInstructor = instructorRepository.findInstructorById(request.getInstructorId());
        InstructorDto.InstructorSelectInfoResponse objectResponse = new InstructorDto.InstructorSelectInfoResponse();
        if(null == dataInstructor){
            objectResponse = null;
        }else{
            objectResponse = Instructor.convertToDto(dataInstructor);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateInstructor(InstructorDto.InstructorUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request || null == request.getInstructorId()){
            throw new CustomException("the object send request not null", "en");
        }
        
        // Check if instructor exists
        Instructor existingInstructor = instructorRepository.findInstructorById(request.getInstructorId());
        if(null == existingInstructor){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, lang);
        }
        
        Instructor objectUpdate = new Instructor();
        objectUpdate.setId(request.getInstructorId());
        objectUpdate.setEmail(request.getEmail());
        objectUpdate.setPhone(request.getPhone());
        objectUpdate.setFullName(request.getFullName());
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        instructorRepository.updateInstructor(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteInstructor(InstructorDto.InstructorDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        Instructor objectDelete = new Instructor();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        instructorRepository.deleteInstructor(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }
}
