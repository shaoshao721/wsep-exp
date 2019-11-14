package com.ss23485.contentserver;

import java.util.regex.Pattern;

public class ConstValue {

    public static final String ROLE_DEFENDER = "defender";
    public static final String ROLE_ATTACKER = "attacker";
    public static final String ACTION_ATTACK = "attack";
    public static final String ACTION_CONNECT = "connect";

    public static final String UNIT_GB = "GB";
    public static final String UNIT_KB = "KB";
    public static final String UNIT_MB = "MB";

    public static final Long NODE_TYPE_DEFENDER = 1L;
    public static final Long NODE_TYPE_ATTACKER = 0L;

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final Pattern LimitStrPattern = Pattern.compile("^(\\d*\\.?\\d+)\\s*?(KB|MB|GB|kb|mb|gb)$");

    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final String DEFAULT_PAGE_SIZE_STR = DEFAULT_PAGE_SIZE.toString();

    // env
    public static final String ENV_POD_ROLE = "POD_ROLE";
    public static final String ENV_POD_NAME = "POD_NAME";
    public static final String ENV_C_LOG_PATH = "C_LOG_PATH";
    public static final String ENV_H_LOG_PATH = "H_LOG_PATH";
    public static final String ENV_PROMETHEUS_HOST = "PROMETHEUS_HOST";
    public static final String ENV_PROMETHEUS_PORT = "PROMETHEUS_PORT";
    public static final String ENV_PROMETHEUS_NAMESPACE = "PROMETHEUS_NAMESPACE";
    public static final String ENV_ELK_NAMESPACE = "ELK_NAMESPACE";
    public static final String ENV_ELK_HOST = "ELK_HOST";
    public static final String ENV_ELK_PORT = "ELK_PORT";

    public static final String ENV_ATTACK_NODE = "ATTACK_NODE_NAMES";
    public static final String ENV_CONNECTED_NODE = "CONNECTED_NODE_NAMES";
    public static final String EXPOSED_PORTS = "EXPOSE_PORTS";


}
