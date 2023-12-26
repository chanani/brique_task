package com.task.brique_task.employ.service;

import com.task.brique_task.command.EmployVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface EmployMapper {
    public ArrayList<EmployVO> allData();

}
