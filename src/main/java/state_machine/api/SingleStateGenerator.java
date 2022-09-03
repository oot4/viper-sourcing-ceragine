/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package state_machine.api;

import state_machine.standard.GameState;

import java.lang.reflect.InvocationTargetException;

/**
 * A functional interface to be implemented when a single state needs to be generated
 */
@FunctionalInterface
public interface SingleStateGenerator
{

    /**
     * Generates a state with the specified state changer
     *
     * @param stateChanger the specified state changer the returned instance should use
     *
     * @return a new instance of a state {@link GameState state}
     */
    GameState generate( StateChanger stateChanger ) throws InvocationTargetException, InstantiationException, IllegalAccessException;

}