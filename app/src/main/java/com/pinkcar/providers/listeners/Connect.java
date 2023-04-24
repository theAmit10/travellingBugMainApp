package com.pinkcar.providers.listeners;

import java.util.ArrayList;
import java.util.List;

public class Connect {
    private static boolean myBoolean;
    private static List<com.wedrive.driver.Listeners.ConnectionChangeListener> listeners = new ArrayList();

    public static boolean getMyBoolean() {
        return myBoolean;
    }

    public static void setMyBoolean(boolean value) {
        myBoolean = value;

        for (com.wedrive.driver.Listeners.ConnectionChangeListener l : listeners) {
            l.OnMyBooleanChanged();
        }
    }

    public static void addMyBooleanListener(com.wedrive.driver.Listeners.ConnectionChangeListener l) {
        listeners.add(l);
    }
}
