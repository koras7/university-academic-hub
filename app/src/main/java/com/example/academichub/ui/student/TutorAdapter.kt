package com.example.academichub.ui.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.model.TutorProfile
import com.example.academichub.model.TutorType

class TutorAdapter(
    private val tutors: List<TutorProfile>,
    private val onTutorClick: (TutorProfile) -> Unit
) : RecyclerView.Adapter<TutorAdapter.TutorViewHolder>() {

    // ViewHolder holds references to the card's views
    class TutorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tutorName: TextView = itemView.findViewById(R.id.tutorName)
        val tutorType: TextView = itemView.findViewById(R.id.tutorType)
        val tutorSubjects: TextView = itemView.findViewById(R.id.tutorSubjects)
        val tutorAvailability: TextView = itemView.findViewById(R.id.tutorAvailability)
        val tutorRate: TextView = itemView.findViewById(R.id.tutorRate)
    }

    // Creates a new card view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tutor_card, parent, false)
        return TutorViewHolder(view)
    }

    // Fills each card with tutor data
    override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
        val tutor = tutors[position]

        holder.tutorName.text = tutor.name
        holder.tutorSubjects.text = "📚 ${tutor.subjects.joinToString(", ")}"
        holder.tutorAvailability.text = "🕐 ${tutor.availability}"

        // Show tutor type badge
        if (tutor.type == TutorType.UNIVERSITY) {
            holder.tutorType.text = "University"
            holder.tutorType.setBackgroundColor(
                holder.itemView.context.getColor(com.example.academichub.R.color.professor_badge)
            )
        } else {
            holder.tutorType.text = "Peer"
            holder.tutorType.setBackgroundColor(
                holder.itemView.context.getColor(com.example.academichub.R.color.tutor_badge)
            )
        }

        // Show rate or free
        if (tutor.isPaid && tutor.rate != null) {
            holder.tutorRate.text = "💰 ${tutor.rate}"
        } else {
            holder.tutorRate.text = "✅ Free"
        }

        // Handle click on the card
        holder.itemView.setOnClickListener {
            onTutorClick(tutor)
        }
    }

    // Total number of tutors
    override fun getItemCount(): Int = tutors.size
}