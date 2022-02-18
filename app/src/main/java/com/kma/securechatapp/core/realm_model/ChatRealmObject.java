package com.kma.securechatapp.core.realm_model;

public interface ChatRealmObject<T> {
    public  void fromModel(T con) ;
    public T toModel();
}
