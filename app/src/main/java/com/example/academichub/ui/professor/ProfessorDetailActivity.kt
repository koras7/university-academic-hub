package com.example.academichub.ui.professor

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.academichub.R
import com.example.academichub.data.MockData

class ProfessorDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PROFESSOR_ID = "PROFESSOR_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor_detail)

        val professorId = intent.getStringExtra(EXTRA_PROFESSOR_ID)
        val professor = MockData.professors.find { it.id == professorId }
            ?: run { finish(); return }

        // Header
        val initials = professor.name
            .split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().uppercaseChar().toString() }
        findViewById<TextView>(R.id.professorInitialText).text = initials
        findViewById<TextView>(R.id.detailProfessorName).text = professor.name
        findViewById<TextView>(R.id.detailProfessorDepartment).text = professor.department

        // Info cards
        findViewById<TextView>(R.id.detailOfficeHours).text = professor.officeHours
        findViewById<TextView>(R.id.detailLocation).text = professor.location

        // Course pills
        val container = findViewById<LinearLayout>(R.id.coursePillsContainer)
        val gap = dpToPx(8)
        professor.courses.forEach { course ->
            val pill = TextView(this).apply {
                text = course
                setTextColor(Color.WHITE)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                setPadding(dpToPx(12), dpToPx(6), dpToPx(12), dpToPx(6))
                background = ContextCompat.getDrawable(
                    this@ProfessorDetailActivity,
                    R.drawable.bg_featured_indigo
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, gap, 0)
                layoutParams = params
            }
            container.addView(pill)
        }

        // Email button
        findViewById<TextView>(R.id.emailProfessorButton).setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${professor.email}")
            }
            startActivity(emailIntent)
        }
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density + 0.5f).toInt()
}
