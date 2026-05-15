package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.InstructorMapMajorDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "instructor_map_major")
public class InstructorMapMajor extends EntityCommon {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructorInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major majorInfo;

    public static InstructorMapMajorDto.InstructorMapMajorSelectInfoResponse convertToDto(InstructorMapMajor request){
        InstructorMapMajorDto.InstructorMapMajorSelectInfoResponse objectDtoResponse = new InstructorMapMajorDto.InstructorMapMajorSelectInfoResponse();
        if(request != null){
            Long instructorId = null != request.getInstructorInfo() ? request.getInstructorInfo().getId() : null;
            Long majorId = null != request.getMajorInfo() ? request.getMajorInfo().getId() : null;
            String instructorName = null != request.getInstructorInfo() ? request.getInstructorInfo().getFullName() : null;
            String majorName = null != request.getMajorInfo() ? request.getMajorInfo().getMajorName() : null;
            objectDtoResponse.setInstructorMapMajorId(request.getId());
            objectDtoResponse.setInstructorId(instructorId);
            objectDtoResponse.setInstructorName(instructorName);
            objectDtoResponse.setMajorId(majorId);
            objectDtoResponse.setMajorName(majorName);
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setCreateUser(request.getCreateUser());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<InstructorMapMajorDto.InstructorMapMajorListInfo> convertListObjectToDto(List<InstructorMapMajor> listRequestUser, Long totalRecord){
        PageAmtListResponseDto<InstructorMapMajorDto.InstructorMapMajorListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<InstructorMapMajorDto.InstructorMapMajorListInfo> listDto = new ArrayList<>();
        if(listRequestUser.size() > 0 ){
            for (InstructorMapMajor item : listRequestUser){
                Long instructorId = null != item.getInstructorInfo() ? item.getInstructorInfo().getId() : null;
                Long majorId = null != item.getMajorInfo() ? item.getMajorInfo().getId() : null;
                String instructorName = null != item.getInstructorInfo() ? item.getInstructorInfo().getFullName() : null;
                String majorName = null != item.getMajorInfo() ? item.getMajorInfo().getMajorName() : null;
                InstructorMapMajorDto.InstructorMapMajorListInfo newObject = new InstructorMapMajorDto.InstructorMapMajorListInfo();
                newObject.setInstructorMapMajorId(item.getId());
                newObject.setInstructorId(instructorId);
                newObject.setInstructorName(instructorName);
                newObject.setMajorId(majorId);
                newObject.setMajorName(majorName);
                newObject.setStatus(item.getStatus());
                newObject.setCreateAt(item.getCreateAt());
                newObject.setCreateUser(item.getCreateUser());
                listDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }
}
