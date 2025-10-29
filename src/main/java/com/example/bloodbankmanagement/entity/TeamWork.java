package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.TeamWorkDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "team_work")
@AllArgsConstructor
public class TeamWork extends EntityCommon {
    private String teamWorkName;
    private Integer categoryTeam;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructorInfo;//teacher coaching for student

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;

    public static TeamWorkDto.TeamWorkSelectInfoResponse convertToDto(TeamWork request){
        TeamWorkDto.TeamWorkSelectInfoResponse objectDtoResponse = new TeamWorkDto.TeamWorkSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setTeamWorkId(request.getId());
            objectDtoResponse.setTeamWorkName(request.getTeamWorkName());
            if(null != request.getClassRoom()){
                objectDtoResponse.setClassRoomId(request.getClassRoom().getId());
                objectDtoResponse.setClassRoomName(request.getClassRoom().getClassName());
            }
            if(null != request.getInstructorInfo()){
                objectDtoResponse.setInstructorId(request.getInstructorInfo().getId());
                objectDtoResponse.setInstructorName(request.getInstructorInfo().getFullName());
            }
            objectDtoResponse.setCreateUser(request.getCreateUser());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCategoryTeam(request.getCategoryTeam());
            objectDtoResponse.setCreateAt(request.getCreateAt());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<TeamWorkDto.TeamWorkListInfo> convertListObjectToDto(List<TeamWork> listRequestUser){
        PageAmtListResponseDto<TeamWorkDto.TeamWorkListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<TeamWorkDto.TeamWorkListInfo> listTeamWorkDto = new ArrayList<TeamWorkDto.TeamWorkListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                TeamWorkDto.TeamWorkListInfo newObject = new TeamWorkDto.TeamWorkListInfo();
                newObject.setTeamWorkId(listRequestUser.get(i).getId());
                newObject.setTeamWorkName(listRequestUser.get(i).getTeamWorkName());
                if(null != listRequestUser.get(i).getClassRoom()){
                    newObject.setClassRoomId(listRequestUser.get(i).getClassRoom().getId());
                    newObject.setClassRoomName(listRequestUser.get(i).getClassRoom().getClassName());
                }
                if(null != listRequestUser.get(i).getInstructorInfo()){
                    newObject.setInstructorId(listRequestUser.get(i).getInstructorInfo().getId());
                    newObject.setInstructorName(listRequestUser.get(i).getInstructorInfo().getFullName());
                }
                newObject.setCreateUser(listRequestUser.get(i).getCreateUser());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setCategoryTeam(listRequestUser.get(i).getCategoryTeam());
                listTeamWorkDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listTeamWorkDto);
        return objectDtoResponse;
    }
}
