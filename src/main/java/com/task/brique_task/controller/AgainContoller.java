package com.task.brique_task.controller;

import com.task.brique_task.employ.service.EmployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.*;

@Controller
@RequestMapping(value = "/")
public class AgainContoller {

    @Autowired
    @Qualifier("employService")
    private EmployService employService;

    @RequestMapping("/task1")
    public String csvFile(Model model) {

            return "task1";

    }

    @RequestMapping("/task7")
    public String task7() {
        return "/task7";
    }

    @GetMapping("/task7_result")
    @ResponseBody
    public ResponseEntity<String> task7_result(@RequestParam String input) {
        Stack<Character> stack = new Stack<>();
        try {
            int count = 0;
            for (char x : input.toCharArray()) {
                if (x == '(') stack.push(x);
                else {
                    if (!stack.isEmpty() && stack.peek() == '(') {
                        count += 2;
                        stack.clear();
                    }
                }
            }
            return ResponseEntity.ok(String.valueOf(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("처리 중 오류가 발생하였습니다.");
        }
    }

}

