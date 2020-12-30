package com.szhua.pagedemo.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.blankj.utilcode.util.LogUtils
import com.szhua.pagedemo.common.BaseApplication
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.common.BaseConstant.IS_FIRST_LAUNCH
import com.szhua.pagedemo.common.createPager
import com.szhua.pagedemo.db.Converters
import com.szhua.pagedemo.db.dao.FavouriteShoeDao
import com.szhua.pagedemo.db.dao.ShoeDao
import com.szhua.pagedemo.db.dao.UserDao
import com.szhua.pagedemo.db.data.FavouriteShoe
import com.szhua.pagedemo.db.data.Shoe
import com.szhua.pagedemo.db.data.User
import com.szhua.pagedemo.utils.AppPrefsUtils
import com.szhua.pagedemo.worker.ShoeWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * 数据库文件
 */
@Database(entities = [User::class, Shoe::class, FavouriteShoe::class],version = 2,exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase:RoomDatabase() {
    // 得到UserDao
    abstract fun userDao(): UserDao
    // 得到ShoeDao
    abstract fun shoeDao(): ShoeDao
    // 得到FavouriteShoeDao
    abstract fun favouriteShoeDao(): FavouriteShoeDao

    companion object{
        @Volatile
        private var instance:AppDataBase? = null

        fun getInstance(context:Context):AppDataBase{
            return instance?: synchronized(this){
                instance?:buildDataBase(context)
                    .also {
                        instance = it
                    }
            }
        }

        private fun buildDataBase(context: Context):AppDataBase{
            return Room
                .databaseBuilder(context,AppDataBase::class.java,"jetPackDemo-database")
                .addCallback(object :RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)


                    }
                })
                .build()
        }
    }
}