package com.example.bloodbankmanagement.entity;

import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.service.CommentProcessAssignmentDto;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comment_process_assignment")
public class CommentProcessAssignment  extends EntityCommon {
    private String note;
    private LocalDate examDueDate;
    private LocalDate confirmDoneDate;
    private String statusDone;//0:not done 1:done
    @ManyToOne
    @JoinColumn(name = "file_upload_id")
    private FileUpload fileUploadInfo;

    public static CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse convertToDto(CommentProcessAssignment request){
        CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse objectDtoResponse = new CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setCommentProcessAssignmentId(request.getId());
            objectDtoResponse.setNote(request.getNote());
            objectDtoResponse.setStatusDone(request.getStatusDone());
            objectDtoResponse.setExamDueDate(request.getExamDueDate());
            objectDtoResponse.setConfirmDateDone(request.getConfirmDoneDate());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setFileUploadId(null != request.getFileUploadInfo() ? request.getFileUploadInfo().getId() : null);
            objectDtoResponse.setFileUploadName(null != request.getFileUploadInfo() ? request.getFileUploadInfo().getFileName() : null);
            objectDtoResponse.setAssignmentName(null != request.getFileUploadInfo().getAssignmentRegisterInfo() ? request.getFileUploadInfo().getAssignmentRegisterInfo().getAssignmentName() : null);
            if((null != request.getFileUploadInfo().getAssignmentRegisterInfo() && null != request.getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor()
                    && null != request.getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor().getStudentInfo())){
                User studentInfo = request.getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor().getStudentInfo();
                objectDtoResponse.setStudentName(null != studentInfo ? studentInfo.getFullName() : null);

            }
            if((null != request.getFileUploadInfo().getAssignmentRegisterInfo() && null != request.getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor()
                    && null != request.getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor().getInstructorInfo())){
                User instructorInfo = request.getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor().getInstructorInfo();
                objectDtoResponse.setInstructorName(null != instructorInfo ? instructorInfo.getFullName() : null);

            }
            objectDtoResponse.setCreateAt(request.getCreateAt());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<CommentProcessAssignmentDto.CommentProcessAssignmentListInfo> convertListObjectToDto(List<CommentProcessAssignment> listRequestUser, Long totalRecord){
        PageAmtListResponseDto<CommentProcessAssignmentDto.CommentProcessAssignmentListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<CommentProcessAssignmentDto.CommentProcessAssignmentListInfo> listCommentProcessAssignmentDto = new ArrayList<CommentProcessAssignmentDto.CommentProcessAssignmentListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                CommentProcessAssignmentDto.CommentProcessAssignmentListInfo newObject = new CommentProcessAssignmentDto.CommentProcessAssignmentListInfo();
                newObject.setCommentProcessAssignmentId(listRequestUser.get(i).getId());
                newObject.setNote(listRequestUser.get(i).getNote());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setExamDueDate(listRequestUser.get(i).getExamDueDate());
                newObject.setConfirmDateDone(listRequestUser.get(i).getConfirmDoneDate());
                newObject.setStatusDone(listRequestUser.get(i).getStatusDone());
                newObject.setFileUploadId(null != listRequestUser.get(i).getFileUploadInfo() ? listRequestUser.get(i).getFileUploadInfo().getId() : null);
                newObject.setFileUploadName(null != listRequestUser.get(i).getFileUploadInfo() ? listRequestUser.get(i).getFileUploadInfo().getFileName() : null);
                newObject.setAssignmentName(null != listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo() ? listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo().getAssignmentName() : null);
                if((null != listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo() && null != listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor()
                && null != listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor().getStudentInfo())){
                    User studentInfo = listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor().getStudentInfo();
                    newObject.setStudentName(null != studentInfo ? studentInfo.getFullName() : null);

                }
                if((null != listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo() && null != listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor()
                        && null != listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor().getInstructorInfo())){
                    User instructorInfo = listRequestUser.get(i).getFileUploadInfo().getAssignmentRegisterInfo().getStudentMapInstructor().getInstructorInfo();
                    newObject.setInstructorName(null != instructorInfo ? instructorInfo.getFullName() : null);

                }
                listCommentProcessAssignmentDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listCommentProcessAssignmentDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }
}
