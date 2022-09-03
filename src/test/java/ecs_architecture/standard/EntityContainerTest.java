/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.standard;

import junit.framework.TestCase;

import java.awt.event.KeyAdapter;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertThrows;

/**
 * A test class for an entity container
 */
public class EntityContainerTest extends TestCase
{

    /**
     * Tests whether the internal add operation of the entity container works correctly and returns the right value. Also tests that an exception is thrown if the max extended capacity limit is reached
     */
    public void testInternalAdd( )
    {
        TestEntityContainer test = new TestEntityContainer( );
        try
        {
            Field entityField = EntityContainer.class.getDeclaredField( "entities" );
            Set< String > emptySet = ConcurrentHashMap.newKeySet( );
            entityField.setAccessible( true );
            entityField.set( test , emptySet );
            Object entities = entityField.get( test );
            Set< ? > entitiesSet = emptySet.getClass( ).cast( entities );
            assertTrue( "Condition was not true" , entitiesSet.isEmpty( ) );
            String value = test.testAdd( );
            entities = entityField.get( test );
            entitiesSet = emptySet.getClass( ).cast( entities );
            assertFalse( "Condition was not false" , entitiesSet.isEmpty( ) );
            assertTrue( "Condition was not true" , entitiesSet.contains( value ) );
            assertEquals( 1 , entitiesSet.size( ) );
            Field maxField = EntityContainer.class.getDeclaredField( "max" );
            maxField.setAccessible( true );
            maxField.set( test , 2048 );
            for ( int i = 0; i < 2047; i++ )
            {
                emptySet.add( String.valueOf( i ) );
            }
            entityField.setAccessible( true );
            entityField.set( test , emptySet );
            assertThrows( "Exception thrown does not match" , IndexOutOfBoundsException.class , test::testAdd );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the internal remove operation of the entity container works correctly and returns the right value
     */
    public void testInternalRemove( )
    {
        TestEntityContainer test = new TestEntityContainer( );
        try
        {
            Field entityField = EntityContainer.class.getDeclaredField( "entities" );
            Set< String > emptySet = ConcurrentHashMap.newKeySet( );
            emptySet.add( "test string" );
            entityField.setAccessible( true );
            entityField.set( test , emptySet );
            Object entities = entityField.get( test );
            Set< ? > entitiesSet = emptySet.getClass( ).cast( entities );
            assertTrue( "Condition was not true" , entitiesSet.contains( "test string" ) );
            assertTrue( "Condition was not true" , test.testRemove( "test string" ) );
            entities = entityField.get( test );
            entitiesSet = emptySet.getClass( ).cast( entities );
            assertFalse( "Condition was not false" , entitiesSet.contains( "test string" ) );
            assertTrue( "Condition was not true" , entitiesSet.isEmpty( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Dummy entity container
     */
    private static class TestEntityContainer extends EntityContainer
    {

        public String testAdd( ) { return internalAdd( ); }

        public boolean testRemove( String id ) { return internalRemove( id ); }

        @Override
        public void draw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha )
        {

        }

        @Override
        public Map< String, GameComponent > get( Class< ? extends GameComponent > type )
        {
            return null;
        }

        @Override
        public void add( GameComponent... gameComponents )
        {

        }

        @Override
        public void add( String id , GameComponent... gameComponents )
        {

        }

        @Override
        public void add( GameSystem... gameSystems )
        {

        }

        @Override
        public void remove( String id )
        {

        }

        @Override
        public void remove( String id , Class< ? extends GameComponent > type )
        {

        }

        @Override
        public void activate( Class< ? extends GameSystem > type )
        {

        }

        @Override
        public void deactivate( Class< ? extends GameSystem > type )
        {

        }

        @Override
        public void attach( KeyAdapter keyAdapter )
        {

        }

        @Override
        public void detach( KeyAdapter keyAdapter )
        {

        }

        @Override
        public boolean isPressed( int key )
        {
            return false;
        }

        @Override
        public boolean isReleased( int key )
        {
            return false;
        }

        @Override
        public boolean isTyped( int key )
        {
            return false;
        }

    }

}