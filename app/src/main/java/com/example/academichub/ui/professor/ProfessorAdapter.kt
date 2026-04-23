package com.example.academichub.ui.professor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.model.ProfessorInfo

class ProfessorAdapter(
    private val professors: List<ProfessorInfo>
) : RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {

    class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val professorName: TextView = itemView.findViewById(R.id.professorName)
        val professorDepartment: TextView = itemView.findViewById(R.id.professorDepartment)
        val professorCourses: TextView = itemView.findViewById(R.id.professorCourses)
        val professorOfficeHours: TextView = itemView.findViewById(R.id.professorOfficeHours)
        val professorLocation: TextView = itemView.findViewById(R.id.professorLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_professor_card, parent, false)
        return ProfessorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = professors[position]

        holder.professorName.text = professor.name
        holder.professorDepartment.text = professor.department
        holder.professorCourses.text = "📚 ${professor.courses.joinToString(", ")}"
        holder.professorOfficeHours.text = "🕐 ${professor.officeHours}"
        holder.professorLocation.text = "📍 ${professor.location}"

        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                professor.name,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = professors.size
}