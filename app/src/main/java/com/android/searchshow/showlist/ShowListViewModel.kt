package com.android.searchshow.showlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.searchshow.data.DataSource
import com.android.searchshow.data.Show
import kotlin.random.Random

class ShowListViewModel(val dataSource: DataSource) : ViewModel() {

    val showLiveData = dataSource.getShowList()

    fun insertShow(showId: Int? ,showName: String?, showTotalViews: String?, showCreatedAt: String?, showthumb: String?, showRating: String) {
        if (showId==null || showName == null || showTotalViews == null || showCreatedAt == null || showthumb == null || showRating == null) {
            return
        }
        val newShow = Show(
            showId,
            showName,
            showTotalViews,
            showCreatedAt,
            showthumb,
            showRating
        )

        dataSource.addShow(newShow)
    }
}

class ShowListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowListViewModel(
                dataSource = DataSource.getDataSource(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}