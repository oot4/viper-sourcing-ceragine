/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.core;

import routine_handler.routines.api.Routine;
import routine_handler.routines.api.Subroutine;

import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static game_debugger.GameDebugger.validateCondition;

/**
 * An abstract class to be extended by a routine with basic functionality
 */
public class BasicRoutine implements Routine
{

    /**
     * A set of subroutines attached to the routine
     */
    private final Set< Subroutine > subroutines = ConcurrentHashMap.newKeySet( );

    /**
     * Default constructor for creating a new {@code BasicRoutine}
     */
    public BasicRoutine( )
    {
        //No Implementation Details Required
    }

    /**
     * Gets a set of subroutines
     *
     * @return the set of subroutines attached to this routine
     */
    public final Set< Subroutine > getSubroutines( )
    {
        return subroutines;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void attach( Subroutine subroutine )
    {
        validateCondition( ( ) -> subroutine == null , ( ) -> subroutines.add( subroutine ) , MessageFormat.format( "cannot attach {0}" , subroutine ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void detach( Subroutine subroutine )
    {
        validateCondition( ( ) -> subroutine == null , ( ) -> subroutines.remove( subroutine ) , MessageFormat.format( "cannot detach {0}" , subroutine ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int size( )
    {
        return subroutines.size( );
    }

}