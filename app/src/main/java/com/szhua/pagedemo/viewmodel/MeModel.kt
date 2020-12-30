package com.szhua.pagedemo.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.common.BaseConstant.IMAGE_MANIPULATION_WORK_NAME
import com.szhua.pagedemo.common.BaseConstant.KEY_IMAGE_URI
import com.szhua.pagedemo.common.BaseConstant.TAG_OUTPUT
import com.szhua.pagedemo.db.repository.UserRepository
import com.szhua.pagedemo.utils.AppPrefsUtils
import com.szhua.pagedemo.worker.BlurWorker
import com.szhua.pagedemo.worker.CleanUpWorker
import com.szhua.pagedemo.worker.SaveImageToFileWorker
import kotlinx.coroutines.launch

class MeModel (val userRepository: UserRepository) :ViewModel() {
    internal var imageUri: Uri? = null
    internal var outPutUri: Uri? = null
    internal val outPutWorkInfos: LiveData<List<WorkInfo>>
    private val workManager = WorkManager.getInstance()
    val user = userRepository.findUserById(AppPrefsUtils.getLong(BaseConstant.SP_USER_ID))

    init {
        outPutWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    internal  fun  applyBlur(blurLevel: Int){
        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanUpWorker::class.java)
            )

        for (i in 0 until blurLevel) {
            val builder = OneTimeWorkRequestBuilder<BlurWorker>()
            if (i == 0) {
                builder.setInputData(createInputDataForUri())
            }
            continuation = continuation.then(builder.build())
        }

        // 构建约束条件
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true) // 非电池低电量
            .setRequiredNetworkType(NetworkType.CONNECTED) // 网络连接的情况
            .setRequiresStorageNotLow(true) // 存储空间足
            .build()

        // 储存照片
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(save)

        continuation.enqueue()
    }


    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else
            null
    }

    fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    /**
     * setter函数
     */
    internal fun setImageUri(uri: String?) {
        imageUri = uriOrNull(uri)
    }

    internal fun setOutputUri(uri: String?) {
        outPutUri = uriOrNull(uri)
        val value = user.value
        value?.headImage = uri!!
        if (value != null) {
            viewModelScope.launch {
                userRepository.updateUser(value)
            }
        }
    }

}