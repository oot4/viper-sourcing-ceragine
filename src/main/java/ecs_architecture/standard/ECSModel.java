/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.standard;

import routine_handler.routines.api.Launchable;
import utility_library.algorithms.api.Computable;
import utility_library.gameplay.animation.AnimationSystem;
import utility_library.gameplay.effects.EffectSystem;
import utility_library.gameplay.gameclock.ClockSystem;
import utility_library.gameplay.gameclock.TimerSystem;
import utility_library.gameplay.rendering.core.SpriteRenderSystem;
import utility_library.gameplay.text.TextSystem;
import utility_library.structures.Condition;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An abstract class to be extended by an Entity-Component-System model
 */
public abstract class ECSModel extends SystemContainer implements Launchable
{

    /**
     * The test mode status of the model
     */
    private final Condition testMode = Condition.newInstance( );

    /**
     * Is the model updating?
     */
    private boolean updating = false;

    /**
     * Extra or delayed computations
     */
    private final Set< Computable > computations = ConcurrentHashMap.newKeySet( );

    /**
     * Default constructor for creating a new {@code ECSModel}
     */
    protected ECSModel( )
    {
    }

    /**
     * Sets the test mode condition to {@code True} or {@code False}
     */
    void testMode( )
    {
        testMode.change( );
    }

    /**
     * Is the model updating
     *
     * @return {@code True} if the model is currently updating. {@code False} otherwise
     */
    protected final boolean isUpdating( )
    {
        return updating;
    }

    /**
     * Gets the computation set for extra or delayed actions to be performed by the model
     *
     * @return a set of computable objects
     */
    protected final Set< Computable > computations( )
    {
        return computations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void initialize( )
    {
        boolean addSystems = ! testMode.status( );
        if ( addSystems )
        {
            //Add core game systems here
            add( new ClockSystem( ) );
            add( new TimerSystem( ) );
            add( new SpriteRenderSystem( ) );
            add( new AnimationSystem( ) );
            add( new TextSystem( ) );
            add( new EffectSystem( ) );
        }
        systems( ).keySet( ).forEach( this::activate );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void terminate( )
    {
        updating = false;
        systems( ).keySet( ).forEach( this::deactivate );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void perform( )
    {
        updating = true;
        systems( ).values( ).stream( ).filter( GameSystem::isEnabled ).forEach( GameSystem::perform );
        computations.forEach( Computable::compute );
        updating = false;
    }

}