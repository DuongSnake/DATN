package com.example.bloodbankmanagement.dto.mapper;

import com.example.bloodbankmanagement.dto.excelObject.StudentExcel;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface StudentExportMapper extends EntityMapper<StudentDto.StudentForExportExcelResponse, StudentExcel> {
}
