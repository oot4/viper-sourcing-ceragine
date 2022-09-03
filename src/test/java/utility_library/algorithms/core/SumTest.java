/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.core;

import junit.framework.TestCase;

/**
 * A test class for the computation to find the sum of a set of constants
 */
public class SumTest extends TestCase
{

    /**
     * Tests whether finding the sum of a set of values returns the correct result and that trying to find the sum of a null or empty array returns 0
     */
    public void testSumComputable( )
    {
        final double[] objectWithValues = new double[]{ 2 , 3 , 1 };
        final double[] objectWithNoValues = new double[]{ };

        assertEquals( "|Values did not match|" , 6.0 , Sum.of( objectWithValues ) );
        assertEquals( "|Values did not match|" , 0.0 , Sum.of( objectWithNoValues ) );
        assertEquals( "|Values did not match|" , 0.0 , Sum.of( ( double[] ) null ) );
    }

}