package somker.pro.com.mybluetooth.base

import android.text.TextUtils
import somker.pro.com.mybluetooth.constant.APPConstant
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils


/**
 * Created by Smoker on 2019/7/19.
 * 说明：Presenter 基类
 */
abstract class BasePresenter<V: BaseView> {


    lateinit var mView: V

    /**
     * 初始化Presenter
     */
    fun attachView(view: V) {
        this.mView = view
    }

    /**
     * 取消引用
     */
    fun detachView() {
    }

    /**
     * 获取基础参数
     */
    fun getBaseParams(): MutableMap<String, Any> {
        val params = HashMap<String, Any>()
        val mToken = BaseApplication.getInstance().token
        if (!TextUtils.isEmpty(mToken)) {
            params["access_token"] = mToken
        }
        params["app_id"] = "0001"
        return  params
    }

    /**
     * 获取带有设备信息的参数--一般请求token用到
     */
    fun getDeviceInfoParams(): MutableMap<String, Any> {
        val params = getBaseParams()

        params["deviceType"] = APPConstant.deviceType
        params["deviceDesc"] = APPConstant.deviceDesc
        params["deviceOSDesc"] = ""
        params["appName"] = "yun_cloud"
        params["loginName"] = "yunexam_android"
        params["password"] = ""
        params["extcode"] = ""
        params["deviceID"] =DeviceUtils.getUniqueDeviceId()
        params["appVersion"] = AppUtils.getAppVersionCode()

        return params
    }
}
