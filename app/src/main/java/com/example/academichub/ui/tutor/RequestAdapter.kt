package com.example.academichub.ui.tutor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.model.RequestStatus
import com.example.academichub.model.SessionRequest

class RequestAdapter(
    private val requests: MutableList<SessionRequest>,
    private val onAccept: (SessionRequest) -> Unit,
    private val onReject: (SessionRequest) -> Unit
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.studentName)
        val requestStatus: TextView = itemView.findViewById(R.id.requestStatus)
        val requestSubject: TextView = itemView.findViewById(R.id.requestSubject)
        val requestTime: TextView = itemView.findViewById(R.id.requestTime)
        val requestNote: TextView = itemView.findViewById(R.id.requestNote)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        val rejectButton: Button = itemView.findViewById(R.id.rejectButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request_card, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]

        holder.studentName.text = request.studentName
        holder.requestSubject.text = "📚 Subject: ${request.subject}"
        holder.requestTime.text = "🕐 Time: ${request.preferredTime}"

        // Show note if available
        if (request.note.isNotEmpty()) {
            holder.requestNote.text = "📝 Note: ${request.note}"
            holder.requestNote.visibility = View.VISIBLE
        } else {
            holder.requestNote.visibility = View.GONE
        }

        // Show status with color
        when (request.status) {
            RequestStatus.PENDING -> {
                holder.requestStatus.text = "PENDING"
                holder.requestStatus.setBackgroundColor(
                    holder.itemView.context.getColor(R.color.warning)
                )
                holder.acceptButton.visibility = View.VISIBLE
                holder.rejectButton.visibility = View.VISIBLE
            }
            RequestStatus.ACCEPTED -> {
                holder.requestStatus.text = "ACCEPTED"
                holder.requestStatus.setBackgroundColor(
                    holder.itemView.context.getColor(R.color.success)
                )
                holder.acceptButton.visibility = View.GONE
                holder.rejectButton.visibility = View.GONE
            }
            RequestStatus.REJECTED -> {
                holder.requestStatus.text = "REJECTED"
                holder.requestStatus.setBackgroundColor(
                    holder.itemView.context.getColor(R.color.error)
                )
                holder.acceptButton.visibility = View.GONE
                holder.rejectButton.visibility = View.GONE
            }
        }

        // Accept button click
        holder.acceptButton.setOnClickListener {
            onAccept(request)
        }

        // Reject button click
        holder.rejectButton.setOnClickListener {
            onReject(request)
        }
    }

    override fun getItemCount(): Int = requests.size

    fun updateRequests(newRequests: List<SessionRequest>) {
        requests.clear()
        requests.addAll(newRequests)
        notifyDataSetChanged()
    }
}