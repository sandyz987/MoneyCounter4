package com.example.moneycounter4.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.extensions.runOnUiThread
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.utils.CalendarUtil
import com.example.moneycounter4.utils.toTimeString
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream

/**
 *@author zhangzhe
 *@date 2021/6/6
 *@description
 */

class GraphViewModel : BaseViewModel() {
    fun export(context: Context) {
        Toast.makeText(context, "导出excel中，请稍后", Toast.LENGTH_SHORT)

        Thread {
            try {
                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "总账单.xls"
                )
                file.createNewFile()
                val fos = FileOutputStream(file)
                //=====================
                val wb = HSSFWorkbook()
                val sheet: HSSFSheet = wb.createSheet("sheet0")
                val row1 = sheet.createRow(0)
                // 初始化表头
                row1.createCell(0).setCellValue("时间")
                row1.createCell(1).setCellValue("时间戳")
                row1.createCell(2).setCellValue("金额")
                row1.createCell(3).setCellValue("类型")
                row1.createCell(4).setCellValue("种类")
                row1.createCell(5).setCellValue("备注")
                row1.createCell(6).setCellValue("账户")
                row1.createCell(7).setCellValue("账本")

                // 以下为输出到excel
                var line = 1
                val list = DataReader.db?.counterDao()?.all
                list?.sortedBy { it.time }?.forEach {
                    val row = sheet.createRow(line++)
                    var colIndex = 0
                    add(wb, row, colIndex++, CalendarUtil.getCalendar(it.time ?: 0L).toTimeString())
                    add(wb, row, colIndex++, it.time.toString())
                    add(wb, row, colIndex++, it.money.toString())
                    add(wb, row, colIndex++, if (it.money ?: 0.0 > 0.0) "收入" else "支出")
                    add(wb, row, colIndex++, it.type.toString())
                    add(wb, row, colIndex++, it.tips.toString())
                    add(wb, row, colIndex++, it.accountBook.toString())
                    add(wb, row, colIndex, it.wallet.toString())
                }
                wb.write(fos)
                fos.flush()
                fos.close()

                context.runOnUiThread {
                    Toast.makeText(
                        context,
                        "导出excel成功！文件路径为：${file.absolutePath}",
                        Toast.LENGTH_LONG
                    ).show()
                    Toast.makeText(
                        context,
                        "导出excel成功！文件路径为：${file.absolutePath}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // TODO 无效，待重写 通知文件管理器新增了文件
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri = Uri.fromFile(file)
                intent.data = uri
                context.sendBroadcast(intent)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()

    }

    fun add(wb: HSSFWorkbook, row: HSSFRow, index: Int, str: String) {
        val cell = row.createCell(index)
        cell.setCellValue(str)
    }
}