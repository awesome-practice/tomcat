package com.practice.tomcat.tomcatpractice;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.StringJoiner;

/**
 * @author Luo Bao Ding
 * @since 2019/1/4
 */
@RestController
public class TestController {
    private final Path rootLocation;

    public TestController() {
        this.rootLocation = Paths.get("upload-dir");
    }

    @RequestMapping("/sleep/{sleepTimeInSec}")
    public String sleep(@PathVariable("sleepTimeInSec") long sleepTimeInSec) throws InterruptedException {
        System.out.println("start\t" + java.time.Instant.now());
        Thread.sleep(sleepTimeInSec * 1000);
        System.out.println("over\t" + java.time.Instant.now());
        return "ok";
    }


    /**
     * curl --proxy 192.168.63.80:8888  -d 'a=xx&b=1'  'http://192.168.63.80:9201/form'
     * <p>
     * ------------ 能成功一: Content-Length等于body length
     * <pre>
     * $ time telnet localhost 9201
     *
     * POST http://localhost:9201/form HTTP/1.1
     * Host: localhost:9201
     * User-Agent: curl/7.59.0
     * Connection: Keep-Alive
     * Content-Length: 8
     * Content-Type: application/x-www-form-urlencoded
     * <p>
     * a=xx&b=1
     * </pre>
     * ------------ 能成功二: Content-Length小于body length
     * <pre>
     * $ time telnet localhost 9201
     *
     * POST http://localhost:9201/form HTTP/1.1
     * Host: localhost:9201
     * User-Agent: curl/7.59.0
     * Connection: Keep-Alive
     * Content-Length: 8
     * Content-Type: application/x-www-form-urlencoded
     * <p>
     * a=xx
     * </pre>
     */
    @RequestMapping("/form")
    public String form(@RequestParam(name = "a", required = false) String a,
                       @RequestParam(name = "b", required = false) Integer b, HttpServletRequest request) {
        System.out.println("a = [" + a + "], b = [" + b + "]");
        String contentType = request.getContentType();
        System.out.println("contentType = " + contentType);
        return "successful";
    }

    private static class JsonData {
        private String a;
        private String b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", JsonData.class.getSimpleName() + "[", "]")
                    .add("a='" + a + "'")
                    .add("b='" + b + "'")
                    .toString();
        }
    }

    /**
     * curl --proxy 192.168.63.80:8888  -d '{"a":"xx","b":"12"}'  'http://192.168.63.80:9201/json' -H 'Content-Type:application/json'
     * <p>
     * ------------ 能成功一: Content-Length等于body length
     * <pre>
     * telnet 192.168.63.80 9201
     *
     * POST http://localhost:9201/json HTTP/1.1
     * Host: localhost:9201
     * User-Agent: curl/7.59.0
     * Connection: Keep-Alive
     * Content-Length: 19
     * Content-Type: application/json
     *
     * {"a":"xx","b":"12"}
     * </pre>
     * <p>
     * ------------ 能成功二: Content-Length小于body length
     * <pre>
     * telnet 192.168.63.80 9201
     *
     * POST http://localhost:9201/json HTTP/1.1
     * Host: localhost:9201
     * User-Agent: curl/7.59.0
     * Connection: Keep-Alive
     * Content-Length: 19
     * Content-Type: application/json
     *
     * {"a":"xx"}
     * </pre>
     */
    @RequestMapping("/json")
    public String json(@RequestBody JsonData jsonData, HttpServletRequest request) {
        System.out.println(jsonData);
        String contentType = request.getContentType();
        System.out.println("contentType = " + contentType);
        return "successful";
    }

    @RequestMapping("/blank")
    public String blank() {
        System.out.println("TestController.blank");
        return "success";
    }

    @RequestMapping("/file")
    public String file(@RequestPart("file") MultipartFile file, @RequestPart("a") String a) {
        System.out.println("file = [" + file + "], a = [" + a + "]");
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(file.getName()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

}
