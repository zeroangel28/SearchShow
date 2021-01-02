package com.android.searchshow.data

import android.content.res.Resources
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class DownloadShowData {
    private val ShowURL: String = "https://static.linetv.tw/interview/dramas-sample.json"
    private val userList: ArrayList<Show> = ArrayList()

    fun downloadData(): ArrayList<Show>{
        var quaryURL = URL(ShowURL).readText()
        val json_contact = JSONObject(quaryURL)
        val jsonarray_info: JSONArray = json_contact.getJSONArray("data")
        var size:Int = jsonarray_info.length()
        for (i in 0.. size-1) {
            var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
            var show = Show(json_objectdetail.getInt("drama_id"),
                    json_objectdetail.getString("name"),
                    json_objectdetail.getString("thumb"),
                    json_objectdetail.getString("total_views"),
                    json_objectdetail.getString("created_at"),
                    json_objectdetail.getString("rating"));
            userList.add(show)
        }
        return userList
    }
}