package somker.pro.com.mybluetooth.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by Smoker on 2017/6/8.
 * 说明：数字转成 需要显示的样式
 */

public class NumUtils {


    static String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿"};
    static String[] numArray = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    /**
     * 将整数转换成汉字数字
     *
     * @param num 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatInteger(int num) {
        if (num == 0){
            return "0";
        }

        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                if ("十".equals(unit) && "一".equals(numArray[n]) && len == 2) {
                    //避免出现 一十一、一十二这样不是习惯行的叫法
                } else {
                    sb.append(numArray[n]);
                }

                sb.append(unit);
            }
        }
        return sb.toString();
    }

    /**
     * 将小数转换成汉字数字
     *
     * @param decimal 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatDecimal(double decimal) {
        String decimals = String.valueOf(decimal);
        int decIndex = decimals.indexOf(".");
        int integ = Integer.valueOf(decimals.substring(0, decIndex));
        int dec = Integer.valueOf(decimals.substring(decIndex + 1));
        String result = formatInteger(integ) + "." + formatFractionalPart(dec);
        return result;
    }

    /**
     * 格式化小数部分的数字
     *
     * @param decimal 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatFractionalPart(int decimal) {
        char[] val = String.valueOf(decimal).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int n = Integer.valueOf(val[i] + "");
            sb.append(numArray[n]);
        }
        return sb.toString();
    }

    /**
     * 格式化小数的格式 ，去掉后面的0
     * 例如 ：1.2000  调用后变成 1.2
     * 例如：1.200100 调用后变成 1.2001
     * @param s
     * @return
     */
    public static String fmt_prt_double(String s) {
        //自定义格式化输出函数
        int i;
        for (i = s.length() - 1; i >= 0; i--) {
            //从串尾向前检查，遇到非0数据结束循环
            if (s.charAt(i) == '.') {
                //遇到小数点结束，说明是个整数
                break;
            }
            if (s.charAt(i) != '0') {
                //遇到小数中有非0值，结束
                i++;
                break;
            }
        }
        if (i < 0){
            return  s;
        }
        return s.substring(0, i); //返回处理后的子串
    }

    /**
     * 将一个double 保留一位小数，四舍五入
     * @param d
     * @return
     */
    public static String saveOneDec(double d){
        DecimalFormat fnum   =   new DecimalFormat("##0.0");
        String dd = fnum.format(d);
        return dd ;
    }

    /**
     * 将一个double 保留一位小数，舍去多余位数
     * @param d
     * @return
     */
    public static String saveOnDec2(double d){
        String str = String.valueOf(d);
        int position = str.indexOf(".") ;
        if(position + 1 < str.length() && position != -1){
            str = str.substring(0, position + 2) ;
        }else if(position != -1){
            str = str.substring(0, position + 1)  + "0";
        }

        return str ;
    }

    /**
     * 字符串转Double
     * @param str
     * @return
     */
    public static double StringToDouble(String str){
        if (TextUtils.isEmpty(str)){
            return 0 ;
        }

        if (str.length() == 1 && str.equals(".")){
            return 0 ;
        }

        Double d = Double.parseDouble(str);
        return d ;
    }


    /**
     * 根据秒转化成 10:20:05  时分秒
     * @param time
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String changeText(long time){
        int timeH = (int) (time / 60 / 60);
        int timeM = (int) (time % 3600) / 60;
        int timeS = (int) (time % 3600 % 60);

        return String.format("%02d", timeH) + ":" +
                String.format("%02d", timeM) + ":" +
                String.format("%02d", timeS);
    }


    /**
     * 根据秒转化成 10:20:05  时分秒
     * @param time      使用时间
     * @param restrict  限制的时间
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String changeText(long time ,long restrict){
        StringBuilder stringBuffer = new StringBuilder();
        long remainT = Math.abs(restrict - time);
        if (restrict - time <  0){
            stringBuffer.append("- ");
        }
        int timeH = (int) (remainT / 60 / 60);
        int timeM = (int) (remainT % 3600) / 60;
        int timeS = (int) (remainT % 3600 % 60);

        stringBuffer.append(String.format("%02d", timeH)).append(":")
                .append(String.format("%02d", timeM)).append(":")
                .append(String.format("%02d", timeS)) ;
        return stringBuffer.toString();
    }

    /**
     * 根据秒来计算小时
     * @param time
     * @return
     */
    public static int getHourOfDay(long time){
        return  (int) (time / 60 / 60);
    }

    /**
     * 根据秒来计算 几小时几分钟 里面的分钟
     * @param time
     * @return
     */
    public static int getMinute(long time){
        return (int) (time % 3600) / 60 ;
    }

    /**
     * 根据秒来计算等到 60进制是多少秒，比如90秒 ，其实是0:1:30(时：分：秒) ，获取里面的30
     * @param time
     * @return
     */
    public static int getSecond(long time){
        return (int) (time % 60) ;
    }

    /**
     * 根据时和分计算多少秒
     * @param hourOfDay
     * @param minute
     * @return
     */
    public static long getSecond(int hourOfDay ,int minute){
        return hourOfDay*60*60 + minute * 60 ;
    }


    /**
     * 根据时、分、秒来计算多少秒
     * @param hourOfDay
     * @param minute
     * @return
     */
    public static long getSecond(int hourOfDay ,int minute ,int second){
        return hourOfDay*60*60 + minute * 60  + second;
    }





}
