package com.ducva.lollipopdemo.view;

/**
 * Callbacks when list was scrolled up or down.
 *
 * @author Vilius Kraujutis
 */
public interface ScrollDirectionListener {
    void onScrollDown();

    void onScrollUp();
}