package cn.soe.util.common.bean;

/**
 * SnowFlake唯一ID生成器, 生成的ID有64位，为一个Long型
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 1位标识，最高位是符号位，正数是0，负数是1，一般是正数，最高位是0
 * 41位时间截，（当前时间截 - 开始时间截）的差值，开始时间一般是开始使用的时间，由程序指定；可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69
 * 10位的节点ID，可以部署1024个节点，包括5位数据中心ID和5位机器ID
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 * 整体按照时间自增排序，分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，经测试，SnowFlake每秒能够产生25万ID左右
 * @author xiezhenxiang 2019/9/9
 */
public class Snowflake {

    /** 数据中心ID(0~31) */
    private final int dataCenterId;

    /** 工作机器ID(0~31) */
    private final int workerId;

    /** 开始时间截 (2019-01-01) */
    private final long startTime = 1684837278632L;

    /** 机器id所占的位数 */
    private final int workerIdBits = 5;

    /** 数据中心id所占的位数 */
    private final int dataCenterIdBits = 5;

    /** 序列在id中占的位数 */
    private final int sequenceBits = 12;


    /** 数据标识id向左移17位(12+5) */
    private final int datacenterIdShift = 17;

    /** 时间截向左移22位(5+5+12) */
    private final int timestampLeftShift = 22;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final int sequenceMask = ~(-1 << sequenceBits);

    /** 毫秒内序列(0~4095) */
    private static long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    public Snowflake(int dataCenterId, int workerId) {
        // 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
        long maxWorkerId = ~(-1L << workerIdBits);
        // 支持的最大数据标识id
        long maxDataCenterId = ~(-1L << dataCenterIdBits);
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        // 如果当前时间小于上一次ID生成的时间，说明系统时间回拨过，避免ID重复抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("time moved backwards. refusing to generate id for skip %d milliseconds", lastTimestamp - timestamp));
        }
        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞获得下一毫秒的时间戳
                timestamp = nextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }
        // 上次生成ID的时间截
        lastTimestamp = timestamp;
        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - startTime) << timestampLeftShift)
                | ((long) dataCenterId << datacenterIdShift)
                | ((long) workerId << sequenceBits)
                | sequence;
    }

    private long nextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }


    public static void main(String[] args) {
        int n = 0;
        Snowflake snowflake = new Snowflake(1, 1);
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 1000) {
            System.out.println(snowflake.nextId());
            n ++;
        }
        System.out.println(n);
    }
}