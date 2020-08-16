package com.sailing.feignhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI
 * @create: 2020-08-15 23:27:
 **/
@Slf4j
@Component
public class FeignSampleFallback implements Sabnormal {

    @Override
    public Map queryCatAlarmData() {
        log.info("下游接口异常,queryCatAlarmData()");
        return null;
    }
}
