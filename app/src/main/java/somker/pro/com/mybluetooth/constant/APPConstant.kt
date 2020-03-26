package somker.pro.com.mybluetooth.constant

import com.blankj.utilcode.util.*
import java.io.File


/**
 * Created by Smoker on 2019/7/22.
 * 说明：常量类
 */
class APPConstant private constructor(){

    companion object{

        fun getInstance(): APPConstant {
            return APPConstantHolder.appConstant
        }

        var mainPath = PathUtils.getExternalStoragePath() + "/cloudExam/smoker/"

        /*包裹的这个几个参数每个app版本不一样的*/
        const val appId = "safety_exam"//"cgn_exam"// 系统应用ID ,每个公司版本去检查是否要更新apk所携带的参数

        const val deviceType = "Android"//设备类型

        const val deviceDesc = "android_pad"//设备描述  （设备 + 操作系统）

        /**
         * 考评员
         * */
        const val roleTea = "teacher"
        /**
         * 考务负责人*/
        const val roleManager = "manager"

    }

    private object APPConstantHolder {
        val appConstant = APPConstant()
    }


    /**
     * 创建文件夹
     */
    fun createMainFolder(){
        val isCreate = SPUtils.getInstance().getBoolean("isCreateFile" ,false)
        createTBSFile()

        if (isCreate){
            LogUtils.e("已经创建了文件")
            return
        }else{
            LogUtils.e("开始创建文件")
        }

        var successCreate = FileUtils.createOrExistsFile("$mainPath/exam/lib/info.data")
        LogUtils.e("创建 lib 文件夹状态: $successCreate")

        successCreate = FileUtils.createOrExistsFile("$mainPath/exam/offline/info.data")
        LogUtils.e("创建 offline 文件夹状态: $successCreate")

        successCreate = FileUtils.createOrExistsFile("$mainPath/exam/online/info.data")
        LogUtils.e("创建 online 文件夹状态: $successCreate")

        successCreate = FileUtils.createOrExistsFile("$mainPath/result/cache/info.data")
        LogUtils.e("创建 cache 文件夹状态: $successCreate")

        successCreate = FileUtils.createOrExistsFile("$mainPath/result/$\$PC_data$$/info.data")
        LogUtils.e("创建 $\$PC_data$$ 文件夹状态: $successCreate")

        successCreate = FileUtils.createOrExistsFile("$mainPath/history/info.data")
        LogUtils.e("创建 history 文件夹状态: $successCreate")

        successCreate = FileUtils.createOrExistsFile("$mainPath/crash/info.data")
        LogUtils.e("创建 crash 文件夹状态: $successCreate")

        FileUtils.notifySystemToScan("$mainPath/exam/lib/info.data")
        FileUtils.notifySystemToScan("$mainPath/exam/offline/info.data")
        FileUtils.notifySystemToScan("$mainPath/exam/online/info.data")
        FileUtils.notifySystemToScan("$mainPath/result/cache/info.data")
        FileUtils.notifySystemToScan("$mainPath/result/$\$PC_data$$/info.data")
        FileUtils.notifySystemToScan("$mainPath/history/info.data")
        FileUtils.notifySystemToScan("$mainPath/crash/info.data")

        FileUtils.delete("$mainPath/exam/lib/info.data")
        FileUtils.delete("$mainPath/exam/offline/info.data")
        FileUtils.delete("$mainPath/exam/online/info.data")
        FileUtils.delete("$mainPath/result/cache/info.data")
        FileUtils.delete("$mainPath/result/$\$PC_data$$/info.data")
        FileUtils.delete("$mainPath/history/info.data")
        FileUtils.delete("$mainPath/crash/info.data")

        SPUtils.getInstance().put("isCreateFile" ,true)
    }


    private fun createTBSFile(){
        val path = getTbsTestFilePath()
        val xlsxName =  getTbsExcelName()
        if (!FileUtils.isFileExists(path + xlsxName)){
            LogUtils.e("没有${xlsxName} ,正在创建")
            ResourceUtils.copyFileFromAssets(xlsxName ,path + xlsxName)
        }

        val docxName =  getTbsWordName()
        if (!FileUtils.isFileExists(path + docxName)){
            LogUtils.e("没有${docxName} ,正在创建")
            ResourceUtils.copyFileFromAssets(docxName ,path + docxName)
        }
        val pdfName =  getTbsPdfName()
        if (!FileUtils.isFileExists(path + pdfName)){
            LogUtils.e("没有${pdfName} ,正在创建")
            ResourceUtils.copyFileFromAssets(pdfName ,path + pdfName)
        }
    }


    fun getLibPath():String{
        return "$mainPath/exam/lib/"
    }

    fun getOfflinePath():String{
        return "$mainPath/exam/offline/"
    }

    /**
     * 获取压缩包检测名字时，临时解压的地址
     */
    fun getZipCheckPath():String{
        return "$mainPath/exam/zipCheck/"
    }



    fun getOnlinePath():String{
        return "$mainPath/exam/online/"
    }

    fun getCachePath():String{
        return "$mainPath/result/cache/"
    }

    fun getPCDataPath():String{
        return "$mainPath/result/$\$PC_data$$/"
    }

    fun getHistoryPath():String{
        return "$mainPath/history/"
    }

    fun getCrashPath():String{
        return "$mainPath/crash/"
    }

    fun getZipPath():String{
        return "$mainPath/result/scoreLib/"
    }

    /**
     * 获取离线包试题附件地址
     */
    fun getExamFilePath(libId:String):String{
        return "$mainPath/exam/lib/${libId}/examfile/"
    }

    /**
     * 获取创建成绩包的临时地址
     */
    fun getCreateZipPath(libId:String):String{

        return "$mainPath/result/scoreLib/$libId/"
    }

    /**
     * 获取TBS 测试文件存放地址
     */
    fun getTbsTestFilePath() = PathUtils.getExternalStoragePath() + File.separator + "TBScloud" + File.separator
    fun getTbsExcelName() = "tbs_excel.xlsx"
    fun getTbsWordName() = "tbs_word.docx"
    fun getTbsPdfName() = "tbs_pdf.pdf"

}