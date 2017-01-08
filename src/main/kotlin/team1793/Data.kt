package team1793

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.comparisons.compareBy
class WebcamInfo(var  webCamName: String, var webcamIndex: Int)

open class Member(var firstName: String, var lastName: String, val sessions: ObservableList<Session> = FXCollections.observableList(arrayListOf())) {
    fun addSession(session: Session) {
        //TODO already have sessions for today
        sessions.add(session)
    }
    var latestDate : LocalDate
        get() {
            return latestSession.date
        }
        set(value) {}
    var latestLogin : LocalTime
        get() {
            return latestSession.login
        }
        set(value) {}

    var latestLogout: LocalTime
        get() {
            return latestSession.logout
        }
        set(value) {}
    var latestBusPass : Boolean
        get() {
            if(latestSession is RoboticSession)
                return (latestSession as RoboticSession).buspass
            return false
        }
        set(value) {}

    var latestSession: Session
        get() {
            val latest = sessions.sortedWith(compareBy { it.login })
            return latest.last()
        }
        set(value) {
        }
}

open class Session(val date: LocalDate, val login: LocalTime, var logout: LocalTime) {
    override fun toString(): String = "$date,${login.format(DateTimeFormatter.ofPattern("HH:mm"))},${logout.format(DateTimeFormatter.ofPattern("HH:mm"))}"
    fun format(): String = "Date:$date,Login: ${login.format(DateTimeFormatter.ofPattern("HH:mm"))}, Logout: ${logout.format(DateTimeFormatter.ofPattern("HH:mm"))}"
    fun logoutNow() {
        logout = LocalTime.now()
    }
}

class RoboticSession(date: LocalDate, login: LocalTime, logout: LocalTime, val buspass: Boolean ) : Session(date,login,logout) {
    override fun toString(): String = "$date,${login.format(DateTimeFormatter.ofPattern("HH:mm"))},${logout.format(DateTimeFormatter.ofPattern("HH:mm"))},$buspass"
}
