package com.donald.service.dto.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedResponse<U> {
    private List<U> content;
    private Integer count;
    private Long totalC;

    public PagedResponse(List<U> content, Integer count, Long totalC) {
        this.content = content;
        this.count = count;
        this.totalC = totalC;
    }
}
