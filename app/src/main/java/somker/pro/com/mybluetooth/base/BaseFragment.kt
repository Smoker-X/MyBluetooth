package somker.pro.com.mybluetooth.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import somker.pro.com.mybluetooth.R
import somker.pro.com.mybluetooth.ui.dialog.LoadingDialog

/**
 * Created by Smoker on 2019/7/19.
 * 说明：基类Fgm
 */
@Suppress("UNCHECKED_CAST")
abstract class  BaseFragment < V: BaseView,P : BasePresenter<V>>  : Fragment() , BaseView {

    protected var isInit = false
    private var loadingDialog : LoadingDialog?= null

    private lateinit var mView: View

    /**
     * 当前presenter
     */
    protected lateinit var mPresenter: P
    protected lateinit var mDialog:AlertDialog
    /**
     * 当前界面的布局文件
     */
    @LayoutRes
    abstract fun setLayoutId(): Int

    /**
     * 初始化Presenter
     */
    abstract fun initPresenter(): P


    /**
     * 页面初始化之后执行的方法
     */
    abstract fun initLayoutAfter(savedInstanceState: Bundle?)


    private fun initDialog(){
        context?.let {
            mDialog = AlertDialog.Builder(it).setTitle("温馨提示")
                .setMessage("")
                .setPositiveButton("知道了",null)
                .setCancelable(false)
                .create()
        }

    }


    private fun showDialog(){
        hintProgress()
        if (!mDialog.isShowing){
            mDialog.show()
        }
    }

    protected fun hideDialog(){
        if(mDialog.isShowing){
            mDialog.dismiss()
        }
    }
    /**
     * 显示Fragment
     * @param showFragment
     * @param containerId
     * @param tag
     */
    fun showFragment(showFragment: Fragment, containerId: Int, tag: String) {
        val ft = childFragmentManager.beginTransaction()
        if (showFragment.isAdded) {
            ft.show(showFragment)
        } else {
            ft.add(containerId, showFragment, tag)
        }
        ft.commit()
    }


    /**
     * 隐藏Fragment
     *
     * @param hideFragment
     */
    fun hideFragment(hideFragment: Fragment?) {
        val ft = childFragmentManager.beginTransaction()
        if (hideFragment == null) {
            return
        }
        if (hideFragment.isAdded) {
            ft.hide(hideFragment)
        }
        ft.commit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(setLayoutId(), container, false)
        return mView
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BaseRxApiManager.instance.cancel(this.context!!)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDialog()
        mPresenter = initPresenter()
        mPresenter.attachView(this as V)
        isInit = true
        initLayoutAfter(savedInstanceState)
    }


    /**
     * 返回当前所引用的上下文
     */
    override fun mContext(): Context = this.context!!

    /**
     * 显示信息 dialog
     *
     * @param msg
     */
    override fun showInfo(msg: String) {
        mDialog.setIcon(R.mipmap.ic_wrong)
        mDialog.setMessage(msg)
        showDialog()
    }

    /**
     * 显示错误信息
     *
     * @param msg
     */
    override fun showErr(msg: String) {
        mDialog.setIcon(R.mipmap.ic_error)
        mDialog.setMessage(msg)
        showDialog()
    }

    /**
     * 显示成功
     */
    override fun showSuccess(str: String) {
        mDialog.setIcon(R.mipmap.ic_success)
        mDialog.setMessage(str)
        showDialog()
    }

    /**
     * 显示警告
     *
     * @param wrong
     */
    override fun showWrong(wrong: String) {
        mDialog.setIcon(R.mipmap.ic_wrong_yellow)
        mDialog.setMessage(wrong)
        showDialog()
    }

    /**
     * Toast 提示
     *
     * @param msg
     */
    override fun showToast(msg: String) {
        ToastUtils.showShort(msg)
    }


    /**
     * 显示进度框
     *
     * @param msg
     */
    override fun showProgress(msg: String) {
        if(null == this.loadingDialog){
            context?.let {
                this.loadingDialog = LoadingDialog(it)
                this.loadingDialog?.show()
                this.loadingDialog?.setTxtInfo(msg)
            }
        }else{
            this.loadingDialog?.let {
                if (!it.isShowing){
                    it.show()
                }
                it.setTxtInfo(msg)
            }
        }
    }

    /**
     * 隐藏进度框
     */
    override fun hintProgress() {
        this.loadingDialog?.let {
            if (it.isShowing){
                it.dismiss()
            }
        }
    }
}