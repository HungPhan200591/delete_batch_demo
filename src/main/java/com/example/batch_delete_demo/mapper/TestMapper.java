package com.example.batch_delete_demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//@Mapper
public interface TestMapper {
    List<Integer> selectLogEsbSeqLessThan(String endDate);
    int deleteLogEsbByLogEsbSeqIn(@Param("sequences") List<Integer> sequences);
}
