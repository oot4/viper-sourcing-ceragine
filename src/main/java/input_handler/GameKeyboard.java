package input_handler;

import ceragine.Game;
import ecs_architecture.api.KeyboardAPI;
import routine_handler.routines.api.Subroutine;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keyboard used to provide interface a human with the game
 */
public final class GameKeyboard implements KeyboardAPI, Subroutine, KeyEventDispatcher
{

    /**
     * Does an instance of the game keyboard exist?
     * <p>
     * The game keyboard is not allowed to have more than one instance but, the class is not meant to contain an instance of itself. The class functionality is controlled elsewhere but a single instance is enforced
     * </p>
     */
    private static boolean exists = false;

    /**
     * Attached listeners to key input registered in the keyboard
     */
    private final Collection< KeyListener > keyListeners = ConcurrentHashMap.newKeySet( );

    /**
     * Pressed key events
     */
    private final Collection< KeyEvent > pressed = ConcurrentHashMap.newKeySet( );

    /**
     * Released key events
     */
    private final Collection< KeyEvent > released = ConcurrentHashMap.newKeySet( );

    /**
     * Typed key events
     */
    private final Collection< KeyEvent > typed = ConcurrentHashMap.newKeySet( );

    /**
     * Constructor that creates a new instance of a {@code GameKeyboard}
     */
    private GameKeyboard( )
    {
        if ( exists )
        {
            throw new UnsupportedOperationException( "|instance already exists, use any of the methods provided by the game class" );
        }
    }

    /**
     * Creates a new instance of a {@code GameKeyboard}
     *
     * @param game the specified game class used to validate the creation of the game keyboard
     *
     * @return {@code null} if an instance already exists. A new instance of a {@code GameKeyboard} otherwise
     */
    public static GameKeyboard newInstance( Game game )
    {
        GameKeyboard gameKeyboard = null;
        if ( game != null )
        {
            gameKeyboard = new GameKeyboard( );
            KeyboardFocusManager.getCurrentKeyboardFocusManager( ).addKeyEventDispatcher( gameKeyboard );
            exists = true;
        }
        return gameKeyboard;
    }

    /**
     * Adds a specified key event to the provided event list
     *
     * @param event the specified event list to add to
     * @param key   the specified key event to add
     */
    private static void addKey( Collection< KeyEvent > event , KeyEvent key )
    {
        if ( event.stream( ).anyMatch( keyEvent -> keyEvent.getKeyCode( ) == key.getKeyCode( ) ) )
        {
            return;
        }
        event.add( key );
    }

    /**
     * Removes a specified key event from the provided event list
     *
     * @param event the specified event list to remove from
     * @param key   the specified key event to add
     */
    private static void removeKey( Collection< KeyEvent > event , KeyEvent key )
    {
        for ( KeyEvent toRemove : event )
        {
            if ( toRemove.getKeyCode( ) == key.getKeyCode( ) )
            {
                event.remove( toRemove );
                return;
            }
        }
    }

    /**
     * Is the key part of the event list?
     *
     * @param event the specified event list to test the presence of the specified key
     * @param key   the specified key to test the presence of
     *
     * @return {@code True} if the specified key is part of the event list. {@code False} otherwise
     */
    private static boolean eventTriggeredKey( Collection< KeyEvent > event , int key )
    {
        for ( KeyEvent triggered : event )
        {
            if ( triggered.getKeyCode( ) == key )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the keyboard inputs
     */
    public void clear( )
    {
        released.clear( );
        pressed.clear( );
        typed.clear( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attach( KeyAdapter keyAdapter )
    {
        if ( keyListeners.contains( keyAdapter ) )
        {
            return;
        }
        keyListeners.add( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detach( KeyAdapter keyAdapter )
    {
        keyListeners.remove( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPressed( int key )
    {
        return eventTriggeredKey( pressed , key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReleased( int key )
    {
        return eventTriggeredKey( released , key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTyped( int key )
    {
        return eventTriggeredKey( typed , key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchKeyEvent( KeyEvent key )
    {
        switch ( key.getID( ) )
        {
            case KeyEvent.KEY_PRESSED:
                addKey( pressed , key );
                break;
            case KeyEvent.KEY_RELEASED:
                removeKey( pressed , key );
                addKey( released , key );
                addKey( typed , key );
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void perform( )
    {
        keyListeners.forEach( keyListener -> {
            pressed.forEach( keyListener::keyPressed );
            released.forEach( keyListener::keyReleased );
            typed.forEach( keyListener::keyTyped );
        } );
        released.clear( );
        typed.clear( );
    }

}