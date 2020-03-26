package somker.pro.com.mybluetooth.utils

/**
 * Created by Smoker on 2020/2/7.
 * 说明：时间格式工具类
 */
class TimeFormatUtils {
    companion object{

        /**
         * 根据秒转化成 10:20:05  时分秒
         * @param time
         * @return
         */
        fun changeText(time: Long): String {
            val stringBuffer = StringBuffer()
            val timeH = (time / 60 / 60).toInt()
            val timeM = (time % 3600).toInt() / 60
            val timeS = (time % 3600 % 60).toInt()

            stringBuffer.append(String.format("%02d", timeH)).append(":")
                .append(String.format("%02d", timeM)).append(":")
                .append(String.format("%02d", timeS))
            return stringBuffer.toString()
        }


        /**
         * 根据秒来计算小时
         * @param time
         * @return
         */
        fun getHourOfDay(time: Long): Int {
            return (time / 60 / 60).toInt()
        }

        /**
         * 根据秒来计算 几小时几分钟 里面的分钟
         * @param time
         * @return
         */
        fun getMinute(time: Long): Int {
            return (time % 3600).toInt() / 60
        }

        /**
         * 根据秒来计算等到 60进制是多少秒，比如90秒 ，其实是0:1:30(时：分：秒) ，获取里面的30
         * @param time
         * @return
         */
        fun getSecond(time: Long): Int {
            return (time % 60).toInt()
        }


        /**
         * 根据时、分、秒来计算多少秒
         * @param hourOfDay
         * @param minute
         * @return
         */
        fun getSecond(hourOfDay: Int, minute: Int, second: Int): Long {
            return (hourOfDay * 60 * 60 + minute * 60 + second).toLong()
        }
    }
}