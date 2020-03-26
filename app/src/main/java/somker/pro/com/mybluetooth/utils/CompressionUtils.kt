package somker.pro.com.mybluetooth.utils

import android.graphics.Bitmap
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils

/**
 * Created by Smoker on 2020/1/17.
 * 说明：图片压缩工具类
 */
class CompressionUtils {

    companion object{

        /**
         * 图片压缩
         * @param picPath  要压缩的图片
         * @param savePath 压缩后图片的保存位置
         */
        fun compressionPicture(picPath:String ,savePath:String){
            val head = ImageUtils.getBitmap(picPath)?:throw Throwable("图片文件不存在，请重新拍照")
            val compressHead = ImageUtils .compressByQuality(head ,20 ,true)?:throw Throwable("图片压缩失败，请重试")
            val result = ImageUtils.bytes2Bitmap(compressHead)?:throw Throwable("压缩图片转换失败，请重试")
            FileUtils.createFileByDeleteOldFile(savePath)
            val success = ImageUtils.save(result , savePath, Bitmap.CompressFormat.JPEG)
            if (!success){
                throw Throwable("图片保存失败，请重试")
            }
        }

    }
}