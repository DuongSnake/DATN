package com.example.bloodbankmanagement.dto.service.student;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AssignmentRegisterDto {
    @Data
    public static class AssignmentRegisterSelectListInfo {
        private long assignmentStudentRegisterId;
        private String assignmentStudentRegisterName;
        private Long periodAssignmentId;
        private Date fromDate;
        private Date toDate;
        private String status;
        private String regUser;
        private PageRequestDto pageRequestDto;
    }
    @Data
    public static class AssignmentRegisterSelectListOfInstructorIdInfo {
        private long assignmentStudentRegisterId;
        private String assignmentStudentRegisterName;
        private Long periodAssignmentId;
        private Date fromDate;
        private Date toDate;
        private String status;
        private Long intructorId;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class AssignmentRegisterListInfo {
        private long assignmentStudentRegisterId;
        private String assignmentStudentRegisterName;
        private String statusAutoMap;
        private String isApprovedDisplayName;
        private String statusAutoMapDisplayName;
        private Long periodAssignmentId;
        private String periodAssignmentName;
        private LocalDate expirePeriodDate;
        private String studentName;
        private String instructorName;
        private String fileName;
        private String fileType;
        private Integer isApproved;
        private String status;
        private String createUser;
        private LocalDate createAt;
    }

    @Data
    public static class AssignmentRegisterSelectInfo {
        private Long assignmentStudentRegisterId;
    }

    @Data
    public static class AssignmentRegisterSelectInfoResponse {
        private Long assignmentStudentRegisterId;
        private String assignmentStudentRegisterName;
        private String statusAutoMap;
        private String isApprovedDisplayName;
        private String statusAutoMapDisplayName;
        private Long periodAssignmentId;
        private String periodAssignmentName;
        private LocalDate expirePeriodDate;
        private String studentName;
        private String instructorName;
        private String fileName;
        private String fileType;
        private Integer isApproved;
        private String status;
        private String createUser;
        private LocalDate createAt;
    }

    @Data
    public static class AssignmentRegisterInsertInfo {
        private String assignmentStudentRegisterName;
        private Long periodAssignmentId;
        private MultipartFile fileUpload;
        private String statusAutoMap;
        private Long instructorId;
        private Long studentId;
    }

    @Data
    public static class AssignmentRegisterUpdateInfo {
        private long assignmentStudentRegisterId;
        private Long periodAssignmentId;
        private MultipartFile fileUpload;
        private String assignmentStudentRegisterName;
        private Long studentId;
        private Long instructorId;
        private String statusAutoMap;
    }

    @Data
    public static class AssignmentRegisterDeleteInfo {
        private List<Long> listData;
        private String reasonReject;
    }

    @Data
    public static class SendRequestAssignmentInfo {
        private Long requestId;
    }
    @Data
    public static class AssignmentInsertListFileUploadInsertInfo {
        private Long assignmentStudentRegisterId;
        private MultipartFile listFile[];
    }

    @Data
    public static class AssignmentUpdateListFileUploadInfo {
        private Long assignmentStudentRegisterId;
        private List<AssignmentFileUploadInfo> listFile;
    }

    @Data
    public static class AssignmentFileUploadInfo {
        private Long assignmentStudentRegisterId;
        private List<Long> deletedFileIds;
        private List<MultipartFile> listFile;
        private List<String> fileTypes;
        private List<Long> fileId;
    }

    @Data
    public static class SelectListFileInfo {
        private Long assignmentRegisterInfoId;
    }
}
