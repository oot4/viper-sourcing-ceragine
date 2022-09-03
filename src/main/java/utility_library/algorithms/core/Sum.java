/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.core;

import utility_library.algorithms.api.ReturningComputable;

import java.util.Arrays;

/**
 * A class with an algorithm to find the sum of all arguments
 */
public final class Sum implements ReturningComputable< Double >
{

    /**
     * Object representation of a {@code Sum} computable
     */
    private static final Sum COMPUTABLE = new Sum( );

    /**
     * Array of double constants
     */
    private double[] constants;

    /**
     * Creates a new instance of a {@code Sum} computable object
     */
    private Sum( )
    {
        this( ( double[] ) null );
    }

    /**
     * Creates a new instance of a {@code Sum} computable object
     *
     * @param constants the specified constants to find the sum of
     */
    private Sum( double... constants )
    {
        this.constants = constants;
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param constants the specified constants to find the sum of
     *
     * @return the result of performing the sum operation
     */
    public static int of( int... constants )
    {
        return ( int ) of( Arrays.stream( constants ).asDoubleStream( ).toArray( ) );
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param constants the specified constants to find the sum of
     *
     * @return the result of performing the sum operation
     */
    public static long of( long... constants )
    {
        return ( long ) of( Arrays.stream( constants ).asDoubleStream( ).toArray( ) );
    }


    /**
     * Executes the computation function. A value is returned
     *
     * @param constants the specified constants to find the sum of
     *
     * @return the result of performing the sum operation
     */
    public static double of( double... constants )
    {
        COMPUTABLE.setConstants( constants );
        return COMPUTABLE.computeAndGet( );
    }

    /**
     * Sets the array of constant values
     *
     * @param constants The specified arguments to populate the array with
     */
    private void setConstants( double[] constants )
    {
        this.constants = constants;
    }

    /**
     * Recursively finds the sum of all arguments in the constants array
     *
     * @param index the specified index of the value in the constants array to perform the operation on
     *
     * @return the result of performing the sum operation
     */
    private double sum( int index )
    {
        if ( constants == null || index >= constants.length )
        {
            return 0.0;
        }
        return constants[ index ] + sum( ++ index );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double computeAndGet( )
    {
        return sum( 0 );
    }

}