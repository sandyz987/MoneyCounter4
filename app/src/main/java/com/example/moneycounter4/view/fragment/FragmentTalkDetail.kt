package com.example.moneycounter4.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.beannew.DynamicItem
import com.example.moneycounter4.beannew.ReplyInfo
import com.example.moneycounter4.beannew.findEquals
import com.example.moneycounter4.extensions.dp2px
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.view.adapter.LikeRecyclerViewAdapter
import com.example.moneycounter4.view.adapter.TalkCommentRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.CommunityViewModel
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.widgets.ClipboardController
import com.example.moneycounter4.widgets.KeyboardController
import com.example.moneycounter4.widgets.OptionalPopWindow
import com.example.moneycounter4.widgets.ReplyPopWindow
import com.mredrock.cyxbs.qa.ui.widget.OptionalDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_talk_detail.*
import kotlinx.android.synthetic.main.item_talk_big.*

class FragmentTalkDetail : BaseFragment() {

    private var dynamic: DynamicItem? = null

    lateinit var adapter: TalkCommentRecyclerViewAdapter
    private var dynamicId: Int = -1
    lateinit var listener: SwipeRefreshLayout.OnRefreshListener

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
        dynamicId = arguments?.getInt("dynamic_id") ?: -1

        dynamic = findDynamic()


