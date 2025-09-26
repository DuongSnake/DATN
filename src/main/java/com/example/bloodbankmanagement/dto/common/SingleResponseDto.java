package com.example.bloodbankmanagement.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponseDto<T>  extends BasicResponseDto {
    private T result;
}
