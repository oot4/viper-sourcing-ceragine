/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ceragine;

import game_debugger.Alert;
import game_debugger.GameDebugger;
import resource_manager.writable.core.ConfigurationBundle;
import resource_manager.writable.core.GameDataBundle;
import routine_handler.components.core.RoutineClock;
import routine_handler.routines.core.DebugRoutine;
import routine_handler.routines.core.DisplayRoutine;
import routine_handler.routines.core.InteractiveApplicationRoutine;
import routine_handler.routines.core.RenderRoutine;
import routine_handler.routines.core.ResponseRoutine;
import state_machine.api.StateChanger;
import state_machine.core.GameStateMachine;
import state_machine.standard.GameState;
import utility_library.algorithms.api.Computable;
import utility_library.algorithms.core.Quotient;
import utility_library.wrappers.core.SpriteWrapper;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.util.function.Supplier;

/**
 * A class representing a game
 */
public final class Game
{

    /**
     * A bundle of configuration properties
     */
    private ConfigurationBundle configuration;

    /**
     * A wrapper for sprites
     */
    private SpriteWrapper sprites;

    /**
     * Debug listener
     */
    private DebugRoutine debugger;

    /**
     * Response listener
     */
    private ResponseRoutine responder;

    /**
     * State machine
     */
    private GameStateMachine states;

    /**
     * Core application loop
     */
    private InteractiveApplicationRoutine routine;

    /**
     * Render listener
     */
    private RenderRoutine renderer;

    /**
     * Display listener
     */
    private DisplayRoutine display;

    /**
     * Is the game initialized
     */
    private static boolean initialized = false;

    /**
     * Static instance of the game class
     */
    private static Game game;

    /**
     * Creates a new instance of a {@code Game}
     */
    private Game( )
    {
        if ( game != null )
        {
            throw new UnsupportedOperationException( "|instance of the game already exists and cannot be instantiated again|" );
        }
    }

    /**
     * Gets the configuration bundle with configurable properties
     *
     * @return a bundle of configuration properties as a {@link ConfigurationBundle configuration bundle}
     */
    public static ConfigurationBundle configuration( )
    {
        return game( ).configuration;
    }

    /**
     * Gets the wrapper for sprite data
     *
     * @return a wrapper containing a map of spritesheets
     *
     * @throws NullPointerException throws an exception if this method is used before the sprite wrapper is initialized
     */
    public static SpriteWrapper sprites( )
    {
        return game( ).sprites;
    }

    /**
     * Helper function to store log messages to the {@link GameDebugger game debugger} to be executed later
     *
     * @param message the specified message to log
     */
    public static void log( String message )
    {
        game( ).debugger.log( message );
    }

    /**
     * Helper function to store log messages to the {@link GameDebugger game debugger} to be executed later. The message is constructed by invoking the provided supplier function
     *
     * @param message the specified message supplier function to construct the message
     */
    public static void log( Supplier< String > message )
    {
        game( ).debugger.log( message );
    }

    /**
     * Replies to key events with the specified key adapter
     *
     * @param keyAdapter the specified key listener to reply with
     */
    public static void attach( KeyAdapter keyAdapter )
    {
        game( ).responder.attach( keyAdapter );
    }

    /**
     * Removes the specified key adapter from listening to key events
     *
     * @param keyAdapter the specified key adapter to remove as a listener
     */
    public static void detach( KeyAdapter keyAdapter )
    {
        game( ).responder.detach( keyAdapter );
    }

    /**
     * Is the specified key pressed?
     *
     * @param key the specified key code to test the pressed state of
     *
     * @return {@code True} if the specified key was pressed. {@code False} otherwise
     */
    public static boolean isPressed( int key )
    {
        return game( ).responder.isPressed( key );
    }

    /**
     * Is the specified key released?
     *
     * @param key the specified key code to test the release state of
     *
     * @return {@code True} if the specified key was released. {@code False} otherwise
     */
    public static boolean isReleased( int key )
    {
        return game( ).responder.isReleased( key );
    }

    /**
     * Is the specified key a typed key?
     *
     * @param key the specified key code to test the type state of
     *
     * @return {@code True} if the specified key was typed. {@code False} otherwise
     */
    public static boolean isTyped( int key )
    {
        return game( ).responder.isTyped( key );
    }

    /**
     * Sets the offset of rendered tile images
     *
     * @param x the specified value {@code x} to offset rendered tile images by
     * @param y the specified value {@code y} to offset rendered tile images by
     */
    public static void setRenderOffset( int x , int y )
    {
        game( ).renderer.setRenderOffset( x , y );
    }

