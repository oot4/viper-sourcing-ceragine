/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.api;

import java.awt.event.KeyAdapter;

/**
 * An interface to be implemented when using a key event dispatcher to reply to key events
 */
public interface KeyboardAPI
{

    /**
     * Replies to key events with the specified key adapter
     *
     * @param keyAdapter the specified key listener to reply with
     */
    void attach( KeyAdapter keyAdapter );

    /**
     * Removes the specified key adapter from listening to key events
     *
     * @param keyAdapter the specified key adapter to remove as a listener
     */
    void detach( KeyAdapter keyAdapter );

    /**
     * Is the specified key pressed?
     *
     * @param key the specified key code to test the pressed state of
     *
     * @return {@code True} if the specified key was pressed. {@code False} otherwise
     */
    boolean isPressed( int key );

    /**
     * Is the specified key released?
     *
     * @param key the specified key code to test the release state of
     *
     * @return {@code True} if the specified key was released. {@code False} otherwise
     */
    boolean isReleased( int key );

    /**
     * Is the specified key a typed key?
     *
     * @param key the specified key code to test the type state of
     *
     * @return {@code True} if the specified key was typed. {@code False} otherwise
     */
    boolean isTyped( int key );


}