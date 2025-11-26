package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.InstructorMapPeriodAssignmentDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "instructor_map_instructor")//thong tin thoi gian lam do an cua tung chuyen nganh map giao vien
public class InstructorMapPeriodAssignment extends EntityCommon {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructorInfo;//teacher coaching for student

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_assignment_id")
    private PeriodAssignment periodAssignmentInfo;//The time major was assign

    public static InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfoResponse convertToDto(InstructorMapPeriodAssignment request){
        InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfoResponse objectDtoResponse = new InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentSelectInfoResponse();
        if(request != null){
            Long instructorId = null != request.getInstructorInfo() ? request.getInstructorInfo().getId() : null;
            Long periodAssignmentId = null != request.getPeriodAssignmentInfo() ? request.getPeriodAssignmentInfo().getId() : null;
            String instructorName = null != request.getInstructorInfo() ? request.getInstructorInfo().getFullName() : null;
            String majorName = null != request.getPeriodAssignmentInfo() ? request.getPeriodAssignmentInfo().getMajorInfo().getMajorName() : null;
            objectDtoResponse.setInstructorMapPeriodAssignmentId(request.getId());
            objectDtoResponse.setInstructorId(instructorId);
            objectDtoResponse.setInstructorName(instructorName);
            objectDtoResponse.setPeriodAssignmentId(periodAssignmentId);
            objectDtoResponse.setMajorName(majorName);
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setCreateUser(request.getCreateUser());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentListInfo> convertListObjectToDto(List<InstructorMapPeriodAssignment> listRequestUser, Long totalRecord){
        PageAmtListResponseDto<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentListInfo> listInstructorMapPeriodAssignmentDto = new ArrayList<InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                Long instructorId = null != listRequestUser.get(i).getInstructorInfo() ? listRequestUser.get(i).getInstructorInfo().getId() : null;
                Long periodAssignmentId = null != listRequestUser.get(i).getPeriodAssignmentInfo() ? listRequestUser.get(i).getPeriodAssignmentInfo().getId() : null;
                String instructorName = null != listRequestUser.get(i).getInstructorInfo() ? listRequestUser.get(i).getInstructorInfo().getFullName() : null;
                String majorName = null != listRequestUser.get(i).getPeriodAssignmentInfo() ? listRequestUser.get(i).getPeriodAssignmentInfo().getMajorInfo().getMajorName() : null;
                InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentListInfo newObject = new InstructorMapPeriodAssignmentDto.InstructorMapPeriodAssignmentListInfo();
                newObject.setInstructorMapPeriodAssignmentId(listRequestUser.get(i).getId());
                newObject.setInstructorId(instructorId);
                newObject.setInstructorName(instructorName);
                newObject.setPeriodAssignmentId(periodAssignmentId);
                newObject.setMajorName(majorName);
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setCreateUser(listRequestUser.get(i).getCreateUser());
                listInstructorMapPeriodAssignmentDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listInstructorMapPeriodAssignmentDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }


}
