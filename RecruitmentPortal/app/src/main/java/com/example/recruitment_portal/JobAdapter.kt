package com.example.recruitment_portal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobAdapter(private val jobs: MutableList<Job>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.tvTitle)
        val expandable = view.findViewById<LinearLayout>(R.id.layoutExpandable)
        val desc = view.findViewById<TextView>(R.id.tvDesc)
        val chipAge = view.findViewById<com.google.android.material.chip.Chip>(R.id.chipAge)
        val chipDept = view.findViewById<com.google.android.material.chip.Chip>(R.id.chipDept)
        val chipDate = view.findViewById<com.google.android.material.chip.Chip>(R.id.chipDate)
        val chevron = view.findViewById<TextView>(R.id.tvChevron)
        val btnInfo = view.findViewById<android.widget.ImageButton>(R.id.btnInfo)
        val btnApply = view.findViewById<android.widget.Button>(R.id.btnApply)
        val btnViewDoc = view.findViewById<android.widget.Button>(R.id.btnViewDoc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]

        holder.title.text = job.title
        holder.desc.text = job.description

        holder.chipAge.text = "Age: ${job.age}"
        holder.chipDept.text = job.department
        holder.chipDate.text = "${job.startDate} - ${job.endDate}"

        holder.expandable.visibility =
            if (job.isExpanded) View.VISIBLE else View.GONE

        holder.chevron.rotation = if (job.isExpanded) 270f else 90f

        holder.itemView.setOnClickListener {
            job.isExpanded = !job.isExpanded
            notifyItemChanged(position)
        }

        holder.btnInfo.setOnClickListener {
            it.startClickAnimation()
            com.google.android.material.dialog.MaterialAlertDialogBuilder(holder.itemView.context)
                .setTitle(job.title)
                .setMessage("Department: ${job.department}\nAge Limit: ${job.age}\nApplication Period: ${job.startDate} to ${job.endDate}\n\nDescription:\n${job.description}")
                .setPositiveButton("Close", null)
                .show()
        }

        holder.btnApply.setOnClickListener {
            it.startClickAnimation()
            if (SessionManager.isLoggedIn) {
                val intent = android.content.Intent(holder.itemView.context, ApplyWizardActivity::class.java)
                holder.itemView.context.startActivity(intent)
            } else {
                NotificationHelper.showStackedNotification(
                    holder.itemView.rootView.findViewById(R.id.notificationStack), 
                    "Please login to first to apply for this job"
                )
            }
        }

        holder.btnViewDoc.setOnClickListener {
            it.startClickAnimation()
            NotificationHelper.showStackedNotification(holder.itemView.rootView.findViewById(R.id.notificationStack), "Opening document for ${job.title}")
        }
    }

    override fun getItemCount() = jobs.size
}