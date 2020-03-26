package somker.pro.com.mybluetooth.utils

import android.media.MediaPlayer
import android.support.annotation.RawRes
import somker.pro.com.mybluetooth.base.BaseApplication
import com.blankj.utilcode.util.LogUtils
import somker.pro.com.mybluetooth.R
import java.io.IOException

/**
 * Created by Smoker on 2019/11/27.
 * 说明：超时提示音工具类
 * https://ai.baidu.com/tech/speech/tts
 */
class SoundUtils  : MediaPlayer.OnCompletionListener {
    /**
     * 音频播放者
     */
    private  var player :MediaPlayer = MediaPlayer()

    companion object {
        /**超时的提示*/
        const val OVER_TIME_RES = R.raw.over_time
        /**核对考生信息是否一致*/
        const val CONFIRM_STU_INFO = R.raw.confirm_stu_info

        val instance = MediaPlayerHolder.holder
        private object MediaPlayerHolder {
            val holder = SoundUtils()
        }
    }



    /**
     * 音频播放完成的回调
     * @param mp
     */
    override fun onCompletion(mp: MediaPlayer?) {
        LogUtils.e("<<<播放完成>>")
    }


    /**
     * 初始化播放器
     */
    private fun initPlayer(@RawRes resId:Int) {
        val file = BaseApplication.getInstance().resources.openRawResourceFd(resId)
        file?.let { mFile ->
            try {
                player.setDataSource(
                    mFile.fileDescriptor, mFile.startOffset,
                    mFile.length
                )
                player.prepare()
                mFile.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            player.setVolume(0.8f, 0.8f)
            player.isLooping = false
            player.setOnCompletionListener(this)

        }

    }


    /**
     * 关闭
     */
    fun close() {
        player.stop()
        player.reset()
    }

    /**
     * 播放音频
     */
    fun play(@RawRes resId:Int) {
        if (player.isPlaying){
            return
        }
        player.stop()
        player.reset()
        initPlayer(resId)
        player.start()
    }


}