package com.study.dal.mapper.query;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoCondition {

    private Long id;

    private List<Long> ids;

    private String name;

    private Integer limit;

    private Integer offset;

}
