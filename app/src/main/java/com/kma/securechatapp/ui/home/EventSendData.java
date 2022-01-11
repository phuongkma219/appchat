package com.kma.securechatapp.ui.home;

import com.kma.securechatapp.core.api.model.UserInfo;
import com.kma.securechatapp.core.api.model.userprofile.login.DataLogin;
import com.kma.securechatapp.core.api.model.userprofile.login.UserLogin;
import com.kma.securechatapp.core.event.EventBus;

import java.util.ArrayList;
import java.util.List;

public class EventSendData {
    public abstract static class EventSendAction{
        public  void onLogin(DataLogin u){}
        public  void onLogout(DataLogin u){}
    }
    private List<EventSendData.EventSendAction> evenBusActions;

    private static EventSendData _instance = null;

    public static EventSendData getInstance(){

        if (_instance == null)
            _instance = new EventSendData();
        return _instance;
    }
    private EventSendData(){
        evenBusActions = new ArrayList<EventSendData.EventSendAction>();
    }
    public void addEvent(EventSendData.EventSendAction action){
        this.evenBusActions.add(action);
    }
    public void pushOnLogin(DataLogin u){
        for (EventSendData.EventSendAction eventBus:evenBusActions){
            eventBus.onLogin(u);
        }
    }

    public void pushOnLogout(DataLogin u){
        for (EventSendData.EventSendAction eventBus:evenBusActions){
            eventBus.onLogout(u);
        }
    }
}
