package routine_handler.routines.core;

import ceragine.Game;
import ecs_architecture.api.KeyboardAPI;
import input_handler.GameKeyboard;
import routine_handler.components.api.ResponseListener;
import routine_handler.routines.standard.VariableLoopRoutine;
import utility_library.algorithms.api.Computable;

import java.awt.event.KeyAdapter;

/**
 * A class representing a routine for responding to input
 */
public final class ResponseRoutine extends VariableLoopRoutine implements KeyboardAPI, ResponseListener
{

    /**
     * Is this response routine actively responding?
     */
    private boolean responding;

    /**
     * Key event dispatcher
     */
    private final GameKeyboard keyboard;

    /**
     * Constructor for creating a new instance of a {@code ResponseRoutine}
     *
     * @param keyboard the specified key event dispatcher to respond to
     */
    private ResponseRoutine( GameKeyboard keyboard ) { this.keyboard = keyboard; }

    /**
     * Creates a new instance of a {@code ResponseRoutine}
     *
     * @param game the specified game class used to validate the creation of the game keyboard
     *
     * @return a {@code ResponseRoutine} object
     */
    public static ResponseRoutine newInstance( Game game )
    {
        return new ResponseRoutine( GameKeyboard.newInstance( game ) );
    }

    /**
     * Clears all input from the keyboard
     *
     * @return a computable for clearing all keyboard input
     */
    public Computable clear( )
    {
        return keyboard::clear;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void respond( )
    {
        keyboard.perform( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attach( KeyAdapter keyAdapter )
    {
        keyboard.attach( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detach( KeyAdapter keyAdapter )
    {
        keyboard.detach( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPressed( int key )
    {
        return keyboard.isPressed( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReleased( int key )
    {
        return keyboard.isReleased( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTyped( int key )
    {
        return keyboard.isTyped( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize( )
    {
        responding = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalTerminate( )
    {
        responding = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void begin( )
    {
        if ( responding )
        {
            respond( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute( )
    {
        responding = false;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finish( long interpolation , boolean updated )
    {
        responding = true;
    }

}