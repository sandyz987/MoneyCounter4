package beannew

import com.google.gson.Gson
import java.io.Serializable

open class InfoWrapper : Serializable {
    var status: Int = 0
    var info: String = ""

    companion object {
        fun newInfo(status: Int = -1, info: String = ""): String {
            return InfoWrapper().apply {
                this.status = status
                this.info = info
            }.parseToString()
        }
    }
}

val InfoWrapper.isSuccessful get() = (status == 200 || status == 10000)

fun InfoWrapper.parseToString(): String {
    return Gson().toJson(this)
}

