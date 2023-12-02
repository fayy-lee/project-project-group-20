package com.example.hams;

public interface OnUserDataChangedListener {
    void onUserDataChanged(User user);
    void onUserDataChanged(User user, int position);
}
