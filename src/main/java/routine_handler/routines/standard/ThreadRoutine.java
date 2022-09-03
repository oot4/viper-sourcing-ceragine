/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.standard;

import game_debugger.Alert;
import game_debugger.GameDebugger;
import routine_handler.routines.api.Launchable;
import routine_handler.routines.api.Routine;
import routine_handler.routines.api.Subroutine;
import routine_handler.routines.core.BasicRoutine;
import utility_library.algorithms.api.Computable;

import java.text.MessageFormat;

/**
 * An abstract class to be extended by a thread routine with basic functionality
 */
public abstract class ThreadRoutine implements Routine, Runnable
{

    /**
     * Flag indicating whether launching the routine is possible
     */
    private boolean launch = true;

    /**
     * Flag indicating whether initializing the routine is possible
     */
    private boolean initialize = false;

    /**
     * Flag indicating whether performing the routine is possible
     */
    private boolean perform = false;

    /**
     * Flag indicating whether terminating the routine is possible
     */
    private boolean terminate = false;

    /**
     * Thread of the routine
     */
    private final Thread thread;

    /**
     * Container object for subroutines
     */
    private final BasicRoutine subroutineContainer = new BasicRoutine( );

    /**
     * Default constructor for creating a new {@code ThreadRoutine}
     */
    protected ThreadRoutine( )
    {
        this( "unnamed_t_routine" );
    }

    /**
     * Default constructor for creating a new {@code ThreadRoutine}
     *
     * @param name the name of the thread handling the routine
     */
    protected ThreadRoutine( String name )
    {
        thread = new Thread( this , name );
    }

    /**
     * Can the routine be launched?
     *
     * @return {@code True} if the routine can be launched. {@code False} otherwise
     */
    private boolean canLaunch( )
    {
        return launch;
    }

    /**
     * Can the routine be initialized?
     *
     * @return {@code True} if the routine can be initialized. {@code False} otherwise
     */
    private boolean canInitialize( )
    {
        return initialize;
    }

    /**
     * Can the routine be performed?
     *
     * @return {@code True} if the routine can be performed. {@code False} otherwise
     */
    private boolean canPerform( )
    {
        return perform;
    }

    /**
     * Can the routine be terminated?
     *
     * @return {@code True} if the routine can be terminated. {@code False} otherwise
     */
    private boolean canTerminate( )
    {
        return terminate;
    }

    /**
     * Initializes the routine. used post-launch
     */
    protected abstract void internalInitialize( );

    /**
     * Runs the routine process. Default implementation when trying to update each attached {@code Subroutine}
     */
    protected void internalPerform( )
    {
        performSubroutines( );
    }

    /**
     * Terminates the routine. used post-initialization and execution
     */
    protected abstract void internalTerminate( );

    /**
     * Launches this runnable routine in a new thread
     */
    public final synchronized void launch( )
    {
        try
        {
            if ( canLaunch( ) )
            {
                launch = false;
                initialize = true;
                thread.start( );
            }
        }
        catch ( Exception exception )
        {
            GameDebugger.log( Alert.DIRE , MessageFormat.format( "Failed to launch the routine..{0}" , this ) , exception );
            throw exception;
        }
    }

    /**
     * Initializes the subroutines if they can be initialized
     */
    private void initializeSubroutines( )
    {
        for ( Subroutine subroutine : subroutineContainer.getSubroutines( ) )
        {
            try
            {
                if ( subroutine == null )
                {
                    throw new NullPointerException( );
                }
                if ( subroutine instanceof Launchable )
                {
                    ( ( Launchable ) subroutine ).initialize( );
                }
            }
            catch ( NullPointerException exception )
            {
                GameDebugger.log( Alert.DIRE , MessageFormat.format( "Failed to initialize subroutine..{0}" , subroutine ) , exception );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attach( Subroutine subroutine )
    {
        subroutineContainer.attach( subroutine );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detach( Subroutine subroutine )
    {
        subroutineContainer.detach( subroutine );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size( )
    {
        return subroutineContainer.size( );
    }

    /**
     * {@inheritDoc}
     * <p>
     * The routine must first be launched and not already initialized for this to work
     * </p>
     */
    @Override
    public final void initialize( )
    {
        try
        {
            if ( canInitialize( ) )
            {
                initialize = false;
                internalInitialize( );
                initializeSubroutines( );
                perform = true;
            }
        }
        catch ( Exception exception )
        {
            GameDebugger.log( Alert.DIRE , MessageFormat.format( "Failed to initialize the routine..{0}" , this ) , exception );
            throw exception;
        }
    }

    /**
     * Performs the subroutines if they can be performed
     */
    private void performSubroutines( )
    {
        for ( Subroutine subroutine : subroutineContainer.getSubroutines( ) )
        {
            try
            {
                if ( subroutine == null )
                {
                    throw new NullPointerException( );
                }
                subroutine.perform( );
            }
            catch ( NullPointerException exception )
            {
                GameDebugger.log( Alert.DIRE , MessageFormat.format( "Failed to perform subroutine..{0}" , subroutine ) , exception );
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The routine must first be launched or initialized, or has not been terminated, for this to work
     * </p>
     */
    @Override
    public final void perform( )
    {
        try
        {
            if ( canPerform( ) )
            {
                perform = false;
                internalPerform( );
                terminate = true;
            }
        }
        catch ( Exception exception )
        {
            GameDebugger.log( Alert.DIRE , MessageFormat.format( "Failed to perform the routine..{0}" , this ) , exception );
            throw exception;
        }
    }

    /**
     * Terminates the subroutines if they can be terminated
     */
    private void terminateSubroutines( )
    {
        for ( Subroutine subroutine : subroutineContainer.getSubroutines( ) )
        {
            try
            {
                if ( subroutine == null )
                {
                    throw new NullPointerException( );
                }
                if ( subroutine instanceof Launchable )
                {
                    ( ( Launchable ) subroutine ).terminate( );
                }
            }
            catch ( NullPointerException exception )
            {
                GameDebugger.log( Alert.DIRE , MessageFormat.format( "Failed to terminate subroutine..{0}" , subroutine ) , exception );
            }
        }
    }

    /**
     * Terminates the routine. This is used post-initialization
     *
     * @param thread        the specified thread to terminate
     * @param terminateWith the specified termination function
     */
    private synchronized void terminate( Thread thread , Computable terminateWith )
    {
        try
        {
            thread.join( 100 );
        }
        catch ( InterruptedException e )
        {
            thread.interrupt( );
        }
        terminateWith.compute( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void terminate( )
    {
        try
        {
            if ( canTerminate( ) )
            {
                terminate = false;
                terminateSubroutines( );
                Computable terminateWith = this::internalTerminate;
                launch = true;
                terminate( thread , terminateWith );
            }
        }
        catch ( Exception exception )
        {
            GameDebugger.log( Alert.DIRE , MessageFormat.format( "Failed to terminate the routine..{0}" , this ) , exception );
            throw exception;
        }
    }

    /**
     * Runs the routines process. Default implementation updates each attached {@code Subroutine}
     */
    @Override
    public final void run( )
    {
        initialize( );
        perform( );
        terminate( );
    }

}