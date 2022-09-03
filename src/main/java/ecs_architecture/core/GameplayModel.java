/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.core;

import ceragine.Game;
import ecs_architecture.standard.ECSModel;
import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;
import utility_library.algorithms.api.Computable;

import java.awt.event.KeyAdapter;

/**
 * An abstract class to be extended by a gameplay model with an Entity-Component-System programming interface
 */
public final class GameplayModel extends ECSModel
{

    /**
     * Constructor for creating a new {@code GameplayModel}
     */
    private GameplayModel( )
    {
    }

    /**
     * Creates a new instance of an entity-component-system gameplay model
     *
     * @return a new instance as a {@link GameplayModel gameplay} model
     */
    public static GameplayModel ecs( )
    {
        return new GameplayModel( );
    }

    /**
     * Queues up the specified computable if the model is still performing updates, otherwise immediately executes them
     *
     * @param computable the specified computable to queue or immediately compute
     */
    private void queueOrCompute( Computable computable )
    {
        if ( isUpdating( ) )
        {
            computations( ).add( computable );
        }
        else
        {
            computable.compute( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add( GameComponent... gameComponents )
    {
        Computable computable = ( ) -> internalAdd( gameComponents );
        queueOrCompute( computable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add( String id , GameComponent... gameComponents )
    {
        Computable computable = ( ) -> internalAdd( id , gameComponents );
        queueOrCompute( computable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add( GameSystem... gameSystems )
    {
        Computable computable = ( ) -> internalAdd( this , gameSystems );
        queueOrCompute( computable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( String id )
    {
        Computable computable = ( ) -> internalRemove( id );
        queueOrCompute( computable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( String id , Class< ? extends GameComponent > type )
    {
        Computable computable = ( ) -> internalRemove( id , type );
        queueOrCompute( computable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha )
    {
        Game.draw( spritesheet , x , y , tile , mirror , scale , alpha );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attach( KeyAdapter keyAdapter )
    {
        Game.attach( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detach( KeyAdapter keyAdapter )
    {
        Game.detach( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPressed( int key )
    {
        return Game.isPressed( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReleased( int key )
    {
        return Game.isReleased( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTyped( int key )
    {
        return Game.isTyped( key );
    }

}