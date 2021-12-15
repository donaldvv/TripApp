package com.donald.service.mapper.impl;

import com.donald.service.dto.request.TripsFilterRequest;
import com.donald.service.mapper.PageMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PageMapperImpl implements PageMapper {
    public static final int PAGE_SIZE = 30;
    private static final Logger logger = LogManager.getLogger("com.donald.tripapplhind.service");

    @Override
    public Pageable toPageable(TripsFilterRequest request) {

        if(request == null) {
            logger.warn("Default will be used!");
            return PageRequest.of(0, PAGE_SIZE);
        }

        int pageSize = request.getPageSize();

        return PageRequest.of(request.getPageNo(), pageSize == 0 ? PAGE_SIZE : pageSize);
    }
}
