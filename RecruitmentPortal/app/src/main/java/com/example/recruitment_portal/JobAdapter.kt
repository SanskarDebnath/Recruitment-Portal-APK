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
    }

    override fun getItemCount() = jobs.size
}