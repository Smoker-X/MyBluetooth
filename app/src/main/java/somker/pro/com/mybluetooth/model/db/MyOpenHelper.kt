package somker.pro.com.mybluetooth.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.greenrobot.greendao.database.Database
import somker.pro.com.mybluetooth.greendao.gen.DaoMaster
import somker.pro.com.mybluetooth.greendao.gen.TeacherBeanDao

/**
 * Created by Smoker on 2019/12/25.
 * 说明：
 */
class MyOpenHelper : DaoMaster.DevOpenHelper{

    constructor(context: Context  ,name:String) : super(context ,name)

    constructor(context: Context ,name: String ,factory:SQLiteDatabase.CursorFactory):super(context, name, factory)


    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
       // super.onUpgrade(db, oldVersion, newVersion)

        MigrationHelper.migrate(db , object : MigrationHelper.ReCreateAllTableListener {
            override fun onCreateAllTables(db: Database?, ifNotExists: Boolean) {
                DaoMaster.createAllTables(db ,ifNotExists)
            }

            override fun onDropAllTables(db: Database?, ifExists: Boolean) {
                DaoMaster.dropAllTables(db ,ifExists)
            }

        },  TeacherBeanDao::class.java)
    }
}