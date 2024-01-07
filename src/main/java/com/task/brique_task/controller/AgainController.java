package com.task.brique_task.controller;

import ch.qos.logback.core.status.StatusUtil;
import com.task.brique_task.employ.service.EmployService;
import com.task.brique_task.util.CsvReader;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
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

import java.io.*;
import java.net.Socket;
import java.util.*;

@Controller
@RequestMapping(value = "/")
public class AgainController {

    @Autowired
    @Qualifier("employService")
    private EmployService employService;

    final static String SERVER_IP = "127.0.0.1";
    final static int SERVER_PORT = 56768;

    @RequestMapping("/task1")
    public String csvFile(Model model) {
        CsvReader csv = new CsvReader(); // csvRead 객체 생성
        List<List<String>> list = csv.readCSV(); // 읽은 데이터 list에 초기화
        List<List<Double>> result = new ArrayList<>();
        int totalLine = list.size();
        int calculLine = 0;
        String errorMsg = "";
        for (int i = 1; i < list.size(); i++) {
            try {
                double[] doubles = Arrays.stream(list.get(i).toString().substring(1, list.get(i).toString().length() - 1).replaceAll(" ", "").split(","))
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                DescriptiveStatistics stats = new DescriptiveStatistics(doubles);
                double min = StatUtils.min(doubles);
                double max = StatUtils.max(doubles);
                double sum = StatUtils.sum(doubles);
                double avg = StatUtils.mean(doubles);
                double std = stats.getStandardDeviation();
                double median = stats.getPercentile(50);
                calculLine++; // 계산된 라인 카운트
                List resultLine = new ArrayList();
                resultLine.add(min);
                resultLine.add(max);
                resultLine.add(sum);
                resultLine.add(avg);
                resultLine.add(std);
                resultLine.add(median);
                result.add(resultLine);
            } catch (Exception e) {
                errorMsg += list.get(i).toString().replaceAll("[^a-zA-Z]","");
            }
        }
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("totalLine", totalLine);
        model.addAttribute("calculLine", calculLine);
        model.addAttribute("list", result);
        return "task1";

    }

    @RequestMapping("/task2")
    public String chat() {
        return "/task2";
    }

    @GetMapping("/chat")
    @ResponseBody
    public ResponseEntity<String> chatInput(@RequestParam String content) {

        Socket socket = null;

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("socket 연결");

            /**    Client에서 Server로 보내기 위한 통로 */
            OutputStream os = socket.getOutputStream();
            /**    Server에서 보낸 값을 받기 위한 통로 */
            InputStream is = socket.getInputStream();

            os.write(content.getBytes());
            os.flush();

            byte[] data = new byte[16];
            int n = is.read(data);
            final String resultFromServer = new String(data, 0, n);

            System.out.println(resultFromServer);
            socket.close();
            return ResponseEntity.ok(resultFromServer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 오류가 발생하였습니다.");
        }
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 오류가 발생하였습니다.");
        }
    }

}

