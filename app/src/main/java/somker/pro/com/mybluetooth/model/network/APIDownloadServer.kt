package somker.pro.com.mybluetooth.model.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import somker.pro.com.mybluetooth.entity.ResultBean

/**
 * Created by Smoker on 2019/11/25.
 * 说明：
 */
interface APIDownloadServer {

    /**
     * 上传单个文件
     */
    @Multipart
    @POST("fserver/upload.do")
    fun  uploadFile(@Part body: MultipartBody.Part, @QueryMap params:MutableMap<String ,Any>):Observable<ResultBean<MutableMap<String, Any>>>


    /**
     * 上传多个文件
     */
    @Multipart
    @POST("fserver/multiUpload.do")
    fun <T> uploadFileList(@Part("files[]") files: RequestBody, @QueryMap params:MutableMap<String ,Any>):Observable<ResultBean<T>>

    /**
     * 文件下载
     *
     * @param url 文件的完整地址
     * @return
     */
    @Streaming
    @GET
    fun downloadFile(@Url url: String): Observable<ResponseBody>


    /**
     * 文件下载
     *
     * @param url    文件的完整地址
     * @param params 拼接在连接后面的参数
     * @return
     */
    @Streaming
    @GET
    fun downloadFileWithQueryMap(@Url url: String, @QueryMap params: MutableMap<String, Any>): Observable<ResponseBody>
}