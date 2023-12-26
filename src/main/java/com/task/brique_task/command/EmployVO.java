package com.task.brique_task.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployVO {

    private int emp_no;
    private String first_name;
    private String last_name;
    private char gender;
    private Date hire_date;
    private String dept_name;
    private String title;
    private Integer max_salary;

}
