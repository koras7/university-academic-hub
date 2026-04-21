package com.example.academichub.ui.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.model.RequestStatus
import com.example.academichub.model.SessionRequest

class MyRequestsAdapter(
    private val requests: List<SessionRequest>
) : RecyclerView.Adapter<MyRequestsAdapter.MyRequestViewHolder>() {

    class MyRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tutorNameText: TextView = itemView.findViewById(R.id.tutorNameText)
        val requestStatusText: TextView = itemView.findViewById(R.id.requestStatusText)
        val subjectText: TextView = itemView.findViewById(R.id.subjectText)
        val timeText: TextView = itemView.findViewById(R.id.timeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_request_card, parent, false)
        return MyRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRequestViewHolder, position: Int) {
        val request = requests[position]

        holder.tutorNameText.text = request.tutorName
        holder.subjectText.text = "📚 Subject: ${request.subject}"
        holder.timeText.text = "🕐 Time: ${request.preferredTime}"

        // Show status with color
        when (request.status) {
            RequestStatus.PENDING -> {
                holder.requestStatusText.text = "PENDING"
                holder.requestStatusText.setBackgroundColor(
                    holder.itemView.context.getColor(R.color.warning)
                )
            }
            RequestStatus.ACCEPTED -> {
                holder.requestStatusText.text = "ACCEPTED"
                holder.requestStatusText.setBackgroundColor(
                    holder.itemView.context.getColor(R.color.success)
                )
            }
            RequestStatus.REJECTED -> {
                holder.requestStatusText.text = "REJECTED"
                holder.requestStatusText.setBackgroundColor(
                    holder.itemView.context.getColor(R.color.error)
                )
            }
        }
    }

    override fun getItemCount(): Int = requests.size
}