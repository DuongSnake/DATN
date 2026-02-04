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
import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.entity.AdmissionPeriod;
import com.example.bloodbankmanagement.entity.Major;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.entity.Role;
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
public class UserServiceImpl {
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
    public BasicResponseDto insertUser(@RequestBody UserDto.UserInsertInfo request, @RequestHeader("lang") String lang){
        BasicResponseDto result;
        checkBeforeInsertUser(request.getUsername(), lang);
        //Convert dto to entity
        User objectInsert = UserDto.UserInsertInfo.convertToEntity(request);
        //Set value default when create new user (0:not paid, 1:paid success)
        String defaultPassword = "ktx2024";
        objectInsert.setPassword(encoder.encode(defaultPassword));
        objectInsert.setStatus(CommonUtil.STATUS_USE);
        objectInsert.setCreateAt(LocalDate.now());
        objectInsert.setCreateUser(CommonUtil.getUsernameByToken());
        //Set role
        Set<Role> roles = roleService.getRole(request.getRoles());
        Role userRole = roles.stream().findFirst().get();//Get first role
        objectInsert.setRoleInfo(userRole);
        userRepository.save(objectInsert);
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
        //convert object response
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    @Transactional
    public BasicResponseDto updateUser(@RequestBody UserDto.UserUpdateInfo request, @RequestHeader("lang") String lang){
        String userId = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        //Check exist before update
        checkExistUser(request.getId(), lang);
        User objectUpdate = UserDto.UserUpdateInfo.convertToEntity(request,userId);
        objectUpdate.setUpdateAt(LocalDate.now());
        userRepository.updateUser(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    public SingleResponseDto<PageAmtListResponseDto<UserDto.UserSelectListInfo>> selectListUser(@RequestBody UserDto.UserSelectListRequest request) {
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Set value page
        PageAmtListResponseDto<UserDto.UserSelectListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        Page<User> listDataUser = userRepository.findListUsers(request,pageable);
        //Select list role
        pageAmtObject = User.convertListObjectToDto(listDataUser.getContent());
        this.getRoleByUserId(pageAmtObject, listDataUser.getContent());
        pageAmtObject.setTotalRecord((int) listDataUser.getTotalElements());
        objectResponse = responseCommon.getSingleResponse(pageAmtObject, new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto deleteUser(@RequestBody UserDto.UserDeleteInfo request, @RequestHeader("lang") String lang) {
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
    public BasicResponseDto insertListStudent(@RequestBody UserDto.StudentInsertInfo request, @RequestHeader("lang") String lang) {
        BasicResponseDto objectResponse;
        //Check user have exist or not
        User objectEnity = UserDto.StudentInsertInfo.convertToEntity(request);
        //update data date time and userId
        objectEnity.setStatus(CommonUtil.STATUS_USE);
        //Set role
        Set<Role> roles = roleService.getRole(null);//If the
        Role userRole = roles.stream().findFirst().get();//Get first role
        objectEnity.setRoleInfo(userRole);
        //Check period
        AdmissionPeriod inforAdminPeriod = admissionPeriodRepository.findByFileId(request.getPeriodId());
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

    public SingleResponseDto<UserDto.UserSelectInfoResponse> selectUser(@RequestBody UserDto.UserSelectInfo request, @RequestHeader("lang") String lang){
        SingleResponseDto<UserDto.UserSelectInfoResponse> singleResponseDto;
        //Check user have exist or not
        User selectUser = userRepository.getById(Long.valueOf(request.getId()));
        if(ObjectUtils.isEmpty(selectUser)){
            return responseCommon.getSingleFailResult(CommonUtil.NOT_FOUND_DATA_USER, lang);
        }
        UserDto.UserSelectInfoResponse objectResponse = User.convertToDto(selectUser);
        singleResponseDto = responseService.getSingleResponse(objectResponse);
        return singleResponseDto;
    }

    public SingleResponseDto<PageAmtListResponseDto<Role>> selectListAllRole(){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<Role> pageAmtObject = new PageAmtListResponseDto<>();
        //Check user have exist or not
        List<Role> selectAllRole = roleRepository.getAllRole();
        if(ObjectUtils.isEmpty(selectAllRole)){
            return responseCommon.getSingleFailResult(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        pageAmtObject.setTotalRecord(selectAllRole.size());
        pageAmtObject.setData(selectAllRole);
        objectResponse = responseCommon.getSingleResponse(pageAmtObject, new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    @Transactional
    public SingleResponseDto<UserDto.ChangePasswordInfo> changePasswordUser(@RequestBody UserDto.ChangePasswordInfo changePasswordInfoRequest, @RequestHeader("lang") String lang){
        SingleResponseDto<UserDto.ChangePasswordInfo> objectResponse = new SingleResponseDto<>();
        String userId = CommonUtil.getUsernameByToken();
        //Check user have exist or not
        User updateUser = userRepository.getById(changePasswordInfoRequest.getId());
        if(ObjectUtils.isEmpty(updateUser)){
            return responseCommon.getSingleFailResult(CommonUtil.NOT_FOUND_DATA_USER, lang);
        }
        String newPasswordEncoded = passwordEncoder.encode(changePasswordInfoRequest.getNewPassword());
        if (!passwordEncoder.matches(changePasswordInfoRequest.getCurrentPassword(), updateUser.getPassword())) {
            return responseCommon.getSingleFailResult("CurrentPasswordNotMatch", lang);
        }
        if (passwordEncoder.matches(changePasswordInfoRequest.getNewPassword(), updateUser.getPassword())) {
            return responseCommon.getSingleFailResult("OldPasswordMatched", lang);
        }
        //Update data password
        updateUser.setPassword(newPasswordEncoded);
        updateUser.setUpdateUser(userId);
        updateUser.setUpdateAt(LocalDate.now());
        userRepository.changePassword(updateUser);
        objectResponse = responseCommon.getSingleResponse(changePasswordInfoRequest, new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.updateSuccess);
        return objectResponse;
    }

    @Transactional
    public SingleResponseDto<UserDto.ForgotPasswordInfo> resetPasswordUser(@RequestBody UserDto.ForgotPasswordInfo changePasswordInfoRequest, @RequestHeader("lang") String lang){
        SingleResponseDto<UserDto.ForgotPasswordInfo> objectResponse = new SingleResponseDto<>();
        //Check user have exist or not
        User updateUser = userRepository.getById(changePasswordInfoRequest.getId());
        if(ObjectUtils.isEmpty(updateUser)){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, lang);
        }
        String newPasswordEncoded = passwordEncoder.encode(changePasswordInfoRequest.getNewPassword());
        //Update data password
        updateUser.setPassword(newPasswordEncoded);
        updateUser.setUpdateUser(CommonUtil.getUsernameByToken());
        updateUser.setUpdateAt(LocalDate.now());
        userRepository.changePassword(updateUser);
        objectResponse = responseCommon.getSingleResponse(changePasswordInfoRequest, new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.updateSuccess);
        return objectResponse;
    }

    @Transactional
    public BasicResponseDto uploadFileRegisterListUser(UserDto.UploadBatchFileRegisterUserInfo request, @RequestHeader("lang") String lang){
        //Check list in file upload
        BasicResponseDto objectResponse = null;
        boolean statusInsertAll = true;
        List<UserDto.UploadFileRegisterUserInfo> listPosPayerFromExcel = buildListUserFromExcel(request.getFileUploadContent(), lang);
        validateFileExcel(listPosPayerFromExcel, lang);
        //set default value success
        objectResponse=responseService.getSingleResponse(listPosPayerFromExcel);
        //Set value if have any problem when insert
        for (UserDto.UploadFileRegisterUserInfo objectLoop:listPosPayerFromExcel) {
            if (null != objectLoop.getErrors() && objectLoop.getErrors().size() > 0) {
                statusInsertAll = false;
                objectResponse=responseCommon.getSingleResponseHandleMessage(listPosPayerFromExcel, CommonUtil.failValue, "Noi dung message fail");
                break;
            }
        }
        //check status before insert all
        if(statusInsertAll){
            //Insert list
            nativeSqlInsertStrategy.bulkInsert(listPosPayerFromExcel);
            //Handle send announcement to list email (asynchronous with @Async or a task executor)
            //Send mail announcement register new user
            logger.info("Vao insert thong tin");
            String defaultPassword = "ktx2024";
            for(int i = 0; i < listPosPayerFromExcel.size(); i++){
                Map<String, String> params = new HashMap<>();
                params.put("userNm", listPosPayerFromExcel.get(i).getFullName());
                params.put("email", listPosPayerFromExcel.get(i).getEmail());
                params.put("password", defaultPassword);
                params.put("urlLogin", urlLogin);
                boolean sendSuccess = mailService.sendEmailByTemplate(params, EmailTemplate.REG_NEW.getName(), EmailTemplate.REG_NEW.getSubject());
                if (!sendSuccess) {
                    return responseCommon.getSingleFailResult("EmailSendFail", lang);
                }
            }
        }
        return objectResponse;
    }

    public SingleResponseDto<UserDto.MailForgotPasswordInfo> sendMailResetPassword(@RequestBody UserDto.MailForgotPasswordInfo forgotPasswordInfoRequest, @RequestHeader("lang") String lang){
        SingleResponseDto<UserDto.MailForgotPasswordInfo> objectResponse = new SingleResponseDto<>();
        //Check email have exist or not
        List<User> updateUser = userRepository.getByEmail(forgotPasswordInfoRequest.getEmail());
        if(ObjectUtils.isEmpty(updateUser)){
            return responseCommon.getSingleFailResult("NotFoundDataEmail", lang);
        }
        //Send mail anoucement register new user
        Map<String, String> params = new HashMap<>();
        params.put("email", forgotPasswordInfoRequest.getEmail());
        String urlForgotPass = urlRegetPassword +"?id="+updateUser.get(0).getId()+"&userId="+updateUser.get(0).getUsername();
        params.put("urlForgotPass", urlForgotPass);
        params.put(CommonUtil.SendToSingleMailYes, CommonUtil.YES_VALUE);
        boolean sendSuccess = mailService.sendEmailByTemplate(params, EmailTemplate.RESET_PASSWORD.getName(), EmailTemplate.RESET_PASSWORD.getSubject());
        if (!sendSuccess) {
            return responseCommon.getSingleFailResult("EmailSendFail", lang);
        }
        objectResponse = responseCommon.getSingleResponse(forgotPasswordInfoRequest, new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.sendMAilSuccess);
        return objectResponse;
    }

    public SingleResponseDto<List<UserDto.UserSelectListInfo>> selectListStudent() {
        SingleResponseDto objectResponse = new SingleResponseDto();
        List<UserDto.UserSelectListInfo> pageAmtObject = new ArrayList<>();
        List<User> listDataUser = userRepository.getListUserByRoleName(ERole.ROLE_USER.toString());
        pageAmtObject = User.convertListObjectStudentOrInstructorToDto(listDataUser);
        objectResponse = responseCommon.getSingleResponse(pageAmtObject, new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<List<UserDto.UserSelectListInfo>> selectListInstructor() {
        SingleResponseDto objectResponse = new SingleResponseDto();
        List<UserDto.UserSelectListInfo> pageAmtObject = new ArrayList<>();
        List<User> listDataUser = userRepository.getListUserByRoleName(ERole.ROLE_MODERATOR.toString());
        pageAmtObject = User.convertListObjectStudentOrInstructorToDto(listDataUser);
        objectResponse = responseCommon.getSingleResponse(pageAmtObject, new String[]{responseCommon.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public void getRoleByUserId(PageAmtListResponseDto<UserDto.UserSelectListInfo> objectResponseData, List<User> listRequestUser){
        ArrayList<Long> listUserId = new ArrayList<>();
        for (User userData: listRequestUser){
            listUserId.add(userData.getId());
        }
        //Query get role by userId
        List<User> listUserRole = userRepository.findAllByIdIn(listUserId);
        //For
        for (User userData: listUserRole){
            for (UserDto.UserSelectListInfo userRequest: objectResponseData.getData()){
                if(userData.getId().equals(userRequest.getId())){
                    userRequest.setRoles(userData.getRoleInfo());
                }
            }
        }
    }

    public void checkBeforeInsertUser(String userName, String lang){
        Optional<User> countExist = userRepository.findByUsername(userName);
        if(!ObjectUtils.isEmpty(countExist)){
            throw new CustomException(CommonUtil.EXIST_DATA_IN_DB, lang);
        }
    }

    public void checkExistUser(long userId, String lang){
        User countExist = userRepository.getById(userId);
        if(ObjectUtils.isEmpty(countExist)){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, lang);
        }
    }

    public List<UserDto.UploadFileRegisterUserInfo> buildListUserFromExcel(MultipartFile file, String language){
        logger.info("Start build list user from excel");
        List<UserDto.UploadFileRegisterUserInfo> batchList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            int rowNum = 0;

            while (rowIterator.hasNext()) {
                List<String> listError = new ArrayList<>();
                Row row = rowIterator.next();
                //Not get value in 3 row first
                if (rowNum++ < 3) {
                    continue;
                }
                //Not get value when data in row is empty
                if (ExcelUtils.isRowEmpty(row)) continue;
                UserDto.UploadFileRegisterUserInfo pos = new UserDto.UploadFileRegisterUserInfo();
                pos.setNumberIndex(ExcelUtils.getCellValue(row.getCell(1)));
                pos.setEmail(ExcelUtils.getCellValue(row.getCell(2)));
                pos.setPhone(ExcelUtils.getCellValue(row.getCell(3)));
                pos.setFullName(ExcelUtils.getCellValue(row.getCell(4)));
                pos.setIdentityCard(ExcelUtils.getCellValue(row.getCell(5)));
                pos.setAddress(ExcelUtils.getCellValue(row.getCell(6)));
                //Handle value column convert to long
                Long valueColumnRoleId = (StringUtils.isEmpty(ExcelUtils.getCellValue(row.getCell(9))) ||
                        !matches(ExcelUtils.getCellValue(row.getCell(9)), "^[0-9]*$",
                                ExcelUtils.UserExcelCode.ADMISSION_PERIOD_MAP_INVALID_FORMAT, listError) ) ? null : Long.valueOf(ExcelUtils.getCellValue(row.getCell(9)));
                Long valueColumnMajorId = (StringUtils.isEmpty(ExcelUtils.getCellValue(row.getCell(7))) ||
                        !matches(ExcelUtils.getCellValue(row.getCell(7)), "^[0-9]*$",
                                ExcelUtils.UserExcelCode.ADMISSION_PERIOD_MAP_INVALID_FORMAT, listError) ) ? null : Long.valueOf(ExcelUtils.getCellValue(row.getCell(7)));
                Long valueColumnAdmissionPeriodId = (StringUtils.isEmpty(ExcelUtils.getCellValue(row.getCell(8))) ||
                        !matches(ExcelUtils.getCellValue(row.getCell(8)), "^[0-9]*$",
                                ExcelUtils.UserExcelCode.ADMISSION_PERIOD_MAP_INVALID_FORMAT, listError) )  ? null : Long.valueOf(ExcelUtils.getCellValue(row.getCell(8)));
                pos.setMajorId(valueColumnMajorId);
                pos.setAdmissionPeriodId(valueColumnAdmissionPeriodId);
                pos.setRoleId(valueColumnRoleId);
                pos.setNote(ExcelUtils.getCellValue(row.getCell(10)));
                batchList.add(pos);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("End build list user from excel");
        return batchList;
    }

    private List<UserDto.UploadFileRegisterUserInfo> validateFileExcel(
            List<UserDto.UploadFileRegisterUserInfo> excelData, String language) {
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
            //Find the object have value duplicate ->update value message error
            for(int i=0;i< listUserExist.size();i++){
                int valueIndex = i;
                excelData.stream().filter(str -> str.getFullName().equals(listUserExist.get(valueIndex).getUsername()))
                        .forEach(str -> str.setErrors(errorsDuplicate));
            }
             return excelData;
        }
        Map<Long, AdmissionPeriod> admissionPeriodMap = admissionPeriodRepository.selectListAdmissionPeriodActiveInNowYear().stream()
                .collect(Collectors.toMap(AdmissionPeriod::getId, c -> c));
        Map<Long, Major> majorMap = majorRepository.getAllMajorActive().stream()
                .collect(Collectors.toMap(Major::getId, c -> c));
        Map<Long, Role> roleMap = roleRepository.getAllRole().stream()
                .collect(Collectors.toMap(Role::getId, c -> c));
        excelData.forEach(item -> {
            List<String> errors = new ArrayList<>();
            //Check format value input full name
            matches(item.getFullName(), "^[a-zA-Z0-9]*$",
                    ExcelUtils.UserExcelCode.ADMISSION_PERIOD_MAP_INVALID_FORMAT, errors);
            // ===== ADMISSION_PERIOD =====
            validateField(String.valueOf(item.getAdmissionPeriodId()), 20,
                    ExcelUtils.UserExcelCode.ADMISSION_PERIOD_MAP_NOT_BLANK,
                    ExcelUtils.UserExcelCode.ADMISSION_PERIOD_MAP_LENGTH, errors);
            //Check value input exist in database or not
            if (!admissionPeriodMap.containsKey(item.getAdmissionPeriodId())) {
                addErrorMessage(ExcelUtils.UserExcelCode.ADMISSION_PERIOD_MAP_NOT_FOUND, errors);
            }
            // ===== MAJOR =====
            validateField(String.valueOf(item.getMajorId()), 10,
                    ExcelUtils.UserExcelCode.MAJOR_ID_NOT_BLANK,
                    ExcelUtils.UserExcelCode.MAJOR_ID_LENGTH, errors);
            //Check value input exist in database or not
            if (!majorMap.containsKey(item.getMajorId())) {
                addErrorMessage(ExcelUtils.UserExcelCode.MAJOR_ID_NOT_FOUND, errors);
            }
            // ===== ROLE =====
            validateField(String.valueOf(item.getRoleId()), 10,
                    ExcelUtils.UserExcelCode.ROLE_ID_NOT_BLANK,
                    ExcelUtils.UserExcelCode.ROLE_ID_LENGTH, errors);
            //Check value input exist in database or not
            if (!roleMap.containsKey(item.getRoleId())) {
                addErrorMessage(ExcelUtils.UserExcelCode.ROLE_ID_NOT_FOUND, errors);
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

    private boolean matches(String value, String regex, ExcelUtils.UserExcelCode errorCode, List<String> errors) {
        if (value != null && !value.matches(regex)) {
            addErrorMessage(errorCode, errors);
            return false;
        }
        return true;
    }
}
