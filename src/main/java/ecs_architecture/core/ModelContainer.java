/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.core;

import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;
import routine_handler.routines.api.Launchable;

/**
 * A class representing a container for a gameplay model
 */
public final class ModelContainer implements Launchable
{

    /**
     * Gameplay model which allows interaction with the ECS interface
     */
    private final GameplayModel model = GameplayModel.ecs( );

    /**
     * Constructor for creating a new instance of a {@code ModelContainer}
     */
    private ModelContainer( )
    {
    }

    /**
     * Creates a new instance of a {@code ModelContainer}
     *
     * @return a newly constructed instance of a model {@link ModelContainer container}
     */
    public static ModelContainer newInstance( )
    {
        return new ModelContainer( );
    }

    /**
     * Adds the specified game system(s)
     *
     * @param gameSystems the specified game {@link GameSystem system(s)} to add
     */
    public void add( GameSystem... gameSystems ) { model.add( gameSystems ); }

    /**
     * Adds a new entity with the specified component(s)
     *
     * @param gameComponents the specified {@link GameComponent component(s)} to give the entity
     */
    public void add( GameComponent... gameComponents ) { model.add( gameComponents ); }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize( )
    {
        model.initialize( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate( )
    {
        model.terminate( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void perform( )
    {
        model.perform( );
    }

}