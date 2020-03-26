package somker.pro.com.mybluetooth.base

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.*
import android.widget.PopupWindow
import somker.pro.com.mybluetooth.R

/**
 * Created by Smoker on 2020/2/9.
 * 说明：PopupWindow基类
 */
abstract class BasePopupWindow:PopupWindow{

    protected lateinit var mView:View

    protected var mContext: Context

    abstract fun initAfterViews()

    private var popupWidth: Int = 0
    private var popupHeight: Int = 0

    private var isChangeBack = true//是否变黑

    abstract fun initLayout(): Int

    abstract fun getAnimId(): Int

    constructor(context: Context) : super(context){
        this.mContext = context
        init()
        initAfterViews()
    }

    protected fun init() {
        mView = LayoutInflater.from(mContext).inflate(initLayout(), null)
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        animationStyle = getAnimId()
        isOutsideTouchable = false
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响背景
        setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.shape_transparent_bg))
        contentView = mView

        popConfig()
    }


    private fun popConfig() {
        //获取自身的长宽高
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        popupHeight = mView.measuredHeight
        popupWidth = mView.measuredWidth
    }


    override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
        if (isChangeBack) {
            setPopWindowBg(0.4f)
        }
    }

    override fun dismiss() {
        super.dismiss()
        if (isChangeBack) {
            setPopWindowBg(1f)
        }
    }

    protected fun setPopWindowBg(alpha: Float) {
        // 设置背景颜色变暗
        val lp = (mContext as Activity).window.attributes
        lp.alpha = alpha
        //避免华为机有些不会变黑
        (mContext as Activity).window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        (mContext as Activity).window.attributes = lp
    }



    /**
     * 设置显示在v正下方
     * @param v
     */
    open fun showDown(v: View) {

        val location = IntArray(2)
        v.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]

        if (Build.VERSION.SDK_INT >= 24) { //解决7.0 及以上的系统显示在控件下方异常情况
            val visibleFrame = Rect()
            v.getGlobalVisibleRect(visibleFrame)
            val height = v.resources.displayMetrics.heightPixels - visibleFrame.bottom
            setHeight(height)
            showAsDropDown(v, (v.width - popupWidth) / 2, y + v.height)
        } else {
            showAsDropDown(v, (v.width - popupWidth) / 2, 0)
        }
    }

    /**
     * 设置显示在v上方
     * @param v
     */
    fun showUp(v: View) {

        val location = IntArray(2)
        v.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]

        if (Build.VERSION.SDK_INT >= 24) { //解决7.0 及以上的系统显示在控件下方异常情况
            val visibleFrame = Rect()
            v.getGlobalVisibleRect(visibleFrame)
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            showAsDropDown(v, 10, -popupHeight + v.height / 2)
        } else {
            showAsDropDown(v, 10, -popupHeight + v.height / 2)
        }

    }

    /**
     * 设置显示在v上方（以v的中心位置为开始位置）
     * @param v
     */
    fun showUp2(v: View) {
        //获取需要在其上方显示的控件的位置信息
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        //在控件上方显示
        showAtLocation(
            v,
            Gravity.NO_GRAVITY,
            location[0] + v.width / 2 - popupWidth / 2,
            location[1] - popupHeight
        )
    }

    /**
     * 设置显示在v的右上
     * @param v
     */
    fun showUpRightTop(v: View) {
        //获取需要在其上方显示的控件的位置信息
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, location[0] + v.width, location[1] - popupHeight)
    }


    /**
     * 设置显示在v左上
     * @param v
     */
    fun showUpLeftTop(v: View) {
        //获取需要在其上方显示的控件的位置信息
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, location[0] - popupWidth, location[1] - popupHeight)
    }
}