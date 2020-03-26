package somker.pro.com.mybluetooth.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import somker.pro.com.mybluetooth.R

/**
 * Created by Smoker on 2019/10/10.
 * 说明：dialog 基类
 */
abstract class BaseDialog : Dialog {

    protected var mView :View

    protected lateinit var listener: DialogClickListener

    constructor(context: Context) : super(context, R.style.BaseDialogStyle){
        mView = LayoutInflater.from(context).inflate(this.initLayout() ,null)
    }

    constructor(context: Context ,themeResId :Int):super(context ,themeResId){
        mView = LayoutInflater.from(context).inflate(this.initLayout(),null)
    }

    fun addDialogBtnClickListener(@Nullable listener: DialogClickListener){
        this.listener = listener
    }


    protected abstract fun initLayout(): Int

    protected abstract fun initAfter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mView)
        this.isClickBackDismissDialog(true)//默认点击返回键关闭dialog
        this.isClickOutsideDismissDialog(false)//默认点击dialog外的区域不关闭dialog
        setAnim(R.style.pop_and_dialog_anim_style)//默认动画
        this.initAfter()

    }


    /**
     * 是否可以点击返回键来管理dialog
     *
     * @param flag
     */
     fun isClickBackDismissDialog(flag: Boolean) {
        setCancelable(flag)
    }


    /**
     * 是否点击dialog以外的区域消失
     *
     * @param flag
     */
    fun isClickOutsideDismissDialog(flag: Boolean) {
        setCanceledOnTouchOutside(flag)
    }


    /**
     * 设置dialog的显示和退出动画
     *
     * @param res
     */
    protected fun setAnim(res: Int) {
        val window = window
        window!!.setWindowAnimations(res)
    }

    /**
     * 触发回调
     */
    fun clickComeBack(view: View,comeBackInfo:Any){
        if (this::listener.isInitialized){
            this.listener.onDialogBtnClickBack(view ,comeBackInfo)
        }

    }

    /**
     * 设置dialog的大小 //在show（）;之后调用
     * @param width
     * @param height
     */
    protected fun setDialogWindowAttr(width: Int, height: Int) {
        val window = this.window
        val lp = window!!.attributes
        lp.gravity = Gravity.CENTER
        lp.width = width//宽高可设置具体大小
        if (height != -1) {
            lp.height = height
        }
        window.attributes = lp
    }


    interface DialogClickListener{
        fun onDialogBtnClickBack(view: View ,comeBackInfo:Any)
    }
}