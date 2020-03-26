package somker.pro.com.mybluetooth.ui.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattService
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_blue_service.view.*
import somker.pro.com.mybluetooth.R
import somker.pro.com.mybluetooth.base.BaseRecyclerViewAdapter

/**
 * Created by Smoker on 2020/3/24.
 * 说明：目标蓝牙拥有的服务通道
 */
class BlueServiceAdapter :BaseRecyclerViewAdapter<BlueServiceAdapter.BlueServiceHolder>(){
    private lateinit var listService:MutableList<BluetoothGattService>

    fun setDataList(list :MutableList<BluetoothGattService>){
        this.listService = list
        this.notifyDataSetChanged()
    }

    override fun onCreateLayout(parent: ViewGroup, itemType: Int): Int = R.layout.item_blue_service

    override fun onCreateHolder(mView: View, itemType: Int): BlueServiceHolder = BlueServiceHolder(mView)

    override fun getItemCount(): Int = if (this::listService.isInitialized) listService.size else 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BlueServiceHolder, position: Int) {
        val service = listService[position]
        holder.itemView.tvUUid.text = "服务UUID：${service.uuid.toString().toUpperCase()}"
        holder.itemView.tvType.text = if (service.type == BluetoothGattService.SERVICE_TYPE_PRIMARY){
            "主要"
        }else{
            "次要"
        }
    }

    inner class BlueServiceHolder(itemView: View) :BaseRecyclerViewHolder(itemView){
        init {
            addClick(itemView)
        }
    }
}