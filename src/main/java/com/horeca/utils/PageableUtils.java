package com.horeca.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageableUtils {

    public static <T> Page<T> extractPage(List<T> source, Pageable pageable) {
        int totalCount = source.size();
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int fromIndex = Math.max(pageNumber * pageSize, 0);
        int toIndex = Math.max(Math.min(fromIndex + pageSize, totalCount), 0);

        List<T> sublist = source.subList(fromIndex, toIndex);
        return new PageImpl<>(sublist, pageable, totalCount);
    }
}
