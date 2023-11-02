package com.epic.diklas.data.model

import androidx.lifecycle.ViewModel
import com.epic.diklas.data.AttendStat
import com.epic.diklas.data.TaskStat
import com.epic.diklas.data.activeClass
import com.epic.diklas.data.userActive
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TeacherViewModel: ViewModel() {
    data class TeacherHistoryData(
        val name: String,
        val studentId: String,
        val room: String,
        val meet: Int,
        val totalAttendances: Int,
        val totalMeet: Int,
        val attendances: List<Int>
    )

    private val database = Firebase.database.reference

    private val _historyTeacherState = MutableStateFlow<List<TeacherHistoryData>>(emptyList())

    val historyDataTeacher: StateFlow<List<TeacherHistoryData>> get() = _historyTeacherState

    init {
        val teacherHistoryListener = object : ValueEventListener {
            val data = mutableListOf<TeacherHistoryData>()

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("data_schedule").child("kelas").children.forEach { classCode ->
                    if (classCode.child("guru").value.toString() == userActive)  {
                        val kode = classCode.key.toString()
                        val room = classCode.child("kelas").value.toString()
                        val meet = classCode.child("pertemuan").value.toString().toInt()
                        val totalMeet = classCode.child("total_pertemuan").value.toString().toInt()

                        snapshot.child("data_schedule").child("kehadiran").child(kode).children.forEach { studentCode ->
                            val name = snapshot.child("data_user").child(studentCode.key.toString()).child("nama").value.toString()
                            var totalAttendances = 0
                            val attendances = mutableListOf<Int>()
                            studentCode.children.forEach { meetIndex ->
                                val incrementValue = if (meetIndex.value.toString().toInt() == 1) 1 else 0
                                totalAttendances += incrementValue
                                attendances.add(meetIndex.value.toString().toInt())
                            }

                            data.add(TeacherHistoryData(name, studentCode.key.toString(), room, meet, totalAttendances, totalMeet, attendances))
                        }

                    }
                }
                _historyTeacherState.value = data
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(teacherHistoryListener)
    }

    data class TeacherTaskData(
        val answer: String,
        val studentId: String,
        val studentName: String,
        val studentClass: String,
        val description: String,
        val statusTask: TaskStat,
        val deadline: String,
        val note: String,
        val score: Int
    )

    private val _teacherTaskState = MutableStateFlow<List<TeacherTaskData>>(emptyList())
    val teacherTaskState: StateFlow<List<TeacherTaskData>> get() = _teacherTaskState

    init {
        val teacherTaskListener = object : ValueEventListener {
            val data = mutableListOf<TeacherTaskData>()
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("data_task").child("tugas").children.forEach {  task ->
                    val taskCode = task.key.toString()
                    val description = task.child("deskripsi").value.toString()

                    val day = task.child("tenggat_waktu").child("hari").value.toString()
                    val month = task.child("tenggat_waktu").child("bulan").value.toString()
                    val year = task.child("tenggat_waktu").child("tahun").value.toString()
                    val hour = task.child("tenggat_waktu").child("jam").value.toString()
                    val minute = task.child("tenggat_waktu").child("menit").value.toString()

                    val date = "$day/$month/$year"
                    val time = "${if(hour.length < 2) "0$hour" else hour}:${if(minute.length < 2) "0$minute" else minute}"
                    val deadline = "$date $time"

                    snapshot.child("data_task").child("penilaian").child(taskCode).children.forEach { result ->
                        val answer = result.child("jawaban").value.toString()
                        val studentId = result.key.toString()
                        val studentName = snapshot.child("data_user").child(result.key.toString()).child("nama").value.toString()
                        val studentClass = snapshot.child("data_user").child(result.key.toString()).child("kelasAtauSubjek").value.toString()
                        val statusTask = when(result.child("status").value.toString().toInt()) {
                            0 -> TaskStat.NOT_DONE
                            1 -> TaskStat.ASSESSED
                            2 -> TaskStat.NOT_YET_ASSESSED
                            3 -> TaskStat.LATE
                            else -> TaskStat.NOT_DONE
                        }

                        val note = result.child("catatan").value.toString()
                        val score = result.child("nilai").value.toString().toInt()

                        data.add(TeacherTaskData(answer, studentId, studentName, studentClass, description, statusTask, deadline, note, score))
                    }
                }

                _teacherTaskState.value = data
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(teacherTaskListener)
    }

    data class Attendance(
        val name: String,
        val code: String,
        val meet: Int,
        val date: String,
        val status: AttendStat
    )

    private val _manualAttendanceState = MutableStateFlow<List<Attendance>>(emptyList())
    val manualAttendanceState: StateFlow<List<Attendance>> get() = _manualAttendanceState

    init {
        if (activeClass.isNotEmpty()) {
            val manualAttendanceListener = object : ValueEventListener {
                val data = mutableListOf<Attendance>()

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.child("data_schedule").child("kehadiran").child(activeClass).children.forEach { student ->
                        val name = snapshot.child("data_user").child(student.key.toString()).child("nama").value.toString()
                        val code = student.key.toString()
                        val meet = snapshot.child("data_schedule").child("kelas").child(activeClass).child("pertemuan").value.toString().toInt()
                        val date = "22/2/22"
                        val status = when (student.child((meet -1).toString()).value.toString().toInt()) {
                            0 -> AttendStat.NOT_ATTENDANCE
                            1 -> AttendStat.ATTENDANCE
                            2 -> AttendStat.PERMISSION
                            else -> {
                                AttendStat.NOT_ATTENDANCE
                            }
                        }

                        data.add(Attendance(name, code, meet, date, status))
                    }
                    _manualAttendanceState.value = data
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }

            database.addValueEventListener(manualAttendanceListener)
        }
    }

    fun changeQR(scheduleCode: String, newCode: String) {
        database.child("data_schedule").child("kelas").child(scheduleCode).child("kode")
            .setValue(newCode)
    }

    fun changeStatus(studentId: String, meet: Int, value: Int) {
        database.child("data_schedule").child("kehadiran").child(activeClass).child(studentId)
            .child((meet - 1).toString()).setValue(value)
    }
}

