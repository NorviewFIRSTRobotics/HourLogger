package team1793

import javafx.scene.layout.BorderPane
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import tornadofx.*
import java.io.File
import java.io.Reader
import java.io.Writer
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 *
 *
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 10/29/16
 */
class HourLoggerApp : App(HourLogger::class)

class HourLogger : View("HourLogger-Standard-Edition") {
    val file: File = File("hourlogger.csv")
    val timeFormat: DateTimeFormatter? = DateTimeFormatter.ofPattern("HH:mm")
    val months: MutableList<Month> = mutableListOf()
    val login = form {
        maxWidth = 100.0
        minWidth = maxWidth

        val login = button("Login")
        val logout = button("Logout")
        login.isVisible = false
        logout.isVisible = false
        val date = LocalDate.now()
        fun showButtons() {
            val session = getSession(date)
            if (session == null) {
                login.isVisible = true
                logout.isVisible = false
            } else {
                if (session.isDone()) {
                    login.isVisible = false
                    logout.isVisible = false
                } else {
                    logout.isVisible = true
                    login.isVisible = false
                }
            }
        }
        showButtons()
        login.setOnAction {
            val month = getMonth(date)
            if (!month.dates.contains(LocalDate.now())) {
                var session = Session(LocalDate.now(), LocalTime.now(), LocalTime.now())
                month.addSession(session)
            }
            write()
            showButtons()
            table.refresh()
        }
        logout.setOnAction {
            val month = getMonth(date)
            if (month.dates.contains(LocalDate.now())) {
                month.sessions.find { date == LocalDate.now() }?.let {
                    it.logoutNow()
                }
            }
            write()
            showButtons()
            table.refresh()
        }
    }

    fun getSession(date: LocalDate): Session? {
        return months.flatMap { it.sessions }.find { it.date.isEqual(date) }
    }

    fun getMonth(date: LocalDate): Month {
        var month = (months.find { it.month.equals(date.month.name, true) && it.year == date.year })
        println(month)
        if (month == null) {
            month = Month(date.month.name.toLowerCase(), date.year)
            months.add(month)
        }
        return month
    }

    val table = tableview(months.observable()) {
        column("Month", Month::format)
        column("Total Hours", Month::totalhours)
        column("Total Pay", Month::totalpay)
        rowExpander {
            tableview(it.sessions) {
                column("Date", Session::date)
                column("Login", Session::login)
                column("Logout", Session::logout)
                column("Hours", Session::hours)
                prefHeight = 100.0
            }
        }
        columnResizePolicy = SmartResize.POLICY
    }

    override val root = BorderPane()

    init {
        runAsync {
            Thread.sleep(10)
        } ui {
            read()
            write()
            SmartResize.POLICY.requestResize(table)
            with(root) {
                center = table
                left = login
            }
        }
    }

    private fun read() {
        var reader: Reader? = null
        var parser: CSVParser? = null
        try {
            if (!file.exists())
                file.createNewFile()
            reader = file.reader()
            parser = CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withHeader(*(arrayListOf("Month", "Year", "Sessions").toTypedArray())))
            parser.records.forEachIndexed { i, record ->
                if (i != 0) {
                    val readSessions = record.get("Sessions").split('|')
                    val sessions: ArrayList<Session> = arrayListOf()
                    readSessions.forEach {
                        if (it.isEmpty())
                            return@forEach
                        val s = it.split(',')
                        if (s.isEmpty())
                            return@forEach
                        val session: Session = Session(LocalDate.parse(s[0]), LocalTime.parse(s[1], timeFormat), LocalTime.parse(s[2], timeFormat))
                        sessions.add(session)
                    }
                    months.add(Month(record.get("Month"), record.get("Year").toInt(), sessions.observable()))
                }
            }
        } catch(e: Exception) {
            e.printStackTrace()
        } finally {
            reader?.close()
            parser?.close()
        }
    }

    private fun write() {
        var writer: Writer? = null
        var printer: CSVPrinter? = null
        try {
            writer = file.writer()
            if (!file.exists())
                file.createNewFile()
            printer = CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter(';'))
            printer.printRecord(arrayListOf("Month", "Year", "Sessions"))
            months.forEach {
                val record = arrayListOf(it.month, it.year, it.sessions.joinToString(separator = "|"))
                printer!!.printRecord(record)
            }
        } catch(e: Exception) {
            e.printStackTrace()
        } finally {
            writer?.flush()
            writer?.close()
            printer?.close()
        }
    }

}

