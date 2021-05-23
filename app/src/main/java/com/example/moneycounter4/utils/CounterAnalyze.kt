package com.example.moneycounter4.utils

import android.util.Log
import com.example.moneycounter4.beannew.CounterDataItem
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


object CounterAnalyze {
    fun getStringProposal(list: List<CounterDataItem>): List<String> {
        Log.e("sandyzhang", list.toString())
        val proposal: ArrayList<String> = ArrayList()
        var weekDayOut = 0.0
        var weekendOut = 0.0
        val c: Calendar = Calendar.getInstance()
        /**
         * 周末比平日支出多
         */
        for (i in list.indices) {
            if (list[i].money!! > 0) {
                continue
            }
            c.timeInMillis = list[i].time!!
            val day: Int = c.get(Calendar.DAY_OF_WEEK)
            if (day == 7 || day == 1) {
                weekendOut -= list[i].money!!
            } else {
                weekDayOut -= list[i].money!!
            }
        }
        if (weekendOut / 2 > weekDayOut / 5) {
            proposal.add(
                "周末比平日支出多："
                        + (((weekendOut / 2) / (weekDayOut / 5) - 1) * 100).formatToString()
                        + "%" + "，\n记得周末节制用钱噢~"
            )
        } else proposal.add(
            "周末消费居然低于工作日："
                    + (abs(((weekendOut / 2) / (weekDayOut / 5) - 1) * 100)).formatToString()
                    + "%" + "，看来阁下又节省了不少钱哟~"
        )
        /**
         * 当前总收入比支出多
         */
        var weekIn = 0.0
        var weekOut = 0.0
        for (i in 0 until list.size) {
            if (list[i].money!! > 0) {
                weekIn += (list[i].money)!!
            }
            if (list[i].money!! < 0) {
                weekOut -= (list[i].money)!!
            }
        }
        if (weekIn > weekOut) {
            val t = weekIn / weekOut - 1
            if (t <= 0.3) proposal.add("当前总收入比支出多" + (t * 100).formatToString() + "%" + "亲，您的收入用得差不多了，小心囊中羞涩啊") else if (t <= 0.5) proposal.add(
                "当前总收入比支出多" + (t * 100).formatToString() + "%" + "阁下的余额已不多，花钱时务必三思而后行"
            ) else if (t <= 0.7) proposal.add("当前总收入比支出多" + (t * 100) + "%" + "爱卿余额还够，可以放纵，但仅限一次") else proposal.add(
                "当前总收入比支出多" + (t * 100).formatToString() + "%" + "哇！余额充足，恭喜您重新拥有钱财自由分配权"
            )
        }
        /**
         * 备注中有吃，对吃的次数和钱数分析
         */
        var weekendEat = 0.0
        var weekDayEat = 0.0
        var weekendEatN = 0
        var weekDayEatN = 0
        for (i in list.indices) {
            if (list[i].money!! > 0) {
                weekIn += (list[i].money)!!
            }
            list[i].time?.let { c.timeInMillis = it }
            val day: Int = c.get(Calendar.DAY_OF_WEEK)
            if (list[i].type!!.contains("餐饮") ||
                list[i].tips!!.contains("吃") ||
                list[i].type!!.contains("零食") ||
                list[i].type!!.contains("烟酒") ||
                list[i].type!!.contains("水果")
            ) {
                if (day == 7 || day == 1) {
                    weekendEat -= (list[i].money)!!
                    weekendEatN++
                } else {
                    weekDayEat -= (list[i].money)!!
                    weekDayEatN++
                }
            }
        }
        if (weekendEatN != 0 || weekDayEatN != 0) {
            var t: String? = null
            if (weekendEatN != 0 && weekDayEatN != 0) t =
                "周末吃" + (weekendEatN) + "次东西的花费：" + weekendEat.formatToString() + "￥" + "，工作日吃" + weekDayEatN + "次东西的花费：" + weekDayEat.formatToString() + "￥\n"
            val t1 =
                "周末平均每天花费" + (weekendEat / 2).formatToString() + "￥" + "，工作日平均每天花费" + (weekDayEat / 5).formatToString() + "￥\n" + "周末平均每次花费" + (weekendEat / weekendEatN).formatToString() + "￥\n" + "工作日平均每次花费" + (weekDayEat / weekDayEatN).formatToString() + "￥\n"
            if (weekendEat / 2 > weekDayEat / 5 || ((weekendEatN != 0) && (weekDayEatN != 0) && (weekendEat / weekendEatN > weekDayEat / weekDayEatN))) proposal.add(
                t + t1 + "周末吃东西较多，注意运动，小心变胖哦"
            ) else proposal.add(t + t1 + "周末饮食良好，继续保持完美身材")
        }
        /**
         * 周末与平日收入
         */
        var weekdayin = 0.0
        var weekendin = 0.0
        for (i in list.indices) {
            if (list[i].money!! < 0) {
                continue
            }
            c.timeInMillis = list[i].time!!
            val day: Int = c.get(Calendar.DAY_OF_WEEK)
            if (day == 7 || day == 1) {
                weekendin += (list[i].money)!!
            } else {
                weekdayin += (list[i].money)!!
            }
        }
        if (weekendin / 2 > weekdayin / 5) {
            proposal.add(
                ("周末" + weekendin.formatToString() + "￥ 比" + "平日" + weekdayin.formatToString() + "￥ 收入多："
                        + (((weekendin / 2) / (weekdayin / 5) - 1) * 100).formatToString()
                        + "%" + "，原来阁下周末赚钱更努力，记得劳逸结合，保重身体哦~")
            )
        } else proposal.add(
            ("平日" + weekdayin.formatToString() + "￥ 比" + "周末" + weekendin.formatToString() + "￥ 收入多："
                    + (((weekdayin / 5) / (weekendin / 2) - 1) * 100).formatToString()
                    + "%" + "，阁下在工作日努力赚钱，在周末适当放松也是很好的~")
        )
        /**
         * 备注中有玩，对玩的次数和钱数分析
         */
        weekendEat = 0.0
        weekDayEat = 0.0
        weekendEatN = 0
        weekDayEatN = 0
        for (i in list.indices) {
            if (list[i].money!! > 0) {
                weekIn += (list[i].money)!!
            }
            c.timeInMillis = list[i].time!!
            val day: Int = c.get(Calendar.DAY_OF_WEEK)
            if (list[i].tips!!.contains("玩") ||
                list[i].tips!!.contains("充值") ||
                list[i].type!!.contains("社交") ||
                list[i].type!!.contains("彩票") ||
                list[i].type!!.contains("礼金") ||
                list[i].type!!.contains("礼品") ||
                list[i].type!!.contains("朋友") ||
                list[i].type!!.contains("娱乐")
            ) {
                if (day == 7 || day == 1) {
                    weekendEat -= (list[i].money)!!
                    weekendEatN++
                } else {
                    weekDayEat -= (list[i].money)!!
                    weekDayEatN++
                }
            }
        }
        if (weekendEatN != 0 || weekDayEatN != 0) {
            var t: String? = null
            if (weekendEatN != 0 && weekDayEatN != 0) t =
                "周末玩" + weekendEatN + "次 总计的花费：" + weekendEat.formatToString() + "￥\n" + "工作日玩" + weekDayEatN + "次 总计的花费：" + weekDayEat.formatToString() + "￥"
            val t1 =
                "，周末平均每天花费" + (weekendEat / 2).formatToString() + "￥\n" + "工作日平均每天花费" + (weekDayEat / 5).formatToString() + "￥，" + "周末平均每次花费" + (weekendEat / weekendEatN).formatToString() + "￥\n" + "工作日平均每次花费" + (weekDayEat / weekDayEatN).formatToString() + "￥\n"
            if (weekendEat / 2 > weekDayEat / 5 || ((weekendEatN != 0) && (weekDayEatN != 0) && (weekendEat / weekendEatN > weekDayEat / weekDayEatN))) proposal.add(
                t + t1 + "周末玩的较多，注意金钱和时间的开销吖"
            ) else proposal.add(t + t1 + "周末玩的次数在合理范围内，继续保持优良作风，但不是杜绝玩耍喔")
        }
        return proposal
    }
}


fun Double.formatToString(): String {
    return String.format("%.2f", this)
}