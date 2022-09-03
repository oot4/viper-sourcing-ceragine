/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package state_machine.standard;

import game_debugger.Alert;
import game_debugger.GameDebugger;
import state_machine.api.StateAPI;
import state_machine.api.StateChanger;
import state_machine.api.StateGenerator;
import utility_library.algorithms.api.Computable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * An abstract class to be extended by a finite state machine
 */
public abstract class StateMachine implements StateAPI
{

    /**
     * The current state
     */
    private State current;

    /**
     * The last persisted state to be removed
     */
    private PersistedState removed;

    /**
     * Are we performing a subroutine?
     */
    private boolean performing = false;

    /**
     * Deck of states
     */
    private final Deque< PersistedState > states = new ConcurrentLinkedDeque<>( );

    /**
     * An action to change state
     */
    private Computable stateChangeAction = ( ) -> { };

    /**
     * Default constructor for creating a new {@code StateMachine}
     */
    protected StateMachine( )
    {
    }

    /**
     * Executes the {@code Subroutine} operation
     *
     * @param persistedStates the specified deck of states with subroutines to perform
     */
    private static void perform( Deque< PersistedState > persistedStates )
    {
        PersistedState persistedState = persistedStates.pollFirst( );
        if ( persistedState == null )
        {
            return;
        }
        if ( ! persistedState.isInactive( ) )
        {
            persistedState.getState( ).perform( );
        }
        perform( persistedStates );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void perform( )
    {
        performing = true;
        Deque< PersistedState > persistedStates = new ArrayDeque<>( states );
        perform( persistedStates );
        stateChangeAction = ( ) -> { };
        if ( current != null )
        {
            current.perform( );
        }
        stateChangeAction.compute( );
        stateChangeAction = ( ) -> { };
        performing = false;
    }

    /**
     * Gets the function for generating states
     *
     * @return a functional interface that can generate states
     */
    protected abstract StateGenerator stateGenerator( );

    /**
     * Unloads or terminates the specified state
     *
     * @param state the specified state to unload
     * @param <T>   the class type of states that can be unloaded
     */
    private static < T extends State > void unload( T state )
    {
        state.terminate( );
    }

    /**
     * Loads or initializes the specified state
     *
     * @param state the specified state to load
     * @param <T>   the class type of states that can be loaded
     */
    private < T extends State > void load( T state )
    {
        if ( removed != null && state == removed.getState( ) )
        {
            return;
        }
        state.initialize( );
    }

    /**
     * Removes the specified persisted state from the state deck
     *
     * @param persistedState the specified persisted state to remove
     *
     * @return the state object that was persisted
     */
    private State remove( PersistedState persistedState )
    {
        removed = persistedState.copy( );
        states.remove( persistedState );
        return removed.getState( );
    }

    /**
     * Gets the specified state from the provided state generator
     *
     * @param identifier the specified type the state can be identified with
     * @param <T>        the class type this method accepts
     *
     * @return a new instance of a standard {@link State state}
     */
    private < T extends GameState > State getState( Class< T > identifier )
    {
        if ( stateGenerator( ) == null )
        {
            throw new NullPointerException( "State generator returned null, initialize and return a state generator" );
        }
        PersistedState persistable = states.stream( ).filter( persistedState -> persistedState.getState( ).getClass( ) == identifier ).findFirst( ).orElse( null );
        return persistable == null ? stateGenerator( ).get( identifier , stateChanger( ) ) : remove( persistable );
    }

    /**
     * Persists the specified state to the state deck
     *
     * @param state   the specified state to persist
     * @param disable should the state be persisted as an inactive state?
     */
    private void persist( State state , boolean disable )
    {
        PersistedState persistable = new PersistedState( state , disable );
        states.addLast( persistable );
    }

    /**
     * Changes the current state to the one specified at a future time
     *
     * @param identifier the specified type used to identify the state to change to
     * @param persist    should the current state be persisted?
     * @param disable    should the current state be disabled?
     * @param <T>        the class type of states this method accepts
     */
    private < T extends GameState > void addChangeStateTask( Class< T > identifier , boolean persist , boolean disable )
    {
        stateChangeAction = ( ) -> internalChangeState( identifier , persist , disable );
    }

    /**
     * Changes the current state to the one specified
     *
     * @param identifier the specified type used to identify the state to change to
     * @param persist    should the current state be persisted?
     * @param disable    should the current state be disabled?
     * @param <T>        the class type of states this method accepts
     */
    private < T extends GameState > void changeState( Class< T > identifier , boolean persist , boolean disable )
    {
        if ( performing )
        {
            addChangeStateTask( identifier , persist , disable );
        }
        else
        {
            internalChangeState( identifier , persist , disable );
        }
    }

    /**
     * Changes the current state to the one specified
     *
     * @param identifier the specified type used to identify the state to change to
     * @param persist    should the current state be persisted?
     * @param disable    should the current state be disabled?
     * @param <T>        the class type of states this method accepts
     */
    private < T extends GameState > void internalChangeState( Class< T > identifier , boolean persist , boolean disable )
    {
        State newState = getState( identifier );
        if ( newState == null )
        {
            GameDebugger.log( Alert.WARN , "The state does not exist or is null" );
        }
        else
        {
            State previous = current;
            current = newState;
            if ( persist )
            {
                persist( previous , disable );
            }
            else
            {
                if ( previous != null )
                {
                    unload( previous );
                }
            }
            load( newState );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final StateChanger stateChanger( )
    {
        return this::changeState;
    }

    /**
     * A class representing a persisted state
     */
    private static final class PersistedState
    {

        /**
         * The state to persist
         */
        private final State state;

        /**
         * Is this persisted state inactive?
         */
        private final boolean inactive;

        /**
         * Creates a new instance of a {@code PersistedState}
         *
         * @param state    the specified state to persist
         * @param inactive should the specified state be stored as an inactive state?
         */
        public PersistedState( State state , boolean inactive )
        {
            this.state = state;
            this.inactive = inactive;
        }

        /**
         * Copies the persisted state
         *
         * @return a copy of this persisted state
         */
        public PersistedState copy( )
        {
            return new PersistedState( state , inactive );
        }

        /**
         * Gets the persisted state
         *
         * @return a preserved state object as a standard {@link State state }
         */
        public State getState( )
        {
            return state;
        }

        /**
         * Is this persisted state inactive?
         *
         * @return {@code True} if the persisted state is inactive. {@code False} otherwise
         */
        public boolean isInactive( )
        {
            return inactive;
        }

    }

}