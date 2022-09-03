/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package state_machine.standard;

import ecs_architecture.core.ModelContainer;
import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;
import state_machine.api.StateChanger;

/**
 * An abstract class to be extended by a state for a game
 */
public abstract class GameState extends State
{

    /**
     * Gameplay interface container
     */
    private final ModelContainer modelContainer = ModelContainer.newInstance( );

    /**
     * Default constructor for creating a new {@code GameState}
     *
     * @param stateChanger the specified state {@link StateChanger changer} the state will use. When inheriting from this class, ensure to add this to the constructor signature
     */
    protected GameState( StateChanger stateChanger )
    {
        super( stateChanger );
    }

    /**
     * Adds the specified game system(s)
     *
     * @param gameSystems the specified game {@link GameSystem system(s)} to add
     */
    public void add( GameSystem... gameSystems ) { modelContainer.add( gameSystems ); }

    /**
     * Adds a new entity with the specified component(s)
     *
     * @param gameComponents the specified {@link GameComponent component(s)} to give the entity
     */
    public void add( GameComponent... gameComponents ) { modelContainer.add( gameComponents ); }

    /**
     * Initializes the routine. Should be used post-launch
     */
    protected abstract void internalInitialize( );

    /**
     * Terminates the routine. This should be called post-initialization
     */
    protected abstract void internalTerminate( );

    /**
     * Executes the {@code Subroutine} operation
     */
    protected abstract void internalPerform( );

    /**
     * {@inheritDoc}
     */
    @Override
    public final void initialize( )
    {
        internalInitialize( );
        modelContainer.initialize( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void terminate( )
    {
        modelContainer.terminate( );
        internalTerminate( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void perform( )
    {
        modelContainer.perform( );
        internalPerform( );
    }

}