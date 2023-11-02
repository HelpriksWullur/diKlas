package com.epic.diklas

import androidx.lifecycle.ViewModel
import com.epic.diklas.data.ClassStat
import com.epic.diklas.data.Role
import com.epic.diklas.data.activeClass
import com.epic.diklas.data.userActive
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class DiKlasViewModel : ViewModel() {
    data class UserData(
        val id: String,
        val name: String,
        val role: Role,
        val classOrSubject: String
    )

    data class ScheduleData(
        val keyCode: String,
        val subject: String,
        val code: String,
        val meet: Int,
        val room: String,
        val teacherName: String,
        val scheduleTime: String,
        val status: ClassStat
    )

    private val database = Firebase.database.reference

    private val _userDataState = MutableStateFlow(UserData("", "", Role.STUDENT, ""))
    val userDataState: StateFlow<UserData> get() = _userDataState

    init {
        val userDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val id = userActive
                val name = snapshot.child("data_user").child(id).child("nama").value.toString()
                val role = if (snapshot.child("data_user").child(id).child("role").value.toString()
                        .toInt() == 1
                ) Role.TEACHER else Role.STUDENT
                val classOrSubject =
                    snapshot.child("data_user").child(id).child("kelasAtauSubjek").value.toString()

                _userDataState.value = UserData(id, name, role, classOrSubject)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(userDataListener)
    }

    private val _scheduleDataState =
        MutableStateFlow(ScheduleData("", "", "", 0, "", "", "", ClassStat.NO_CLASS))
    val scheduleDataState: StateFlow<ScheduleData> get() = _scheduleDataState

    init {
        val scheduleDataListener = object : ValueEventListener {
            val time = Calendar.getInstance()
            val today = time.get(Calendar.DAY_OF_WEEK)

            val hour = time.get(Calendar.HOUR_OF_DAY)
            val minute = time.get(Calendar.MINUTE)
            val currentTime = hour * 60 + minute

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("data_schedule").child("kelas").children.forEach {
                    if (it.child("hari").value.toString() == today.toString()) {
                        // actualStartHour = jam mulai dengan format 1-2 digit
                        // startHour = jam mulai dengan format menit
                        val actualStartHour =
                            it.child("waktu_mulai").child("jam").value.toString().toInt()
                        val startHour = actualStartHour * 60

                        // startMinute = menit mulai dengan format normal
                        // start = jam mulai dalam format menit ditambah menit yang sebenarnya
                        val startMinute =
                            it.child("waktu_mulai").child("menit").value.toString().toInt()
                        val start = startHour + startMinute

                        // actualEndHour = jam selesai dengan format 1-2 digit
                        // endHour = jam selesai dengan format menit
                        val actualEndHour =
                            it.child("waktu_selesai").child("jam").value.toString().toInt()
                        val endHour = actualEndHour * 60

                        // endMinute = menit selesai dengan format normal
                        // end = jam selesai dengan format menit ditambah menit yang sebenernya
                        val endMinute =
                            it.child("waktu_selesai").child("menit").value.toString().toInt()
                        val end = endHour + endMinute

                        // scheduleTime[0] = format jam:menit mulai
                        // scheduleTime[1] = format jam:menit selesai
                        val scheduleTime = listOf(
                            "${if (actualStartHour.toString().length < 2) "0$actualStartHour" else actualStartHour}:${if (startMinute.toString().length < 2) "0$startMinute" else startMinute}",
                            "${if (actualEndHour.toString().length < 2) "0$actualEndHour" else actualEndHour}:${if (endMinute.toString().length < 2) "0$endMinute" else endMinute}"
                        )

                        val keyCode = it.key.toString()
                        val subject = it.child("subjek").value.toString()
                        val code = it.child("kode").value.toString()
                        val room = it.child("kelas").value.toString()
                        val teacherCode = it.child("guru").value.toString()
                        val teacherName = snapshot.child("data_user").child(teacherCode)
                            .child("nama").value.toString()
                        val meet = it.child("pertemuan").value.toString().toInt()

                        if (currentTime in start..end) {
                            val status = ClassStat.STARTED

                            _scheduleDataState.value = ScheduleData(
                                keyCode,
                                subject,
                                code,
                                meet,
                                room,
                                teacherName,
                                "${scheduleTime[0]} - ${scheduleTime[1]}",
                                status
                            )

                            activeClass = keyCode
                        } else if (currentTime < start) {
                            val status = ClassStat.NOT_YET_STARTED

                            _scheduleDataState.value = ScheduleData(
                                keyCode,
                                subject,
                                code,
                                meet,
                                room,
                                teacherName,
                                "${scheduleTime[0]} - ${scheduleTime[1]}",
                                status
                            )
                        } else {
                            val status = ClassStat.NO_CLASS

                            _scheduleDataState.value = ScheduleData(
                                keyCode,
                                subject,
                                code,
                                meet,
                                room,
                                teacherName,
                                "${scheduleTime[0]} - ${scheduleTime[1]}",
                                status
                            )
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(scheduleDataListener)
    }

    private val _studentAttendanceState = MutableStateFlow<List<Int>>(emptyList())
    val studentAttendanceState: StateFlow<List<Int>> get() = _studentAttendanceState

    init {
        val studentStatusListener = object : ValueEventListener {
            val data = mutableListOf<Int>()

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("data_schedule").child("kehadiran").child(activeClass).child(
                    userActive).children.forEach { meet ->
                    data.add(meet.value.toString().toInt())
                }
                _studentAttendanceState.value = data
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(studentStatusListener)
    }

    private val _teacherAndRoomState = MutableStateFlow<List<String>>(emptyList())
    val teacherAndRoomData: StateFlow<List<String>> get() = _teacherAndRoomState

    init {
        val teacherAndRoomListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = mutableListOf<String>()

                snapshot.child("data_schedule").children.forEach {
                    if (it.child("guru").value.toString() == userActive) {
                        data.add(it.child("kelas").value.toString())
                    }
                }
                _teacherAndRoomState.value = data.toSet().toList()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(teacherAndRoomListener)
    }
}
