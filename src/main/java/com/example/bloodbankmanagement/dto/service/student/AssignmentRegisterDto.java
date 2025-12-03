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
        private long assignmentRegisterId;
        private String assignmentRegisterName;
        private Date fromDate;
        private Date toDate;
        private String status;
        private String regUser;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class AssignmentRegisterListInfo {
        private long assignmentRegisterId;
        private String assignmentRegisterName;
        private Long studentMapInstructorId;
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
        private Long assignmentRegisterId;
    }

    @Data
    public static class AssignmentRegisterSelectInfoResponse {
        private Long assignmentRegisterId;
        private String assignmentRegisterName;
        private Long studentMapInstructorId;
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
        private String assignmentRegisterName;
        private MultipartFile fileUpload;
    }

    @Data
    public static class AssignmentRegisterUpdateInfo {
        private long assignmentRegisterId;
        private MultipartFile fileUpload;
        private String assignmentRegisterName;
    }

    @Data
    public static class AssignmentRegisterDeleteInfo {
        private List<Long> listData;
    }

    @Data
    public static class SendRequestAssignmentInfo {
        private Long requestId;
    }
}
