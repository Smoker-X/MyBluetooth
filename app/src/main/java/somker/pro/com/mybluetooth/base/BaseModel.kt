package somker.pro.com.mybluetooth.base

import android.text.TextUtils
import android.util.Log
import somker.pro.com.mybluetooth.model.network.API
import somker.pro.com.mybluetooth.model.network.BaseRequestBack
import somker.pro.com.mybluetooth.model.network.MyObserver
import somker.pro.com.mybluetooth.constant.APPConstant
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import somker.pro.com.mybluetooth.entity.ResultBean
import java.util.concurrent.TimeUnit

/**
 * Created by Smoker on 2019/7/22.
 * 说明：业务处理层基类
 */
abstract class BaseModel {

    /**
     * 接口文件
     */
    protected var mApi = API.instance.getAPIServer()

    fun refreshAPI(){
        mApi = API.instance.getAPIServer()
    }




    /**
     * 获取离线包
     */
    fun getLibId():String{
        val libId = BaseApplication.getInstance().getLibId()
        if (TextUtils.isEmpty(libId)){
            throw Throwable("未勾选离线包")
        }
        LogUtils.e("离线包id：${libId}")
        return libId
    }




    /**
     * 获取指定文件的json
     * ResultBean 的data 为 list 类型
     */
    fun <T>getFileResultList(path:String , type:Class<T>): ResultBean<ArrayList<T>>?{
        val json = FileIOUtils.readFile2String(path)
        return GsonUtils.fromJson<ResultBean<ArrayList<T>>>(json , GsonUtils.getType(ResultBean::class.java ,GsonUtils.getListType(type)))
    }


    /**
     * 请求的简单封装
     */
    protected fun <T> request(observable: Observable<ResultBean<T>>, tag: Any, back: BaseRequestBack<T>) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(MyObserver(tag, back))
    }


    /**
     * 请求的简单封装
     * 延迟一秒执行回调（避免过快请求。加载框闪烁效果）
     */
    protected fun <T> requestDelay(observable: Observable<ResultBean<T>>, tag: Any, back: BaseRequestBack<T>) {
        observable
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(MyObserver(tag, back))
    }


    /**
     * 请求时，token 无效，自动请求token ，三次无效后自己处理
     * 成功请求到token，将自动重新请求数据
     */
    protected fun <T>  requestRefreshToken(params: HashMap<String, Any>, tag: Any, back: BaseRequestBack<T>, requestBack: RequestCallBack<T>) {
        var errCount = 0

        Observable.just(params)
            .flatMap {
                it["access_token"] = SPUtils.getInstance().getString("token_id","")
                requestBack.getObservable(it)
                    .subscribeOn(Schedulers.newThread())
            }
            .subscribeOn(Schedulers.newThread())
            .flatMap {
                if (it.code == 900){
                    Log.e("===>>" ,"您还没有登录系统 或者 token失效")
                    Observable.error { TokenInvalidException() }
                }else{
                    Observable.just(it)
                }
            }
            .subscribeOn(Schedulers.newThread())
            .retryWhen {retry ->
                retry.flatMap { mThrow ->
                    if (mThrow is TokenInvalidException){
                        val loginParams =HashMap<String ,Any>()
                        loginParams["loginName"] =SPUtils.getInstance().getString("loginName","")
                        loginParams["password"] =SPUtils.getInstance().getString("password","")
                        loginParams["app_id"] = APPConstant.appId
                        loginParams["deviceType"] = "1"
                        loginParams["deviceID"] = "1"
                        loginParams["extcode"] = "safety_exam"
                        loginParams["mpp_id"] = 0
                        loginParams["s_client"] = "app"
                        loginParams["device_id"] = "0"
                        mApi.login<HashMap<String, Any>>(loginParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .flatMap {loginResult ->

                                if (loginResult.success){
                                    val newToken = loginResult.data?.get("token_id") as String
                                    BaseApplication.getInstance().token = newToken

                                }else{
                                    errCount +=1
                                }
                                /*重试3次，如果三次都不成功 ，需要重新登录*/
                                if (errCount > 3){
                                    Observable.error { ResetLoginException() }
                                }else{
                                    Observable.just(params)
                                }

                            }
                    }else{
                        Observable.error<T> { IllegalAccessException() }
                    }
                }

            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(MyObserver(tag, back))

    }

    /**
     * token 无效异常
     */
    open class TokenInvalidException : Exception()

    /**
     * 需要重新登录
     */
    open class ResetLoginException : Exception()

    interface RequestCallBack <T>{
        fun getObservable(params: HashMap<String, Any>): Observable<ResultBean<T>>
    }
}