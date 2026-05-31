package com.example.bloodbankmanagement.service;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.*;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.StudentManagementDto;
import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.Student;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.StudentRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import com.example.bloodbankmanagement.service.authorization.RoleServiceImpl;
import com.example.bloodbankmanagement.service.email.MailServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentManagementServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(StudentManagementServiceImpl.class);
    private final StudentRepository studentRepository;
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

    @Autowired
    NativeSqlInsertStrategy nativeSqlInsertStrategy;

    @Transactional
    public BasicResponseDto insertStudent(StudentManagementDto.StudentInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdCreate = CommonUtil.getUsernameByToken();
        
        Student objectInsert = new Student();
        objectInsert.setEmail(request.getEmail());
        objectInsert.setPhone(request.getPhone());
        objectInsert.setFullName(request.getFullName());
        objectInsert.setCreateUser(userIdCreate);
        objectInsert.setStatus(CommonUtil.STATUS_USE);
        objectInsert.setCreateAt(LocalDate.now());

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
        Set<Role> roles = roleService.getRole("1");//4:Student
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
        //Add code update vao bang Ky hoc _ So luong
        studentRepository.insertStudent(objectInsert);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<StudentManagementDto.StudentListInfo>> selectListStudent(StudentManagementDto.StudentSelectListRequest request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<StudentManagementDto.StudentListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        
        // Select list students
        Page<Student> listDataStudent = studentRepository.findListStudent(request, pageable);
        pageAmtObject = Student.convertListObjectToDto(listDataStudent.getContent(), listDataStudent.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<StudentManagementDto.StudentSelectInfoResponse> selectStudent(StudentManagementDto.StudentSelectInfo request, String lang){
        if(null == request || null == request.getStudentId()){
            throw new CustomException("Not found value request param", "en");
        }
        SingleResponseDto<StudentManagementDto.StudentSelectInfoResponse> selectObject = new SingleResponseDto<>();
        Student dataStudent = studentRepository.findStudentById(request.getStudentId());
        StudentManagementDto.StudentSelectInfoResponse objectResponse = new StudentManagementDto.StudentSelectInfoResponse();
        if(null == dataStudent){
            objectResponse = null;
        }else{
            objectResponse = Student.convertToDto(dataStudent);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateStudent(StudentManagementDto.StudentUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request || null == request.getStudentId()){
            throw new CustomException("the object send request not null", "en");
        }
        
        // Check if student exists
        Student existingStudent = studentRepository.findStudentById(request.getStudentId());
        if(null == existingStudent){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, lang);
        }
        
        Student objectUpdate = new Student();
        objectUpdate.setId(request.getStudentId());
        objectUpdate.setEmail(request.getEmail());
        objectUpdate.setPhone(request.getPhone());
        objectUpdate.setFullName(request.getFullName());
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        studentRepository.updateStudent(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteStudent(StudentManagementDto.StudentDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        Student objectDelete = new Student();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        studentRepository.deleteStudent(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }
    @Transactional
    public BasicResponseDto uploadFileRegisterListStudent(StudentManagementDto.UploadBatchFileRegisterUserInfo request, @RequestHeader("lang") String lang){
        //Check list in file upload
        BasicResponseDto objectResponse = null;
        boolean statusInsertAll = true;
        List<StudentDto.UploadFileRegisterStudentInfo> listPosPayerFromExcel = buildListUserFromExcel(request.getFileUploadContent(), lang);
        validateFileExcel(listPosPayerFromExcel, lang);
        //set default value success
        objectResponse=responseService.getSingleResponse(listPosPayerFromExcel);
        //Set value if have any problem when insert
        for (StudentDto.UploadFileRegisterStudentInfo objectLoop:listPosPayerFromExcel) {
            if (null != objectLoop.getErrors() && objectLoop.getErrors().size() > 0) {
                statusInsertAll = false;
                objectResponse=responseCommon.getSingleResponseHandleMessage(listPosPayerFromExcel, CommonUtil.failValue, "Noi dung message fail");
                break;
            }
        }
        //check status before insert all
        if(statusInsertAll){
            //Insert list
            nativeSqlInsertStrategy.bulkInsertStudent(listPosPayerFromExcel);
        }
        return objectResponse;
    }

    public List<StudentDto.UploadFileRegisterStudentInfo> buildListUserFromExcel(MultipartFile file, String language){
        logger.info("Start build list user from excel");
        List<StudentDto.UploadFileRegisterStudentInfo> batchList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            int rowNum = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //Not get value in 5 row first
                if (rowNum++ < 4) {
                    continue;
                }
                //Not get value when data in row is empty
                if (ExcelUtils.isRowEmpty(row)) continue;
                StudentDto.UploadFileRegisterStudentInfo pos = new StudentDto.UploadFileRegisterStudentInfo();
                pos.setNumberIndex(ExcelUtils.getCellValue(row.getCell(1)));
                pos.setEmail(ExcelUtils.getCellValue(row.getCell(2)));
                pos.setPhone(ExcelUtils.getCellValue(row.getCell(3)));
                pos.setFullName(ExcelUtils.getCellValue(row.getCell(4)));
                pos.setIdentityCard(ExcelUtils.getCellValue(row.getCell(5)));
                pos.setAddress(ExcelUtils.getCellValue(row.getCell(6)));
                pos.setMajorName(ExcelUtils.getCellValue(row.getCell(7)));
                pos.setTotalLessonDebtStringValue(ExcelUtils.getCellValue(row.getCell(8)));
                pos.setStatusLessonDebt(ExcelUtils.getCellValue(row.getCell(9)));
                pos.setNote(ExcelUtils.getCellValue(row.getCell(10)));
                logger.info("Object java:"+CommonUtil.getJsonStringFromObject(pos));
                batchList.add(pos);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("End build list user from excel");
        return batchList;
    }

    private List<StudentDto.UploadFileRegisterStudentInfo> validateFileExcel(
            List<StudentDto.UploadFileRegisterStudentInfo> excelData, String language) {
        logger.info("Start validateFileExcel");
        if (excelData == null || excelData.isEmpty()) {
            return excelData;
        }
        //Check list account have any exist in database or not
        List<String> listUserNameRegister = excelData.stream().map(item -> item.getFullName())
                .collect(Collectors.toList());
        List<User> listUserExist = userRepository.getListUsersActiveByUserName(listUserNameRegister);
        if(null != listUserExist && listUserExist.size() > 0){
            List<String> errorsDuplicate = new ArrayList<>();
            errorsDuplicate.add("Trùng dữ liệu thông tin tài khoản");
            //Ficessage error
            for(int i=0;i< listUserExist.size();i++){
                int valueIndex = i;
                excelData.stream().filter(str -> str.getFullName().equals(listUserExist.get(valueIndex).getUsername()))
                        .forEach(str -> str.setErrors(errorsDuplicate));
            }
            return excelData;
        }
        for(int i=0;i< excelData.size();i++) {
            List<String> errors = new ArrayList<>();
            Integer valueTotalLessonDebt = 0;
            // ===== Total lesson debt=====
            boolean validTotalLessonDebt = matches(excelData.get(i).getTotalLessonDebtStringValue(), "^[0-9]*$",
                    ExcelUtils.UserExcelCode.ADMISSION_PERIOD_MAP_INVALID_FORMAT, errors);
            if(validTotalLessonDebt){
                valueTotalLessonDebt = Integer.valueOf(excelData.get(i).getTotalLessonDebtStringValue());
                excelData.get(i).setTotalLessonDebt(valueTotalLessonDebt);
            }
            // ===== Status lesson debt=====
            String valueStatusDebtUpperCase = excelData.get(i).getStatusLessonDebt().toUpperCase();
            boolean validStatusTotalLessonDebt = matches(valueStatusDebtUpperCase, "^[YN]*$",
                    ExcelUtils.UserExcelCode.ADMISSION_PERIOD_MAP_INVALID_FORMAT, errors);
            excelData.get(i).setErrors(errors);
            if(validStatusTotalLessonDebt){
                //If value status lesson debt equal Y or N => remain value otherwise default is N
                String valueStatusLessonDebt = (CommonUtil.YES_VALUE.equals(valueStatusDebtUpperCase) ||CommonUtil.NO_VALUE.equals(valueStatusDebtUpperCase))
                        ? valueStatusDebtUpperCase : CommonUtil.NO_VALUE;
                excelData.get(i).setStatusLessonDebt(valueStatusLessonDebt);
            }
        };
        return excelData;
    }

    public void addErrorMessage(ExcelUtils.UserExcelCode code, List<String> errors) {
        BasicResponseDto dto = responseService.getFailResult(code.toString());
        if (dto != null && StringUtils.isNotBlank(dto.getResponseMsg())) {
            errors.add(dto.getResponseMsg());
        }
    }

    private boolean matches(String value, String regex, ExcelUtils.UserExcelCode errorCode, List<String> errors) {
        if (value != null && !value.matches(regex)) {
            addErrorMessage(errorCode, errors);
            return false;
        }
        return true;
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void runSendEmailAccountLoginForStudent() {
        try {
            logger.info("Running daily job at 7 AM");
            //Send mail announcement register new user
            List<User> listAccountSendMailFail  = userRepository.getListAccountNotSendMailBefore(CommonUtil.STATUS_USE, CommonUtil.NO_VALUE);
            if(ObjectUtils.isEmpty(listAccountSendMailFail)){
                logger.info("Don't have any student not have account to login user site");
                return;
            }
            String defaultPassword = CommonUtil.DEFAULT_PASSWORD;
            Role userRole = roleService.getRoleByName(ERole.ROLE_USER);
            Long roleStudentId = userRole.getId();
            logger.info("value role:"+roleStudentId);
            List<Long> listIdSendSuccess = new ArrayList<>();
            for(int i = 0; i < listAccountSendMailFail.size(); i++){
                Map<String, String> params = new HashMap<>();
                params.put("userNm", listAccountSendMailFail.get(i).getUsername());
                params.put("email", listAccountSendMailFail.get(i).getEmail());
                params.put("password", defaultPassword);
                params.put("urlLogin", urlLogin);
                boolean sendSuccess = mailService.sendEmailByTemplate(params, EmailTemplate.REG_NEW.getName(), EmailTemplate.REG_NEW.getSubject());
                if (sendSuccess) {
                    logger.info("Send mail success to email:"+listAccountSendMailFail.get(i).getEmail());
                    listIdSendSuccess.add(listAccountSendMailFail.get(i).getId());
                }else{
                    logger.info("EmailSendFail to mail:"+listAccountSendMailFail.get(i).getEmail());
                }
            }
            //Update all status at mail send success
            if(listIdSendSuccess.size() > 0){
                userRepository.updateListAccountStatusSendMailSuccess(CommonUtil.YES_VALUE, listIdSendSuccess);
                logger.info("update list account success send mail");
            }
        } catch (Exception ex) {
            logger.error("Error in scheduled task: {}", ex.getMessage());
        }
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void runInsertAccountLoginForStudent() {
        try {
            logger.info("Running daily job at 2 AM");
            //Insert list account login for student
            List<Student> getListStudentDontHaveAccountLogin  = studentRepository.getAllStudentActiveDontHaveAccountLogin(CommonUtil.STATUS_USE, CommonUtil.NO_VALUE);
            if(ObjectUtils.isEmpty(getListStudentDontHaveAccountLogin)){
                logger.info("Don't have any student not have account to login user site");
                return;
            }
            String defaultPasswordEncrypt = encoder.encode(CommonUtil.DEFAULT_PASSWORD);
            Role userRole = roleService.getRoleByName(ERole.ROLE_USER);
            Long roleStudentId = userRole.getId();
            logger.info("value role:"+roleStudentId);
            nativeSqlInsertStrategy.bulkInsertUser(getListStudentDontHaveAccountLogin, roleStudentId, defaultPasswordEncrypt);
            //Update list student have account
            List<Long> listStudentId = getListStudentDontHaveAccountLogin.stream().map(student -> student.getId())
                    .collect(Collectors.toList());
            studentRepository.updateListAccountStatusSendMailSuccess(CommonUtil.YES_VALUE, listStudentId);
            logger.info("update status success:");
        } catch (Exception ex) {
            logger.error("Error in scheduled task: {}", ex.getMessage());
        }
    }


}
