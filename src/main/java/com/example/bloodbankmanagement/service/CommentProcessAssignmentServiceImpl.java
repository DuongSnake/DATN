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
import com.example.bloodbankmanagement.dto.service.CommentProcessAssignmentDto;
import com.example.bloodbankmanagement.entity.CommentProcessAssignment;
import com.example.bloodbankmanagement.entity.FileUpload;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.CommentProcessAssignmentRepository;
import com.example.bloodbankmanagement.repository.FileMetadataRepository;
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
public class CommentProcessAssignmentServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final CommentProcessAssignmentRepository commentProcessAssignmentRepository;
    private final UserRepository userRepository;
    private final FileMetadataRepository fileMetadataRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertCommentProcessAssignment(CommentProcessAssignmentDto.CommentProcessAssignmentInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        CommentProcessAssignment objectUpdate = new CommentProcessAssignment();
        //check exist file upload
        FileUpload fileUploadInfo = fileMetadataRepository.findByFileId(request.getFileUploadId());
        if(null == fileUploadInfo){
            throw new CustomException("Not found value request param ", "en");
        }
        objectUpdate.setFileUploadInfo(fileUploadInfo);
        objectUpdate.setNote(request.getNote());
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(LocalDate.now());
        commentProcessAssignmentRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<CommentProcessAssignmentDto.CommentProcessAssignmentListInfo>> selectListCommentProcessAssignment(CommentProcessAssignmentDto.CommentProcessAssignmentSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        PageAmtListResponseDto<CommentProcessAssignmentDto.CommentProcessAssignmentListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        request.getPageRequestDto().setPageNum(PageRequestDto.reduceValuePage(request.getPageRequestDto().getPageNum()));
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<CommentProcessAssignment> listDataFileMetadata = commentProcessAssignmentRepository.findListCommentProcessAssignment(request, pageable);
        pageAmtObject = CommentProcessAssignment.convertListObjectToDto(listDataFileMetadata.getContent(), listDataFileMetadata.getTotalElements());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse> selectCommentProcessAssignment(CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfo request, String lang){
        if(null == request || request.getCommentProcessAssignmentId().equals("") || null == request.getCommentProcessAssignmentId()){
            throw new CustomException("Not found value request param ", "en");
        }
        SingleResponseDto<CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse> selectObject = new SingleResponseDto<>();
        CommentProcessAssignment dataFileMetadata = commentProcessAssignmentRepository.findByFileId(request.getCommentProcessAssignmentId());
        CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse objectResponse= new CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = CommentProcessAssignment.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateCommentProcessAssignment(CommentProcessAssignmentDto.CommentProcessAssignmentUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request){
            throw new CustomException("the object send request not null ", "en");
        }
        //check exist file upload
        FileUpload fileUploadInfo = fileMetadataRepository.findByFileId(request.getFileUploadId());
        if(null == fileUploadInfo){
            throw new CustomException("Not found value request param ", "en");
        }
        CommentProcessAssignment objectUpdate = new CommentProcessAssignment();
        objectUpdate.setId(request.getCommentProcessAssignmentId());
        objectUpdate.setFileUploadInfo(fileUploadInfo);
        objectUpdate.setNote(request.getNote());
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        commentProcessAssignmentRepository.updateCommentProcessAssignment(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteCommentProcessAssignment(CommentProcessAssignmentDto.CommentProcessAssignmentDeleteInfo listFileId, String lang){
        BasicResponseDto objectResponse;
        CommentProcessAssignment objectDelete = new CommentProcessAssignment();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        commentProcessAssignmentRepository.deleteCommentProcessAssignment(objectDelete, listFileId.getListData());
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
