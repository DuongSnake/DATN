package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class TeamWorkDto {
    @Data
    public static class TeamWorkSelectListInfo {
        private long teamWorkId;
        private String teamWorkName;
        private Long classRoomId;
        private Long instructorId;//teacher coaching id
        private String createUser;
        private Integer categoryTeam;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String status;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class TeamWorkListInfo {
        private Long teamWorkId;
        private String teamWorkName;
        private Long classRoomId;
        private String classRoomName;
        private Long instructorId;
        private Integer categoryTeam;
        private String instructorName;
        private String createUser;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class TeamWorkSelectInfo {
        private Long teamWorkId;
    }

    @Data
    public static class TeamWorkSelectInfoResponse {
        private Long teamWorkId;
        private String teamWorkName;
        private Long classRoomId;
        private String classRoomName;
        private Long instructorId;
        private Integer categoryTeam;
        private String instructorName;
        private String createUser;
        private String status;
        private LocalDate createAt;
    }

    @Data
    public static class TeamWorkInsertInfo {
        private String teamWorkName;
        private Long classRoomId;
        private Long instructorId;
        private Integer categoryTeam;
    }

    @Data
    public static class TeamWorkUpdateInfo {
        private long teamWorkId;
        private String teamWorkName;
        private Integer categoryTeam;
        private Long classRoomId;
        private Long instructorId;
    }

    @Data
    public static class TeamWorkDeleteInfo {
        private List<Long> listTeamWorkId;
    }
}
