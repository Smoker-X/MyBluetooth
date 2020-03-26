package somker.pro.com.mybluetooth.base

import org.greenrobot.greendao.AbstractDao


/**
 * Created by Smoker on 2019/12/25.
 * 说明：数据库操作基类
 */
abstract class BaseDao <T: AbstractDao<*,*>> {
    protected val mDao :T

    init {
        mDao = this.createDao()
    }

    fun getLibId():String = BaseApplication.getInstance().getLibId()

    abstract fun createDao():T


}