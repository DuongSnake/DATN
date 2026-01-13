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
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comment_process_assignment")
public class CommentProcessAssignment  extends EntityCommon {
    private String note;
    @ManyToOne
    @JoinColumn(name = "file_upload_id")
    private FileUpload fileUploadInfo;

    public static CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse convertToDto(CommentProcessAssignment request){
        CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse objectDtoResponse = new CommentProcessAssignmentDto.CommentProcessAssignmentSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setCommentProcessAssignmentId(request.getId());
            objectDtoResponse.setNote(request.getNote());
            objectDtoResponse.setStatus(request.getStatus());
            objectDtoResponse.setFileUploadId(null != request.getFileUploadInfo() ? request.getFileUploadInfo().getId() : null);
            objectDtoResponse.setFileUploadName(null != request.getFileUploadInfo() ? request.getFileUploadInfo().getFileName() : null);
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
                newObject.setFileUploadId(null != listRequestUser.get(i).getFileUploadInfo() ? listRequestUser.get(i).getFileUploadInfo().getId() : null);
                newObject.setFileUploadName(null != listRequestUser.get(i).getFileUploadInfo() ? listRequestUser.get(i).getFileUploadInfo().getFileName() : null);
                listCommentProcessAssignmentDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listCommentProcessAssignmentDto);
        objectDtoResponse.setTotalRecord(Math.toIntExact(totalRecord));
        return objectDtoResponse;
    }
}
