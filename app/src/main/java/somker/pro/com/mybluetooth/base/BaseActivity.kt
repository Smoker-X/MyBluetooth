package somker.pro.com.mybluetooth.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import somker.pro.com.mybluetooth.R
import somker.pro.com.mybluetooth.ui.dialog.LoadingDialog

/**
 * Created by Smoker on 2019/7/18.
 * 说明：基类BaseActivity
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<V: BaseView,P : BasePresenter<V> >: AppCompatActivity(), BaseView {

    private var loadingDialog : LoadingDialog?= null

    /**
     * 当前显示的Fragment
     */
    private var fragment: Fragment? = null

    /**
     * 当前presenter
     */
    protected  lateinit var mPresenter: P

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
        mDialog = AlertDialog.Builder(this).setTitle("温馨提示")
            .setMessage("")
            .setPositiveButton("知道了",null)
            .setCancelable(false)
            .create()
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
     * 初始化状态栏
     */
    open fun initImmersionBar() {
       /* if (this is LoginAtv){
            ImmersionBar.with(this)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                .init()
        }else{
            ImmersionBar.with(this)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .init()
        }*/
        ImmersionBar.with(this)
            .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
            .fitsSystemWindows(true)
            .titleBar(R.id.mLayoutToolbar)
            .init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutId())
        initDialog()
        initImmersionBar()
        mPresenter = initPresenter()
        mPresenter.attachView(this as V)
        initLayoutAfter(savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        BaseRxApiManager.instance.cancel(this)
    }



    protected fun initToolbar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onClickToolbarCallBack(it)
        }
    }

    protected fun setToolbarTitle(title:CharSequence){
        supportActionBar?.title = title
    }

    open fun onClickToolbarCallBack(view:View){
        ActivityUtils.finishActivity(this)
    }

    /**
     * Fragment 的切换
     * @param showFragment
     * @param containerId
     * @param tag
     */
    protected fun showFragment(showFragment: Fragment, containerId: Int, tag: String) {
        showFragment(showFragment, fragment, containerId, tag)
        fragment = showFragment
    }

    /**
     * Fragment 的切换
     *
     * @param showFragment 需要显示的Fragment
     * @param hideFragment 需要隐藏的Fragment
     * @param containerId  Fragment的容器
     * @param tag          当前显示的Fragment的Tag
     */
    protected fun showFragment(showFragment: Fragment?, hideFragment: Fragment?, containerId: Int, tag: String) {
        if (showFragment == null) {
            return
        }
        if (showFragment === hideFragment) {
            LogUtils.e("==>> 相同的Fragment ，不执行显示跟隐藏")
            return
        }
        val ft = supportFragmentManager.beginTransaction()
        //ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
        if (hideFragment != null) {
            ft.hide(hideFragment)
        }
        if (showFragment.isAdded) {
            ft.show(showFragment)
        } else {
            ft.add(containerId, showFragment, tag)
        }
        ft.commit()
    }







    /**
     * Fragment 的切换
     * @param showFragment
     * @param containerId
     * @param tag
     * @param animType
     */
    protected fun showFragmentAnim(showFragment: Fragment, containerId: Int, tag: String ,animType:Int) {
        showFragmentAnim(showFragment, fragment, containerId, tag ,animType)
        fragment = showFragment
    }

    /**
     * Fragment 的切换
     *
     * @param showFragment 需要显示的Fragment
     * @param hideFragment 需要隐藏的Fragment
     * @param containerId  Fragment的容器
     * @param tag          当前显示的Fragment的Tag
     */
    private fun showFragmentAnim(showFragment: Fragment?, hideFragment: Fragment?, containerId: Int, tag: String ,animType:Int) {
        if (showFragment == null) {
            return
        }
        if (showFragment === hideFragment) {
            LogUtils.e("==>> 相同的Fragment ，不执行显示跟隐藏")
            return
        }
        val ft = supportFragmentManager.beginTransaction()
        if (animType > 0){
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
        }else{
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out, R.anim.slide_right_in, R.anim.slide_left_out)
        }
        if (hideFragment != null) {
            ft.hide(hideFragment)
        }
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
    protected fun hideFragment(hideFragment: Fragment?) {
        val ft = supportFragmentManager.beginTransaction()
        if (hideFragment == null) {
            return
        }
        if (hideFragment.isAdded) {
            ft.hide(hideFragment)
        }
        ft.commit()
    }



    /**
     * 移除Fragment
     *
     * @param removeFragment
     */
    protected fun removeFragment(removeFragment: Fragment?) {
        removeFragment?.let {
            val ft = supportFragmentManager.beginTransaction()
            ft.remove(it)
            ft.commit()
        }
    }

    /**
     * 返回当前所引用的上下文
     */
    override fun mContext(): Context = this

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
            this.loadingDialog = LoadingDialog(this)
            this.loadingDialog?.show()
            this.loadingDialog?.setTxtInfo(msg)
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