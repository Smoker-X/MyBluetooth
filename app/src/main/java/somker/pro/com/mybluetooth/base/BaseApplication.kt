package somker.pro.com.mybluetooth.base

import android.app.Application
import android.support.multidex.MultiDex
import android.view.Gravity
import somker.pro.com.mybluetooth.constant.APPConstant
import somker.pro.com.mybluetooth.model.db.GreenDaoManager
import com.blankj.utilcode.util.*
import somker.pro.com.mybluetooth.BuildConfig

/**
 * Created by Smoker on 2019/7/18.
 * 说明：自定义 BaseApplication
 */
class BaseApplication : Application() {

    var token = ""


    companion object {
        private lateinit var instance: BaseApplication

        fun getInstance(): BaseApplication {
            return instance
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        // 主要是添加下面这句代码
        MultiDex.install(this)
        if(BuildConfig.DEBUG){
            LogUtils.getConfig()
                .setLogSwitch(true)
                .setConsoleSwitch(true)
                .setGlobalTag("smoker_log")
                .setLogHeadSwitch(true)
                .setBorderSwitch(false)
                .setSingleTagSwitch(false).saveDays = 1
        }
        ToastUtils.setGravity(Gravity.CENTER ,0 ,0)
        CrashUtils.init(APPConstant.getInstance().getCrashPath())




    }


    /**
     * 获取离线包id
     */
    fun getLibId():String = SPUtils.getInstance().getString("lib_id" ,"")


    /**
     * 是否添加考生流程
     */
    fun isAddMode():Boolean{
        return SPUtils.getInstance().getBoolean("is_add_user_mode" ,false)
    }

    /**
     * 保存当前模式是添加考生流程
     */
    fun saveAddMode(b:Boolean){
        SPUtils.getInstance().put("is_add_user_mode" ,b)
    }


    /**
     * 是否扫描考生准考证号进入的考评
     */
    fun isScanQRMode():Boolean{
        return SPUtils.getInstance().getBoolean("is_scan_qr_exam" ,false)
    }

    /**
     * 保存当前是否 扫描考生准考证号进入的考评
     */
    fun saveScanQRMode(b:Boolean){
        SPUtils.getInstance().put("is_scan_qr_exam" ,b)
    }

    /**
     * 是否有TBS内核
     */
    fun hasTBSX5():Boolean = SPUtils.getInstance().getBoolean("have_load_x5" ,false)

    /**
     * 加载TBS 内核完成
     */
    fun loadTBSX5Finish(){
        SPUtils.getInstance().put("have_load_x5" ,true)
    }


    override fun onTerminate() {
        super.onTerminate()
        GreenDaoManager.getInstance().closeDao()
    }
}