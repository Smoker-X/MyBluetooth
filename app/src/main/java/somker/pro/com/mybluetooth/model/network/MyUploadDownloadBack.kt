package somker.pro.com.mybluetooth.model.network

/**
 * Created by Smoker on 2019/11/25.
 * 说明：下载监听的回调
 */
abstract class MyUploadDownloadBack <T> : MyRequestBack<T>(){

    /**
     * 监听进度的改变
     * @param bytesWritten  已经发送的长度
     * @param contentLength 总长度
     */
    fun onProgressChange(bytesWritten: Long, contentLength: Long) {
        val progress = (bytesWritten * 100 / contentLength).toInt()
        onProgress(progress)
    }

    /**
     * 上传进度回调
     * @param progress
     */
    abstract fun onProgress(progress: Int)


    /**
     * 下载完成
     * 文件保存的地址
     */
    abstract fun onDownloadFinish(path:String)
}