package com.szhua.pagedemo.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.szhua.pagedemo.common.BaseApplication
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.db.RepositoryProvider
import com.szhua.pagedemo.db.data.Shoe
import com.szhua.pagedemo.utils.AppPrefsUtils
import kotlinx.coroutines.*

class ShoeWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val TAG by lazy {
        ShoeWorker::class.java.simpleName
    }

    // 指定Dispatchers
    override val coroutineContext: CoroutineDispatcher
        get() = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        try {
            return@coroutineScope BaseApplication.context.assets.open("shoes.json").use {
                JsonReader(it.reader()).use {
                    val shoeType = object : TypeToken<List<Shoe>>() {}.type
                    val shoeList: List<Shoe> = Gson().fromJson(it, shoeType)
                    val shoeDao = RepositoryProvider.providerShoeRepository(applicationContext)
                    shoeList.forEach {
                        LogUtils.d(it.brand)
                    }
                    shoeDao.insertShoes(shoeList)
                    for (i in 0..6) {
                        for (shoe in shoeList) {
                            shoe.id += shoeList.size
                        }
                        shoeDao.insertShoes(shoeList)
                    }
                    AppPrefsUtils.putBoolean(BaseConstant.IS_FIRST_LAUNCH,false)
                    Result.success()
                }

            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }
}