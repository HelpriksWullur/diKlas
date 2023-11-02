package com.epic.diklas.data

data class TaskDataStudent(val subject: String, val teacher: String, val description: String, val deadlineDay: Short, val deadlineMonth: Short, val deadlineYear: Int, val stat: TaskStat, val result: Short, val note: String)
