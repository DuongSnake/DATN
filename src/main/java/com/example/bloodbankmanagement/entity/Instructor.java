package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.InstructorDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "instructor")
@AllArgsConstructor
public class Instructor extends EntityCommon {
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;
    private String phone;
    private String fullName;

    public static InstructorDto.InstructorSelectInfoResponse convertToDto(Instructor request){
        InstructorDto.InstructorSelectInfoResponse objectDtoResponse = new InstructorDto.InstructorSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setInstructorId(request.getId());
            objectDtoResponse.setEmail(request.getEmail());
            objectDtoResponse.setPhone(request.getPhone());
            objectDtoResponse.setFullName(request.getFullName());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setUpdateAt(request.getUpdateAt());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<InstructorDto.InstructorListInfo> convertListObjectToDto(List<Instructor> listRequestInstructor, Long totalRecord){
        PageAmtListResponseDto<InstructorDto.InstructorListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<InstructorDto.InstructorListInfo> listInstructorDto = new ArrayList<InstructorDto.InstructorListInfo>();
        if(listRequestInstructor.size() > 0){
            for (int i=0; i<listRequestInstructor.size(); i++){
                InstructorDto.InstructorListInfo newObject = new InstructorDto.InstructorListInfo();
                newObject.setInstructorId(listRequestInstructor.get(i).getId());
                newObject.setEmail(listRequestInstructor.get(i).getEmail());
                newObject.setPhone(listRequestInstructor.get(i).getPhone());
                newObject.setFullName(listRequestInstructor.get(i).getFullName());
                newObject.setStatus(listRequestInstructor.get(i).getStatus());
                newObject.setCreateAt(listRequestInstructor.get(i).getCreateAt());
                listInstructorDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listInstructorDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }
}
