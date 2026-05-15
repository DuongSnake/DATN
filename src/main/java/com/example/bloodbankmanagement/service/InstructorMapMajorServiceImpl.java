package com.example.bloodbankmanagement.service;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.ERole;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.InstructorMapMajorDto;
import com.example.bloodbankmanagement.entity.Major;
import com.example.bloodbankmanagement.entity.InstructorMapMajor;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.InstructorMapMajorRepository;
import com.example.bloodbankmanagement.repository.MajorRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstructorMapMajorServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final InstructorMapMajorRepository instructorMapMajorRepository;
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertInstructorMapMajor(InstructorMapMajorDto.InstructorMapMajorInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdRegister = getValueUserIdRegister();
        InstructorMapMajor objectUpdate = new InstructorMapMajor();
        //Find the value information of instructor
        User instructorInfo = getInfoInstructorById(request.getInstructorId());
        //Find the value major
        Optional<Major> majorOptional = majorRepository.findById(request.getMajorId());
        if(!majorOptional.isPresent()){
            throw new CustomException(CommonUtil.NOT_ACCEPT_EMPTY_VALUE, "en");
        }
        objectUpdate.setInstructorInfo(instructorInfo);
        objectUpdate.setMajorInfo(majorOptional.get());
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(LocalDate.now());
        instructorMapMajorRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<InstructorMapMajorDto.InstructorMapMajorListInfo>> selectListInstructorMapMajor(InstructorMapMajorDto.InstructorMapMajorSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<InstructorMapMajorDto.InstructorMapMajorListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<InstructorMapMajor> listDataFileMetadata = instructorMapMajorRepository.findListInstructorMapMajor(request, pageable);
        pageAmtObject = InstructorMapMajor.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<InstructorMapMajorDto.InstructorMapMajorSelectInfoResponse> selectInstructorMapMajor(InstructorMapMajorDto.InstructorMapMajorSelectInfo request, String lang){
        if(null == request || request.getInstructorMapMajorId().equals("") || null == request.getInstructorMapMajorId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<InstructorMapMajorDto.InstructorMapMajorSelectInfoResponse> selectObject = new SingleResponseDto<>();
        InstructorMapMajor dataFileMetadata = instructorMapMajorRepository.findByInstructorMapMajorId(request.getInstructorMapMajorId());
        InstructorMapMajorDto.InstructorMapMajorSelectInfoResponse objectResponse= new InstructorMapMajorDto.InstructorMapMajorSelectInfoResponse();
        if(null == dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = InstructorMapMajor.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateInstructorMapMajor(InstructorMapMajorDto.InstructorMapMajorUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        //Find the value information of instructor
        User instructorInfo = getInfoInstructorById(request.getInstructorId());
        //Find the value major
        Optional<Major> majorOptional = majorRepository.findById(request.getMajorId());
        if(!majorOptional.isPresent()){
            throw new CustomException(CommonUtil.NOT_ACCEPT_EMPTY_VALUE, "en");
        }
        InstructorMapMajor objectUpdate = new InstructorMapMajor();
        objectUpdate.setId(request.getInstructorMapMajorId());
        objectUpdate.setInstructorInfo(instructorInfo);
        objectUpdate.setMajorInfo(majorOptional.get());
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        instructorMapMajorRepository.updateInstructorMapMajor(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteInstructorMapMajor(InstructorMapMajorDto.InstructorMapMajorDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        InstructorMapMajor objectDelete = new InstructorMapMajor();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        instructorMapMajorRepository.deleteInstructorMapMajor(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    public String checkExistUser(String userName){
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(ObjectUtils.isEmpty(userInfo.get())){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo.get().getUsername();
    }

    public User getInfoInstructorById(Long userid){
        User userInfo = userRepository.getValueUserByIdAndRole(userid, ERole.ROLE_INSTRUCTOR.toString());
        if(ObjectUtils.isEmpty(userInfo)){
            logger.info("Not found instructor info with userId: "+userid);
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo;
    }

    public String getValueUserIdRegister(){
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        return userIdRegister;
    }
}
