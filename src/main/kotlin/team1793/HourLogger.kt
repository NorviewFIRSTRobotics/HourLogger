package team1793

import com.github.sarxos.webcam.Webcam
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.Result
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.concurrent.Task
import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import tornadofx.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.Reader
import java.io.Writer
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicReference

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
    val enableBusPasses = false
    val enableWebcam = false
    val saveFile: File = File("hourlogger.csv")
    val timeFormat: DateTimeFormatter? = DateTimeFormatter.ofPattern("HH:mm")
    val members: MutableList<Member> = mutableListOf()
    var imageProperty = SimpleObjectProperty<Image>()
    var qrProperty = SimpleStringProperty()


    val login = form {
        maxWidth = 350.0
        minWidth = maxWidth
        label("Enter Name To Login") {}
        val nameInput = textfield {}

        val infoLabel = label { isWrapText = true }
        val login = button("Login")
        val logout = button("Logout")

        fun attemptToLogin(name: String): Boolean {
            val firstName = name.substringBefore(" ")
            val lastName = name.substringAfter(" ")
            var member = members.find { m -> m.firstName.equals(firstName, true) && m.lastName.equals(lastName, true) }

            if (member != null && member.latestDate.equals(LocalDate.now()))
                return false
            val session: Session
            if (enableBusPasses) {
                val value = alert(Alert.AlertType.CONFIRMATION, "Bus Pass Dialog", "Do you need a Bus Pass?", ButtonType.YES, ButtonType.NO)
                session = RoboticSession(LocalDate.now(), LocalTime.now(), LocalTime.now(), value.result == ButtonType.YES)
            } else {
                session = Session(LocalDate.now(), LocalTime.now(), LocalTime.now())
            }
            member?.apply {
                addSession(session)
                infoLabel.text = String.format("Successfully Logged in %s at %s", name, session.login)
                nameInput.clear()
                return true
            }
            member = Member(firstName, lastName)
            member.addSession(session)
            members.add(member)
            infoLabel.text = String.format("Add and Logged in %s at %s", name, session.login)
            write(saveFile)
            table.refresh()
            nameInput.clear()
            return true
        }

        fun attemptToLogout(name: String) {
            val firstName = name.substringBefore(" ")
            val lastName = name.substringAfter(" ")
            val member = members.find { m -> m.firstName.equals(firstName, true) && m.lastName.equals(lastName, true) }
            member?.apply {
                latestSession.logoutNow()
                infoLabel.text = String.format("Logged out %s at %s", name, member.latestSession.logout)
            }
            write(saveFile)
            table.refresh()
            nameInput.clear()
        }
        login.setOnAction {
            nameInput.text = nameInput.text.trim()
            attemptToLogin(nameInput.text)
        }

        label {
            bind(qrProperty)
            isVisible = false
            qrProperty.onChange {
                if (!attemptToLogin(text)) {
                    attemptToLogout(text)
                }
            }
        }
        logout.setOnAction {
            nameInput.text = nameInput.text.trim()
            attemptToLogout(nameInput.text)
        }
        nameInput.setOnKeyPressed { e ->
            if (e.code == KeyCode.ENTER) {
                if (!attemptToLogin(nameInput.text)) {
                    attemptToLogout(nameInput.text)
                }
            }
        }
        if (enableWebcam) {
            borderpane {
                translateY = 50.0
                val w = 300.0
                val imageview = imageview {
                    isPreserveRatio = true
                    fitWidth = w
                }
                center = imageview
                runWebCam(imageview)
            }
        }
    }
    val table = tableview(members.observable()) {
        isEditable = true
        val col1 = column("First Name", Member::firstName)
        col1.makeEditable()
        col1.setOnEditCommit { col -> col.rowValue.firstName = col.newValue; write(saveFile) }
        val col2 = column("Last Name", Member::lastName)
        col2.makeEditable()
        col2.setOnEditCommit { col -> col.rowValue.lastName = col.newValue; write(saveFile) }
        nestedColumn("Latest Session") {
            column("Date", Member::latestDate)
            column("Login", Member::latestLogin)
            column("Logout", Member::latestLogout)
            if (enableBusPasses)
                column("Buspass", Member::latestBusPass)
        }
        rowExpander {
            tableview(it.sessions) {
                column("Date", Session::date)
                column("Login", Session::login)
                column("Logout", Session::logout)
                prefHeight = 100.0
            }
        }
        columnResizePolicy = SmartResize.POLICY
        onUserDelete {
            members.remove(selectedItem)
            write(saveFile)
            refresh()
        }
    }

    override val root = BorderPane()

    init {
        runAsync {
            Thread.sleep(10)
        } ui {
            SmartResize.POLICY.requestResize(table)
            read(saveFile)
            write(saveFile)
            with(root) {
                center = table
                left = login
            }
        }
    }

    private fun read(file: File) {
        var reader: Reader? = null
        var parser: CSVParser? = null
        try {
            reader = file.reader()
            parser = CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withHeader(*(arrayListOf("First Name", "Last Name", "Sessions").toTypedArray())))
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
                        val session: Session
                        if (enableBusPasses) {
                            session = RoboticSession(LocalDate.parse(s[0]), LocalTime.parse(s[1], timeFormat), LocalTime.parse(s[2], timeFormat), s[3].toBoolean())
                        } else {
                            session = Session(LocalDate.parse(s[0]), LocalTime.parse(s[1], timeFormat), LocalTime.parse(s[2], timeFormat))
                        }
                        sessions.add(session)
                    }
                    val member = Member(record.get("First Name"), record.get("Last Name"), sessions.observable())
                    members.add(member)
                }
            }
        } catch(e: Exception) {
            e.printStackTrace()
        } finally {
            reader?.close()
            parser?.close()
        }
    }

    private fun write(file: File) {
        var writer: Writer? = null
        var printer: CSVPrinter? = null
        try {
            writer = (file.writer())
            printer = CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter(';'))
            printer.printRecord(arrayListOf("First Name", "Last Name", "Sessions"))
            members.forEach { m ->
                val record = arrayListOf(m.firstName, m.lastName, m.sessions.joinToString(separator = "|"))
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

    private fun runWebCam(view: ImageView) {
        val webCam = Webcam.getDefault()
        val webCamTask = object : Task<Void>() {
            @Throws(Exception::class)
            override fun call(): Void? {
                webCam.open()
                startWebCam(webCam)
                view.imageProperty().bind(imageProperty)
                return null
            }
        }
        val webCamThread = Thread(webCamTask)
        webCamThread.isDaemon = true
        webCamThread.start()
    }

    private fun startWebCam(webCam: Webcam) {
        val stopCamera = false
        val task = object : Task<Void>() {
            @Throws(Exception::class)
            override fun call(): Void? {
                val ref = AtomicReference<WritableImage>()
                var img: BufferedImage
                while (!stopCamera) {
                    try {
                        img = webCam.image
                        if (img != null) {
                            val qr = detectQR(img)
                            ref.set(SwingFXUtils.toFXImage(img, ref.get()))
                            img.flush()
                            Platform.runLater {
                                imageProperty.set(ref.get())
                                if (!qr.isEmpty()) {
                                    qrProperty.set(qr)
                                }
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                return null
            }
        }
        val th = Thread(task)
        th.isDaemon = true
        th.start()
    }

    private fun detectQR(image: BufferedImage): String {
        var result: Result? = null
        val source = BufferedImageLuminanceSource(image)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            result = MultiFormatReader().decode(bitmap)
        } catch (e: NotFoundException) {
            // fall thru, it means there is no QR code in image
        }
        result?.apply { return text }
        return ""
    }
}

