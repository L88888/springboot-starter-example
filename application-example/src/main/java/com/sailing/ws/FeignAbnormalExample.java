package com.sailing.ws;

import com.sailing.feignhandler.Sabnormal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: spring-starter
 * @description: 测试feign500状态，异常信息捕获
 * @author: LIULEI
 * @create: 2020-08-15 23:20:
 **/
@Slf4j
@RestController
public class FeignAbnormalExample {

    @Autowired
    private Sabnormal sabnormal;

    /**
     * curl http://127.0.0.1:8080/getalarmdata/v1
     * http://127.0.0.1:8080/getalarmdata/v1
     * @return
     */
    @RequestMapping(value = "getalarmdata/v1",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map getAlarmData() {
        Map resultData = new HashMap();
        resultData.put("data","test one success.");
        Map catData = sabnormal.queryCatAlarmData();

        log.info("0000000000{}",catData);
        return resultData;
    }


    /**
     * http://127.0.0.1:8080/querycatalarmdata/v1
     * @return
     */
    @RequestMapping(value = "querycatalarmdata/v1",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map queryCatAlarmData() {
        Map resultData = new HashMap();
        resultData.put("data","test one querycatalarmdata full.");

        boolean forkLiand = true;
        if (forkLiand){
            Integer.parseInt("s");
            throw new RuntimeException("数据类型转换异常.");
        }
        return resultData;
    }
}
