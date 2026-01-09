package com.example.bloodbankmanagement.service.student;

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
public class StudentServiceImpl {
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

    private final static int MAX_RECORD = 303;
    @Transactional
    public BasicResponseDto updateStudent(@RequestBody StudentDto.StudentUpdateInfo request, @RequestHeader("lang") String lang){
        String userId = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        //Check exist before update
        checkExistStudent(request.getId(), lang);
        User objectUpdate = StudentDto.StudentUpdateInfo.convertToEntity(request,userId);
        //Check period
        AdmissionPeriod inforAdminPeriod = admissionPeriodRepository.findByFileId(request.getAdmissionPeriodId());
        if(null != inforAdminPeriod){
            objectUpdate.setPeriodTime(inforAdminPeriod);
        }
        //Check major
        Major majorInfo = majorRepository.findByFileId(request.getMajorId());
        if(null != majorInfo){
            objectUpdate.setMajorInfo(majorInfo);
        }
        objectUpdate.setUpdateAt(LocalDate.now());
        userRepository.updateStudent(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    public SingleResponseDto<PageAmtListResponseDto<StudentDto.StudentSelectListInfo>> selectListStudent(@RequestBody StudentDto.StudentSelectListRequest request) {
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
    public BasicResponseDto deleteStudent(@RequestBody StudentDto.StudentDeleteInfo request, @RequestHeader("lang") String lang) {
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
    public BasicResponseDto insertListStudent(@RequestBody StudentDto.StudentInsertInfo request, @RequestHeader("lang") String lang) {
        BasicResponseDto objectResponse;
        //Check user have exist or not
        User objectEnity = StudentDto.StudentInsertInfo.convertToEntity(request);
        //update data date time and userId
        objectEnity.setStatus(CommonUtil.STATUS_USE);
        //Set role
        Role rolesStudent = roleRepository.findByName(ERole.ROLE_USER).get();
        objectEnity.setRoleInfo(rolesStudent);
        //Check period
        AdmissionPeriod inforAdminPeriod = admissionPeriodRepository.findByFileId(request.getAdmissionPeriodId());
        if(null != inforAdminPeriod){
            objectEnity.setPeriodTime(inforAdminPeriod);
        }
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

    public SingleResponseDto<StudentDto.StudentSelectInfoResponse> selectStudent(@RequestBody StudentDto.StudentSelectInfo request, @RequestHeader("lang") String lang){
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

    @Transactional
    public BasicResponseDto uploadFileRegisterListStudent(StudentDto.UploadBatchFileRegisterStudentInfo request, @RequestHeader("lang") String lang){
        //Check list in file upload
        BasicResponseDto objectResponse = null;
        boolean statusInsertAll = true;
        List<StudentDto.UploadFileRegisterStudentInfo> listPosPayerFromExcel = buildListStudentFromExcel(request.getFileUploadContent(), lang);
        validateFileExcel(listPosPayerFromExcel, lang);
        //set default value success
        objectResponse=responseService.getSingleResponse(listPosPayerFromExcel);
        //Set value if have any problem when insert
        for (int i=0; i<listPosPayerFromExcel.size() && statusInsertAll;i++ ) {
            if (listPosPayerFromExcel.get(i).getErrors().size() > 0) {
                statusInsertAll = false;
                objectResponse=responseCommon.getSingleResponseHandleMessage(listPosPayerFromExcel, CommonUtil.failValue, "Noi dung message fail");
                break;
            }
        }
        //Set role
        Role rolesStudent = roleRepository.findByName(ERole.ROLE_USER).get();
        //check status before insert all
        if(statusInsertAll){
            //Insert list
            nativeSqlInsertStrategy.bulkInsertStudent(listPosPayerFromExcel, rolesStudent.getId());
        }
        return objectResponse;
    }
    public void checkExistStudent(long userId, String lang){
        User countExist = userRepository.getById(userId);
        if(ObjectUtils.isEmpty(countExist)){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, lang);
        }
    }

    public List<StudentDto.UploadFileRegisterStudentInfo> buildListStudentFromExcel(MultipartFile file, String language){
        logger.info("Start build list user from excel");
        List<StudentDto.UploadFileRegisterStudentInfo> batchList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            logger.info("Number of rows in excel: {}", numberOfRows);
            if (numberOfRows > MAX_RECORD) {
                throw new CustomException("EXCEED_MAX_RECORDS", language);
            }
            Iterator<Row> rowIterator = sheet.iterator();
            int rowNum = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //Not get value in 3 row first
                if (rowNum++ < 3) {
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
                pos.setMajorId(Long.valueOf(ExcelUtils.getCellValue(row.getCell(7))));
                pos.setAdmissionPeriodId(Long.valueOf(ExcelUtils.getCellValue(row.getCell(8))));
                pos.setRoleId(Long.valueOf(ExcelUtils.getCellValue(row.getCell(9))));
                pos.setNote(ExcelUtils.getCellValue(row.getCell(10)));
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
        Map<Long, AdmissionPeriod> admissionPeriodMap = admissionPeriodRepository.selectListAdmissionPeriodActiveInNowYear().stream()
                .collect(Collectors.toMap(AdmissionPeriod::getId, c -> c));
        Map<Long, Major> majorMap = majorRepository.getAllMajorActive().stream()
                .collect(Collectors.toMap(Major::getId, c -> c));
        excelData.forEach(item -> {
            List<String> errors = new ArrayList<>();
            // ADMISSION_PERIOD
            validateField(String.valueOf(item.getAdmissionPeriodId()), 20,
                    ExcelUtils.UserExcelCode.CUSTOMER_ID_NOT_BLANK,
                    ExcelUtils.UserExcelCode.CUSTOMER_ID_LENGTH,errors);
            if (ExcelUtils.isValid(String.valueOf(item.getAdmissionPeriodId()))) {
                matches(item.getFullName(), "^[a-zA-Z0-9]*$",
                        ExcelUtils.UserExcelCode.CUSTOMER_ID_INVALID_FORMAT, language, errors);
                if (!admissionPeriodMap.containsKey(item.getAdmissionPeriodId())) {
                    addErrorMessage(ExcelUtils.UserExcelCode.CUSTOMER_ID_NOT_FOUND, errors);
                }
            }
            // MAJOR
            validateField(String.valueOf(item.getMajorId()), 10,
                    ExcelUtils.UserExcelCode.PROVIDER_ID_NOT_BLANK,
                    ExcelUtils.UserExcelCode.PROVIDER_ID_LENGTH,errors);
            if (ExcelUtils.isValid(String.valueOf(item.getMajorId()))) {
                matches(String.valueOf(item.getMajorId()), "^[a-zA-Z0-9]*$",
                        ExcelUtils.UserExcelCode.PROVIDER_ID_INVALID_FORMAT, language, errors);
                if (!majorMap.containsKey(item.getMajorId())) {
                    addErrorMessage(ExcelUtils.UserExcelCode.PROVIDER_ID_NOT_FOUND, errors);
                }
            }
            item.setErrors(errors);
        });
        return excelData;
    }

    public void validateField(String value, int maxLength, ExcelUtils.UserExcelCode notBlankKey,
                              ExcelUtils.UserExcelCode lengthKey, List<String> errors) {
        if (StringUtils.isBlank(value)) {
            addErrorMessage(notBlankKey, errors);
        } else if (value.length() > maxLength) {
            addErrorMessage(lengthKey, errors);
        }
    }

    public void addErrorMessage(ExcelUtils.UserExcelCode code, List<String> errors) {
        BasicResponseDto dto = responseService.getFailResult(code.toString());
        if (dto != null && StringUtils.isNotBlank(dto.getResponseMsg())) {
            errors.add(dto.getResponseMsg());
        }
    }

    private boolean matches(String value, String regex, ExcelUtils.UserExcelCode errorCode,
                            String language, List<String> errors) {
        if (value != null && !value.matches(regex)) {
            addErrorMessage(errorCode, errors);
            return false;
        }
        return true;
    }
}
