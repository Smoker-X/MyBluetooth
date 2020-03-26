package somker.pro.com.mybluetooth.ui.atv

import android.app.Activity
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.android.synthetic.main.atv_characteristic.*
import somker.pro.com.mybluetooth.R
import somker.pro.com.mybluetooth.base.BaseActivity
import somker.pro.com.mybluetooth.base.BaseRecyclerViewAdapter
import somker.pro.com.mybluetooth.mvp.i.IEmtpyView
import somker.pro.com.mybluetooth.mvp.p.EmtpyPresenter
import somker.pro.com.mybluetooth.ui.adapter.BlueCharacteristicAdapter
import java.util.ArrayList

/**
 * Created by Smoker on 2020/3/24.
 * 说明：特征通道列表
 */
class BlueCharacteristicAtv :BaseActivity<IEmtpyView ,EmtpyPresenter>() ,BaseRecyclerViewAdapter.OnRecyclerViewItemClick{

    private lateinit var list :MutableList<BluetoothGattCharacteristic>

    private val adapter = BlueCharacteristicAdapter ()

    companion object{
        fun openBlueCharacteristicAtv(atv:BaseActivity<*,*> ,list: MutableList<BluetoothGattCharacteristic>){
            val intent = Intent(atv ,BlueCharacteristicAtv::class.java)
            intent.putParcelableArrayListExtra("ch_list" , ArrayList(list))
            atv.startActivityForResult(intent ,1001)
        }
    }
    /**
     * 当前界面的布局文件
     */
    override fun setLayoutId(): Int = R.layout.atv_characteristic

    /**
     * 初始化Presenter
     */
    override fun initPresenter(): EmtpyPresenter = EmtpyPresenter()
    /**
     * 页面初始化之后执行的方法
     */
    override fun initLayoutAfter(savedInstanceState: Bundle?) {
        adapter.addClickListener(this)
        mRecycler.adapter = adapter
        list = intent.getParcelableArrayListExtra("ch_list")
        adapter.setDataList(list)
    }

    override fun onRecyclerViewItemClick(view: View, position: Int) {
        val chUUid = list[position].uuid.toString()
        val intent = Intent()
        intent.putExtra("chUUid" ,chUUid)
        setResult(Activity.RESULT_OK ,intent)
        ActivityUtils.finishActivity(this)
    }

}