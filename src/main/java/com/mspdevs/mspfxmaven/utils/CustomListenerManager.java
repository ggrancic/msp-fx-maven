package com.mspdevs.mspfxmaven.utils;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class CustomListenerManager<T> {
    private final ObservableValue<T> observableValue;
    private final ChangeListener<T> listener;

    public CustomListenerManager(ReadOnlyStringProperty property, ChangeListener<T> listener) {
        this.observableValue = (ObservableValue<T>) property;
        this.listener = listener;
    }

    public void addListener() {
        observableValue.addListener(listener);
    }

    public void removeListener() {
        observableValue.removeListener(listener);
    }
}
