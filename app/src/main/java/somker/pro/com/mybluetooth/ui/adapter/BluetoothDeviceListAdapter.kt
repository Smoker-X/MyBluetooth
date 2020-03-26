package somker.pro.com.mybluetooth.ui.adapter

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_blue_device.view.*
import somker.pro.com.mybluetooth.R
import somker.pro.com.mybluetooth.base.BaseRecyclerViewAdapter
import somker.pro.com.mybluetooth.entity.DeviceBean

/**
 * Created by Smoker on 2020/3/24.
 * 说明：蓝牙列表适配器
 */
class BluetoothDeviceListAdapter : BaseRecyclerViewAdapter<BluetoothDeviceListAdapter.DeviceHolder>(){
    private val deviceList = mutableListOf<DeviceBean>()

    fun setDeviceList(list:MutableList<DeviceBean>){
        this.deviceList.clear()
        this.deviceList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateLayout(parent: ViewGroup, itemType: Int): Int = R.layout.item_blue_device

    override fun onCreateHolder(mView: View, itemType: Int): DeviceHolder = DeviceHolder(mView)

    override fun getItemCount(): Int = deviceList.size

    override fun onBindViewHolder(holder: DeviceHolder, position: Int) {
        val device = deviceList[position]
        holder.itemView.tvName.text = device.blueDevice.name?:"null"
        holder.itemView.tvAddress.text = device.blueDevice.address
        holder.itemView.tvRssi.text = device.rssi.toString()
        if (device.isConnect){
            holder.itemView.btnConnect.visibility = View.GONE
            holder.itemView.btnDisConnect.visibility = View.VISIBLE
            holder.itemView.btnSent.visibility = View.VISIBLE
        }else{
            holder.itemView.btnConnect.visibility = View.VISIBLE
            holder.itemView.btnDisConnect.visibility = View.GONE
            holder.itemView.btnSent.visibility = View.GONE
        }
    }

    inner class DeviceHolder(itemView:View) : BaseRecyclerViewHolder(itemView){
        init {
            addClick(itemView.btnConnect)
            addClick(itemView.btnDisConnect)
            addClick(itemView.btnSent)
        }
    }
}