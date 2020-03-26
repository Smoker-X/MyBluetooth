package somker.pro.com.mybluetooth.base

import android.content.Context

/**
 * Created by Smoker on 2019/7/19.
 * 说明：baseView基类
 */
interface BaseView {
    /**
     * 返回当前所引用的上下文
     */
    fun mContext():Context
    /**
     * 显示信息 dialog
     *
     * @param msg
     */
    fun showInfo(msg: String)

    /**
     * 显示错误信息
     *
     * @param msg
     */
    fun showErr(msg: String)

    /**
     * 显示成功
     */
    fun showSuccess(str: String)

    /**
     * 显示警告
     *
     * @param wrong
     */
    fun showWrong(wrong: String)

    /**
     * Toast 提示
     *
     * @param msg
     */
    fun showToast(msg: String)

    /**
     * 显示进度框
     * @param msg
     */
    fun showProgress(msg: String)

    /**
     * 隐藏进度框
     */
    fun hintProgress()



}