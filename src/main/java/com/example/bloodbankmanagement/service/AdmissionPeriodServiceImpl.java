package com.example.bloodbankmanagement.service;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.ResponseCommon;
import com.example.bloodbankmanagement.common.exception.CustomException;
import com.example.bloodbankmanagement.common.security.AuthTokenFilter;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import com.example.bloodbankmanagement.dto.service.AdmissionPeriodDto;
import com.example.bloodbankmanagement.entity.AdmissionPeriod;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.AdmissionPeriodRepository;
import com.example.bloodbankmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdmissionPeriodServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final AdmissionPeriodRepository majorRepository;
    private final UserRepository userRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertAdmissionPeriod(AdmissionPeriodDto.AdmissionPeriodInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        AdmissionPeriod objectUpdate = new AdmissionPeriod();
        objectUpdate.setAdmissionPeriodName(request.getAdmissionPeriodName());
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(LocalDate.now());
        objectUpdate.setStartPeriod(request.getStartPeriod());
        objectUpdate.setEndPeriod(request.getEndPeriod());
        majorRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<AdmissionPeriodDto.AdmissionPeriodListInfo>> selectListAdmissionPeriod(AdmissionPeriodDto.AdmissionPeriodSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Set value userId
        String userId = isUserHaveRoleAdmin(request.getCreateUser());
        request.setCreateUser(userId);
        PageAmtListResponseDto<AdmissionPeriodDto.AdmissionPeriodListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<AdmissionPeriod> listDataFileMetadata = majorRepository.findListAdmissionPeriod(request, pageable);
        pageAmtObject = AdmissionPeriod.convertListObjectToDto(listDataFileMetadata.getContent());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<AdmissionPeriodDto.AdmissionPeriodSelectInfoResponse> selectAdmissionPeriod(AdmissionPeriodDto.AdmissionPeriodSelectInfo request, String lang){
        if(null == request || request.getAdmissionPeriodId().equals("") || null == request.getAdmissionPeriodId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<AdmissionPeriodDto.AdmissionPeriodSelectInfoResponse> selectObject = new SingleResponseDto<>();
        AdmissionPeriod dataFileMetadata = majorRepository.findByFileId(request.getAdmissionPeriodId());
        AdmissionPeriodDto.AdmissionPeriodSelectInfoResponse objectResponse= new AdmissionPeriodDto.AdmissionPeriodSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = AdmissionPeriod.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateAdmissionPeriod(AdmissionPeriodDto.AdmissionPeriodUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request){
            throw new CustomException("the object send request not null ", lang);
        }
        AdmissionPeriod objectUpdate = new AdmissionPeriod();
        objectUpdate.setId(request.getAdmissionPeriodId());
        objectUpdate.setStartPeriod(request.getStartPeriod());
        objectUpdate.setEndPeriod(request.getEndPeriod());
        objectUpdate.setAdmissionPeriodName(request.getAdmissionPeriodName());
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        majorRepository.updateAdmissionPeriod(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteAdmissionPeriod(AdmissionPeriodDto.AdmissionPeriodDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        AdmissionPeriod objectDelete = new AdmissionPeriod();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        majorRepository.deleteAdmissionPeriod(objectDelete, listFileId.getListData());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    public String isUserHaveRoleAdmin(String userName){
        boolean isTypeAdmin = false;
        if(ObjectUtils.isEmpty(userName)){
            return "";
        }
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(ObjectUtils.isEmpty(userInfo.get())){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        for(Role objectRole: userInfo.get().getRoles()){
            logger.info("Value role name: "+objectRole.getName() +" of userId: "+ userInfo.get().getUsername());
            if(CommonUtil.ROLE_ADMIN.equals(objectRole.getName().toString())){
                isTypeAdmin = true;
            }
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
