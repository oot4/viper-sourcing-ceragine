/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package game_debugger;

import utility_library.algorithms.api.ReturningComputable;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A class representing a debugger
 */
public final class GameDebugger
{

    /**
     * Symbol for message formatting
     */
    private static final String FORMAT = "{0}";

    /**
     * Game debugger container
     */
    private static GameDebugger gameDebugger;

    /**
     * A java logger
     */
    private Logger logger;

    /**
     * Is debugging mode active?
     */
    private boolean debugMode = false;

    /**
     * Creates a new instance of a {@code GameDebugger}. This should never be called outside the {@code GameDebugger} class
     *
     * @throws UnsupportedOperationException initializing this class when its static instance exists, throws an exception
     */
    private GameDebugger( )
    {
        if ( gameDebugger != null )
        {
            throw new UnsupportedOperationException( "|instance already exists, use any of the static methods instead|" );
        }
    }

    /**
     * The instance of a {@code GameDebugger}
     *
     * @return the static instance of {@link GameDebugger Game debugger}
     *
     * @throws NullPointerException throws an exception if the static instance of the class could not be initialized
     */
    private static GameDebugger getInstance( )
    {
        if ( gameDebugger == null )
        {
            synchronized ( GameDebugger.class )
            {
                boolean unsuccessful = ! initialize( );
                if ( gameDebugger == null || unsuccessful )
                {
                    throw new NullPointerException( "|game logger could not be initialized|" );
                }
            }
        }
        return gameDebugger;
    }

    /**
     * Initializes the static instance of {@code GameDebugger}
     *
     * @return {@code True} if the static instance of {@code GameDebugger} was initialized successfully. {@code False} otherwise
     */
    private static boolean initialize( )
    {
        try
        {
            gameDebugger = new GameDebugger( );
            LogManager.getLogManager( ).reset( );
            ConsoleHandler consoleHandler = new ConsoleHandler( );
            consoleHandler.setFormatter( new SimpleFormatter( ) );
            consoleHandler.setLevel( Level.ALL );

            FileHandler fileHandler = new FileHandler( "ceragine.log" , 0xea60 , 0x1 , true );
            fileHandler.setFormatter( new SimpleFormatter( ) );
            fileHandler.setLevel( Level.WARNING );

            gameDebugger.internalSetLogger( Logger.getLogger( GameDebugger.class.getSimpleName( ) ) );
            gameDebugger.internalGetLogger( ).addHandler( consoleHandler );
            gameDebugger.internalGetLogger( ).addHandler( fileHandler );
            return true;
        }
        catch ( IOException | SecurityException exception )
        {
            gameDebugger.logger.log( Level.SEVERE , exception.getMessage( ) , exception );
        }
        return false;
    }

    /**
     * The state of the debugging mode
     *
     * @return {@code True} if debugging mode is active. {@code False} otherwise
     */
    private boolean internalDebugMode( ) { return debugMode; }

    /**
     * Switches debugging mode on or off
     *
     * @param debugOn is debugging mode active?
     */
    private void internalSetDebugMode( boolean debugOn ) { this.debugMode = debugOn; }

    /**
     * Gets the Java logger
     *
     * @return the Java logger initialized for the {@code GameDebugger}
     */
    private Logger internalGetLogger( ) { return logger; }

    /**
     * Sets the Java logger for the {@code GameDebugger}
     *
     * @param logger The specified Java logger
     */
    private void internalSetLogger( Logger logger ) { this.logger = logger; }

    /**
     * Switches debugging mode on or off
     *
     * @param debugOn is debugging mode active?
     */
    public static void setDebugMode( boolean debugOn ) { getInstance( ).internalSetDebugMode( debugOn ); }

    /**
     * Helper function to log messages to the Java logger without arguments
     *
     * @param level   the specified alert level identifier, e.g., {@code INFO}
     * @param message the specified message to log
     */
    public static void log( Alert level , String message )
    {
        String messageToLowerCase = message.toLowerCase( );
        String formattedMessage = MessageFormat.format( FORMAT , messageToLowerCase );
        if ( getInstance( ).internalDebugMode( ) )
        {
            getInstance( ).internalGetLogger( ).log( level.getLevel( ) , formattedMessage );
        }
    }

    /**
     * Helper function to log messages to the Java logger. The message is constructed by invoking the provided supplier function
     *
     * @param level   the specified  alert level identifier, e.g., {@code INFO}
     * @param message the specified message supplier function to construct the message
     */
    public static void log( Alert level , Supplier< String > message )
    {
        if ( getInstance( ).internalDebugMode( ) )
        {
            getInstance( ).internalGetLogger( ).log( level.getLevel( ) , ( ) -> {
                String messageToLowerCase = message.get( ).toLowerCase( );
                return MessageFormat.format( FORMAT , messageToLowerCase );
            } );
        }
    }

    /**
     * Helper function to log messages to the Java logger with a throwable argument
     *
     * @param level     the specified alert level identifier, e.g., {@code INFO}
     * @param message   the specified message to log
     * @param throwable the specified throwable associated with the log message
     */
    public static void log( Alert level , String message , Throwable throwable )
    {
        String messageToLowerCase = message.toLowerCase( );
        String formattedMessage = MessageFormat.format( FORMAT , messageToLowerCase );
        if ( getInstance( ).internalDebugMode( ) )
        {
            getInstance( ).internalGetLogger( ).log( level.getLevel( ) , formattedMessage , throwable );
        }
    }

    /**
     * Helper function to log messages to the Java logger with a throwable argument. The message is constructed by invoking the provided supplier function
     *
     * @param level     the specified alert level identifier, e.g., {@code INFO}
     * @param message   the specified message supplier function to construct the message
     * @param throwable the specified throwable associated with the log message
     */
    public static void log( Alert level , Supplier< String > message , Throwable throwable )
    {
        if ( getInstance( ).internalDebugMode( ) )
        {
            getInstance( ).internalGetLogger( ).log( level.getLevel( ) , throwable , ( ) -> {
                String messageToLowerCase = message.get( ).toLowerCase( );
                return MessageFormat.format( FORMAT , messageToLowerCase );
            } );
        }
    }

    /**
     * Helper function to log a message to the Java logger based on a fail and pass condition
     *
     * @param failCondition the specified condition evaluated as {@code True} if a failure occurs
     * @param passCondition the specified condition evaluated as {@code True} if a successful operation occurs
     * @param format        the specified message to log if failure occurs
     */
    public static void validateCondition( ReturningComputable< Boolean > failCondition , ReturningComputable< Boolean > passCondition , String format )
    {
        try
        {
            if ( Boolean.TRUE.equals( failCondition.computeAndGet( ) ) )
            {
                throw new UnsupportedOperationException( );
            }
            boolean unsuccessful = Boolean.FALSE.equals( passCondition.computeAndGet( ) );
            if ( unsuccessful )
            {
                throw new UnsupportedOperationException( );
            }
        }
        catch ( Exception exception )
        {
            GameDebugger.log( Alert.DIRE , format , exception );
        }
    }

}