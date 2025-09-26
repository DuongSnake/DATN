package com.example.bloodbankmanagement.dto.pagination;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Getter
@Setter
public class PageRequestDto {

    private Integer pageNum = 0;
    private Integer pageSize = 10;

    public Pageable getPageable(PageRequestDto dto){
        Integer page = Objects.nonNull(dto.getPageNum()) ? dto.getPageNum() : this.pageNum;
        Integer size = Objects.nonNull(dto.getPageSize()) ? dto.getPageSize() : this.pageSize;
        PageRequest request = PageRequest.of(page, size);
        return request;
    }

    public Pageable getPageableWithSort(PageRequestDto dto, String columnName){
        Integer page = Objects.nonNull(dto.getPageNum()) ? dto.getPageNum() : this.pageNum;
        Integer size = Objects.nonNull(dto.getPageSize()) ? dto.getPageSize() : this.pageSize;
        PageRequest request = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, columnName));
        return request;
    }

    public static Integer reduceValuePage(Integer page){
        if(1 == page || 0 == page){
            page = 0;
        }else{
            page = page -1;
        }
        return page;
    }

}