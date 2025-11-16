package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.PeriodAssignmentDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "period_assignment")//The time doing assignment of major
public class PeriodAssignment  extends EntityCommon {
    private LocalDate startPeriod;
    private LocalDate endPeriod;
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_period_id")
    private AdmissionPeriod admissionPeriodInfo;//thong tin ky han nop do an nam o ky nao trong nam(xuan,ha,thu,dong)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major majorInfo;//Ma chuyen nganh

    public static PeriodAssignmentDto.PeriodAssignmentSelectInfoResponse convertToDto(PeriodAssignment request){
        PeriodAssignmentDto.PeriodAssignmentSelectInfoResponse objectDtoResponse = new PeriodAssignmentDto.PeriodAssignmentSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setPeriodAssignmentId(request.getId());
            //Check constructor info
            if(null != request.getAdmissionPeriodInfo()){
                Long admissionPeriodId = request.getAdmissionPeriodInfo().getId();
                String admissionPeriodName = request.getAdmissionPeriodInfo().getAdmissionPeriodName();
                objectDtoResponse.setAdmissionPeriodId(admissionPeriodId);
                objectDtoResponse.setAdmissionPeriodIdName(admissionPeriodName);

            }
            //Check major info
            if(null != request.getMajorInfo()){
                Long majorId = request.getMajorInfo().getId();
                String majorName = request.getMajorInfo().getMajorName();
                objectDtoResponse.setMajorId(majorId);
                objectDtoResponse.setMajorName(majorName);

            }
            objectDtoResponse.setStartPeriod(request.getStartPeriod());
            objectDtoResponse.setEndPeriod(request.getEndPeriod());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setNote(request.getNote());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<PeriodAssignmentDto.PeriodAssignmentListInfo> convertListObjectToDto(List<PeriodAssignment> listRequestUser){
        PageAmtListResponseDto<PeriodAssignmentDto.PeriodAssignmentListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<PeriodAssignmentDto.PeriodAssignmentListInfo> listPeriodAssignmentDto = new ArrayList<PeriodAssignmentDto.PeriodAssignmentListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                PeriodAssignmentDto.PeriodAssignmentListInfo newObject = new PeriodAssignmentDto.PeriodAssignmentListInfo();
                newObject.setPeriodAssignmentId(listRequestUser.get(i).getId());
                //Check constructor info
                if(null != listRequestUser.get(i).getAdmissionPeriodInfo()){
                    Long instructorId = listRequestUser.get(i).getAdmissionPeriodInfo().getId();
                    String instructorName = listRequestUser.get(i).getAdmissionPeriodInfo().getAdmissionPeriodName();
                    newObject.setAdmissionPeriodId(instructorId);
                    newObject.setAdmissionPeriodIdName(instructorName);

                }
                //Check major info
                if(null != listRequestUser.get(i).getMajorInfo()){
                    Long majorId = listRequestUser.get(i).getMajorInfo().getId();
                    String majorName = listRequestUser.get(i).getMajorInfo().getMajorName();
                    newObject.setMajorId(majorId);
                    newObject.setMajorName(majorName);

                }
                newObject.setStartPeriod(listRequestUser.get(i).getStartPeriod());
                newObject.setEndPeriod(listRequestUser.get(i).getEndPeriod());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setNote(listRequestUser.get(i).getNote());
                listPeriodAssignmentDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listPeriodAssignmentDto);
        return objectDtoResponse;
    }

}
