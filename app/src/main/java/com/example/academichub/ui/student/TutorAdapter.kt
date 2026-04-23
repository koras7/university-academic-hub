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
    private val tutors: MutableList<TutorProfile>,
    private val onTutorClick: (TutorProfile) -> Unit
) : RecyclerView.Adapter<TutorAdapter.TutorViewHolder>() {

    // ViewHolder holds references to the card's views
    class TutorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tutorName: TextView = itemView.findViewById(R.id.tutorName)
        val tutorType: TextView = itemView.findViewById(R.id.tutorType)
        val tutorSubjects: TextView = itemView.findViewById(R.id.tutorSubjects)
        val tutorAvailability: TextView = itemView.findViewById(R.id.tutorAvailability)
        val tutorRate: TextView = itemView.findViewById(R.id.tutorRate)
        val tutorInitial: TextView = itemView.findViewById(R.id.tutorInitial)
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
        holder.tutorInitial.text = tutor.name.first().uppercaseChar().toString()

        // Show tutor type badge
        if (tutor.type == TutorType.UNIVERSITY) {
            holder.tutorType.text = "University"
        } else {
            holder.tutorType.text = "Peer"
        }

        // Show rate or free
        if (tutor.isPaid && tutor.rate != null) {
            holder.tutorRate.text = "💰 ${tutor.rate}"
            holder.tutorRate.setTextColor(
                holder.itemView.context.getColor(R.color.paid_color)
            )
        } else {
            holder.tutorRate.text = "✅ Free"
            holder.tutorRate.setTextColor(
                holder.itemView.context.getColor(R.color.free_color)
            )
        }

        // Handle click on the card
        holder.itemView.setOnClickListener {
            onTutorClick(tutor)
        }
    }

    // Total number of tutors
    override fun getItemCount(): Int = tutors.size

    fun updateTutors(newTutors: List<TutorProfile>) {
        (tutors as MutableList).clear()
        (tutors as MutableList).addAll(newTutors)
        notifyDataSetChanged()
    }
}