package somker.pro.com.mybluetooth.ui.atv

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.android.synthetic.main.atv_blue_service.*
import somker.pro.com.mybluetooth.R
import somker.pro.com.mybluetooth.base.BaseActivity
import somker.pro.com.mybluetooth.base.BaseRecyclerViewAdapter
import somker.pro.com.mybluetooth.mvp.i.IBlueService
import somker.pro.com.mybluetooth.mvp.p.BlueServicePresenter
import somker.pro.com.mybluetooth.ui.adapter.BlueServiceAdapter
import somker.pro.com.mybluetooth.utils.GattUtils
import somker.pro.com.mybluetooth.utils.HexUtil
import java.lang.Exception

/**
 * Created by Smoker on 2020/3/24.
 * 说明：蓝牙设备连接 和 获取服务
 */
class BlueServiceAtv : BaseActivity<IBlueService, BlueServicePresenter>(),
    BaseRecyclerViewAdapter.OnRecyclerViewItemClick {


    /**
     * 需要操作的设备
     */
    private lateinit var blueDevice: BluetoothDevice

    private val adapter = BlueServiceAdapter()
    /**
     * GATT 协议
     */
    private lateinit var gatt: BluetoothGatt

    private var clickIndex = 0

    /**
     * 目标蓝牙拥有的服务列表
     */
    private val listService = mutableListOf<BluetoothGattService>()


    companion object {
        fun openBlueServiceAtv(context: Context, device: BluetoothDevice) {
            val intent = Intent(context, BlueServiceAtv::class.java)
            intent.putExtra("device", device)
            context.startActivity(intent)
        }
    }

    /**
     * 当前界面的布局文件
     */
    override fun setLayoutId(): Int = R.layout.atv_blue_service

    /**
     * 初始化Presenter
     */
    override fun initPresenter(): BlueServicePresenter = BlueServicePresenter()

    override fun onDestroy() {
        super.onDestroy()
        disConnectDevice()
    }

    /**
     * 页面初始化之后执行的方法
     */
    override fun initLayoutAfter(savedInstanceState: Bundle?) {
        try {
            blueDevice = intent.getParcelableExtra("device") as BluetoothDevice
            adapter.addClickListener(this)
            mRecycler.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
            AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage("蓝牙设备信息传入失败，请重试")
                .setPositiveButton("知道了", null)
                .setIcon(R.mipmap.ic_error)
                .setOnDismissListener {
                    ActivityUtils.finishActivity(this)
                }
                .setCancelable(false)
                .create()
        }


        ClickUtils.applySingleDebouncing(arrayOf(btnConnect, btnDisConnect)) {
            when (it.id) {
                R.id.btnConnect -> {
                    if (isConnect()) {
                        showInfo("已与设备连接，无需重复连接")
                    } else {
                        connectDevice()
                    }
                }
            }
        }
    }


    private fun isConnect(): Boolean {
        val manager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return when (manager.getConnectionState(blueDevice, BluetoothProfile.GATT)) {
            BluetoothProfile.STATE_CONNECTING,
            BluetoothProfile.STATE_CONNECTED -> {
                LogUtils.e("== >> 连接中 ，或者已经连接了")
                true
            }
            BluetoothProfile.STATE_DISCONNECTING,
            BluetoothProfile.STATE_DISCONNECTED -> {
                LogUtils.e("== >> 断开连接中 或者未连接")
                false
            }
            else -> false
        }
    }


    /**
     * 连接设备
     */
    private fun connectDevice() {
        gatt = blueDevice.connectGatt(this, true, mGattCallBack)
    }

    /**
     * 断开连接
     */
    private fun disConnectDevice() {
        if (this::gatt.isInitialized) {
            this.gatt.disconnect()
            this.gatt.close()
            LogUtils.e("断开GATT协议")
        }
    }

    /**
     * GATT 连接
     */
    private val mGattCallBack = object : BluetoothGattCallback() {

        /**
         * 蓝牙连接状态变化回调
         * 连接状态：
         * The profile is in disconnected state   *public static final int STATE_DISCONNECTED  = 0;
         * The profile is in connecting state     *public static final int STATE_CONNECTING    = 1;
         * The profile is in connected state      *public static final int STATE_CONNECTED    = 2;
         * The profile is in disconnecting state  *public static final int STATE_DISCONNECTING = 3;
         *
         */
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTING -> {
                        LogUtils.e("BluetoothGattCallback 正在连接中...")
                    }
                    BluetoothProfile.STATE_CONNECTED -> {
                        LogUtils.e("BluetoothGattCallback 连接成功")
                        gatt?.discoverServices()

                    }
                    BluetoothProfile.STATE_DISCONNECTING -> {
                        LogUtils.e("BluetoothGattCallback 断开中...")
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        LogUtils.e("BluetoothGattCallback 断开成功")
                    }
                }
            } else {
                LogUtils.e("BluetoothGattCallback GATT断开连接 ")
            }

        }

        /**
         *  发现服务回调
         */
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            runOnUiThread {
                getBlueService()
            }
        }

        /**
         * Characteristic 内容改变回调
         */
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            LogUtils.e("内容改变回调")
            val value = characteristic?.value?: ByteArray(0)
            val strValue = HexUtil.formatHexString(value ,true)
            LogUtils.e("内容改变回调 == >> $strValue")

        }

        /**
         * Characteristic 读取回调
         */
        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS){
                LogUtils.e("读取的回调 成功")
                characteristic?.let {
                    val info = HexUtil.formatHexString(it.value ,true)
                    LogUtils.e("读取的内容 ==>> $info")
                }
            }else{
                LogUtils.e("读取的回调 异常")
            }
        }

        /**
         * Characteristic 发送回调
         */
        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS){
                LogUtils.e("发送回调成功")
                val value = characteristic?.value?: ByteArray(0)
                val strValue = HexUtil.formatHexString(value ,true)
                LogUtils.e("发送回调 == >> $strValue")

            }else{
                LogUtils.e("发送回调失败")
            }
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
            super.onReliableWriteCompleted(gatt, status)
        }
    }

    /**
     * 获取蓝牙的服务
     */
    private fun getBlueService() {
        listService.clear()
        gatt.services.forEach { service ->
            service?.let { gattService ->
                listService.add(gattService)
            }
        }
        adapter.setDataList(listService)
    }

    /**
     * 获取指定服务下的特征通道
     */
    private fun getCharacteristicList(service:BluetoothGattService):MutableList<BluetoothGattCharacteristic>{
        val chList = mutableListOf<BluetoothGattCharacteristic>()
        service.characteristics.forEach {
            it?.let { ch->
                LogUtils.e("特征通道 ==>> ${ch.uuid}")
                chList.add(ch)
            }
        }
        return chList
    }


    override fun onRecyclerViewItemClick(view: View, position: Int) {
        clickIndex = position
        val chList = getCharacteristicList(listService[position])
        BlueCharacteristicAtv.openBlueCharacteristicAtv(this,chList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1001){
            val chUUid = data?.getStringExtra("chUUid")
            chUUid?.let {uuid ->
                val gattChar = getCharacteristic(uuid)
                gattChar?.let {
                    if (GattUtils.characteristicCanRead(it.properties)){
                        read2Characteristic(it)
                    }
                    if (GattUtils.characteristicCanWrite(it.properties)){
                        writeCharacteristic(it)
                    }
                }
            }
        }
    }

    /**
     * 根据UUID 获取BluetoothGattCharacteristic
     */
    private fun getCharacteristic(chUUid :String):BluetoothGattCharacteristic?{
        val chList = getCharacteristicList(listService[clickIndex])
        chList.forEach {
            if (it.uuid.toString() == chUUid){
                return it
            }
        }
        return null
    }

    /**
     * 读取特征
     */
    private fun read2Characteristic(gattChar:BluetoothGattCharacteristic){
        gatt.readCharacteristic(gattChar)
    }

    /**
     * 写入特征
     * AA 55 C3 03 01 21 E7
     */
    @SuppressLint("DefaultLocale")
    private fun writeCharacteristic(gattChar:BluetoothGattCharacteristic){
        /*用ByteArray 数据拼接*/
        /*val sentInfo = ByteArray(7)
        sentInfo[0] = 0xAA.toByte()
        sentInfo[1] = 0x55.toByte()
        sentInfo[2] = 0xC3.toByte()
        sentInfo[3] = 0x03.toByte()
        sentInfo[4] = 0x01.toByte()
        sentInfo[5] = 0x21.toByte()
        sentInfo[6] = 0xE7.toByte()
        val log = HexUtil.formatHexString(sentInfo ,true)?.toUpperCase()
        LogUtils.e(" 发送的内容  ==>> $log")*/

        val sentStr = "AA5593030121B7"
        val sentByte = HexUtil.hexStringToBytes(sentStr)
        LogUtils.e(" 发送的内容  ==>> ${HexUtil.formatHexString(sentByte ,true).toUpperCase()} 大小：${sentByte.size}")
        LogUtils.e("gattCharUUid == > ${gattChar.uuid}")
//        LogUtils.e("gattCharUUid == > ${gattChar.uuid}")
        gattChar.value = sentByte
        gatt.writeCharacteristic(gattChar)
    }
}