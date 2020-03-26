package somker.pro.com.mybluetooth.model.db

import com.blankj.utilcode.util.LogUtils
import org.greenrobot.greendao.query.QueryBuilder
import somker.pro.com.mybluetooth.base.BaseApplication
import somker.pro.com.mybluetooth.greendao.gen.DaoMaster
import somker.pro.com.mybluetooth.greendao.gen.DaoSession

/**
 * Created by Smoker on 2019/12/25.
 * 说明：GreenDao 管理者
 */
class GreenDaoManager private constructor() {
    private var mDaoMaster : DaoMaster
    private var mDaoSession : DaoSession
    private val dataBaseName = "smokerPro.do"
    private val dataBasePassword = "smokerPsd"

    companion object{
        private lateinit var instance : GreenDaoManager
        fun getInstance(): GreenDaoManager {
            if (!this::instance.isInitialized){
                instance = GreenDaoManager()
            }
            return instance
        }
    }
    init {
        val openHelper = MyOpenHelper(BaseApplication.getInstance() , dataBaseName )
        val dataBase = openHelper.getEncryptedWritableDb(dataBasePassword)
        mDaoMaster = DaoMaster(dataBase)
        mDaoSession = mDaoMaster.newSession()

        QueryBuilder.LOG_SQL = true
        QueryBuilder.LOG_VALUES = true
    }

    fun getMaster():DaoMaster{
        return mDaoMaster
    }


    fun getSession():DaoSession{
        return mDaoSession
    }

    fun getNewSession():DaoSession{
        return mDaoMaster.newSession()
    }

    fun closeDao(){
        LogUtils.e("==>> 关闭数据库")
        mDaoSession.clear()
        mDaoMaster.database.close()
    }
}