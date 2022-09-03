/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.core;

import utility_library.algorithms.api.ReturningComputable;

/**
 * A class with an algorithm to clamp a value between two maximum and minimum thresholds
 */
public final class Clamp implements ReturningComputable< Double >
{

    /**
     * Object representation of a {@code Clamp} computable
     */
    private static final Clamp COMPUTABLE = new Clamp( );

    /**
     * Value to clamp
     */
    private double constant;

    /**
     * Minimum clamp threshold
     */
    private double minimum;

    /**
     * Maximum clamp threshold
     */
    private double maximum;

    /**
     * Creates a new instance of a {@code Clamp} computable object
     */
    private Clamp( )
    {
        this( 0.0D , 0.0D , 0.0D );
    }

    /**
     * Creates a new instance of a {@code Clamp} computable object
     *
     * @param constant the specified constant to clamp
     * @param minimum  the lower limit of the clamp
     * @param maximum  the upper limit of the clamp
     */
    private Clamp( double constant , double minimum , double maximum )
    {
        this.constant = constant;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param constant the specified constant to clamp
     * @param minimum  the lower limit of the clamp
     * @param maximum  the upper limit of the clamp
     *
     * @return the result of performing the clamp operation
     */
    private static double getResult( double constant , double minimum , double maximum )
    {
        COMPUTABLE.setAll( constant , minimum , maximum );
        return COMPUTABLE.computeAndGet( );
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param constant the specified constant to clamp
     * @param minimum  the lower limit of the clamp
     * @param maximum  the upper limit of the clamp
     *
     * @return the result of performing the clamp operation
     */
    public static int toInt( int constant , int minimum , int maximum )
    {
        return ( int ) getResult( constant , minimum , maximum );
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param constant the specified constant to clamp
     * @param minimum  the lower limit of the clamp
     * @param maximum  the upper limit of the clamp
     *
     * @return the result of performing the clamp operation
     */
    public static long toLong( long constant , long minimum , long maximum )
    {
        return ( long ) getResult( constant , minimum , maximum );
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param constant the specified constant to clamp
     * @param minimum  the lower limit of the clamp
     * @param maximum  the upper limit of the clamp
     *
     * @return the result of performing the clamp operation
     */
    public static float toFloat( float constant , float minimum , float maximum )
    {
        return ( float ) getResult( constant , minimum , maximum );
    }

    /**
     * Executes the computation function. A value is returned
     *
     * @param constant the specified constant to clamp
     * @param minimum  the lower limit of the clamp
     * @param maximum  the upper limit of the clamp
     *
     * @return the result of performing the clamp operation
     */
    public static double toDouble( double constant , double minimum , double maximum )
    {
        return getResult( constant , minimum , maximum );
    }

    /**
     * Sets the lower threshold, upper threshold and value to clamp
     *
     * @param constant the specified constant to clamp
     * @param minimum  the lower limit of the clamp
     * @param maximum  the upper limit of the clamp
     */
    private void setAll( double constant , double minimum , double maximum )
    {
        this.constant = constant;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double computeAndGet( )
    {
        return Math.max( Math.min( constant , maximum ) , minimum );
    }

}