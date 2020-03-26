package somker.pro.com.mybluetooth.model.db.daoutils

/**
 * Created by Smoker on 2019/12/25.
 * 说明：数据库操作工具类的实现类
 * 全部数据库操作类在这里进行返回，
 * 这样就避免每个工具类要写单例
 */
class DaoUtils private constructor(): IDaoUtils {


    companion object{
        val instance = DaoUtilsHolder.daoUtils
    }

    private object DaoUtilsHolder{
        val daoUtils = DaoUtils()
    }


}