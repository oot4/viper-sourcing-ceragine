/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.core;

import junit.framework.TestCase;

/**
 * A test class for the computation to find the quotient of two constants
 */
public class QuotientTest extends TestCase
{

    /**
     * Tests whether finding the quotient of two constants returns the correct result and trying to find the quotient of negative numbers also returns the correct result
     */
    public void testOf( )
    {
        //Ensure we get the value 0.5 after the operation with negative values
        double dividend = - 1.0D;
        double divisor = - 2.0D;
        assertEquals( "|Values did not match|" , 0.5 , Quotient.toDouble( dividend , divisor ) );

        //Ensure we get the value 2 after the operation
        dividend = 2.0D;
        divisor = 1.0D;
        assertEquals( "|Values did not match|" , 2.0 , Quotient.toDouble( dividend , divisor ) );

        //Ensure that negative values are handled correctly
        dividend = 1.0D;
        divisor = - 2.0D;
        assertEquals( "|Values did not match|" , - 0.5 , Quotient.toDouble( dividend , divisor ) );
    }

}