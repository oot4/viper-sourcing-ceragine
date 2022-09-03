/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.core;

import routine_handler.components.api.DisplayListener;
import routine_handler.components.api.RenderListener;
import routine_handler.components.core.RoutineAction;
import routine_handler.components.core.RoutineClock;
import routine_handler.routines.api.Tracker;
import routine_handler.routines.standard.VariableLoopRoutine;
import utility_library.algorithms.api.Computable;
import utility_library.algorithms.core.Quotient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class representing a routine for an interactive application. Integrates rendering and displaying
 */
public final class InteractiveApplicationRoutine extends VariableLoopRoutine
{

    /**
     * Exit code on successful game run operation
     */
    private static final int GAME_CLOSED = 0;

    /**
     * Function to trigger a render operation
     */
    private RenderListener renderListener;

    /**
     * Function to trigger a display operation
     */
    private DisplayListener displayListener;

    /**
     * Tracker to execute some function when the updates and frames have been tallied
     */
    private Tracker tracker;

    /**
     * The persisted actions to be performed
     */
    private final List< RoutineAction > actions = new CopyOnWriteArrayList<>( );

    /**
     * Creates a new instance of a {@code InteractiveApplicationRoutine}
     *
     * @param name the specified name to give the routine
     */
    public InteractiveApplicationRoutine( String name )
    {
        super( name );
    }

    /**
     * Attaches a render listener to the routine
     *
     * @param listener the specified {@link RenderListener render listener} to attach
     */
    public void attach( RenderListener listener )
    {
        if ( renderListener == null )
        {
            renderListener = listener;
        }
    }

    /**
     * Attaches a display listener to the routine
     *
     * @param listener the specified {@link DisplayListener display listener} to attach
     */
    public void attach( DisplayListener listener )
    {
        if ( displayListener == null )
        {
            displayListener = listener;
        }
    }

    /**
     * Attaches a tracker to the routine
     *
     * @param listener the specified {@link Tracker tracker} to attach
     */
    public void attach( Tracker listener )
    {
        if ( tracker == null )
        {
            tracker = listener;
        }
    }

    /**
     * Adds a new instance of a {@code RoutineAction} to the list to be performed later
     *
     * @param milliseconds the milliseconds to wait before performing the action
     * @param computable   the specified action to perform
     */
    public void runAfter( long milliseconds , Computable computable )
    {
        long duration = 0L;
        if ( milliseconds > 0L )
        {
            long divisor = RoutineClock.MICRO_CONVERSION_FACTOR * milliseconds;
            duration = ( long ) Quotient.toDouble( getClock( ).getModifier( ) , divisor );
        }
        long executionTime = getCycle( ) + duration;
        actions.add( new RoutineAction( executionTime , computable ) );
    }

    /**
     * Computes all routine actions that are ready to be executed
     */
    private void computeAllActions( )
    {
        List< RoutineAction > completed = new ArrayList<>( );
        for ( RoutineAction action : actions )
        {
            if ( action.getExecutionTime( ) <= getCycle( ) )
            {
                action.compute( );
                completed.add( action );
            }
        }
        actions.removeAll( completed );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize( )
    {
        //Nothing to initialize
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalTerminate( )
    {
        System.exit( GAME_CLOSED );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void begin( )
    {
        renderListener.perform( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute( )
    {
        computeAllActions( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finish( long interpolation , boolean updated )
    {
        renderListener.render( interpolation );
        if ( updated )
        {
            displayListener.display( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Tracker tracker( )
    {
        if ( tracker == null )
        {
            return super.tracker( );
        }
        return tracker;
    }

}