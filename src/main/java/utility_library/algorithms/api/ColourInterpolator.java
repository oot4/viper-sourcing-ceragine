/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.api;

import static java.lang.Math.ceil;

/**
 * A functional interface to be implemented when interpolating colour values
 */
@FunctionalInterface
public interface ColourInterpolator
{

    /**
     * Interpolates between a range of values.
     *
     * @param colour1  The specified starting value.
     * @param colour2  The specified final value.
     * @param modifier The specified constant by which to interpolate.
     *
     * @return The resulting value after applying the calculation.
     */
    int interpolateColour( int colour1 , int colour2 , double modifier );

    /**
     * Interpolates between a range of values.
     *
     * @param colour1  The specified starting value.
     * @param colour2  The specified final value.
     * @param modifier The specified constant by which to interpolate.
     *
     * @return The resulting value after applying the calculation.
     */
    default int interpolateWithStandard( int colour1 , int colour2 , double modifier )
    {
        int alpha1 = ( colour1 >> 24 ) & 255;
        int alpha2 = ( colour2 >> 24 ) & 255;
        int red1 = ( colour1 >> 16 ) & 255;
        int red2 = ( colour2 >> 16 ) & 255;
        int green1 = ( colour1 >> 8 ) & 255;
        int green2 = ( colour2 >> 8 ) & 255;
        int blue1 = colour1 & 255;
        int blue2 = colour2 & 255;
        int alpha = ( int ) ceil( ( alpha2 - alpha1 ) * modifier + alpha1 );
        int red = ( int ) ceil( ( red2 - red1 ) * modifier + red1 );
        int green = ( int ) ceil( ( green2 - green1 ) * modifier + green1 );
        int blue = ( int ) ceil( ( blue2 - blue1 ) * modifier + blue1 );
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

}