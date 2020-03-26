package somker.pro.com.mybluetooth.ui.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattCharacteristic
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_blue_service.view.*
import somker.pro.com.mybluetooth.R
import somker.pro.com.mybluetooth.base.BaseRecyclerViewAdapter
import somker.pro.com.mybluetooth.utils.GattUtils


/**
 * Created by Smoker on 2020/3/24.
 * 说明：目标蓝牙拥有的服务通道
 */
class BlueCharacteristicAdapter :BaseRecyclerViewAdapter<BlueCharacteristicAdapter.BlueCharacteristicHolder>(){
    private lateinit var listCharacteristic:MutableList<BluetoothGattCharacteristic>

    fun setDataList(list :MutableList<BluetoothGattCharacteristic>){
        this.listCharacteristic = list
        this.notifyDataSetChanged()
    }

    override fun onCreateLayout(parent: ViewGroup, itemType: Int): Int = R.layout.item_blue_service

    override fun onCreateHolder(mView: View, itemType: Int): BlueCharacteristicHolder = BlueCharacteristicHolder(mView)

    override fun getItemCount(): Int = if (this::listCharacteristic.isInitialized) listCharacteristic.size else 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BlueCharacteristicHolder, position: Int) {
        val characteristic = listCharacteristic[position]
        holder.itemView.tvUUid.text = "通道UUID：${characteristic.uuid.toString().toUpperCase()}"
        holder.itemView.tvType.text = "属性 ：${GattUtils.getCharacteristicProperties(characteristic.properties)}"
    }

    inner class BlueCharacteristicHolder(itemView: View) :BaseRecyclerViewHolder(itemView){
        init {
            addClick(itemView)
        }
    }
}