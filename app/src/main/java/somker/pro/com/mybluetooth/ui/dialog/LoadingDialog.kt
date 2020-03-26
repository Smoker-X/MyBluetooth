package somker.pro.com.mybluetooth.ui.dialog

import android.content.Context
import android.text.TextUtils
import android.view.View
import somker.pro.com.mybluetooth.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_loading.*
import somker.pro.com.mybluetooth.R

/**
 * Created by Smoker on 2019/10/10.
 * 说明：自定义的loading框
 */
class LoadingDialog(mContext: Context) : BaseDialog(mContext) {


    override fun initLayout(): Int = R.layout.dialog_loading


    override fun initAfter() {
        setCancelable(false)

    }

    fun setTxtInfo(str :CharSequence){
        if (TextUtils.isEmpty(str)){
            tv_dialogInfo.visibility = View.GONE
        }else{
            tv_dialogInfo.visibility = View.VISIBLE
            tv_dialogInfo.text = str
        }

    }

}