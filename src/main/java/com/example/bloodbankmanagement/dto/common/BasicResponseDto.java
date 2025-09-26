package com.example.bloodbankmanagement.dto.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasicResponseDto {
    private String responseCd;
    private String responseMsg;
    private String responseTs;
    private String responseTraceId;
    private String requesterTrId;
}
