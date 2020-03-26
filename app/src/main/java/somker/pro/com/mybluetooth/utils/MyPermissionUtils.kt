package somker.pro.com.mybluetooth.utils

import android.app.AlertDialog
import android.content.Context
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import somker.pro.com.mybluetooth.R

/**
 * Created by Smoker on 2020/1/21.
 * 说明：请求权限
 */
class MyPermissionUtils {
    companion object{

        /**
         * 请求APP权限
         */
        fun requestAPPPermission(context: Context,checkBack: OnMyPermissionUtilsBack){
            PermissionUtils.permission(PermissionConstants.STORAGE ,PermissionConstants.PHONE)
                .rationale {
                    AlertDialog.Builder(context)
                        .setTitle("系统提示")
                        .setIcon(R.mipmap.ic_wrong_red)
                        .setMessage("程序运行需要权限，请同意权限请求")
                        .setPositiveButton("好的") { _, _ ->
                            it.again(true)
                        }
                        .setNegativeButton("退出程序"){_,_->
                            ActivityUtils.finishAllActivities()
                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(permissionsGranted: MutableList<String>?) {
                        checkBack.onGrantedPermission()
                    }
                    override fun onDenied(permissionsDeniedForever: MutableList<String>?, permissionsDenied: MutableList<String>?) {
                        if (permissionsDeniedForever !=null && permissionsDeniedForever.size > 0) {//说明权限永远禁止了，需要手动打开
                            AlertDialog.Builder(context)
                                .setTitle("系统提示")
                                .setIcon(R.mipmap.ic_wrong_red)
                                .setMessage("权限已被禁止，请手动打开.")
                                .setPositiveButton("好的") { _, _ ->
                                    PermissionUtils.launchAppDetailsSettings()
                                }
                                .setNegativeButton("退出程序"){_ ,_ ->
                                    ActivityUtils.finishAllActivities()
                                }
                                .setCancelable(false)
                                .create()
                                .show()
                        } else {
                            requestAPPPermission(context ,checkBack)
                        }
                    }
                }).request()
        }



        /**
         * 请求存储权限
         */
        fun requestStorage(context: Context,checkBack: OnMyPermissionUtilsBack){
            PermissionUtils.permission(PermissionConstants.STORAGE)
                .rationale {
                    AlertDialog.Builder(context)
                        .setTitle("系统提示")
                        .setIcon(R.mipmap.ic_wrong_red)
                        .setMessage("程序运行需要存储权限，请同意权限请求")
                        .setPositiveButton("好的") { _, _ ->
                            it.again(true)
                        }
                        .setNegativeButton("退出程序"){_,_->
                            ActivityUtils.finishAllActivities()
                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(permissionsGranted: MutableList<String>?) {
                        checkBack.onGrantedPermission()
                    }
                    override fun onDenied(permissionsDeniedForever: MutableList<String>?, permissionsDenied: MutableList<String>?) {
                        if (permissionsDeniedForever !=null && permissionsDeniedForever.size > 0) {//说明权限永远禁止了，需要手动打开
                            AlertDialog.Builder(context)
                                .setTitle("系统提示")
                                .setIcon(R.mipmap.ic_wrong_red)
                                .setMessage("存储权限已被禁止，请手动打开.")
                                .setPositiveButton("好的") { _, _ ->
                                    PermissionUtils.launchAppDetailsSettings()
                                }
                                .setNegativeButton("退出程序"){_ ,_ ->
                                    ActivityUtils.finishAllActivities()
                                }
                                .setCancelable(false)
                                .create()
                                .show()
                        } else {
                            requestStorage(context ,checkBack)
                        }
                    }
                }).request()
        }




        /**
         * 请求拍照权限
         */
        fun requestTakePhoto(context: Context,checkBack: OnMyPermissionUtilsBack){
            PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .rationale {
                    AlertDialog.Builder(context)
                        .setTitle("系统提示")
                        .setIcon(R.mipmap.ic_wrong_red)
                        .setMessage("拍照需要权限，请同意权限请求")
                        .setPositiveButton("好的") { _, _ ->
                            it.again(true)
                        }
                        .setNegativeButton("关闭", null)
                        .create()
                        .show()
                }
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(permissionsGranted: MutableList<String>?) {
                        checkBack.onGrantedPermission()
                    }
                    override fun onDenied(permissionsDeniedForever: MutableList<String>?, permissionsDenied: MutableList<String>?) {
                        if (permissionsDeniedForever !=null && permissionsDeniedForever.size > 0) {//说明权限永远禁止了，需要手动打开
                            AlertDialog.Builder(context)
                                .setTitle("系统提示")
                                .setIcon(R.mipmap.ic_wrong_red)
                                .setMessage("拍照权限已被禁止，请手动打开.")
                                .setPositiveButton("好的") { _, _ ->
                                    PermissionUtils.launchAppDetailsSettings()
                                }
                                .setNegativeButton("关闭", null)
                                .create()
                                .show()
                        } else {
                            requestTakePhoto(context ,checkBack)
                        }
                    }
                }).request()
        }




        /**
         * 请求录音权限
         */
        fun requestRecord(context: Context,checkBack: OnMyPermissionUtilsBack){
            PermissionUtils.permission(PermissionConstants.MICROPHONE)
                .rationale {
                    AlertDialog.Builder(context)
                        .setTitle("系统提示")
                        .setIcon(R.mipmap.ic_wrong_red)
                        .setMessage("录音需要权限，请同意权限请求")
                        .setPositiveButton("好的") { _, _ ->
                            it.again(true)
                        }
                        .setNegativeButton("关闭", null)
                        .create()
                        .show()
                }
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(permissionsGranted: MutableList<String>?) {
                        checkBack.onGrantedPermission()
                    }
                    override fun onDenied(permissionsDeniedForever: MutableList<String>?, permissionsDenied: MutableList<String>?) {
                        if (permissionsDeniedForever !=null && permissionsDeniedForever.size > 0) {//说明权限永远禁止了，需要手动打开
                            AlertDialog.Builder(context)
                                .setTitle("系统提示")
                                .setIcon(R.mipmap.ic_wrong_red)
                                .setMessage("录音权限已被禁止，请手动打开.")
                                .setPositiveButton("好的") { _, _ ->
                                    PermissionUtils.launchAppDetailsSettings()
                                }
                                .setNegativeButton("关闭", null)
                                .create()
                                .show()
                        } else {
                            requestRecord(context ,checkBack)
                        }
                    }
                }).request()
        }



        /**
         * 请求 LOCATION 权限
         */
        fun requestLocation(context: Context,checkBack: OnMyPermissionUtilsBack){
            PermissionUtils.permission(PermissionConstants.LOCATION)
                .rationale {
                    AlertDialog.Builder(context)
                        .setTitle("系统提示")
                        .setIcon(R.mipmap.ic_wrong_red)
                        .setMessage("此App需要请求位置权限，请允许")
                        .setPositiveButton("好的") { _, _ ->
                            it.again(true)
                        }
                        .setNegativeButton("关闭", null)
                        .create()
                        .show()
                }
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(permissionsGranted: MutableList<String>?) {
                        checkBack.onGrantedPermission()
                    }
                    override fun onDenied(permissionsDeniedForever: MutableList<String>?, permissionsDenied: MutableList<String>?) {
                        if (permissionsDeniedForever !=null && permissionsDeniedForever.size > 0) {//说明权限永远禁止了，需要手动打开
                            AlertDialog.Builder(context)
                                .setTitle("系统提示")
                                .setIcon(R.mipmap.ic_wrong_red)
                                .setMessage("请求位置权限已被禁止，允许赋予权限.")
                                .setPositiveButton("好的") { _, _ ->
                                    requestLocation(context ,checkBack)
                                }
                                .setNegativeButton("关闭", null)
                                .create()
                                .show()
                        } else {
                            requestLocation(context ,checkBack)
                        }
                    }
                }).request()
        }

    }

    interface OnMyPermissionUtilsBack{
        fun onGrantedPermission()
    }
}