/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package state_machine.api;

import routine_handler.routines.api.Launchable;

/**
 * An interface to be implemented by a state
 */
public interface StateAPI extends Launchable
{

    /**
     * Gets a function for changing states
     *
     * @return a functional interface for changing states as a {@link StateChanger state changer}
     */
    StateChanger stateChanger( );

}