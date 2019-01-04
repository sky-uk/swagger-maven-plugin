package com.wordnik.sample.model;


import io.swagger.v3.oas.annotations.Parameter;

public class PaginationHelper {
    private Integer limit;
    private Integer offset;

    public PaginationHelper() {
    }

    public Integer getLimit() {
        return limit;
    }

    @Parameter(description = "The pagination limit", name = "limit")
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    @Parameter(description = "The pagination offset", name = "offset")
    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
