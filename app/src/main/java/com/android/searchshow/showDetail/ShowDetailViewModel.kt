package com.android.searchshow.showDetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.searchshow.data.DataSource
import com.android.searchshow.data.Show


class ShowDetailViewModel(private val datasource: DataSource) : ViewModel() {
    fun getShowForId(id: Int) : Show? {
        return datasource.getShowForId(id)
    }
}

class ShowDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowDetailViewModel(
                datasource = DataSource.getDataSource(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}