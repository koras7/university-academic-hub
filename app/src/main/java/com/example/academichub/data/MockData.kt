package com.example.academichub.data

import com.example.academichub.model.*
import com.example.academichub.model.TutoringSession

object MockData {

    val tutors = listOf(
        TutorProfile(
            id = "t1",
            userId = "u2",
            name = "Sara",
            type = TutorType.PEER,
            subjects = listOf("Mathematics", "Physics"),
            courses = listOf("MATH 201", "PHYS 101", "MATH 301"),
            availability = "Mon, Wed 2-5 PM",
            isPaid = false
        ),
        TutorProfile(
            id = "t2",
            userId = "u3",
            name = "Michael",
            type = TutorType.PEER,
            subjects = listOf("Computer Science", "Programming"),
            courses = listOf("CS 101", "CS 201", "CS 3318"),
            availability = "Tue, Thu 3-6 PM",
            isPaid = true,
            rate = "$20/hour"
        ),
        TutorProfile(
            id = "t3",
            userId = "u4",
            name = " Emily ",
            type = TutorType.UNIVERSITY,
            subjects = listOf("Biology", "Chemistry"),
            courses = listOf("BIOL 101", "CHEM 101"),
            availability = "Mon-Fri 10 AM-12 PM",
            isPaid = false
        ),
        TutorProfile(
            id = "t4",
            userId = "u5",
            name = "James ",
            type = TutorType.PEER,
            subjects = listOf("Economics", "Statistics"),
            courses = listOf("ECON 201", "STAT 200"),
            availability = "Mon, Wed, Fri 1-4 PM",
            isPaid = true,
            rate = "$15/hour"
        ),
        TutorProfile(
            id = "t5",
            userId = "u6",
            name = "Academic Success Center",
            type = TutorType.UNIVERSITY,
            subjects = listOf("Writing", "Study Skills", "Math"),
            courses = listOf("All Levels"),
            availability = "Mon-Fri 9 AM-5 PM",
            isPaid = false
        )
    )

    val professors = listOf(
        ProfessorInfo(
            id = "p1",
            name = "Dr. Robert Smith",
            department = "Computer Science",
            courses = listOf("CS 3318", "CS 4350"),
            officeHours = "Tue, Thu 2-4 PM",
            location = "Science Building 204",
            email = "rsmith@university.edu"
        ),
        ProfessorInfo(
            id = "p2",
            name = "Prof. Maria Garcia",
            department = "Mathematics",
            courses = listOf("MATH 201", "MATH 301"),
            officeHours = "Mon, Wed 11 AM-1 PM",
            location = "Math Building 105",
            email = "mgarcia@university.edu"
        ),
        ProfessorInfo(
            id = "p3",
            name = "Dr. David Lee",
            department = "Physics",
            courses = listOf("PHYS 101", "PHYS 201"),
            officeHours = "Mon, Fri 3-5 PM",
            location = "Physics Lab 301",
            email = "dlee@university.edu"
        ),
        ProfessorInfo(
            id = "p4",
            name = "Test Professor",
            department = "Computer Science",
            courses = listOf("CS 3318", "CS 4412"),
            officeHours = "Mon, Wed 2-4 PM",
            location = "Science Building 101",
            email = "prof@university.edu"
        )
    )

    // Mutable list to store session requests
    val sessionRequests = mutableListOf<SessionRequest>()

    // Current logged-in user (will be set at login)
    var currentUser: User? = null

    // Mutable list to store posted tutoring sessions
    val tutoringSessions = mutableListOf<TutoringSession>()
}