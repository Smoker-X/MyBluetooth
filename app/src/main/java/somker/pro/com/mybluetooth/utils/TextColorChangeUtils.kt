package somker.pro.com.mybluetooth.utils

import android.content.Context
import android.content.res.ColorStateList
import android.support.annotation.XmlRes
import android.widget.TextView

/**
 * Created by Smoker on 2020/1/16.
 * 说明：点击更改TextView的字体颜色
 */
class TextColorChangeUtils {
    companion object{


        /**
         * 点击更改TextView的字体颜色
         */
        fun changeTextViewColor(context:Context ,tv:TextView , color:Int){
            val xrp = context.resources.getXml(color)
            try {
                val csl = ColorStateList.createFromXml(context.resources, xrp)
                tv.setTextColor(csl)
            } catch (e: Exception) {
            }

        }
    }
}