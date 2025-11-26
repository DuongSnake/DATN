package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "score_assignment")
public class ScoreAssignment extends EntityCommon {
    private Double scoreAverage;
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_register_info_id")
    private AssignmentStudentRegister assignmentRegisterInfo;

    public static ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse convertToDto(ScoreAssignment request){
        ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse objectDtoResponse = new ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse();
        if(request != null){
            Long assignmentRegisterId = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getId() : null;
            String assignmentRegisterName = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getAssignmentName() : null;
            objectDtoResponse.setScoreAssignmentId(request.getId());
            objectDtoResponse.setScoreAverage(request.getScoreAverage());
            objectDtoResponse.setAssignmentRegisterId(assignmentRegisterId);
            objectDtoResponse.setAssignmentRegisterName(assignmentRegisterName);
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo> convertListObjectToDto(List<ScoreAssignment> listRequestUser, Long totalRecord){
        PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<ScoreAssignmentDto.ScoreAssignmentListInfo> listScoreAssignmentDto = new ArrayList<ScoreAssignmentDto.ScoreAssignmentListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                Long assignmentRegisterId = null != listRequestUser.get(i).getAssignmentRegisterInfo() ? listRequestUser.get(i).getAssignmentRegisterInfo().getId() : null;
                String assignmentRegisterName = null != listRequestUser.get(i).getAssignmentRegisterInfo() ? listRequestUser.get(i).getAssignmentRegisterInfo().getAssignmentName() : null;
                ScoreAssignmentDto.ScoreAssignmentListInfo newObject = new ScoreAssignmentDto.ScoreAssignmentListInfo();
                newObject.setScoreAssignmentId(listRequestUser.get(i).getId());
                newObject.setScoreAverage(listRequestUser.get(i).getScoreAverage());
                newObject.setAssignmentRegisterId(assignmentRegisterId);
                newObject.setAssignmentRegisterName(assignmentRegisterName);
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                listScoreAssignmentDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listScoreAssignmentDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }
}
