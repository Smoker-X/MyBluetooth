package somker.pro.com.mybluetooth.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Created by Smoker on 2020/3/23.
 * 说明：
 */
public class AAAByteUtils {
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    public static int byte2int(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        return (res[0] & 0xff) | ((res[1] << 8) & 0xff00) | ((res[2] << 24) >>> 8) | (res[3] << 24);// | 表示安位或
    }

    public static byte[] intToByteArray(int i) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);
        System.out.println("i:" + i);
        out.writeInt(i);
        byte[] b = buf.toByteArray();
        System.out.println("i:" + b);
        out.close();
        buf.close();
        return b;
    }

    /**
     * 将16位的short转换成byte数组
     *
     * @param s short
     * @return byte[] 长度为2
     */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * 注释：字节数组到short的转换！
     *
     * @param b
     * @return
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * 把byte[]转换成16进制进制字符串
     *
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * byte[]转换成bit
     *
     * @param bytes
     * @return
     */
    public static String bytesToBits(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(byteToBits(b));
        }
        return sb.toString();
    }

    /**
     * byte转换成8位bit
     *
     * @param b
     * @return
     */
    public static String byteToBits(byte b) {
        int z = b;
        z |= 256;
        String str = Integer.toBinaryString(z);
        int len = str.length();
        return str.substring(len - 8, len);
    }

    /**
     * 计算校验和
     *
     * @param bytes
     * @return
     */
    public static final int calculateCheckSum(byte[] bytes) {
        int sum = 0;
        for (byte b : bytes) {
            sum += (short) b;
        }
        return sum > 65535 ? (sum - 65535) : sum;
    }
}
