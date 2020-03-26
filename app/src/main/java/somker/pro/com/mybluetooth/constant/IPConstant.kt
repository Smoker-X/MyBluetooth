package somker.pro.com.mybluetooth.constant

import com.blankj.utilcode.util.SPUtils
import somker.pro.com.mybluetooth.base.BaseApplication


/**
 * Created by Smoker on 2019/7/22.
 * 说明：ip 地址常量初始化
 */
class IPConstant private constructor() {

    companion object{
        fun getInstance(): IPConstant {
            return IPConstantHolder.ipConstant
        }

        //http://gz.kaoxve.com:18865/BRService/
        /**
         * 默认的 http 协议
         */
        const val def_http_type = "http"

        /**
         * 默认的ip地址
         */
        const val def_http_ip = "gz.kaoxve.com"//"gz.kaoxve.com"


        /**
         * 默认的后台项目地址(服务端项目名字  当需要更换项目，但接口不变的情况下，修改此值即可)
         */
        const val def_http_project = "BRService"


        /**
         * 默认的端口号
         */
        const val def_http_port = 18823//18865
    }

    private object IPConstantHolder{
        val ipConstant = IPConstant()
    }

    /**
     * http 协议类型
     */
    fun getHttpType():String = SPUtils.getInstance().getString("http_type", def_http_type)

    /**
     * http ip地址
     */
    fun getHttpIp() :String= SPUtils.getInstance().getString("httpIp", def_http_ip)

    /**
     * http ip 地址所属项目
     */
    fun getHttpProject():String = SPUtils.getInstance().getString("httpProject", def_http_project)

    /**
     * http 端口号
     */
    fun getHttpPort():Int = SPUtils.getInstance().getInt("httpPort", def_http_port)

    fun saveHttpType(type:String){
        SPUtils.getInstance().put("http_type" ,type)
    }

    fun saveHttpIp(ip:String){
        SPUtils.getInstance().put("httpIp" ,ip)
    }

    fun saveHttpProject(project:String){
        SPUtils.getInstance().put("httpProject" ,project)
    }

    fun saveHttpPort(portStr:String){
        val port = portStr.toInt()
        SPUtils.getInstance().put("httpPort" ,port)
    }


    /**
     * 返回请求主路径
     */
    fun getRequestPath(): String {
        val sbP = StringBuffer()
        val proName = getHttpProject()
        if (proName.isBlank()){
            sbP.append(getHttpType())
                .append("://")
                .append(getHttpIp())
                .append(":")
                .append(getHttpPort())
                .append("/")
        }else{
            sbP.append(getHttpType())
                .append("://")
                .append(getHttpIp())
                .append(":")
                .append(getHttpPort())
                .append("/")
                .append(getHttpProject())
                .append("/")
        }

        return sbP.toString()
    }

    /**
     * 获取离线包下载地址
     */
    fun getLibDownloadPath(): String{
        return getRequestPath() + "fserver/view.do"
    }

    /**
     * 获取apk下载地址
     */
    fun getAPKDownloadPath(): String{
        return getRequestPath() + "fserver/download.do"
    }

    /**
     * 获取图片的地址
     */
    fun getImgPath(imgName: String): String {
        return StringBuilder(getRequestPath())
            .append("fserver/view.do?filename=")
            .append(imgName)
            .append("&access_token=")
            .append(BaseApplication.getInstance().token)
            .toString()
    }

}