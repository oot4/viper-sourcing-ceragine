/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package state_machine.core;

import game_debugger.Alert;
import game_debugger.GameDebugger;
import state_machine.api.SingleStateGenerator;
import state_machine.api.StateChanger;
import state_machine.api.StateGenerator;
import state_machine.standard.GameState;
import state_machine.standard.StateMachine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * A class representing a state machine for a game application. Example uses could be for a {@code MenuState, IntroState, GameplayState, InventoryState, etc..}
 * </p>
 * <p>
 * This state machine should not be used for game objects as this defeats the purpose of the entity-component-system architecture, although this maybe possible...
 * </p>
 */
public final class GameStateMachine extends StateMachine
{

    /**
     * The class type identifier of the initial state
     */
    private Class< ? extends GameState > initialState;

    /**
     * A map of single state generators
     */
    private final Map< Class< ? extends GameState >, SingleStateGenerator > generators = new ConcurrentHashMap<>( );

    /**
     * Creates a new instance of a {@code GameStateMachine}
     */
    private GameStateMachine( )
    {
    }

    /**
     * Creates a new instance of a {@code GameStateMachine}
     *
     * @return a finite state machine with push-down automata functionality
     */
    public static GameStateMachine newInstance( )
    {
        return new GameStateMachine( );
    }

    /**
     * Attaches the specified state type as the initial state that the state machine will begin its operations in
     *
     * @param identifier the specified type used to identify the state to attach
     * @param <T>        the class type of the states this method accepts
     *
     * @throws NoSuchMethodException throws an exception if attaching the state of the specified class type fails
     */
    public < T extends GameState > void attachInitialState( Class< T > identifier ) throws NoSuchMethodException
    {
        initialState = identifier;
        attach( identifier );
    }

    /**
     * Attaches the specified state type to the state machine
     *
     * @param identifier the specified type used to identify the state to attach
     * @param <T>        the class type of the states this method accepts
     *
     * @throws NoSuchMethodException throws an exception if attaching the state of the specified class type fails
     */
    public < T extends GameState > void attach( Class< T > identifier ) throws NoSuchMethodException
    {
        if ( generators.containsKey( identifier ) )
        {
            GameDebugger.log( Alert.INFO , "State class type already exists" );
            return;
        }
        if ( ! Arrays.stream( identifier.getDeclaredConstructors( ) ).allMatch( constructor -> constructor.getParameterCount( ) == 1 && constructor.getParameterTypes( )[ 0 ] == StateChanger.class ) )
        {
            GameDebugger.log( Alert.WARN , "The class provided has one or more constructors that do not accept, a single state changer interface as a parameter ONLY" );
            throw new UnsupportedOperationException( "The class provided has one or more constructors that do not accept, a single state changer interface as a parameter ONLY" );
        }
        Constructor< T > constructor = identifier.getDeclaredConstructor( StateChanger.class );
        constructor.setAccessible( true );
        SingleStateGenerator singleStateGenerator = constructor::newInstance;
        generators.putIfAbsent( identifier , singleStateGenerator );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize( )
    {
        if ( initialState == null )
        {
            GameDebugger.log( Alert.WARN , "No initial state has been attached" );
            throw new NullPointerException( "No initial state has been attached" );
        }
        stateChanger( ).change( initialState );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate( )
    {
        initialState = null;
        generators.clear( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected StateGenerator stateGenerator( )
    {
        return ( type , stateChanger ) -> {
            try
            {
                return generators.get( type ).generate( stateChanger );
            }
            catch ( InvocationTargetException | InstantiationException | IllegalAccessException e )
            {
                GameDebugger.log( Alert.WARN , "Could not generate state" );
                return null;
            }
        };
    }

}