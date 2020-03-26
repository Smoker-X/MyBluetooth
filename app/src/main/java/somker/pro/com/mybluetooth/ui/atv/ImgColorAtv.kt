package somker.pro.com.mybluetooth.ui.atv

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import kotlinx.android.synthetic.main.atv_img_color.*
import somker.pro.com.mybluetooth.R
import somker.pro.com.mybluetooth.base.BaseActivity
import somker.pro.com.mybluetooth.mvp.i.IEmtpyView
import somker.pro.com.mybluetooth.mvp.p.EmtpyPresenter
import somker.pro.com.mybluetooth.ui.customise.colorpicker.OnColorChangerCallBack

/**
 * Created by Smoker on 2020/3/23.
 * 说明：图片获取颜色
 */
class ImgColorAtv : BaseActivity<IEmtpyView, EmtpyPresenter>() {

    /**
     * 当前界面的布局文件
     */
    override fun setLayoutId(): Int = R.layout.atv_img_color

    /**
     * 初始化Presenter
     */
    override fun initPresenter(): EmtpyPresenter = EmtpyPresenter()

    /**
     * 页面初始化之后执行的方法
     */
    override fun initLayoutAfter(savedInstanceState: Bundle?) {
        smokerColorView.setColorChangerListener(object : OnColorChangerCallBack {
            override fun onColorChanger(color: Int) {
                LogUtils.e("color ==>> $color")
                mTvColor.setBackgroundColor(color)
            }
        })

    }

}