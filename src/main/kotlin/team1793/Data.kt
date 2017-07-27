package team1793

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

open class Month(val month: String, val year: Int, val sessions: ObservableList<Session> = FXCollections.observableList(arrayListOf())) {

    fun addSession(session: Session) {
        sessions.add(session)
    }

    val totalhours: Int
        get() {
            return sessions.map { it.hours }.sum()
        }

    val totalpay: String
        get() {
            return "$${totalhours * 12}"
        }

    val dates: List<LocalDate>
        get() {
            return sessions.map { it.date }
        }
    val format: String
        get() {
            return toString()
        }

    override fun toString(): String {
        return "${month.capitalize()}/$year"
    }
}


open class Session(val date: LocalDate, val login: LocalTime, var logout: LocalTime) {
    override fun toString(): String = "$date,${login.format(DateTimeFormatter.ofPattern("HH:mm"))},${logout.format(DateTimeFormatter.ofPattern("HH:mm"))}"
    val hours: Int
        get() = (ChronoUnit.MINUTES.between(login,logout) / 60).toInt()

    fun logoutNow() {
        logout = LocalTime.now()
    }

    fun isDone(): Boolean {
        return login.isBefore(logout)
    }
}
