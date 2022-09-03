package utility_library.algorithms.api;

@FunctionalInterface
public interface LinearInterpolator
{

    double toDouble( double value1 , double value2 , double tConstant );

    default int toInt( int value1 , int value2 , double tConstant )
    {
        return ( int ) toDouble( value1 , value2 , tConstant );
    }

    default long toLong( long value1 , long value2 , double tConstant )
    {
        return ( long ) toDouble( value1 , value2 , tConstant );
    }

    default float toFloat( float value1 , float value2 , double tConstant )
    {
        return ( float ) toDouble( value1 , value2 , tConstant );
    }

}