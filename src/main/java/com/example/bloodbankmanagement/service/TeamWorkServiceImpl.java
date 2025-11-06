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
import com.example.bloodbankmanagement.dto.service.TeamWorkDto;
import com.example.bloodbankmanagement.entity.ClassRoom;
import com.example.bloodbankmanagement.entity.TeamWork;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import com.example.bloodbankmanagement.repository.ClassRoomRepository;
import com.example.bloodbankmanagement.repository.TeamWorkRepository;
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
public class TeamWorkServiceImpl {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
    private final TeamWorkRepository teamWorkRepository;
    private final UserRepository userRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ResponseCommon responseService;

    @Transactional
    public BasicResponseDto insertTeamWork(TeamWorkDto.TeamWorkInsertInfo request, String lang) {
        BasicResponseDto result;
        String userIdCreate = CommonUtil.getUsernameByToken();
        String userIdRegister = checkExistUser(userIdCreate);
        TeamWork objectUpdate = new TeamWork();
        objectUpdate.setTeamWorkName(request.getTeamWorkName());
        //Find the instructor information
        User instructorInfo =  userRepository.getById(request.getInstructorId());
        if(null != instructorInfo || CommonUtil.STATUS_USE.equals(instructorInfo.getStatus())){
            objectUpdate.setInstructorInfo(instructorInfo);
        }else if(null == instructorInfo && (null != request.getInstructorId())){
            throw new CustomException("Not found information of instructor ", lang);
        }
        //Find the info classroom
        ClassRoom classRoomInfo = classRoomRepository.findByFileId(request.getClassRoomId());
        if(null != classRoomInfo || CommonUtil.STATUS_USE.equals(classRoomInfo.getStatus())){
            objectUpdate.setClassRoom(classRoomInfo);
        }else if(null == classRoomInfo && (null != request.getClassRoomId())){
            throw new CustomException("Not found information of classroom ", lang);
        }
        objectUpdate.setCreateUser(userIdRegister);
        objectUpdate.setCategoryTeam(request.getCategoryTeam());
        objectUpdate.setStatus(CommonUtil.STATUS_USE);
        objectUpdate.setCreateAt(LocalDate.now());
        teamWorkRepository.save(objectUpdate);
        result = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.insertSuccess);
        return result;
    }

    public SingleResponseDto<PageAmtListResponseDto<TeamWorkDto.TeamWorkListInfo>> selectListTeamWork(TeamWorkDto.TeamWorkSelectListInfo request){
        SingleResponseDto objectResponse = new SingleResponseDto();
        //Set value userId
        String userId = isUserHaveRoleAdmin(request.getCreateUser());
        request.setCreateUser(userId);
        PageAmtListResponseDto<TeamWorkDto.TeamWorkListInfo> pageAmtObject = new PageAmtListResponseDto<>();
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequestDto());
        //Select list file upload
        Page<TeamWork> listDataFileMetadata = teamWorkRepository.findListTeamWork(request, pageable);
        pageAmtObject = TeamWork.convertListObjectToDto(listDataFileMetadata.getContent());
        objectResponse = responseService.getSingleResponse(pageAmtObject, new String[]{responseService.getConstI18n(CommonUtil.userValue)}, CommonUtil.querySuccess);
        return objectResponse;
    }

    public SingleResponseDto<TeamWorkDto.TeamWorkSelectInfoResponse> selectTeamWork(TeamWorkDto.TeamWorkSelectInfo request, String lang){
        if(null == request || request.getTeamWorkId().equals("") || null == request.getTeamWorkId()){
            throw new CustomException("Not found value request param ", lang);
        }
        SingleResponseDto<TeamWorkDto.TeamWorkSelectInfoResponse> selectObject = new SingleResponseDto<>();
        TeamWork dataFileMetadata = teamWorkRepository.findByFileId(request.getTeamWorkId());
        TeamWorkDto.TeamWorkSelectInfoResponse objectResponse= new TeamWorkDto.TeamWorkSelectInfoResponse();
        if(null ==dataFileMetadata){
            objectResponse = null;
        }else{
            objectResponse = TeamWork.convertToDto(dataFileMetadata);
        }
        selectObject = responseService.getSingleResponseHandleMessage(objectResponse, CommonUtil.successValue, CommonUtil.querySuccess);
        return selectObject;
    }

    @Transactional
    public BasicResponseDto updateTeamWork(TeamWorkDto.TeamWorkUpdateInfo request, String lang){
        String userIdUpdate = CommonUtil.getUsernameByToken();
        BasicResponseDto messageResponse;
        if(null == request){
            throw new CustomException("the object send request not null ", lang);
        }
        TeamWork objectUpdate = new TeamWork();
        objectUpdate.setId(request.getTeamWorkId());
        objectUpdate.setTeamWorkName(request.getTeamWorkName());
        objectUpdate.setCategoryTeam(request.getCategoryTeam());
        //Find the instructor information
        User instructorInfo =  userRepository.getById(request.getInstructorId());
        if(null == instructorInfo || CommonUtil.STATUS_USE.equals(instructorInfo.getStatus())){
            throw new CustomException("Not found information of instructor ", lang);
        }else{
            objectUpdate.setInstructorInfo(instructorInfo);
        }
        //Find the info classroom
        ClassRoom classRoomInfo = classRoomRepository.findByFileId(request.getClassRoomId());
        if(null == classRoomInfo || CommonUtil.STATUS_USE.equals(classRoomInfo.getStatus())){
            throw new CustomException("Not found information of classroom ", lang);
        }else{
            objectUpdate.setClassRoom(classRoomInfo);
        }
        objectUpdate.setUpdateUser(userIdUpdate);
        objectUpdate.setUpdateAt(LocalDate.now());
        teamWorkRepository.updateTeamWork(objectUpdate);
        messageResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return messageResponse;
    }

    @Transactional
    public BasicResponseDto deleteTeamWork(TeamWorkDto.TeamWorkDeleteInfo request, String lang){
        BasicResponseDto objectResponse;
        if(null == request || null == request.getListTeamWorkId()){
            throw new CustomException("the object send request not null ", lang);
        }
        TeamWork objectDelete = new TeamWork();
        objectDelete.setStatus(CommonUtil.STATUS_EXPIRE);
        objectDelete.setUpdateAt(LocalDate.now());
        objectDelete.setUpdateUser(CommonUtil.getUsernameByToken());
        teamWorkRepository.deleteTeamWork(objectDelete, request.getListTeamWorkId());
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.deleteSuccess);
        return objectResponse;
    }

    public BasicResponseDto assignInstructor(TeamWorkDto.TeamWorkAssignInstructorInfo request, String lang){
        //Check request is null
        if(null == request || null == request.getListTeamWorkId()
        || request.getListTeamWorkId().size() == 0 || null == request.getInstructorId()){
            throw new CustomException("the object send request not null ", lang);
        }
        //Find the instructor information
        User instructorInfo =  userRepository.getById(request.getInstructorId());
        if(null == instructorInfo || null == instructorInfo.getStatus() || null == instructorInfo.getRoles()
        || !CommonUtil.STATUS_USE.equals(instructorInfo.getStatus())){
            throw new CustomException("Not found information of instructor ", lang);
        }else if(null != instructorInfo.getRoles()){
            //Check role account equal type instructor (like mentor for student)
            for(Role objectRole: instructorInfo.getRoles()){
                logger.info("Value role name: "+objectRole.getName() +" of userId: "+ instructorInfo.getUsername());
                if(!ERole.ROLE_MODERATOR.equals(objectRole.getName())){
                    throw new CustomException("User not have permission for this API." +
                            "Please choose another account have role instructor", lang);
                }
            }
        }
        //run query update
        teamWorkRepository.assignInstructor(request.getInstructorId(), request.getListTeamWorkId());
        BasicResponseDto objectResponse;
        objectResponse = responseService.getSuccessResultHaveValueMessage(CommonUtil.successValue, CommonUtil.updateSuccess);
        return objectResponse;
    }

    public String isUserHaveRoleAdmin(String userName){
        boolean isTypeAdmin = false;
        if(ObjectUtils.isEmpty(userName)){
            return "";
        }
        Optional<User> userInfo = userRepository.findByUsername(userName);
        if(ObjectUtils.isEmpty(userInfo.get()) || CommonUtil.STATUS_USE.equals(userInfo.get().getStatus())){
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
        if(ObjectUtils.isEmpty(userInfo.get()) || CommonUtil.STATUS_USE.equals(userInfo.get().getStatus())){
            throw new CustomException(CommonUtil.NOT_FOUND_DATA_USER, "en");
        }
        return userInfo.get().getUsername();
    }

}
