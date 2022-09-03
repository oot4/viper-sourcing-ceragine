package utility_library.algorithms.api;

import utility_library.algorithms.core.Clamp;

/**
 * A functional interface to be implemented when and interpolation function is provided
 */
@FunctionalInterface
public interface Interpolator
{

    /**
     * Interpolates between the given two points based on constant {@code t} which is a value {@code [0-1]}
     *
     * @param value1    the specified starting value
     * @param value2    the specified final value
     * @param tConstant the specified constant by which to interpolate
     *
     * @return the resulting value after computing the interpolation
     */
    double interpolate( double value1 , double value2 , double tConstant );

    /**
     * Constructs a new standard interpolation algorithm
     *
     * @return a functional interface for interpolating between values
     */
    static Interpolator standard( )
    {
        return ( start , end , tValue ) -> {
            double t = Clamp.toDouble( tValue , 0.0D , 1.0D );
            return start - ( start * t ) + ( end * t );
        };
    }

    /**
     * Interpolates between the given two points based on constant {@code t} which is a value {@code [0-1]}
     *
     * @param value1    the specified starting value
     * @param value2    the specified final value
     * @param tConstant the specified constant by which to interpolate
     *
     * @return the resulting value after computing the interpolation
     */
    default int toInt( int value1 , int value2 , double tConstant )
    {
        return ( int ) toDouble( value1 , value2 , tConstant );
    }

    /**
     * Interpolates between the given two points based on constant {@code t} which is a value {@code [0-1]}
     *
     * @param value1    the specified starting value
     * @param value2    the specified final value
     * @param tConstant the specified constant by which to interpolate
     *
     * @return the resulting value after computing the interpolation
     */
    default long toLong( long value1 , long value2 , double tConstant )
    {
        return ( long ) toDouble( value1 , value2 , tConstant );
    }

    /**
     * Interpolates between the given two points based on constant {@code t} which is a value {@code [0-1]}
     *
     * @param value1    the specified starting value
     * @param value2    the specified final value
     * @param tConstant the specified constant by which to interpolate
     *
     * @return the resulting value after computing the interpolation
     */
    default float toFloat( float value1 , float value2 , double tConstant )
    {
        return ( float ) toDouble( value1 , value2 , tConstant );
    }

    /**
     * Interpolates between the given two points based on constant {@code t} which is a value {@code [0-1]}
     *
     * @param value1    the specified starting value
     * @param value2    the specified final value
     * @param tConstant the specified constant by which to interpolate
     *
     * @return the resulting value after computing the interpolation
     */
    default double toDouble( double value1 , double value2 , double tConstant )
    {
        return interpolate( value1 , value2 , tConstant );
    }

}