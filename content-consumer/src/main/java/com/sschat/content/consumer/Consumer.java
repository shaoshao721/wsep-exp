package com.sschat.content.consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Consumer {

    static List<String> users = new ArrayList<>();

    public static void init() {
        users = new ArrayList<>();
//        users.add("Bevis");
//        users.add("bill");
//        users.add("bishop");
//        users.add("Blair");
//        users.add("black");
//        users.add("Blythe");
//        users.add("Bob");
//        users.add("Boris");
//        users.add("Bowen");
//        users.add("Boyce");
//        users.add("Bradley");
//        users.add("Brady");
//        users.add("Brandon");
//        users.add("Bryan");
//        users.add("Celt");
//        users.add("Gail  ");
//        users.add("buck");
//        users.add("buck");
//        users.add("bunnier");
//        users.add("Bolton");
//        users.add("Byron");
//        users.add("Xize");
//        users.add("Calvin");
//        users.add("Kaixi");
//        users.add("Cedric");
//        users.add("Cedric");
//        users.add("Chad Chad");
//        users.add("Chad Channing");
//        users.add("chebman");
//        users.add("Charles");
//        users.add("chazor");
//        users.add("Chester");
//        users.add("klester");
//        users.add("Christine");
//        users.add("Christopher");
//        users.add("Clare");
//        users.add("Clarence");
//        users.add("Clark");
//        users.add("Cleveland");
//        users.add("cliff");
//        users.add("Colin");
//        users.add("Conrad");
//        users.add("corner");
//        users.add("Curtis");
//        users.add("Cyril");
//        users.add("Danner");
//        users.add("Daniel");
//        users.add("Darcy");
//        users.add("Darnell");
//        users.add("Darren");
//        users.add("Duff");
//        users.add("David");
//        users.add("Dean");
//        users.add("Dennis");
//        users.add("dirrick");
//        users.add("dominidominic");
//        users.add("Dominic");
//        users.add("Dominic");
//        users.add("donahu");
//        users.add("Donald");
//        users.add("Douglas");
//        users.add("DURU");
//        users.add("Duke");
//        users.add("Duncan");
//        users.add("Donne");
//        users.add("DeWitt");
//        users.add("Dylan");
//        users.add("ed");
//        users.add("Eden");
//        users.add("Edgar");
//        users.add("Edison Eugene");

        users.add("");
        users.add(null);
        users.add("");
        users.add(null);
        users.add("");
        users.add(null);
        users.add("");
        users.add(null);
    }

    public static void consume() throws InterruptedException {
        init();
        while (true) {
            Thread.sleep(30);
            int index = RandomUtil.randomInt(users.size());
            String user = users.get(index);
            sendRequest(user);
        }
    }

    static long succCount = 0;
    static long errCount = 0;

    public static void sendRequest(String user) {
        HttpRequest request;
        if (user != null) {
            request = HttpUtil.createGet("http://node3/index" + "&user=" + user);
        } else {
            request = HttpUtil.createGet("http://node3/index");
        }
        try {
            TimeInterval timer = DateUtil.timer();
            HttpResponse response = request.execute();
            long diff = timer.intervalMs();
            if (response.isOk()) {
                log.info("metrics|{},{},{},{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, "latency", System.currentTimeMillis(), diff, "value");
                log.info("metrics|{},{},{},{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, "successQPS", System.currentTimeMillis(), ++succCount, "counter");
            } else {
                log.info("metrics|{},{},{},{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, "errorQPS", System.currentTimeMillis(), ++errCount, "counter");
            }

        } catch (Exception e) {
            log.info("metrics|{},{},{},{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, "errorQPS", System.currentTimeMillis(), ++errCount, "counter");
            log.error("error|{}", e.getMessage(), e);
        }


    }
}
