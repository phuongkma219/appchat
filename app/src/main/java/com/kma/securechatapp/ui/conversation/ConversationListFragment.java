package com.kma.securechatapp.ui.conversation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kma.securechatapp.R;
import com.kma.securechatapp.adapter.ConversationAdapter;
import com.kma.securechatapp.core.api.model.Conversation;
import com.kma.securechatapp.core.event.EventBus;
import com.kma.securechatapp.ui.control.suggestview.OnlineView;
import com.kma.securechatapp.utils.misc.EndlessRecyclerOnScrollListener;
import com.kma.securechatapp.utils.misc.RecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {

    private ConversationListViewModel conversationListViewModel;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    ConversationAdapter conversationAdapter = new ConversationAdapter();
    @BindView(R.id.conversation_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.conversation_swipcontainer)
    SwipeRefreshLayout swipeRefreshLayout;

    EventBus.EvenBusAction evenBus;
    OnlineView onlineView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        conversationListViewModel =
                ViewModelProviders.of(this).get(ConversationListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.bind(this,root);

        onlineView =  new OnlineView(this.getActivity(),root.findViewById(R.id.online_view));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 1);
        recyclerView.setAdapter(conversationAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {

            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                conversationListViewModel.trigerLoadData(0);

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(this.getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (conversationAdapter.getConversationList().size() <= position ){
                            return;
                        }
                        Conversation conversation = conversationAdapter.getConversationList().get(position);
                        Intent intent1 = new Intent(ConversationListFragment.this.getContext(), InboxActivity.class);
                        intent1.putExtra("uuid", conversation.UUID);
                        Log.d("conversation", "onItemClick: "+ conversation.UUID);
                        startActivity(intent1);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        if (conversationAdapter.getConversationList().size() <= position ){
                            return;
                        }
                        Conversation conversation = conversationAdapter.getConversationList().get(position);
                        new AlertDialog.Builder(ConversationListFragment.this.getContext())
                                .setMessage("Bạn có muốn xoá cuộc hội thoại này?")
                                .setCancelable(false)
                                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        conversationListViewModel.removeConversation(conversation);
                                    }
                                })
                                .setNegativeButton("Không", null)
                                .show();
                    }
                })
        );

        conversationListViewModel.getConversations().observe(getViewLifecycleOwner(),conversations -> {
            conversationAdapter.setConversationList(conversations);
            conversationAdapter.notifyDataSetChanged();
        });

        conversationListViewModel.getListOnline().observe( getViewLifecycleOwner() , users->{
            onlineView.upDateList(users);
        });
        conversationListViewModel.trigerLoadOnline();

        registerEvent();
        return root;
    }
    void registerEvent(){
        evenBus = new EventBus.EvenBusAction() {
            @Override
            public void onRefreshConversation() {
                conversationListViewModel.trigerLoadData(0);
                conversationListViewModel.trigerLoadOnline();
            }
        };
        EventBus.getInstance().addEvent(evenBus);
        conversationListViewModel.registEvent();
    }

    @Override
    public void onRefresh() {
        conversationListViewModel.trigerLoadOnline();
        conversationListViewModel.trigerLoadData(0);
         swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDetach() {
        if (evenBus != null);
        EventBus.getInstance().removeEvent(evenBus);

        super.onDetach();
    }
}