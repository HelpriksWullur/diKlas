package com.epic.diklas.data.model

import androidx.lifecycle.ViewModel
import com.epic.diklas.data.TaskStat
import com.epic.diklas.data.userActive
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StudentViewModel : ViewModel() {
    private val database = Firebase.database.reference

    data class StudentHistoryData(
        val teacherName: String,
        val classCode: String,
        val subject: String,
        val room: String,
        val meet: Int,
        val totalAttendances: Int,
        val totalMeet: Int,
        val attendances: List<Int>
    )

    private val _historyStudentState = MutableStateFlow<List<StudentHistoryData>>(emptyList())
    val historyStudentState: StateFlow<List<StudentHistoryData>> get() = _historyStudentState

    init {
        val historyStudentListener = object : ValueEventListener {
            val data = mutableListOf<StudentHistoryData>()
            override fun onDataChange(snapshot: DataSnapshot) {
                val room = snapshot.child("data_user").child(userActive).child("kelasAtauSubjek").value.toString()

                snapshot.child("data_schedule").child("kehadiran").children.forEach { kelas ->
                    kelas.children.forEach { studentCode ->
                        if (studentCode.key.toString() == userActive) {
                            val teacherId = snapshot.child("data_schedule").child("kelas").child(kelas.key.toString()).child("guru").value.toString()
                            val teacherName = snapshot.child("data_user").child(teacherId).child("nama").value.toString()

                            val classInfo = snapshot.child("data_schedule").child("kelas").child(kelas.key.toString())
                            val subject = classInfo.child("subjek").value.toString()
                            val meet = classInfo.child("pertemuan").value.toString().toInt()
                            val totalMeet = classInfo.child("total_pertemuan").value.toString().toInt()
                            var totalAttendances = 0
                            val attendances = mutableListOf<Int>()
                            studentCode.children.forEach { meetIndex ->
                                val incrementValue = if (meetIndex.value.toString().toInt() == 1) 1 else 0
                                totalAttendances += incrementValue
                                attendances.add(meetIndex.value.toString().toInt())
                            }

                            data.add(StudentHistoryData(teacherName, kelas.key.toString(), subject, room, meet, totalAttendances, totalMeet, attendances))
                        }
                    }
                }

                _historyStudentState.value = data
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(historyStudentListener)
    }

    data class StudentTaskData(
        val subject: String,
        val taskCode: String,
        val answer: String,
        val teacherName: String,
        val description: String,
        val statusTask: TaskStat,
        val deadline: String,
        val note: String,
        val score: Int,
    )

    private val _studentTaskState = MutableStateFlow<List<StudentTaskData>>(emptyList())

    val studentTaskData: StateFlow<List<StudentTaskData>> get() = _studentTaskState

    init {
        val studentTaskListener = object : ValueEventListener {
            val data = mutableListOf<StudentTaskData>()

            override fun onDataChange(snapshot: DataSnapshot) {
                val room = snapshot.child("data_user").child(userActive).child("kelasAtauSubjek").value.toString()

                snapshot.child("data_task").child("tugas").children.forEach { task ->
                    if (task.child("kelas").value.toString() == room) {
                        val subject = task.child("subjek").value.toString()
                        val taskCode = task.key.toString()
                        val teacherId = task.child("guru").value.toString()
                        val teacherName = snapshot.child("data_user").child(teacherId).child("nama").value.toString()
                        val description = task.child("deskripsi").value.toString()

                        val day = task.child("tenggat_waktu").child("hari").value.toString()
                        val month = task.child("tenggat_waktu").child("bulan").value.toString()
                        val year = task.child("tenggat_waktu").child("tahun").value.toString()
                        val hour = task.child("tenggat_waktu").child("jam").value.toString()
                        val minute = task.child("tenggat_waktu").child("menit").value.toString()

                        val date = "$day/$month/$year"
                        val time = "${if(hour.length < 2) "0$hour" else hour}:${if(minute.length < 2) "0$minute" else minute}"
                        val deadline = "$date $time"

                        val answer =
                            snapshot.child("data_task").child("penilaian").child(task.key.toString())
                                .child(userActive).child("jawaban").value.toString()

                        val status = snapshot.child("data_task").child("penilaian").child(task.key.toString()).child(
                            userActive).child("status").value.toString().toInt()
                        val statusTask = when(status) {
                            0 -> TaskStat.NOT_DONE
                            1 -> TaskStat.ASSESSED
                            2 -> TaskStat.NOT_YET_ASSESSED
                            3 -> TaskStat.LATE
                            else -> { TaskStat.NOT_DONE }
                        }

                        val note = snapshot.child("data_task").child("penilaian").child(task.key.toString()).child(
                            userActive).child("catatan").value.toString()
                        val score = snapshot.child("data_task").child("penilaian").child(task.key.toString()).child(
                            userActive).child("nilai").value.toString().toInt()

                        data.add(StudentTaskData(subject, taskCode, answer, teacherName, description, statusTask, deadline, note, score))
                    }
                }

                _studentTaskState.value = data
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(studentTaskListener)
    }
}