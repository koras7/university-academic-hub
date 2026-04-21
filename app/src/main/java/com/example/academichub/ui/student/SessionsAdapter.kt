package com.example.academichub.ui.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.model.TutoringSession

class SessionsAdapter(
    private val sessions: List<TutoringSession>
) : RecyclerView.Adapter<SessionsAdapter.SessionViewHolder>() {

    class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sessionTutorName: TextView = itemView.findViewById(R.id.sessionTutorName)
        val sessionRate: TextView = itemView.findViewById(R.id.sessionRate)
        val sessionSubject: TextView = itemView.findViewById(R.id.sessionSubject)
        val sessionDescription: TextView = itemView.findViewById(R.id.sessionDescription)
        val sessionTime: TextView = itemView.findViewById(R.id.sessionTime)
        val sessionMaxStudents: TextView = itemView.findViewById(R.id.sessionMaxStudents)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_session_card, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]

        holder.sessionTutorName.text = session.tutorName
        holder.sessionSubject.text = "📚 ${session.subject}"
        holder.sessionDescription.text = session.description
        holder.sessionTime.text = "🕐 ${session.availableTime}"
        holder.sessionMaxStudents.text = "👥 Max ${session.maxStudents} students"

        if (session.isPaid && session.rate != null) {
            holder.sessionRate.text = session.rate
            holder.sessionRate.setBackgroundColor(
                holder.itemView.context.getColor(R.color.warning)
            )
        } else {
            holder.sessionRate.text = "FREE"
            holder.sessionRate.setBackgroundColor(
                holder.itemView.context.getColor(R.color.success)
            )
        }
    }

    override fun getItemCount(): Int = sessions.size
}