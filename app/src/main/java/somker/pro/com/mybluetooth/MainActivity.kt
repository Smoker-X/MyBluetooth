package somker.pro.com.mybluetooth

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.android.synthetic.main.activity_main.*
import somker.pro.com.mybluetooth.mvp.i.IEmtpyView
import somker.pro.com.mybluetooth.mvp.p.EmtpyPresenter
import somker.pro.com.mybluetooth.base.BaseActivity
import android.content.IntentFilter
import android.os.Handler
import android.os.Message
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ConvertUtils
import somker.pro.com.mybluetooth.base.BaseRecyclerViewAdapter
import somker.pro.com.mybluetooth.entity.DeviceBean
import somker.pro.com.mybluetooth.ui.adapter.BluetoothDeviceListAdapter
import somker.pro.com.mybluetooth.ui.atv.BlueServiceAtv
import somker.pro.com.mybluetooth.ui.atv.ImgColorAtv
import somker.pro.com.mybluetooth.utils.AAAByteUtils
import somker.pro.com.mybluetooth.utils.MyPermissionUtils


class MainActivity : BaseActivity<IEmtpyView, EmtpyPresenter>(),
    BaseRecyclerViewAdapter.OnRecyclerViewItemClick {


    /**
     * 蓝牙适配器
     */
    lateinit var blueAdapter: BluetoothAdapter
    /**
     * 蓝牙广播
     */
    private val blueReceiver = MyBlueBoothReceiver()

    /**
     * 蓝牙列表适配器
     */
    private val adapter = BluetoothDeviceListAdapter()

    /**
     * 蓝牙列表
     */
    private val deviceList = mutableListOf<DeviceBean>()

    private val deviceMap = mutableMapOf<String, DeviceBean>()

    private lateinit var mHandler: MyHandler

    /**
     * 当前界面的布局文件
     */
    override fun setLayoutId(): Int = R.layout.activity_main

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
        mHandler = MyHandler(this)

        MyPermissionUtils.requestLocation(this, object : MyPermissionUtils.OnMyPermissionUtilsBack {
            override fun onGrantedPermission() {
                registeredReceiver()
                checkCanUserBluetooth()
                ClickUtils.applySingleDebouncing(arrayOf(chooseColor ,btnOpen, btnScan, btnStopScan, btnClose)) {
                    when (it.id) {
                        R.id.chooseColor -> {
                            ActivityUtils.startActivity(ImgColorAtv::class.java)
                        }
                        R.id.btnOpen -> {
                            if (!blueAdapter.isEnabled) {
                                //隐式打开蓝牙
                                blueAdapter.enable()
                            } else {
                                showInfo("蓝牙已经打开了")
                            }
                        }

                        R.id.btnScan -> {
                            if (!blueAdapter.isEnabled) {
                                showInfo("请先打开蓝牙")
                            } else {
                                scanBlueDevice()
                            }
                        }

                        R.id.btnStopScan -> {
                            if (!blueAdapter.isEnabled) {
                                showInfo("蓝牙未打开")
                            } else {
                                stopScan()
                            }
                        }


                        R.id.btnClose -> {
                            if (blueAdapter.isEnabled) {
                                blueAdapter.disable()
                            } else {
                                showInfo("蓝牙未打开，无需关闭")
                            }
                        }
                    }
                }
            }
        })
    }


    /**
     * 检测设备是否支持5.0
     */
    private fun checkCanUserBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LogUtils.e("当前设备支持蓝牙5.0")
            initBluetooth()
        } else {
            LogUtils.e("当前设备不支持蓝牙5.0")
            showErr("当前设备不支持蓝牙5.0")
        }
    }


    /**
     * 初始化蓝牙
     */
    private fun initBluetooth() {
        val blueManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        blueAdapter = blueManager.adapter
    }


    /**
     * 扫描蓝牙设备
     */
    private fun scanBlueDevice() {
        try {
            deviceMap.clear()
            val bleScanner = blueAdapter.bluetoothLeScanner ?: throw Exception("蓝牙扫描仪为空")
            bleScanner.startScan(scanBack)
            mHandler.sendEmptyMessageDelayed(200, 5 * 1000)
        } catch (e: Exception) {
            LogUtils.e("ble 扫描仪异常")
        }
    }

    /**
     * 停止扫描设备
     */
    private fun stopScan() {
        try {
            val bleScanner = blueAdapter.bluetoothLeScanner ?: throw Exception("蓝牙扫描仪为空")
            bleScanner.stopScan(scanBack)
        } catch (e: Exception) {
            LogUtils.e("ble 扫描仪异常")
        }
    }


    /**
     * 蓝牙扫描回调
     */
    private val scanBack = object : ScanCallback() {
        //当一个蓝牙ble广播被发现时回调
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            result?.let {
                LogUtils.e("bluetoothAddress == >${it.device.address}")
                val address = it.device.address
                if (deviceMap.containsKey(address)) {
                    val deviceBean = deviceMap[address]!!
                    deviceBean.rssi = it.rssi
                } else {
                    val deviceBean = DeviceBean()
                    deviceBean.blueDevice = it.device
                    deviceBean.rssi = it.rssi
                    deviceMap[it.device.address] = deviceBean
                }
                refreshRecycler()
            }
        }

        //批量返回扫描结果
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
        }

        //当扫描不能开启时回调
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            showErr("扫描失败,errorCode : $errorCode")
        }

    }


    /**
     * 刷新列表
     */
    private fun refreshRecycler() {
        deviceList.clear()
        deviceList.addAll(deviceMap.values)
        adapter.setDeviceList(deviceList)
    }

    /**
     * 列表点击事件
     */
    override fun onRecyclerViewItemClick(view: View, position: Int) {

        when (view.id) {
            R.id.btnConnect -> {
                BlueServiceAtv.openBlueServiceAtv(this ,deviceList[position].blueDevice)
            }

            R.id.btnDisConnect -> {

            }

            R.id.btnSent -> {

            }
        }
    }



    /**
     * 注册广播
     */
    private fun registeredReceiver() {
        val intentFilter = IntentFilter()
        // 监视蓝牙关闭和打开的状态
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        // 监视蓝牙设备与APP连接的状态
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        // 注册广播
        registerReceiver(this.blueReceiver, intentFilter)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(this.blueReceiver)
    }


    /**
     * 蓝牙广播
     */
    class MyBlueBoothReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                it.action?.let { action ->
                    when (action) {
                        BluetoothAdapter.ACTION_STATE_CHANGED -> {
                            LogUtils.e("蓝牙正在活动中....")
                            when (it.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                                BluetoothAdapter.STATE_TURNING_ON -> {
                                    LogUtils.e("蓝牙正在打开....")
                                }

                                BluetoothAdapter.STATE_ON -> {
                                    LogUtils.e("蓝牙已经打开....")
                                }

                                BluetoothAdapter.STATE_TURNING_OFF -> {
                                    LogUtils.e("蓝牙正在关闭....")
                                }

                                BluetoothAdapter.STATE_OFF -> {
                                    LogUtils.e("蓝牙已经关闭....")
                                }
                            }

                        }
                        BluetoothDevice.ACTION_ACL_CONNECTED -> {
                            LogUtils.e("蓝牙设备已连接....")
                        }
                        BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                            LogUtils.e("蓝牙设备已断开....")
                        }
                    }


                }
            }

        }

    }


    class MyHandler(private var atv: MainActivity) : Handler() {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                200 -> {
                    LogUtils.e("扫描自动停止")
                    atv.stopScan()
                }
            }
        }
    }
}

fun main() {
    val result = AAAByteUtils.int2byte(55561535)
    val info = AAAByteUtils.bytes2HexString(result)
    println(info)
    println(ConvertUtils.bytes2HexString(result))
    println(AAAByteUtils.byte2int(result))
}
