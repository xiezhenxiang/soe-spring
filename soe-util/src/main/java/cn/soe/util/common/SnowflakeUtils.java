package cn.soe.util.common;

import cn.soe.util.common.bean.Snowflake;

import static cn.soe.util.common.SystemUtils.getLocalIp;
import static cn.soe.util.common.SystemUtils.getLocalMac;

/**
 * 雪花算法工具类
 * @author xiezhenxiang 2023/5/23
 **/
public class SnowflakeUtils {

    private static final Snowflake SNOW_FLAKE;

    static {
        int dataCentId = StringUtils.elfHash(getLocalMac()) % 32;
        int workId = StringUtils.elfHash(getLocalIp()) % 32;
        SNOW_FLAKE = new Snowflake(dataCentId, workId);
    }


    public static Long nextId() {
        return SNOW_FLAKE.nextId();
    }

    public static String nextStrId() {
        return String.valueOf(nextId());
    }
}