package com.example.bloodbankmanagement.dto.common;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

public class PageDto {
    private PageDto() {
    }

    @Data
    @ToString
    @NoArgsConstructor
    public static class PageRequest implements Serializable {


        private int pageNum;


        private int pageSize;

        @Builder
        public PageRequest(int pageNum, int pageSize) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
        }

    }
}
