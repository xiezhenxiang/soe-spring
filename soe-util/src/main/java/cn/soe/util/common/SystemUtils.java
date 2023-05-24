package cn.soe.util.common;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author xiezhenxiang 2022/3/8
 */
public class SystemUtils {

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取计算机MAC地址
    public static String getLocalMac(){
        try {
            NetworkInterface net = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            return macToStr(net.getHardwareAddress());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String macToStr(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (i != 0) {
                buffer.append("-");
            }
            // bytes[i]&0xff将有符号byte数值转换为32位有符号整数，其中高24位为0，低8位为byte[i]
            int intMac = bytes[i]&0xff;
            // toHexString函数将整数类型转换为无符号16进制数字
            String str = Integer.toHexString(intMac);
            buffer.append(str);
        }
        return buffer.toString().toUpperCase();
    }
}
