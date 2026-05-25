package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.ScoreSelectListAssignmentDto;
import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "score_assignment")
public class ScoreAssignment extends EntityCommon {
    private Double scoreInstructor;//Điểm trung bình
    private Double scoreExaminer;//Điểm bảo vệ
    private Double scoreCritical;//Điểm quá trình
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_register_info_id")
    private AssignmentStudentRegister assignmentRegisterInfo;

    public static ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse convertToDto(ScoreAssignment request){
        ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse objectDtoResponse = new ScoreAssignmentDto.ScoreAssignmentSelectInfoResponse();
        if(request != null){
            Long assignmentRegisterId = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getId() : null;
            String assignmentRegisterName = null != request.getAssignmentRegisterInfo() ? request.getAssignmentRegisterInfo().getAssignmentName() : null;
            objectDtoResponse.setScoreAssignmentId(request.getId());                //Check caculate average score
            objectDtoResponse.setScoreExaminer(request.getScoreExaminer());
            objectDtoResponse.setScoreInstructor(request.getScoreInstructor());
            objectDtoResponse.setScoreCritical(request.getScoreCritical());
            if(!StringUtils.isEmpty(String.valueOf(request.getScoreCritical())) && !StringUtils.isEmpty(String.valueOf(request.getScoreInstructor()))
                    && !StringUtils.isEmpty(String.valueOf(request.getScoreExaminer()))){
                Double valueAverage = 0.0;
                valueAverage +=request.getScoreCritical();
                valueAverage +=request.getScoreInstructor();
                valueAverage +=request.getScoreExaminer();
                valueAverage = valueAverage/3;
                objectDtoResponse.setScoreAverage(null);
            }else{
                objectDtoResponse.setScoreAverage(null);
            }
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
                newObject.setScoreExaminer(listRequestUser.get(i).getScoreExaminer());
                newObject.setScoreInstructor(listRequestUser.get(i).getScoreInstructor());
                newObject.setScoreCritical(listRequestUser.get(i).getScoreCritical());
                //Check caculate average score
                if(!StringUtils.isEmpty(String.valueOf(listRequestUser.get(i).getScoreCritical())) && !StringUtils.isEmpty(String.valueOf(listRequestUser.get(i).getScoreInstructor()))
                        && !StringUtils.isEmpty(String.valueOf(listRequestUser.get(i).getScoreExaminer()))){
                    Double valueAverage = 0.0;
                    valueAverage +=listRequestUser.get(i).getScoreCritical();
                    valueAverage +=listRequestUser.get(i).getScoreInstructor();
                    valueAverage +=listRequestUser.get(i).getScoreExaminer();
                    valueAverage = valueAverage/3;
                    newObject.setScoreAverage(valueAverage);
                }else{
                    newObject.setScoreAverage(null);
                }
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

    public static PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListNewInfo> convertListObjectNewToDto(List<ScoreSelectListAssignmentDto> listRequestUser, Long totalRecord, CalculateAverageScore rateCalculateAverage){
        PageAmtListResponseDto<ScoreAssignmentDto.ScoreAssignmentListNewInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        Double rateExam = rateCalculateAverage.getRateExam();
        Double rateInstructor = rateCalculateAverage.getRateInstructor();
        Double totalRate = rateCalculateAverage.getTotalRate();
        List<ScoreAssignmentDto.ScoreAssignmentListNewInfo> listScoreAssignmentDto = new ArrayList<ScoreAssignmentDto.ScoreAssignmentListNewInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                ScoreAssignmentDto.ScoreAssignmentListNewInfo newObject = new ScoreAssignmentDto.ScoreAssignmentListNewInfo();
                newObject.setScoreAssignmentId(listRequestUser.get(i).getScoreAssignmentId());
                newObject.setAssignmentRegisterId(listRequestUser.get(i).getAssignmentRegisterId());
                newObject.setAssignmentRegisterName(listRequestUser.get(i).getAssignmentRegisterName());
                newObject.setScoreExaminer(listRequestUser.get(i).getScoreExaminer());
                newObject.setScoreInstructor(listRequestUser.get(i).getScoreInstructor());
                newObject.setScoreCritical(listRequestUser.get(i).getScoreCritical());
                //Check caculate average score
                if(!StringUtils.isEmpty(String.valueOf(listRequestUser.get(i).getScoreCritical())) && !StringUtils.isEmpty(String.valueOf(listRequestUser.get(i).getScoreInstructor()))
                        && !StringUtils.isEmpty(String.valueOf(listRequestUser.get(i).getScoreExaminer()))){
                    Double valueAverage = 0.0;
                    Double valueScoreExam = rateExam * listRequestUser.get(i).getScoreExaminer();
                    Double valueScoreInstructor = rateInstructor * listRequestUser.get(i).getScoreInstructor();
                    valueAverage = (valueScoreExam +valueScoreInstructor)/totalRate;
                    newObject.setScoreAverage(valueAverage);
                }else{
                    newObject.setScoreAverage(null);
                }
                newObject.setStudentName(listRequestUser.get(i).getStudentName());
                newObject.setAdmissionPeriodId(listRequestUser.get(i).getAdmissionPeriodId());
                newObject.setAdmissionPeriodName(listRequestUser.get(i).getAdmissionPeriodName());
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
