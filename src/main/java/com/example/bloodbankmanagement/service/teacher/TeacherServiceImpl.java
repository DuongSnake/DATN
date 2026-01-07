package com.example.bloodbankmanagement.service.teacher;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.*;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import com.example.bloodbankmanagement.entity.AdmissionPeriod;
import com.example.bloodbankmanagement.entity.Major;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.AdmissionPeriodRepository;
import com.example.bloodbankmanagement.repository.MajorRepository;
import com.example.bloodbankmanagement.repository.RoleRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import com.example.bloodbankmanagement.service.authorization.RoleServiceImpl;
import com.example.bloodbankmanagement.service.email.MailServiceImpl;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    ResponseCommon responseCommon;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    MailServiceImpl mailService;

    @Value("${url-login}")
    String urlLogin;

    @Value("${url-reget-password}")
    String urlRegetPassword;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ResponseCommon responseService;

    @Autowired
    AdmissionPeriodRepository admissionPeriodRepository;

    @Autowired
    MajorRepository majorRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    NativeSqlInsertStrategy nativeSqlInsertStrategy;

    @Transactional
    public BasicResponseDto updateTeacher(@RequestBody StudentDto.StudentUpdateInfo request, @RequestHeader("lang") String lang){
        String userId = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        //Check exist before update
        checkExistTeacher(request.getId(), lang);
        User objectUpdate = StudentDto.StudentUpdateInfo.convertToEntity(request,userId);
        //Check major
        Major majorInfo = majorRepository.findByFileId(request.getMajorId());
        if(null != majorInfo){
            objectUpdate.setMajorInfo(majorInfo);
        }
        objectUpdate.setUpdateAt(LocalDate.now());
        userRepository.updateTeacher(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    public SingleResponseDto<PageAmtListResponseDto<StudentDto.StudentSelectListInfo>> selectListTeacher(@RequestBody StudentDto.StudentSelectListRequest request) {
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Set value page
        PageAmtListResponseDto<StudentDto.StudentSelectListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        Page<User> listDataUser = userRepository.findListStudents(request,pageable);
        //Select list role
        pageAmtObject = User.convertListObjectToDtoStudent(listDataUser.getContent());
        pageAmtObject.setTotalRecord((int) listDataUser.getTotalElements());
        objectResponse = responseCommon.getSingleResponse(pageAmtObject, new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto deleteTeacher(@RequestBody StudentDto.StudentDeleteInfo request, @RequestHeader("lang") String lang) {
        BasicResponseDto objectResponse;
        //Check user have exist or not
        User objectEnity = new User();
        //update data date time and userId
        objectEnity.setStatus(CommonUtil.STATUS_EXPIRE);
        objectEnity.setUpdateAt(LocalDate.now());
        objectEnity.setUpdateUser(CommonUtil.getUsernameByToken());
        userRepository.deleteUsers(objectEnity, request.getListData());
        objectResponse = responseCommon.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto insertListTeacher(@RequestBody StudentDto.StudentInsertInfo request, @RequestHeader("lang") String lang) {
        BasicResponseDto objectResponse;
        //Check user have exist or not
        User objectEnity = StudentDto.StudentInsertInfo.convertToEntity(request);
        //update data date time and userId
        objectEnity.setStatus(CommonUtil.STATUS_USE);
        //Set role
        Role rolesTeacher = roleRepository.findByName(ERole.ROLE_MODERATOR).get();
        Set<Role> roles = new HashSet<>();
        roles.add(rolesTeacher);
        objectEnity.setRoles(roles);
        //Check major
        Major majorInfo = majorRepository.findByFileId(request.getMajorId());
        if(null != majorInfo){
            objectEnity.setMajorInfo(majorInfo);
        }
        objectEnity.setUpdateAt(LocalDate.now());
        String defaultPassword = "ktx2024";
        objectEnity.setPassword(encoder.encode(defaultPassword));
        objectEnity.setUpdateUser(CommonUtil.getUsernameByToken());
        userRepository.save(objectEnity);
        //Send mail announcement register new user
        Map<String, String> params = new HashMap<>();
        params.put("userNm", request.getUsername());
        params.put("email", request.getEmail());
        params.put("password", defaultPassword);
        params.put("urlLogin", urlLogin);
        boolean sendSuccess = mailService.sendEmailByTemplate(params, EmailTemplate.REG_NEW.getName(), EmailTemplate.REG_NEW.getSubject());
        if (!sendSuccess) {
            return responseCommon.getSingleFailResult("EmailSendFail", lang);
        }
        objectResponse = responseCommon.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return objectResponse;
    }

    public SingleResponseDto<StudentDto.StudentSelectInfoResponse> selectTeacher(@RequestBody StudentDto.StudentSelectInfo request, @RequestHeader("lang") String lang){
        SingleResponseDto<StudentDto.StudentSelectInfoResponse> singleResponseDto;
        //Check user have exist or not
        User selectStudent = userRepository.getById(Long.valueOf(request.getId()));
        if(ObjectUtils.isEmpty(selectStudent)){
            return responseCommon.getSingleFailResult(CommonUtil.NOT_FOUND_DATA_USER, lang);
        }
        StudentDto.StudentSelectInfoResponse objectResponse = User.convertToDtoStudent(selectStudent);
        singleResponseDto = responseService.getSingleResponse(objectResponse);
        return singleResponseDto;
    }

    public void checkExistTeacher(long userId, String lang){
        User countExist = userRepository.getById(userId);
        if(ObjectUtils.isEmpty(countExist)){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, lang);
        }
    }
}
