package com.task.brique_task.employ.service;

import com.task.brique_task.command.EmployVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("employService")
public class EmployServiceImpl implements EmployService {

    @Autowired
    private EmployMapper employMapper;


    @Override
    public ArrayList<EmployVO> allData() {
        return employMapper.allData();
    }
}
