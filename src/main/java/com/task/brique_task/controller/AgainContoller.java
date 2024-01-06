package com.task.brique_task.controller;

import com.task.brique_task.config.SocketService;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

@Controller
@RequestMapping(value = "/")
public class AgainContoller {

    @Autowired
    @Qualifier("employService")
    private EmployService employService;

    @Autowired
    private SocketService socketService;

    final static String SERVER_IP = "127.0.0.1";
    final static int SERVER_PORT = 56768;

    @RequestMapping("/task1")
    public String csvFile(Model model) {
       /* CsvReader csv = new CsvReader(); // csvRead 객체 생성
        List<List<String>> list = csv.readCSV(); // 읽은 데이터 list에 초기화
        List<List<Double>> result = new ArrayList<>(); // 데이터를 저장할 List 생성
        String text = ""; // 숫자가 아닌 값들의 모음
        BufferedReader in = null;

        try {
            URL url = new URL("https://drive.google.com/file/d/1Ah0gkauGCIqJHpFGhTgsEZCjYFRscjTh/view?usp=sharing");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println("line : " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

            model.addAttribute("list", result);
            model.addAttribute("text", text);*/
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
            socket = new Socket(SERVER_IP,SERVER_PORT);
            System.out.println("socket 연결");

            /**	Client에서 Server로 보내기 위한 통로 */
            OutputStream os = socket.getOutputStream();
            /**	Server에서 보낸 값을 받기 위한 통로 */
            InputStream is = socket.getInputStream();

            os.write( content.getBytes() );
            os.flush();

            byte[] data = new byte[16];
            int n = is.read(data);
            final String resultFromServer = new String(data,0,n);

            System.out.println(resultFromServer);
            String msg = socketService.socketServer(resultFromServer);
            socket.close();
            System.out.println("server response msg : " + msg);
            return ResponseEntity.ok("send");
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

