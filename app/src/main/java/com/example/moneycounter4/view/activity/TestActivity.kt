package com.example.moneycounter4.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moneycounter4.R
import com.example.moneycounter4.view.costom.DataItem
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        counter_graph_view.data = listOf(
            DataItem("日", 5.6),
            DataItem("一", 3.2),
            DataItem("二", 6.5),
            DataItem("三", 2.4),
            DataItem("四", 2.5),
            DataItem("五", 0.0),
            DataItem("六", 8.0)
        )
        counter_graph_view.title = "收入统计"

    }
}