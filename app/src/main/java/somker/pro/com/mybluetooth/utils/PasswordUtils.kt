package somker.pro.com.mybluetooth.utils

import android.annotation.SuppressLint
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import java.math.BigDecimal
import java.text.SimpleDateFormat

/**
 * Created by Smoker on 2020/1/9.
 * 说明：考评负责人 删除 密码的生成
 * 规则: 生成一个4位数的随机数（1000 ~ 9999）作为验证码
 *      （年月日时 + 验证码）/ 验证码各个位数之和
 *          然后取小数点前6位数作为密码
 *
 *          验证码对应考生，时间不到，不刷新
 *          输入3次错误将失效
 *          加个时间验证，半小时验证码失效
 */
class PasswordUtils {

    companion object{

        private const val ENTRY_MAX = 3

        /**
         * 生成验证码
         * @param teaId 考评员id
         * @param stuId 考生id
         */
        fun createRandom(teaId:String ,stuId:String):String{
            val psdBean = getCachePasswordBean(teaId ,stuId)
                return if (null == psdBean){//说明没有符合条件的bean
                    val random = (1000..9999).random()
                    val createDate = TimeUtils.getNowMills()
                    val bean = PasswordBean(teaId ,stuId ,random.toString() ,createDate ,0)
                    savePassword2Cache(bean)
                    random.toString()
            }else{//说明验证码还有效
                psdBean.code
            }
        }


        /**
         * 检验验证码剩余可输入多少次
         * @param teaId 考评员id
         * @param stuId 考生id
         * @return Int
         */
        fun checkCodeValid(teaId:String ,stuId:String):Int{
            val psdBean = getCachePasswordBean(teaId ,stuId)
            return if ( null == psdBean){
                0
            }else return if (psdBean.inputCount < ENTRY_MAX){
                ENTRY_MAX - psdBean.inputCount
            }else{
                0
            }
        }



        /**·
         * 根据验证码 创建密码
         * 在创建密码的时候，校验验证码是否有效
         * 每次生成一次密码，当做用户输入了一次密码
         * @param random 验证码
         * @param teaId 考评员id
         * @param stuId 考生id
         * @return password 生成密码
         */
        @SuppressLint("SimpleDateFormat")
        fun createPassword(random:Int ,teaId:String ,stuId:String):String{
            val bean = getCachePasswordBean(teaId ,stuId) ?: return ""
            val date = TimeUtils.getNowString(SimpleDateFormat("yyyyMMddHH")).toInt()
            val a = random / 1000
            val b = random % 1000 / 100
            val c = random % 1000 % 100 / 10
            val d = random % 1000 % 100 % 10
            val sum = BigDecimal(a + b + c +d)
            val value = BigDecimal(date).add(BigDecimal(random)).divide(sum ,2 ,BigDecimal.ROUND_HALF_DOWN)
            return if (null == value){
                ""
            }else{
                bean.inputCount +=1
                savePassword2Cache(bean)
                getFront6(value)
            }

        }


        /**
         * 获取小数点前6位数
         */
        private fun getFront6(value:BigDecimal):String{
            val key = String.format("000000%s",value.toString())
            val dotIndex = key.indexOf('.')
            return if (dotIndex == -1){
                key.substring(key.length - 6 ,key.length + 1)
            }else{
                key.substring(dotIndex - 6 ,dotIndex)
            }
        }

        /**
         * 获取缓存的密码bean
         * @param teaId 考评员id
         * @param stuId 考生id
         */
        private fun getCachePasswordBean(teaId:String ,stuId:String): PasswordBean?{
            val json = SPUtils.getInstance().getString("pwd_bean" ,null) ?: return null
            val bean = GsonUtils.fromJson(json  , PasswordBean::class.java) ?: return null
             if (bean.teaId == teaId && bean.stuId == stuId){
                if (bean.inputCount >= ENTRY_MAX){
                    LogUtils.e("输入次数已经超过最大限制次数，验证码无效")
                    return null
                }
                val nowDate = TimeUtils.getNowMills()
                val time = TimeUtils.getTimeSpan(nowDate ,bean.createDate,TimeConstants.MIN)
                 return if (time > 30){
                     LogUtils.e("时间相差半小时以上，验证码无效")
                     null
                 }else{
                     LogUtils.e("时间相差在有效范围内，验证码有效")
                     bean
                 }
            }else{
                 LogUtils.e("考评员 或者 考生对不上，验证码无效")
                 return null
            }
        }

        /**
         * 清除验证码
         */
        fun clearCode(){
            SPUtils.getInstance().remove("pwd_bean")
        }
        /**
         * 将创建的密码数据保存起来
         */
        private fun savePassword2Cache(bean: PasswordBean){
            SPUtils.getInstance().put("pwd_bean" ,GsonUtils.toJson(bean))
        }
    }


    private data class PasswordBean(
        var teaId:String ,
        var stuId:String ,
        var code :String ,
        var createDate :Long ,
        var inputCount :Int

    )
}


