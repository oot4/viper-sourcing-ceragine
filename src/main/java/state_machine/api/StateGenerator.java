/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package state_machine.api;

import state_machine.standard.GameState;

/**
 * A functional interface to be implemented when a state needs to be generated
 */
@FunctionalInterface
public interface StateGenerator
{

    /**
     * Gets the state with the specified class type
     *
     * @param type         the specified class type of the state to generate
     * @param stateChanger the specified state changer the returned instance should use
     *
     * @return a new instance of a standard {@link GameState state}
     */
    GameState get( Class< ? extends GameState > type , StateChanger stateChanger );

}