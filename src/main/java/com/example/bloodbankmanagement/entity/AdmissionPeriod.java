package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.AdmissionPeriodDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "admission_period")
public class AdmissionPeriod extends EntityCommon {
    private String admissionPeriodName;
    private LocalDate startPeriod;
    private LocalDate endPeriod;

    public static AdmissionPeriodDto.AdmissionPeriodSelectInfoResponse convertToDto(AdmissionPeriod request){
        AdmissionPeriodDto.AdmissionPeriodSelectInfoResponse objectDtoResponse = new AdmissionPeriodDto.AdmissionPeriodSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setAdmissionPeriodId(request.getId());
            objectDtoResponse.setAdmissionPeriodName(request.getAdmissionPeriodName());
            objectDtoResponse.setCreateUser(request.getCreateUser());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setStartPeriod(request.getStartPeriod());
            objectDtoResponse.setEndPeriod(request.getEndPeriod());
            objectDtoResponse.setCreateAt(request.getCreateAt());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<AdmissionPeriodDto.AdmissionPeriodListInfo> convertListObjectToDto(List<AdmissionPeriod> listRequestUser){
        PageAmtListResponseDto<AdmissionPeriodDto.AdmissionPeriodListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<AdmissionPeriodDto.AdmissionPeriodListInfo> listAdmissionPeriodDto = new ArrayList<AdmissionPeriodDto.AdmissionPeriodListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                AdmissionPeriodDto.AdmissionPeriodListInfo newObject = new AdmissionPeriodDto.AdmissionPeriodListInfo();
                newObject.setAdmissionPeriodId(listRequestUser.get(i).getId());
                newObject.setAdmissionPeriodName(listRequestUser.get(i).getAdmissionPeriodName());
                newObject.setCreateUser(listRequestUser.get(i).getCreateUser());
                newObject.setStartPeriod(listRequestUser.get(i).getStartPeriod());
                newObject.setEndPeriod(listRequestUser.get(i).getEndPeriod());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                listAdmissionPeriodDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listAdmissionPeriodDto);
        return objectDtoResponse;
    }
}
