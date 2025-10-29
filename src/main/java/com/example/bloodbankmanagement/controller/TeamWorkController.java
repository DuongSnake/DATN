package com.example.bloodbankmanagement.controller;

import com.example.bloodbankmanagement.dto.common.BasicResponseDto;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.common.SingleResponseDto;
import com.example.bloodbankmanagement.dto.service.TeamWorkDto;
import com.example.bloodbankmanagement.service.TeamWorkServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teamWork")
@RequiredArgsConstructor
public class TeamWorkController {

    private final TeamWorkServiceImpl teamWorkService;

    @PostMapping("/insert")
    public ResponseEntity<BasicResponseDto> insertTeamWork(@RequestBody @Valid TeamWorkDto.TeamWorkInsertInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                teamWorkService.insertTeamWork(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BasicResponseDto> updateTeamWork(@RequestBody @Valid TeamWorkDto.TeamWorkUpdateInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                teamWorkService.updateTeamWork(request, lang),
                HttpStatus.OK
        );
    }

    @PostMapping("/selectList")
    public ResponseEntity<SingleResponseDto<PageAmtListResponseDto<TeamWorkDto.TeamWorkListInfo>>> selectListTeamWork(@RequestBody TeamWorkDto.TeamWorkSelectListInfo request) {
        return new ResponseEntity<>(
                teamWorkService.selectListTeamWork(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponseDto> deleteTeamWork(@RequestBody @Valid TeamWorkDto.TeamWorkDeleteInfo request, @RequestHeader("lang") String lang) {
        return new ResponseEntity<>(
                teamWorkService.deleteTeamWork(request, lang),
                HttpStatus.OK
        );
    }

    @GetMapping("/select")
    public ResponseEntity<SingleResponseDto<TeamWorkDto.TeamWorkSelectInfoResponse>> selectTeamWork(@RequestBody TeamWorkDto.TeamWorkSelectInfo request, @RequestHeader("lang") String lang){
        return new ResponseEntity<>(
                teamWorkService.selectTeamWork(request, lang),
                HttpStatus.OK
        );
    }

}
