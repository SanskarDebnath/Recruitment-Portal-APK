package com.example.recruitment_portal

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator

data class OngoingApp(
    val title: String,
    val status: String,
    val progress: Int,
    val nextStep: String,
    val iconResId: Int,
    val iconTint: String,
    val iconBgColor: String
)

class OngoingAdapter(
    private var items: List<OngoingApp>,
    private var isGridMode: Boolean
) : RecyclerView.Adapter<OngoingAdapter.ViewHolder>() {

    fun setGridMode(isGrid: Boolean) {
        if (this.isGridMode != isGrid) {
            this.isGridMode = isGrid
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        
        if (isGridMode) {
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT
            lp.bottomMargin = 0
            view.layoutParams = lp
        } else {
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            view.layoutParams = lp
        }
        
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, isGridMode)
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val layoutList: View = itemView.findViewById(R.id.layoutList)
        private val layoutGrid: View = itemView.findViewById(R.id.layoutGrid)

        // List elements
        private val ivActivityIconList: ImageView = itemView.findViewById(R.id.ivActivityIconList)
        private val tvActivityTitleList: TextView = itemView.findViewById(R.id.tvActivityTitleList)
        private val tvActivityStatusList: TextView = itemView.findViewById(R.id.tvActivityStatusList)
        private val tvProgressPercentList: TextView = itemView.findViewById(R.id.tvProgressPercentList)
        private val activityProgressList: LinearProgressIndicator = itemView.findViewById(R.id.activityProgressList)
        private val tvNextStepList: TextView = itemView.findViewById(R.id.tvNextStepList)

        // Grid elements
        private val ivActivityIconGrid: ImageView = itemView.findViewById(R.id.ivActivityIconGrid)
        private val tvActivityTitleGrid: TextView = itemView.findViewById(R.id.tvActivityTitleGrid)
        private val tvActivityStatusGrid: TextView = itemView.findViewById(R.id.tvActivityStatusGrid)
        private val tvProgressPercentGrid: TextView = itemView.findViewById(R.id.tvProgressPercentGrid)
        private val activityProgressGrid: LinearProgressIndicator = itemView.findViewById(R.id.activityProgressGrid)
        private val tvNextStepGrid: TextView = itemView.findViewById(R.id.tvNextStepGrid)

        fun bind(item: OngoingApp, isGridMode: Boolean) {
            layoutList.visibility = if (isGridMode) View.GONE else View.VISIBLE
            layoutGrid.visibility = if (isGridMode) View.VISIBLE else View.GONE

            if (!isGridMode) {
                ivActivityIconList.setImageResource(item.iconResId)
                ivActivityIconList.imageTintList = ColorStateList.valueOf(Color.parseColor(item.iconTint))
                ivActivityIconList.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.iconBgColor))
                
                tvActivityTitleList.text = item.title
                tvActivityStatusList.text = item.status
                tvProgressPercentList.text = "${item.progress}%"
                activityProgressList.progress = item.progress
                tvNextStepList.text = item.nextStep
            } else {
                ivActivityIconGrid.setImageResource(item.iconResId)
                ivActivityIconGrid.imageTintList = ColorStateList.valueOf(Color.parseColor(item.iconTint))
                ivActivityIconGrid.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.iconBgColor))
                
                tvActivityTitleGrid.text = item.title
                tvActivityStatusGrid.text = item.status
                tvProgressPercentGrid.text = "${item.progress}%"
                activityProgressGrid.progress = item.progress
                tvNextStepGrid.text = item.nextStep
            }
        }
    }
}
