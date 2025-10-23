package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.ClassRoomDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "class_room")
public class ClassRoom  extends EntityCommon {
    private String className;
    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    public static ClassRoomDto.ClassRoomSelectInfoResponse convertToDto(ClassRoom request){
        ClassRoomDto.ClassRoomSelectInfoResponse objectDtoResponse = new ClassRoomDto.ClassRoomSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setClassId(request.getId());
            objectDtoResponse.setClassName(request.getClassName());
            objectDtoResponse.setMajorId(request.getMajor().getId());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<ClassRoomDto.ClassRoomListInfo> convertListObjectToDto(List<ClassRoom> listRequestUser){
        PageAmtListResponseDto<ClassRoomDto.ClassRoomListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<ClassRoomDto.ClassRoomListInfo> listClassRoomDto = new ArrayList<ClassRoomDto.ClassRoomListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                ClassRoomDto.ClassRoomListInfo newObject = new ClassRoomDto.ClassRoomListInfo();
                newObject.setClassId(listRequestUser.get(i).getId());
                newObject.setClassName(listRequestUser.get(i).getClassName());
                newObject.setMajorId(listRequestUser.get(i).getMajor().getId());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                listClassRoomDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listClassRoomDto);
        return objectDtoResponse;
    }
}
