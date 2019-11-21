package com.ss23485.contentserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexController {

    private Logger log = LoggerFactory.getLogger(IndexController.class);
    private Map<String, Long> counterMap = new HashMap<>();

    @GetMapping("/index")
    public String Index(String user) throws Exception {
        if (user == null) {
            user = "error";
            log.info("metrics|{},{},{}_visit_count,{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, user, System.currentTimeMillis(), counterMap.get(user), "counter");
            throw new Exception("error");
        }
        if (user.equals("")) {
            user = "anonymous";
        }
        counterMap.put(user, counterMap.getOrDefault(user, 0L) + 1);
        log.info("metrics|{},{},{}_visit_count,{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, user, System.currentTimeMillis(), counterMap.get(user), "counter");
        return "ok";
    }

}
