package com.epic.diklas.data

data class HistoryDataStudent(val subject: String, val teacher: String, val currentMeet: Int, val attendedMeets: Int, val totalMeets: Int)
data class HistoryDataTeacher(val subject: String)