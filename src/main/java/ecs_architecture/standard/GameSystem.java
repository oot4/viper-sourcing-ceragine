package ecs_architecture.standard;

import ceragine.Game;
import ecs_architecture.api.GameplayAPI;
import routine_handler.routines.api.Launchable;
import state_machine.api.StateChanger;
import state_machine.standard.GameState;
import utility_library.structures.Counter;

import java.awt.event.KeyAdapter;
import java.util.Map;

/**
 * An abstract class to be extended by a game system
 */
public abstract class GameSystem implements Launchable, GameplayAPI, StateChanger
{

    /**
     * Is the game system enabled?
     */
    private boolean enabled = false;

    /**
     * A counter for monitoring the current update cycle
     */
    private Counter counter;

    /**
     * State changer for switching states
     */
    private final StateChanger stateChanger;

    /**
     * Gameplay model interface
     */
    private GameplayAPI model;

    /**
     * Default constructor for creating a new {@code GameSystem}
     */
    protected GameSystem( )
    {
        this( Game.stateChanger( ) );
    }

    /**
     * Default constructor for creating a new {@code GameSystem}
     *
     * @param stateChanger the specified state changer to link this game system to
     */
    protected GameSystem( StateChanger stateChanger )
    {
        this.stateChanger = stateChanger;
    }

    /**
     * Gets the counter object of the game system
     *
     * @return the counter instance as a {@link Counter counter}
     */
    protected final Counter counter( )
    {
        return counter;
    }

    /**
     * Is the system enabled?
     *
     * @return {@code True} if the system is enabled. {@code False} otherwise
     */
    public final boolean isEnabled( )
    {
        return enabled;
    }

    /**
     * Sets the enabled system flag to {@code True} or {@code False}
     *
     * @param enabled is the system enabled?
     */
    final void setEnabled( boolean enabled )
    {
        this.enabled = enabled;
    }

    /**
     * Gets the gameplay programming interface of the system
     *
     * @return the gameplay model interface
     */
    private GameplayAPI model( )
    {
        if ( model == null )
        {
            throw new NullPointerException( "Gameplay interface cannot be null" );
        }
        return model;
    }

    /**
     * Sets the gameplay programming interface to the one specified
     *
     * @param model the specified gameplay programming {@link GameplayAPI interface} to set for the system
     */
    final void setGameplayAPI( GameplayAPI model )
    {
        if ( model == null )
        {
            throw new IllegalArgumentException( "Gameplay interface cannot be null" );
        }
        this.model = model;
    }

    /**
     * Initializes the routine internally. Should be used post-launch
     */
    protected abstract void internalInitialize( );

    /**
     * Terminates the routine internally. Should be called post-initialization
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
        counter = Counter.from( );
        counter.initialize( );
        internalInitialize( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void terminate( )
    {
        internalTerminate( );
        counter.terminate( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void perform( )
    {
        internalPerform( );
        counter.perform( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Map< String, GameComponent > get( Class< ? extends GameComponent > type )
    {
        return model( ).get( type );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void add( GameComponent... gameComponents )
    {
        model( ).add( gameComponents );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void add( String id , GameComponent... gameComponents )
    {
        model( ).add( id , gameComponents );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void add( GameSystem... gameSystems )
    {
        model( ).add( gameSystems );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void remove( String id )
    {
        model( ).remove( id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void remove( String id , Class< ? extends GameComponent > type )
    {
        model( ).remove( id , type );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void activate( Class< ? extends GameSystem > type )
    {
        model( ).activate( type );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void deactivate( Class< ? extends GameSystem > type )
    {
        model( ).deactivate( type );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void attach( KeyAdapter keyAdapter )
    {
        model( ).attach( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void detach( KeyAdapter keyAdapter )
    {
        model( ).detach( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isPressed( int key )
    {
        return model( ).isPressed( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isReleased( int key )
    {
        return model( ).isReleased( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTyped( int key )
    {
        return model( ).isTyped( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void draw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha )
    {
        model( ).draw( spritesheet , x , y , tile , mirror , scale , alpha );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void draw( String spritesheet , double x , double y , int tile , int mirror , int scale )
    {
        GameplayAPI.super.draw( spritesheet , x , y , tile , mirror , scale );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void draw( String spritesheet , double x , double y , int tile , int mirror )
    {
        GameplayAPI.super.draw( spritesheet , x , y , tile , mirror );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void draw( String spritesheet , double x , double y , int tile )
    {
        GameplayAPI.super.draw( spritesheet , x , y , tile );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void change( Class< ? extends GameState > identifier , boolean persist , boolean disable )
    {
        stateChanger.change( identifier , persist , disable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void change( Class< ? extends GameState > identifier )
    {
        StateChanger.super.change( identifier );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void change( Class< ? extends GameState > identifier , boolean persist )
    {
        StateChanger.super.change( identifier , persist );
    }

}