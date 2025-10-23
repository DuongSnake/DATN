package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class ClassRoomDto {
    @Data
    public static class ClassRoomSelectListInfo {
        private Long classId;
        private String className;
        private Long majorId;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String status;
        private String createUser;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class ClassRoomListInfo {
        private Long classId;
        private String className;
        private Long majorId;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class ClassRoomSelectInfo {
        private Long classId;
    }

    @Data
    public static class ClassRoomSelectInfoResponse {
        private Long classId;
        private String className;
        private Long majorId;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class ClassRoomInsertInfo {
        private String className;
        private Long majorId;
    }

    @Data
    public static class ClassRoomUpdateInfo {
        private Long classId;
        private String className;
    }

    @Data
    public static class ClassRoomDeleteInfo {
        private List<Long> listClassRoomId;
    }
}
