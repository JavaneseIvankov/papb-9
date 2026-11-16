package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.repository

import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.model.Student

interface StudentRepository {
    suspend fun getStudents(): List<Student>
    suspend fun getStudent(id: Int): Student
    suspend fun searchStudents(query: String): List<Student>
}
