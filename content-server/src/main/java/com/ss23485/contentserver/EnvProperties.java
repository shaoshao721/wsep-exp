package com.ss23485.contentserver;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class EnvProperties {

    public static String LogFilePath;

    public static String PrometheusQueryInterfaceUrl;
    public static String PrometheusNamespace;
    public static String PrometheusHost;
    public static Integer PrometheusPort;

    public static String ELKEndPoint;
    public static String ELKNamespace;
    public static String ELKHost;
    public static Integer ELKPort;

    public static String ConsulNamespace;
    public static String ConsulHost;
    public static Integer ConsulPort;

    public static String Namespace;
    public static String PodName;
    public static String PodRole;
    public static String MainContainerName;

    public static List<EndPoint> connectedPodEndPoints;
    public static List<EndPoint> attackPodEndPoints;

    public static Integer Interval = 5000;


    public static String getEnv(String key, String defaultValue) {
        String ret = System.getenv(key);
        if (StrUtil.isBlank(ret)) {
            return defaultValue;
        }
        return ret;
    }

    public static String getEnvOrExit(String key) {
        String ret = System.getenv(key);
        if (ret == null) {
            log.error("Could not miss env properties: [{}], exit", key);
            System.exit(1);
        }
        return ret;
    }


    public static void initialize() {

        LogFilePath = getEnv(ConstValue.ENV_C_LOG_PATH, "log");
        PrometheusNamespace = getEnv(ConstValue.ENV_PROMETHEUS_NAMESPACE, "kube-system");
        PrometheusHost = getEnv(ConstValue.ENV_PROMETHEUS_HOST, "http://server.hhchat.cn");
        PrometheusPort = Integer.valueOf(getEnv(ConstValue.ENV_PROMETHEUS_PORT, "30001"));

        MainContainerName = PodName = getEnv(ConstValue.ENV_POD_NAME, "test_pod");
        PodRole = getEnv(ConstValue.ENV_POD_ROLE, "attacker");

        PrometheusQueryInterfaceUrl = getEndPoint(PrometheusHost, PrometheusNamespace, PrometheusPort) + "/api/v1/query";

        initializeNodePoint();

    }

    private static String getEndPoint(String host, String namespace, Integer port) {
        return StrUtil.format("http://{}.{}:{}", host, namespace, port);
    }


    private static void initializeNodePoint() {
        String connectedEndPointString = getEnv(ConstValue.ENV_CONNECTED_NODE, "");
        String attackEndPointString = getEnv(ConstValue.ENV_ATTACK_NODE, "");

        connectedPodEndPoints = new ArrayList<>();
        attackPodEndPoints = new ArrayList<>();

        if (StrUtil.isNotBlank(connectedEndPointString)) {
            for (String endPointString : connectedEndPointString.split(";")) {
                connectedPodEndPoints.add(new EndPoint(endPointString));
            }
        }

        if (StrUtil.isNotBlank(attackEndPointString)) {
            for (String endPointStr : attackEndPointString.split(";")) {
                attackPodEndPoints.add(new EndPoint(endPointStr));
            }
        }
    }


}
