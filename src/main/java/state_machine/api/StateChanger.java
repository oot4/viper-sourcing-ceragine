/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package state_machine.api;

import state_machine.standard.GameState;

/**
 * A functional interface to be implemented by a changer of object or reference state
 */
@FunctionalInterface
public interface StateChanger
{

    /**
     * Switches state of an internal object to the one specified
     *
     * @param identifier the specified class of the object to change to
     * @param persist    should the current state be persisted?
     * @param disable    should the current state be disabled?
     */
    void change( Class< ? extends GameState > identifier , boolean persist , boolean disable );

    /**
     * Switches state of an internal object to the one specified
     *
     * @param identifier the specified class of the object to change to
     */
    default void change( Class< ? extends GameState > identifier )
    {
        change( identifier , false , true );
    }

    /**
     * Switches state of an internal object to the one specified
     *
     * @param identifier the specified class of the object to change to
     * @param persist    should the current state be persisted?
     */
    default void change( Class< ? extends GameState > identifier , boolean persist )
    {
        change( identifier , persist , true );
    }

}