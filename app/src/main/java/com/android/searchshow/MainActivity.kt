package com.android.searchshow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.searchshow.data.DownloadShowData
import com.android.searchshow.data.Show
import com.android.searchshow.data.ShowDatabaseHelper
import com.android.searchshow.showDetail.ShowDetailActivity
import com.android.searchshow.showlist.ShowAdapter
import com.android.searchshow.showlist.ShowListViewModel
import com.android.searchshow.showlist.ShowListViewModelFactory


const val SHOW_ID = "show id"
const val FINISH_USER_DATA = 1
const val ERROR_USER_DATA = 2


class MainActivity : AppCompatActivity() {
    private val showListViewModel by viewModels<ShowListViewModel> {
        ShowListViewModelFactory(this) }
    private val mDBHelper: ShowDatabaseHelper = ShowDatabaseHelper(this)
    private var showList: ArrayList<Show> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editText: EditText = findViewById(R.id.header_edit)
        val ShowAdapter = ShowAdapter(this) { show -> adapterOnClick(show) }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        var temp = ""

        recyclerView.adapter = ShowAdapter

        mDBHelper.initSearchString()

        showListViewModel.showLiveData.observe(this, {
            it?.let {
                ShowAdapter.submitList(it as MutableList<Show>)
            }
        })

        val handler = Handler() {
            if (it.what == FINISH_USER_DATA && mDBHelper.searchString() == "") {
                ShowAdapter.submitList(showList as MutableList<Show>)
            }else if(it.what == ERROR_USER_DATA && mDBHelper.searchString() == ""){
                Toast.makeText(this,
                        "No Network! Please check the Internet or Open the Wifi.",
                        Toast.LENGTH_SHORT).show()
                showList = mDBHelper.showAll()
                ShowAdapter.submitList(showList as MutableList<Show>)
            }else if (mDBHelper.searchString() != ""){
                editText.setText(mDBHelper.searchString())
                showList = mDBHelper.showAll()
                ShowAdapter.submitList(SearchTheShowName(mDBHelper.searchString()) as MutableList<Show>)
        }
            true
        }

        Thread{
            val msg = Message()
            try {
                val downloadShowData = DownloadShowData()
                showList = downloadShowData.downloadData()
                for (i in 0..showList.size - 1) {
                    mDBHelper.addShowData(
                            showList[i].drama_id, showList[i].name, showList[i].total_views,
                            showList[i].created_at, showList[i].thumb, showList[i].rating
                    )
                }
                msg.what = FINISH_USER_DATA
                handler.sendMessage(msg)
            }catch (e: Exception){
                Log.e("Thread", "Error: No Network")
                val msg = Message()
                msg.what = ERROR_USER_DATA
                handler.sendMessage(msg)
            }
        }.start()

        editText.afterTextChanged() {
            if(it != "" || it != temp) {
                ShowAdapter.submitList(SearchTheShowName(it) as MutableList<Show>)
                mDBHelper.modify(1, editText.text.toString())
            }
            temp = editText.text.toString()
        }
    }

    private fun adapterOnClick(show: Show) {
        val intent = Intent(this, ShowDetailActivity()::class.java)
        intent.putExtra(SHOW_ID, show.drama_id)
        startActivity(intent)
    }

    private fun SearchTheShowName(searchString: String): ArrayList<Show>{
        var searchShowList: ArrayList<Show> = ArrayList()
        for(i in 0.. showList.size-1){
            Log.d("Search", showList[i].name)
            if(showList[i].name.indexOf(searchString)>=0){
                searchShowList.add(showList[i])
            }

        }
        return searchShowList
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }
}
