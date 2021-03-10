package com.example.moneycounter4.view.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.beannew.DynamicItem
import com.example.moneycounter4.beannew.ReplyInfo
import com.example.moneycounter4.extensions.dp2px
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.view.adapter.TalkCommentRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.CommunityViewModel
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.widgets.KeyboardController
import com.example.moneycounter4.widgets.ReplyPopWindow
import kotlinx.android.synthetic.main.fragment_talk_detail.*
import kotlinx.android.synthetic.main.item_talk_big.*

class FragmentTalkDetail : BaseFragment() {

    private var dynamic: DynamicItem? = null

    lateinit var adapter: TalkCommentRecyclerViewAdapter
    private var dynamicId: Int = -1

    companion object {
        var viewModel: CommunityViewModel? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_talk_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textViewMore.visibility = View.GONE
        dynamicId = arguments?.getInt("dynamic_id")?: -1

        dynamic = findDynamic()

        adapter = TalkCommentRecyclerViewAdapter(requireContext(),
            { commentItem, view ->
                // 当一级评论被点击
                viewModel?.replyInfo?.value = ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 1)
                KeyboardController.showInputKeyboard(requireContext(), et_reply)

            }, { commentItem, view ->
                // 当二级评论被点击
                viewModel?.replyInfo?.value = ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 2)
                KeyboardController.showInputKeyboard(requireContext(), et_reply)
            }, { commentItem, view ->
                // 当一级评论被长按

            }, { commentItem, view ->
                // 当二级评论被长按

            })

        rv_talk.layoutManager = LinearLayoutManager(context)
        rv_talk.adapter = adapter
        adapter.setList(dynamic?.commentList ?: listOf())


        bindView()

        val listener = SwipeRefreshLayout.OnRefreshListener {
            viewModel?.refreshDynamic()
        }
        swipeRefreshLayout.setOnRefreshListener(listener)
        listener.onRefresh()
        swipeRefreshLayout.isRefreshing = true

        viewModel?.dynamicList?.observe{
            bindView()
            dynamic = findDynamic()
            adapter.setList(dynamic?.commentList ?: listOf())
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel?.replyInfo?.observe {
            it.let {
                if (it.replyId != -1 && !(it.replyId == dynamicId && it.which == 0)) {
        //                    qa_coordinatorlayout.isReplyEdit = true
                    KeyboardController.showInputKeyboard(requireContext(), et_reply)
                    ReplyPopWindow.with(requireContext())
                    ReplyPopWindow.setReplyName(it.nickname, it.contentPreview)
                    ReplyPopWindow.setOnClickEvent {
                        viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId?: -1, 0)
                    }
                    ReplyPopWindow.show(
                        et_reply,
                        ReplyPopWindow.AlignMode.LEFT,
                        requireContext().dp2px(6f)
                    )
                } else {
                    if (ReplyPopWindow.isShowing()) {
                        ReplyPopWindow.dismiss()
                    }
                }

            }
        }

        viewModel?.replyStatus?.observe {
            et_reply.setText("")
            viewModel?.refreshDynamic()
            KeyboardController.hideInputKeyboard(requireContext(), et_reply)
        }

        btn_send.setOnClickListener {
            viewModel?.reply(et_reply.text.toString())
        }

        viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId?: -1, 0)

        findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
            if (ReplyPopWindow.isShowing()) {
                ReplyPopWindow.dismiss()
            }
            KeyboardController.hideInputKeyboard(MainApplication.context, et_reply)
        }

    }

    fun bindView() {


        imageViewLike?.setSelect(false)
        imageViewLike?.setHint("0")

        textViewUsrName?.text = dynamic?.userId
        textViewContent?.text = dynamic?.text?.take(30)

        textViewContent?.text?.length?.let {
            if (it >= 30) {
                textViewContent.text = textViewContent.text.toString() + "..."
                textViewMore?.visibility = View.VISIBLE
            } else {
                textViewMore?.visibility = View.GONE
            }
        }

        textViewTime?.text = TimeUtil.getChatTimeStr(dynamic?.submitTime ?: 0L)
        // TODO 图片显示
        textViewUsrName?.text = dynamic?.nickname
        imageViewUsrPic?.let { Glide.with(requireActivity()).load(dynamic?.avatarUrl).into(it) }



        imageViewTalk?.setHint(dynamic?.commentList?.size.toString())


        imageViewUsrPic?.setOnClickListener {
            val navController = Navigation.findNavController(it)
            val bundle = Bundle()
            bundle.putBoolean("isMine", false)
            bundle.putString("userId", dynamic?.userId)
            navController.navigate(R.id.action_global_fragmentMine, bundle)
        }

        imageViewLike?.setOnClickListener {
            // TODO 点赞
        }
        imageViewTalk?.setOnClickListener {
            val navController = Navigation.findNavController(it)
            val bundle = Bundle()
            bundle.putInt("dynamic_id", dynamic?.dynamicId ?: 0)
            navController.navigate(R.id.action_global_fragmentTalkDetails, bundle)
        }


        val likeListener = View.OnClickListener {
            imageViewLike?.let {
                it.setSelect(!it.getSelect())
            }


        }
        imageViewLike?.setOnClickListener(likeListener)
    }

    fun findDynamic(): DynamicItem? {
        viewModel?.dynamicList?.value?.forEach {
            if (it.dynamicId == dynamicId) {
                return it
            }
        }
        return null
    }


}