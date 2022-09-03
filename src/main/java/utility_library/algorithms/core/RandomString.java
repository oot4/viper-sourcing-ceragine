/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.core;

import java.security.SecureRandom;

/**
 * A class for generating random strings with integers or the english alphabet
 */
public final class RandomString
{

    /**
     * Secure number generator
     */
    private static final SecureRandom SECURE_NUMBER_GENERATOR = new SecureRandom( );

    /**
     * Valid number dictionary
     */
    private static final String VALID_NUMBERS = "0123456789";

    /**
     * Valid lowercase letter dictionary
     */
    private static final String VALID_LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Valid uppercase letter dictionary
     */
    private static final String VALID_UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Creates a new instance of a {@code Random} class
     */
    private RandomString( )
    {
        throw new UnsupportedOperationException( "|this class should never be instantiated, use the static methods instead|" );
    }

    /**
     * Generates a random string from the provided character set
     *
     * @param size       the specified size of the expected output string
     * @param characters the specified set of characters to generate a string from
     *
     * @return a randomly generated string of the specified size from the specified set of characters
     *
     * @throws IndexOutOfBoundsException throws an exception if the provided size is out of bounds
     */
    private static String generate( final int size , String characters )
    {
        if ( size < 1 || size > 64 )
        {
            throw new IndexOutOfBoundsException( "|the string size is not in bounds. range is [1-64]" );
        }
        final char[] keypad = characters == null ? new char[]{ } : characters.toCharArray( );
        final int length = keypad.length;
        final int mask = ( 2 << ( int ) Math.floor( Math.log( length - 1.0D ) / Math.log( 2.0D ) ) ) - 1;
        final int step = ( int ) Math.ceil( 1.6 * mask * size / length );
        final StringBuilder stringBuilder = new StringBuilder( );
        while ( true )
        {
            final byte[] bytes = new byte[ step ];
            SECURE_NUMBER_GENERATOR.nextBytes( bytes );
            for ( int i = 0; i < step; i++ )
            {
                final int index = bytes[ i ] & mask;
                if ( index < length )
                {
                    stringBuilder.append( keypad[ index ] );
                    if ( stringBuilder.length( ) == size )
                    {
                        return stringBuilder.toString( );
                    }
                }
            }
        }
    }

    /**
     * Generates a random integer with the specified size of digits
     *
     * @param size the specified number of digits the integer should have
     *
     * @return a randomly generated integer of the specified size
     *
     * @throws IndexOutOfBoundsException throws an exception if the provided size is out of bounds
     */
    public static int randInt( int size )
    {
        if ( size < 1 || size > 9 )
        {
            throw new IndexOutOfBoundsException( "|the string size is not in bounds. range is [1-64]" );
        }
        return Integer.parseInt( generate( size , VALID_NUMBERS ) );
    }

    /**
     * Generates a random string with the specified size of characters
     *
     * @param size the specified number of characters the string should have
     *
     * @return a randomly generated string of the specified size
     */
    public static String randStr( int size )
    {
        return generate( size , VALID_UPPERCASE_CHARACTERS + VALID_LOWERCASE_CHARACTERS );
    }

    /**
     * Generates a random integer with 4 digits
     *
     * @return a randomly generated integer of 4 digits
     */
    public static int randInt4( )
    {
        return randInt( 4 );
    }

    /**
     * Generates a random integer with 8 digits
     *
     * @return a randomly generated integer of 8 digits
     */
    public static int randInt8( )
    {
        return randInt( 8 );
    }

    /**
     * Generates a random string with 4 characters
     *
     * @return a randomly generated string of 4 characters
     */
    public static String randStr4( )
    {
        return randStr( 4 );
    }

    /**
     * Generates a random string with 8 characters
     *
     * @return a randomly generated string of 8 characters
     */
    public static String randStr8( )
    {
        return randStr( 8 );
    }

}