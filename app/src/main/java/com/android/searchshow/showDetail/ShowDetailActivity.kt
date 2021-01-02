package com.android.searchshow.showDetail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.searchshow.R
import com.android.searchshow.SHOW_ID
import com.bumptech.glide.Glide

class ShowDetailActivity : AppCompatActivity() {

    private val ShowDetailViewModel by viewModels<ShowDetailViewModel> {
        ShowDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_all_info)

        var currentShowId: Int? = null


        val showNameText: TextView = findViewById(R.id.show_name_text)
        val showImage: ImageView = findViewById(R.id.show_image)
        val showTotalViewsText: TextView = findViewById(R.id.show_total_views_text)
        val showRatingText: TextView = findViewById(R.id.show_rating_text)
        val showCreatedText: TextView = findViewById(R.id.show_created_text)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentShowId = bundle.getInt(SHOW_ID)
        }

        currentShowId?.let {
            val currentShow = ShowDetailViewModel.getShowForId(it)
            showNameText.text = currentShow?.name
            Glide.with(this)
                .load(currentShow?.thumb)
                .into(showImage);
            showTotalViewsText.text = currentShow?.total_views
            showRatingText.text = currentShow?.rating
            showCreatedText.text = currentShow?.created_at
        }

    }
}