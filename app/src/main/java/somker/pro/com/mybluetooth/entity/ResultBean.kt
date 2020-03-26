package somker.pro.com.mybluetooth.entity

import java.io.Serializable

/**
 * Created by smoker on 2016/12/14.
 * 说明：接口返回的所用信息bean
 */
class ResultBean<T> : Serializable {


    var message: String = ""


    var data: T? = null


    var code: Int = -1


    var success: Boolean = false


}
