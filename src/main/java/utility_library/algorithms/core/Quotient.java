/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.core;

import utility_library.algorithms.api.ReturningComputable;

/**
 * A class with an algorithm to find the quotient of two arguments
 */
public final class Quotient implements ReturningComputable< Double >
{

    /**
     * Object representation of a {@code Quotient} computable
     */
    private static final Quotient COMPUTABLE = new Quotient( );

    /**
     * The constant to be divided
     */
    private double dividend;

    /**
     * The constant to divide by
     */
    private double divisor;

    /**
     * Creates a new instance of a {@code Quotient} computable object
     */
    private Quotient( )
    {
        this( 0.0D , 0.0D );
    }

    /**
     * Creates a new instance of a {@code Quotient} computable object
     *
     * @param dividend the specified constant to be divided
     * @param divisor  the specified constant to divide by
     */
    private Quotient( double dividend , double divisor )
    {
        this.dividend = dividend;
        this.divisor = divisor;
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param dividend the specified constant to be divided
     * @param divisor  the specified constant to divide by
     *
     * @return the result of performing the quotient operation
     */
    private static double getResult( double dividend , double divisor )
    {
        COMPUTABLE.setAll( dividend , divisor );
        return COMPUTABLE.computeAndGet( );
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param dividend the specified constant to be divided
     * @param divisor  the specified constant to divide by
     *
     * @return the result of performing the quotient operation
     */
    public static int toInt( int dividend , int divisor )
    {
        return ( int ) getResult( dividend , divisor );
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param dividend the specified constant to be divided
     * @param divisor  the specified constant to divide by
     *
     * @return the result of performing the quotient operation
     */
    public static long toLong( long dividend , long divisor )
    {
        return ( long ) getResult( dividend , divisor );
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param dividend the specified constant to be divided
     * @param divisor  the specified constant to divide by
     *
     * @return the result of performing the quotient operation
     */
    public static float toFloat( float dividend , float divisor )
    {
        return ( float ) getResult( dividend , divisor );
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param dividend the specified constant to be divided
     * @param divisor  the specified constant to divide by
     *
     * @return the result of performing the quotient operation
     */
    public static double toDouble( double dividend , double divisor )
    {
        return getResult( dividend , divisor );
    }

    /**
     * Sets the constants used in the quotient operation
     *
     * @param dividend the specified constant to be divided
     * @param divisor  the specified constant to divide by
     */
    private void setAll( double dividend , double divisor )
    {
        this.dividend = dividend;
        this.divisor = divisor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double computeAndGet( )
    {
        return dividend * Math.pow( divisor , - 1.0D );
    }

}