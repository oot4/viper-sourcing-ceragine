/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.core;

import junit.framework.TestCase;

/**
 * A test class for the computation to clamp a constant value between an upper and lower constraint
 */
public class ClampTest extends TestCase
{

    /**
     * Tests whether clamping a value returns the correct result and that trying to clamp a value above or below the constraints yields those upper or lower bounds
     */
    public void testClampComputable( )
    {
        final double min = 1.0D;
        final double max = 10.0D;
        double constant = 3.0D;

        //Ensure the constant value is returned if it fits the constraints (min, max)
        assertEquals( "|Values did not match|" , 3.0 , Clamp.toDouble( constant , min , max ) );

        //Ensure the returned value cannot go lower than the min
        constant = - 1.0;
        assertTrue( "|Statement was not true|" , Clamp.toDouble( constant , min , max ) >= min );

        //Ensure the returned value cannot go higher than the max
        constant = 11.0;
        assertTrue( "|Statement was not true|" , Clamp.toDouble( constant , min , max ) <= max );
    }

}