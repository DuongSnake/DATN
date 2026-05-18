package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AssignmentStudentRegisterDto {
    @Data
    public static class AssignmentStudentRegisterSelectListInfo {
        private long assignmentStudentRegisterId;
        private Long periodAssignmentId;
        private String assignmentStudentRegisterName;
        private Date fromDate;
        private Date toDate;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class AssignmentStudentRegisterListInfo {
        private long assignmentStudentRegisterId;
        private String assignmentStudentRegisterName;
        private Long periodAssignmentId;
        private String periodAssignmentName;
        private LocalDate expirePeriodDate;
        private Long studentId;
        private String studentName;
        private String instructorId;
        private String instructorName;
        private String fileName;
        private String fileType;
        private Integer isApproved;
        private String statusAutoMap;
        private String status;
        private String createUser;
        private LocalDate createAt;
        private Long oldValueId;
    }

    @Data
    public static class AssignmentStudentRegisterSelectInfo {
        private Long assignmentStudentRegisterId;
    }

    @Data
    public static class AssignmentStudentRegisterSelectInfoResponse {
        private Long assignmentStudentRegisterId;
        private String assignmentStudentRegisterName;
        private Long studentId;
        private Long periodAssignmentId;
        private String periodAssignmentName;
        private LocalDate expirePeriodDate;
        private String studentName;
        private String instructorId;
        private String instructorName;
        private String fileName;
        private String fileType;
        private Integer isApproved;
        private String statusAutoMap;
        private String status;
        private String createUser;
        private LocalDate createAt;
        private Long oldValueId;
    }

    @Data
    public static class AssignmentStudentRegisterInsertInfo {
        private String assignmentStudentRegisterName;
        private String statusAutoMap;
        private Long instructorId;
        private MultipartFile fileUpload;
        private Long periodAssignmentId;
        private Long studentId;
    }

    @Data
    public static class AssignmentStudentRegisterUpdateInfo {
        private long assignmentStudentRegisterId;
        private MultipartFile fileUpload;
        @NotNull
        private Long studentId;
        private Long instructorId;
        private Long periodAssignmentId;
        private String assignmentStudentRegisterName;
        private String statusAutoMap;
        @NotNull
        private Long oldValueId;
    }

    @Data
    public static class AssignmentStudentRegisterDeleteInfo {
        private List<Long> listData;
    }
}
