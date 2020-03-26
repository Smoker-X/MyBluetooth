package somker.pro.com.mybluetooth.entity;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Smoker on 2020/3/24.
 * 说明：蓝牙设备bean
 */
public class DeviceBean implements Parcelable {

    private boolean isConnect ;

    private int rssi;

    private BluetoothDevice blueDevice ;

    public DeviceBean() {
    }

    protected DeviceBean(Parcel in) {
        isConnect = in.readByte() != 0;
        rssi = in.readInt();
        blueDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
    }

    public static final Creator<DeviceBean> CREATOR = new Creator<DeviceBean>() {
        @Override
        public DeviceBean createFromParcel(Parcel in) {
            return new DeviceBean(in);
        }

        @Override
        public DeviceBean[] newArray(int size) {
            return new DeviceBean[size];
        }
    };

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }


    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BluetoothDevice getBlueDevice() {
        return blueDevice;
    }

    public void setBlueDevice(BluetoothDevice blueDevice) {
        this.blueDevice = blueDevice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rssi);
        dest.writeByte((byte)(isConnect ?1:0));
        dest.writeParcelable(blueDevice ,flags);
    }

}
