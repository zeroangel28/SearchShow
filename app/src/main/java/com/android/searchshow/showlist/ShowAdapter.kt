package com.android.searchshow.showlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.searchshow.R
import com.android.searchshow.data.Show
import com.bumptech.glide.Glide

class ShowAdapter(context: Context ,private val onClick: (Show) -> Unit) : ListAdapter<Show, ShowAdapter.ShowViewHolder>(ShowDiffCallback) {
    private val context: Context = context

    class ShowViewHolder(context: Context, itemView: View, val onClick: (Show) -> Unit) :
            RecyclerView.ViewHolder(itemView) {
        private val showNameTextView: TextView = itemView.findViewById(R.id.show_name_text)
        private val showCreatedAtTextView: TextView = itemView.findViewById(R.id.show_created_text)
        private val showRatingTextView: TextView = itemView.findViewById(R.id.show_rating_text)
        private val showImageView: ImageView = itemView.findViewById(R.id.show_image)
        private val context: Context = context
        private var currentShow: Show? = null

        init {
            itemView.setOnClickListener {
                currentShow?.let {
                    onClick(it)
                }
            }
        }

        fun bind(show: Show) {
            currentShow = show

            showNameTextView.text = show.name
            showCreatedAtTextView.text = show.created_at
            showRatingTextView.text = show.rating
            Glide.with(context)
                    .load(show.thumb)
                    .into(showImageView);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.show_item, parent, false)
        return ShowViewHolder(context,view, onClick)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val show = getItem(position)
        holder.bind(show)
    }
}
object ShowDiffCallback : DiffUtil.ItemCallback<Show>() {
    override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
        return oldItem.drama_id == newItem.drama_id
    }
}