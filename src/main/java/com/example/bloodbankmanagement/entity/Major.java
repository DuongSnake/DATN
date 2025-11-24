package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.MajorDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "major")//Chuyen nganh
public class Major extends EntityCommon {
    private String majorName;
    private Long departmentId;

    public Major(Long id, LocalDate createAt, LocalDate updateAt, String status, String createUser, String updateUser) {
        super(id, createAt, updateAt, status, createUser, updateUser);
    }

    public static MajorDto.MajorSelectInfoResponse convertToDto(Major request){
        MajorDto.MajorSelectInfoResponse objectDtoResponse = new MajorDto.MajorSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setMajorId(request.getId());
            objectDtoResponse.setMajorName(request.getMajorName());
            objectDtoResponse.setDepartmentId(request.getDepartmentId());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<MajorDto.MajorListInfo> convertListObjectToDto(List<Major> listRequestUser){
        PageAmtListResponseDto<MajorDto.MajorListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<MajorDto.MajorListInfo> listMajorDto = new ArrayList<MajorDto.MajorListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                MajorDto.MajorListInfo newObject = new MajorDto.MajorListInfo();
                newObject.setMajorId(listRequestUser.get(i).getId());
                newObject.setMajorName(listRequestUser.get(i).getMajorName());
                newObject.setDepartmentId(listRequestUser.get(i).getDepartmentId());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                listMajorDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listMajorDto);
        return objectDtoResponse;
    }
}