    /**
     * Draws a sprite which is an {@code 8x8} pixel image
     *
     * @param spritesheet the specified spritesheet to get the image from
     * @param x           the specified {@code x} position to render the image
     * @param y           the specified {@code y} position to render the image
     * @param tile        the specified tile image on the spritesheet to render
     * @param mirror      the specified direction to mirror the image
     * @param scale       the specified scale to resize the image to
     * @param alpha       the specified alpha value to apply to the image
     */
    public static void draw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha )
    {
        game( ).renderer.draw( spritesheet , x , y , tile , mirror , scale , alpha );
    }

    /**
     * Adjusts the current centering of the viewport to match that of the specified value
     *
     * @param x the specified value {@code x} to adjust the centering coordinates to
     * @param y the specified value {@code y} to adjust the centering coordinates to
     */
    public static void centerViewportAt( double x , double y )
    {
        game( ).display.adjustCentering( x , y );
    }

    /**
     * Attaches a state to the games state machine as the initial state when the state machine begins its operations. The state machine is where all core gameplay mechanics are performed and as a result this method will be used often to attach gameplay states
     *
     * @param identifier the specified type used to identify the state to attach
     * @param <T>        the class type of the states this method accepts
     */
    public static < T extends GameState > void setInitialState( Class< T > identifier )
    {
        try
        {
            game( ).states.attachInitialState( identifier );
        }
        catch ( NoSuchMethodException e )
        {
            GameDebugger.log( Alert.INFO , "The state was not attached and set as the initial state" , e );
            throw new IllegalArgumentException( );
        }
    }

    /**
     * Attaches a state to the games state machine. The state machine is where all core gameplay mechanics are performed and as a result this method will be used often to attach gameplay states
     *
     * @param identifier the specified type used to identify the state to attach
     * @param <T>        the class type of the states this method accepts
     */
    public static < T extends GameState > void attach( Class< T > identifier )
    {
        try
        {
            game( ).states.attach( identifier );
        }
        catch ( NoSuchMethodException e )
        {
            GameDebugger.log( Alert.INFO , "The state was not attached" , e );
            throw new IllegalArgumentException( );
        }
    }

    /**
     * Gets the state changer function from the state machine
     *
     * @return a functional interface as a state {@link StateChanger changer}
     */
    public static StateChanger stateChanger( )
    {
        return game( ).states.stateChanger( );
    }

    /**
     * Adds a computable action to a list to be performed later
     *
     * @param milliseconds the milliseconds to wait before performing the action
     * @param computable   the specified action to perform
     */
    public static void runAfter( long milliseconds , Computable computable )
    {
        game( ).routine.runAfter( milliseconds , computable );
    }

    /**
     * Gets the clock that monitors how quickly the application runs
     *
     * @return an attached {@link RoutineClock routine clock} dictating the speed of the application routine
     */
    public static RoutineClock fpsClock( )
    {
        return game( ).routine.getClock( );
    }

    /**
     * Gets the current number of cycles that have started executing, increments when a cycle is started
     *
     * @return the current cycle number
     */
    public static long cycle( )
    {
        return game( ).routine.getCycle( );
    }

    /**
     * Gets the current delta
     *
     * @return the time between the last run cycle
     */
    public static double delta( )
    {
        double fpsDelta9 = fpsClock( ).getFpsDelta9( );
        return Quotient.toDouble( fpsDelta9 , RoutineClock.ONE_SECOND );
    }

    /**
     * Informs the routine to terminate
     */
    public static void exit( )
    {
        game( ).debugger.exitLoop( );
        game( ).responder.exitLoop( );
        game( ).routine.exitLoop( );
    }

    /**
     * Starts the game
     */
    public static synchronized void launch( )
    {
        game( ).display.setup( configuration( ) , fpsClock( ) , Game::exit , game( ).responder.clear( ) );
        game( ).renderer.setViewportPositionValidator( game( ).display.viewportPositionValidator( ) );
        game( ).debugger.launch( );
        game( ).responder.launch( );
        game( ).routine.launch( );
    }

    /**
     * Sets the icon of the game to the one specified
     *
     * @param image the specified image the hosting frame will use as an icon
     */
    public static void setIcon( Image image )
    {
        game( ).display.setIcon( image );
    }

    /**
     * Gets the game instance
     *
     * @return a static instance of a {@link Game game}
     */
    private static Game game( )
    {
        if ( ! initialized )
        {
            throw new NullPointerException( "Game has not been initialized" );
        }
        return game;
    }

    /**
     * Initializes the game with the provided game data
     *
     * @param gameDataBundle the specified bundle of game data used to initialize the game
     *
     * @return {@code True} if the game was correctly initialized. {@code False} otherwise
     */
    private static boolean initialize( GameDataBundle gameDataBundle )
    {
        try
        {
            game = new Game( );
            game.configuration = ConfigurationBundle.initializeFromFile( );
            gameDataBundle.initializeFromFile( game );
            game.sprites = SpriteWrapper.getInstance( gameDataBundle );
            game.debugger = DebugRoutine.newInstance( game.configuration.debugInformation( ).debugRate( ) );
            game.responder = ResponseRoutine.newInstance( game );
            game.states = GameStateMachine.newInstance( );
            game.routine = new InteractiveApplicationRoutine( game.configuration.gameInformation( ).name( ) );
            game.routine.getClock( ).attach( game.debugger.getClock( ) );
            game.routine.getClock( ).attach( game.responder.getClock( ) );
            game.routine.getClock( ).setRate( game.configuration.clientInformation( ).fpsModifier( ) );
            game.renderer = RenderRoutine.from( );
            game.display = DisplayRoutine.from( game.renderer );
            game.routine.attach( game.states );
            game.routine.attach( game.renderer );
            game.routine.attach( game.renderer.tracker( ) );
            game.routine.attach( game.display );
            return true;
        }
        catch ( Exception e )
        {
            GameDebugger.log( Alert.DIRE , "Unable to build the game" );
            return false;
        }
    }

    /**
     * Builds the game internally with the specified bundle of game data
     *
     * @param gameDataBundle the specified bundle of game data used to initialize the game
     */
    private static void internalBuild( GameDataBundle gameDataBundle )
    {
        if ( game == null )
        {
            synchronized ( Game.class )
            {
                initialized = initialize( gameDataBundle );
                boolean unsuccessful = ! initialized;
                if ( game == null || unsuccessful )
                {
                    throw new NullPointerException( "|game could not be built|" );
                }
            }
        }
    }

    /**
     * Builds the game with the specified bundle of game data
     *
     * @param gameDataBundle the specified bundle of game data used to initialize the game
     */
    public static void build( GameDataBundle gameDataBundle )
    {
        GameDebugger.setDebugMode( true );
        internalBuild( gameDataBundle );
        GameDebugger.setDebugMode( configuration( ).debugInformation( ).debugMode( ) );
    }

}