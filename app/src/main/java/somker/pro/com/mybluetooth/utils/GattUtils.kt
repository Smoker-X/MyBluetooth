package somker.pro.com.mybluetooth.utils

import android.bluetooth.BluetoothGattCharacteristic

/**
 * Created by Smoker on 2020/3/24.
 * 说明：
 */
class GattUtils {
    companion object{

        /**
         * 根据特征属性获取到中文类型
         * @param charaProp 特征属性
         */
        fun getCharacteristicProperties(charaProp:Int):String{
            val property = StringBuilder()
            /*可读*/
            if (characteristicCanRead(charaProp)) {
                property.append("Read")
                property.append(" , ")
            }
            /*可写*/
            if (characteristicCanWrite(charaProp)) {
                property.append("Write")
                property.append(" , ")
            }
            /*写入时可以无需响应*/
            if (charaProp and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE > 0) {
                property.append("Write No Response")
                property.append(" , ")
            }
            /*通知*/
            if (charaProp and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                property.append("Notify")
                property.append(" , ")
            }
            /*只是*/
            if (charaProp and BluetoothGattCharacteristic.PROPERTY_INDICATE > 0) {
                property.append("Indicate")
                property.append(" , ")
            }
            if (property.length > 1) {
                property.delete(property.length - 2, property.length - 1)
            }
            return property.toString()
        }

        /**
         * 特征是否可读
         */
        fun characteristicCanRead(charaProp:Int):Boolean{
            return charaProp and BluetoothGattCharacteristic.PROPERTY_READ > 0
        }

        /**
         * 特征是否可写
         */
        fun characteristicCanWrite(charaProp:Int):Boolean{
            return charaProp and BluetoothGattCharacteristic.PROPERTY_WRITE > 0
        }
    }
}