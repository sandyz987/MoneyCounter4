package com.example.moneycounter4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.baoyz.widget.PullRefreshLayout
import com.example.moneycounter4.R
import com.example.moneycounter4.bean.TalkItem
import com.example.moneycounter4.utils.HttpUtilCallback
import com.example.moneycounter4.utils.HttpUtils.HttpUtil
import com.example.moneycounter4.utils.JSonEvalUtils.JSonArray
import com.example.moneycounter4.view.adapter.TalkRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.viewmodel.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_community.*


class FragmentCommunity : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var adapter: TalkRecyclerViewAdapter?

        val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        recyclerViewTalkList.layoutManager = LinearLayoutManager(context)
        if (recyclerViewTalkList.itemDecorationCount == 0) {
            recyclerViewTalkList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }


        val listener = PullRefreshLayout.OnRefreshListener {
            HttpUtil.getInstance()
                .httpGet((activity?.application as MainApplication).connectionUrlMain, object :
                    HttpUtilCallback {
                    override fun doSomething(respond: String?) {
                        val list = ArrayList<TalkItem>()

                        try {
                            val jsonArray = JSonArray(respond)
                            for (i in 0 until jsonArray.size()) {
                                val gson = Gson()
                                list.add(
                                    gson.fromJson(
                                        jsonArray.get(i),
                                        TalkItem::class.javaObjectType
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        list.reverse()
                        adapter = context?.let {
                            TalkRecyclerViewAdapter(
                                activity,
                                it,
                                list,
                                (activity?.application as MainApplication).connectionUrlMain,
                                viewModel.accountNum.get(),
                                viewModel.token
                            )
                        }
                        activity?.runOnUiThread {
                            adapter?.let {
                                recyclerViewTalkList?.adapter = it
                                adapter?.notifyDataSetChanged()
                            }
                            pullRefreshLayout?.setRefreshing(false)
                        }
//                        recyclerViewTalkList.addOnScrollListener(object :
//                            RecyclerView.OnScrollListener() {
//                            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                                super.onScrolled(recyclerView, dx, dy)
//
//                                recyclerView.post {
//                                    recyclerViewTalkList?.let {
//                                        if (!recyclerViewTalkList.canScrollVertically(1)) {
//                                            adapter?.enlargeSize(adapter?.size!! + 8)
//                                        }
//                                    }
//
//                                }
//
//                            }
//                        })

                    }

                    override fun error() {
                        activity?.runOnUiThread {
                            Toast.makeText(activity, "加载失败，请检查网络连接呀", Toast.LENGTH_SHORT).show()
                            pullRefreshLayout?.setRefreshing(false)
                        }

                    }
                }, activity, "action", "gettalk")
        }

        pullRefreshLayout.setOnRefreshListener(listener)
        listener.onRefresh()
        pullRefreshLayout.setRefreshing(true)
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP)

        sendDynamic.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_global_fragmentTalkEdit)
        }


    }


}