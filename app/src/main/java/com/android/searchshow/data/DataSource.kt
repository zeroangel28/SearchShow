package com.android.searchshow.data


import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSource(context: Context) {
    private val showDatabaseHelper: ShowDatabaseHelper = ShowDatabaseHelper(context)
    private val initialShowList = showDatabaseHelper.showAll()
    private val ShowLiveData = MutableLiveData(initialShowList)

    fun addShow(show: Show) {
        val currentList = ShowLiveData.value
        if (currentList == null) {
            ShowLiveData.postValue(arrayListOf(show))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, show)
            ShowLiveData.postValue(updatedList as ArrayList<Show>?)
        }
    }

    fun getShowForId(id: Int): Show? {
        ShowLiveData.value?.let { show ->
            return show.firstOrNull{ it.drama_id == id}
        }
        return null
    }

    fun getShowList(): LiveData<ArrayList<Show>> {
        return ShowLiveData
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(context: Context): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(context)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}