        textViewOptions.setOnClickListener {
            val optionPopWindow = OptionalPopWindow.Builder().with(requireContext())
                .addOptionAndCallback("回复") {
                    viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)
                    KeyboardController.showInputKeyboard(requireContext(), et_reply)
                }.addOptionAndCallback("复制") {
                    ClipboardController.copyText(requireContext(), dynamic?.text ?: "")
                }
            if (dynamic?.userId == Config.userId) {
                optionPopWindow.addOptionAndCallback("删除") {
                    OptionalDialog.show(requireContext(), "是否删除？", {}) {
                        viewModel?.deleteDynamic(dynamicId)
                    }
                }
            }
            optionPopWindow.show(it, OptionalPopWindow.AlignMode.LEFT, 0)
        }

        adapter = TalkCommentRecyclerViewAdapter(requireContext(),
            { commentItem, view ->
                // 当一级评论被点击
                viewModel?.replyInfo?.value =
                    ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 1)
                KeyboardController.showInputKeyboard(requireContext(), et_reply)

            }, { commentItem, view ->
                // 当二级评论被点击
                viewModel?.replyInfo?.value =
                    ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 2)
                KeyboardController.showInputKeyboard(requireContext(), et_reply)
            }, { commentItem, view ->
                // 当一级评论被长按
                val optionPopWindow = OptionalPopWindow.Builder().with(requireContext())
                    .addOptionAndCallback("回复") {
                        viewModel?.replyInfo?.value =
                            ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 1)
                        KeyboardController.showInputKeyboard(requireContext(), et_reply)
                    }.addOptionAndCallback("复制") {
                        ClipboardController.copyText(requireContext(), commentItem.text)
                    }
                if (commentItem.userId == Config.userId) {
                    optionPopWindow.addOptionAndCallback("删除") {
                        OptionalDialog.show(requireContext(), "是否删除？", {}) {
                            viewModel?.deleteComment(commentItem.id, 1)
                        }
                    }
                }
                optionPopWindow.show(view, OptionalPopWindow.AlignMode.CENTER, 0)

            }, { commentItem, view ->
                // 当二级评论被长按
                val optionPopWindow = OptionalPopWindow.Builder().with(requireContext())
                    .addOptionAndCallback("回复") {
                        viewModel?.replyInfo?.value =
                            ReplyInfo(commentItem.nickname, commentItem.text, commentItem.id, 2)
                        KeyboardController.showInputKeyboard(requireContext(), et_reply)
                    }.addOptionAndCallback("复制") {
                        ClipboardController.copyText(requireContext(), commentItem.text)
                    }
                if (commentItem.userId == Config.userId) {
                    optionPopWindow.addOptionAndCallback("删除") {
                        OptionalDialog.show(requireContext(), "是否删除？", {}) {
                            viewModel?.deleteComment(commentItem.id, 2)
                        }
                    }
                }
                optionPopWindow.show(view, OptionalPopWindow.AlignMode.CENTER, 0)
            })

        rv_talk.layoutManager = LinearLayoutManager(context)
        rv_talk.adapter = adapter
        adapter.setList(dynamic?.commentList ?: listOf())


        bindView()

        listener = SwipeRefreshLayout.OnRefreshListener {
            viewModel?.refreshDynamic()
        }
        swipeRefreshLayout.setOnRefreshListener(listener)
        listener.onRefresh()
        swipeRefreshLayout.isRefreshing = true



        viewModel?.dynamicList?.observe {
            bindView()
            dynamic = findDynamic()
            adapter.setList(dynamic?.commentList ?: listOf())
            swipeRefreshLayout.isRefreshing = false
            recyclerViewLikeAccount.adapter =
                LikeRecyclerViewAdapter(this, dynamic!!.praise.reversed())
            recyclerViewLikeAccount.layoutManager = LinearLayoutManager(fragment.context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }

        coordinatorlayout_touch.onReplyCancelEvent = {
            viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)
            KeyboardController.hideInputKeyboard(requireContext(), et_reply)
        }

        viewModel?.replyInfo?.observe {
            it.let {
                if (it.replyId != -1 && !(it.replyId == dynamicId && it.which == 0)) {
                    coordinatorlayout_touch.isReplyEdit = true
                    KeyboardController.showInputKeyboard(requireContext(), et_reply)
                    ReplyPopWindow.with(requireContext())
                    ReplyPopWindow.setReplyName(it.nickname, it.contentPreview)
                    ReplyPopWindow.setOnClickEvent {
                        viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)
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
            swipeRefreshLayout.isRefreshing = true
            viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)
//            rv_talk.scrollToPosition(0)
            KeyboardController.hideInputKeyboard(requireContext(), et_reply)
        }
        viewModel?.deleteStatus?.observe {
            when (it) {
                1 -> {
                    findNavController().popBackStack()
                }
                2 -> {
                    rv_talk.scrollToPosition(0)
                    viewModel?.refreshDynamic()
                    swipeRefreshLayout.isRefreshing = true
                }
            }
        }

        btn_send.setOnClickListener {
            viewModel?.reply(et_reply.text.toString())
        }

        viewModel?.replyInfo?.value = ReplyInfo("", "", dynamic?.dynamicId ?: -1, 0)

        findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
            if (ReplyPopWindow.isShowing()) {
                ReplyPopWindow.dismiss()
            }
            KeyboardController.hideInputKeyboard(MainApplication.context, et_reply)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun bindView() {


        val isPraise = dynamic?.praise?.findEquals { it.userId == Config.userId }
        imageViewLike?.registerLikeView(
            dynamic?.dynamicId ?: 0,
            0,
            isPraise ?: false,
            dynamic?.praise?.size ?: 0
        )
        imageViewLike?.callback = {
            listener.onRefresh()
//            swipeRefreshLayout.isRefreshing = true
        }




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
        if (dynamic?.picUrl?.isNotEmpty() == true) {
            imageViewPic.visibility = View.VISIBLE
            dynamic?.picUrl?.let {
                Glide.with(this).load(it[0]).into(imageViewPic)
            }
        } else {
            imageViewPic.visibility = View.GONE
        }
        textViewUsrName?.text = dynamic?.nickname
        imageViewUsrPic?.let { Glide.with(requireActivity()).load(dynamic?.avatarUrl).into(it) }



        imageViewTalk?.setHint(dynamic?.commentList?.size.toString())


        imageViewUsrPic?.setOnClickListener {
            val navController = Navigation.findNavController(it)
            val bundle = Bundle()
            bundle.putBoolean("isMine", false)
            bundle.putString("userId", dynamic?.userId)
            navController.navigate(R.id.action_global_fragmentIndividual, bundle)
        }

        imageViewTalk?.setOnClickListener {
            val navController = Navigation.findNavController(it)
            val bundle = Bundle()
            bundle.putInt("dynamic_id", dynamic?.dynamicId ?: 0)
            navController.navigate(R.id.action_global_fragmentTalkDetails, bundle)
        }

    }

    private fun findDynamic(): DynamicItem? {
        viewModel?.dynamicList?.value?.forEach {
            if (it.dynamicId == dynamicId) {
                return it
            }
        }
        return null
    }

    private fun findDynamic(dynamicId: Int): Int {
        viewModel?.dynamicList?.value?.let {
            for (i in 0..it.size) {
                if (it[i].dynamicId == dynamicId) {
                    return i
                }
            }
        }
        return 0
    }


}