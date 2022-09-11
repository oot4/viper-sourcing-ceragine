/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.core;

import game_debugger.Alert;
import game_debugger.GameDebugger;
import routine_handler.components.api.DebugListener;
import routine_handler.routines.api.Subroutine;
import routine_handler.routines.standard.VariableLoopRoutine;
import utility_library.structures.Counter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * A class representing a routine for logging debug messages
 */
public final class DebugRoutine extends VariableLoopRoutine implements DebugListener
{

    /**
     * The rate at which the debug listener is called
     */
    private final int debugRate;

    /**
     * Is this debugging routine printing output?
     */
    private boolean debugging;

    /**
     * Counter for monitoring ticks from the start of this routine
     */
    private final Counter counter = Counter.from( );

    /**
     * Set of debug tasks
     */
    private final Set< Subroutine > debugTasks = ConcurrentHashMap.newKeySet( );

    /**
     * Creates a new instance of a {@code DebugRoutine}
     *
     * @param debugRate the specified rate at which to output debug information
     */
    private DebugRoutine( int debugRate ) { this.debugRate = debugRate; }

    /**
     * Creates a new instance of a {@code DebugRoutine}
     *
     * @param debugRate the specified rate at which debug information will be logged
     *
     * @return a {@code DebugRoutine} object
     */
    public static DebugRoutine newInstance( int debugRate )
    {
        return new DebugRoutine( debugRate );
    }

    /**
     * Helper function to store log messages to the {@link GameDebugger game debugger} to be executed later
     *
     * @param message the specified message to log
     */
    public void log( String message )
    {
        debugTasks.add( ( ) -> GameDebugger.log( Alert.INFO , message ) );
    }

    /**
     * Helper function to store log messages to the {@link GameDebugger game debugger} to be executed later. The message is constructed by invoking the provided supplier function
     *
     * @param message the specified message supplier function to construct the message
     */
    public void log( Supplier< String > message )
    {
        debugTasks.add( ( ) -> GameDebugger.log( Alert.INFO , message ) );
    }

    /**
     * {@inheritDoc}
     * This method calls all the stored subroutines in the {@link DebugRoutine#debugTasks debug task} set
     */
    @Override
    public void debug( )
    {
        if ( debugTasks.isEmpty( ) )
        {
            return;
        }
        debugTasks.forEach( Subroutine::perform );
        debugTasks.clear( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize( )
    {
        debugging = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalTerminate( )
    {
        debugging = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void begin( )
    {
        debugging = counter.reachedInterval( debugRate );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute( )
    {
        if ( debugging )
        {
            debug( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finish( long interpolation , boolean updated )
    {
        counter.perform( );
    }

}