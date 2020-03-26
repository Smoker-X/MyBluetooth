package somker.pro.com.mybluetooth.model.network

import io.reactivex.Observable
import retrofit2.http.*
import somker.pro.com.mybluetooth.entity.ResultBean

/**
 * Created by Smoker on 2019/7/22.
 * 说明：接口地址文档
 *
 *
 * 注释：@QueryMap 是放在连接后面的
 * 注释：@Body     是在请求体的
 * 注释：@Headers("Content-Type: application/json; charset=utf-8") 是修改请求体里面的格式
 */
interface APIServer{

    /**
     * 获取token
     */
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getToken(@Url path:String ,@QueryMap params :MutableMap<String ,Any>): Observable<ResultBean<MutableMap<String, Any>>>

    /**
     * 获取token
     */
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("token/register.do")
    fun getToken(@QueryMap params :MutableMap<String ,Any>): Observable<ResultBean<MutableMap<String ,Any>>>


    /**
     * 登录接口
     */
    @POST("minter/register.do")
    fun <T>login(@QueryMap params :MutableMap<String ,Any>): Observable<ResultBean<T>>


    /**
     * 成绩包解析接口
     */
    @POST("an_offlineExam/import_v2.do")
    fun analyzeZip(@Body body:MutableMap<String ,Any> ,@QueryMap tokenMap:MutableMap<String ,Any>): Observable<ResultBean<MutableMap<String, Any>>>

}