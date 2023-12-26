package com.task.brique_task.controller;

import com.task.brique_task.command.EmployVO;
import com.task.brique_task.command.randomVO;
import com.task.brique_task.employ.service.EmployService;
import com.task.brique_task.util.CsvReader;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Controller
@RequestMapping(value = "/")
public class mainController {

    @Autowired
    @Qualifier("employService")
    private EmployService employService;

    @RequestMapping("")
    public String main() {
        return "/main";
    }

    @RequestMapping("/task1")
    public String csvFile(Model model) {
        CsvReader csv = new CsvReader(); // csvRead 객체 생성
        List<List<String>> list = csv.readCSV(); // 읽은 데이터 list에 초기화
        List<List<Double>> result = new ArrayList<>(); // 데이터를 저장할 List 생성
        String text = ""; // 숫자가 아닌 값들의 모음
        for (int i = 1; i < list.size(); i++) { // list.size()
            List<Double> asList = new ArrayList<>(); // 연산된 list 저장할 list 생성
            String str = list.get(i).toString().substring(1, list.get(i).toString().length() - 1);
            String[] arr = str.split(", ");
            Arrays.sort(arr);

            double min = Integer.MAX_VALUE; // 최솟값
            double max = 0; // 최대값
            double sum = 0; // 합계
            double avg = 0; // 평균
            double sd = 0; // 표준 편차 / 2
            ArrayList<Double> midList = new ArrayList<>(); // 중간값을 계산하기 위한 list 생성
            for (int j = 0; j < arr.length; j++) { // 표준 편차 구하기 위해 먼저 합계 계산
                try {
                    sum += Double.parseDouble(arr[j]);
                } catch (NumberFormatException e) {

                }
            }
            avg = sum / arr.length; // 표준 편차 구하기 위해 먼저 평균 계산

            for (int j = 0; j < arr.length; j++) {
                try {
                    midList.add(Double.parseDouble(arr[j]));
                    double item = Double.parseDouble(arr[j]);
                    if (min > item) min = item;
                    if (max < item) max = item;
                    sd += Math.abs(avg - Double.parseDouble(arr[j])) * Math.abs(avg - Double.parseDouble(arr[j]));
                } catch (NumberFormatException e) {
                    text += arr[j].replace("\"", "");
                }
            }
            Collections.sort(midList); // 중간값 구하기 위한 정렬

            asList.add(min); // min
            asList.add(max); // max
            asList.add(sum); // sum
            asList.add(avg); // avg
            asList.add(Math.sqrt(sd / (arr.length - 1))); // sd
            asList.add(midList.get(midList.size() / 2)); // mid
            result.add(asList);
        }
        model.addAttribute("list", result);
        model.addAttribute("text", text);
        return "task1";
    }

    @RequestMapping("/task2")
    public String chat() {
        return "/task2";
    }

    @GetMapping("/chat")
    @ResponseBody
    public ResponseEntity<String> chatInput(@RequestParam String content) {

        try {
            String send = content;
            if (content.equals("ping")) {
                send = "pong";
            }

            return ResponseEntity.ok(send);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("처리 중 오류가 발생하였습니다.");
        }
    }

    @RequestMapping("/task3")
    public String table(Model model) {
        ArrayList<EmployVO> list = employService.allData();
        model.addAttribute("list", list);
        return "/task3";
    }

    @RequestMapping("/task4")
    public String task4(Model model) {

        return "/task4";
    }

    @RequestMapping("/task5")
    public String task5(Model model) {
        BufferedReader in = null;
        Map<String, Integer> countMap = new HashMap<>();
        Map<String, String> dataMap = new HashMap<>();
        ArrayList<randomVO> list = new ArrayList<>();
        String id = "";
        for (int i = 0; i < 100; i++) {
            try {
                URL url = new URL("http://codingtest.brique.kr:8080/random");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line = "";
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    id = line.substring(6, line.indexOf(","));
                    dataMap.put(id, line);
                }
                countMap.put(id, countMap.getOrDefault(id, 0) + 1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (String str : countMap.keySet()) {
            randomVO vo = new randomVO();
            vo.setId(str);
            vo.setData(dataMap.get(str));
            vo.setCount(countMap.get(str));
            list.add(vo);
        }
        list.sort(Comparator.comparingInt(randomVO::getCount).reversed());
        model.addAttribute("list", list);
        return "/task5";
    }

    @RequestMapping("/task7")
    public String task7() {
        return "/task7";
    }

    @GetMapping("/task7_result")
    @ResponseBody
    public ResponseEntity<String> task7_result(@RequestParam String input) {
        try {
            String[] arr = input.replaceAll("[^\\( | \\)]","").split("");
            int count = 0;
            for (int i = 0; i < arr.length - 1; i++) {
                   if (arr[i].equals("(") && !arr[i].equals(arr[i + 1])) count++;
            }
            return ResponseEntity.ok(String.valueOf(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("처리 중 오류가 발생하였습니다.");
        }
    }


}
