package utility_library.structures;

import junit.framework.TestCase;

import java.lang.reflect.Field;

/**
 * Test class for a counter object
 */
public class CounterTest extends TestCase
{

    /**
     * Tests whether checking the reached interval returns the correct values
     */
    public void testReachedInterval( )
    {
        Counter counter = Counter.from( 200 );
        try
        {
            Field field = Counter.class.getDeclaredField( "frame" );
            field.setAccessible( true );
            field.set( counter , 180 );
            assertTrue( counter.reachedInterval( 179 ) );
            assertFalse( counter.reachedInterval( 300 ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

}