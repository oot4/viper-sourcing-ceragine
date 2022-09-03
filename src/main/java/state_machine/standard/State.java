/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package state_machine.standard;

import state_machine.api.StateAPI;
import state_machine.api.StateChanger;

/**
 * An abstract class to be extended by a representation of a state
 */
abstract class State implements StateAPI
{

    /**
     * Function for changing states
     */
    private final StateChanger stateChanger;

    /**
     * Default constructor for creating a new {@code State}
     *
     * @param stateChanger the specified {@link StateChanger state changer} the state will use. When inheriting from this class, ensure to add this to the constructor signature
     */
    protected State( StateChanger stateChanger )
    {
        if ( stateChanger == null )
        {
            throw new NullPointerException( "state changer cannot be null" );
        }
        this.stateChanger = stateChanger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final StateChanger stateChanger( )
    {
        return stateChanger;
    }

}