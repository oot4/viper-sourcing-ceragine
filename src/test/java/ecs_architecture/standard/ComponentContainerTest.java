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
 * A test class for a component container
 */
public class ComponentContainerTest extends TestCase
{

    /**
     * Tests whether the internal add operation works correctly. Also, tests whether an exception is thrown when providing a null or empty ID string and null game components are ignored. Tests whether the specified entity ID is present before adding the game components
     */
    public void testInternalAdd( )
    {
        TestComponentContainer container = new TestComponentContainer( );
        TestGameComponent component = new TestGameComponent( );
        Map< Class< ? extends GameComponent >, Map< String, GameComponent > > testComponents = new ConcurrentHashMap<>( );
        try
        {
            Field componentsField = ComponentContainer.class.getDeclaredField( "components" );
            componentsField.setAccessible( true );
            componentsField.set( container , testComponents );
            Object componentsObject1 = componentsField.get( container );
            Map< ?, ? > components1 = testComponents.getClass( ).cast( componentsObject1 );
            assertThrows( "Exception thrown does not match" , IllegalArgumentException.class , ( ) -> container.testAdd( "test" , ( GameComponent ) null ) );

            Field entityField = EntityContainer.class.getDeclaredField( "entities" );
            Set< String > emptySet = ConcurrentHashMap.newKeySet( );
            emptySet.add( "test" );
            entityField.setAccessible( true );
            entityField.set( container , emptySet );
            container.testAdd( "test" , ( GameComponent ) null );
            assertTrue( "Condition was not true" , components1.isEmpty( ) );
            assertThrows( "Exception thrown does not match" , NullPointerException.class , ( ) -> container.testAdd( "" , component ) );
            assertThrows( "Exception thrown does not match" , NullPointerException.class , ( ) -> container.testAdd( ( String ) null , component ) );

            container.testAdd( "test" , component );
            Map< ?, ? > entityMap = ( Map< ?, ? > ) components1.get( TestGameComponent.class );
            assertFalse( "Condition was not false" , components1.isEmpty( ) );
            assertTrue( "Condition was not true" , components1.containsKey( TestGameComponent.class ) );
            assertTrue( "Condition was not true" , entityMap.containsKey( "test" ) );
            assertTrue( "Condition was not true" , entityMap.containsValue( component ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the overload method of the internal add operation works correctly by generating a random id for an entity
     */
    public void testInternalAddGeneratesKey( )
    {
        TestComponentContainer container = new TestComponentContainer( );
        TestGameComponent component = new TestGameComponent( );
        Map< Class< ? extends GameComponent >, Map< String, GameComponent > > testComponents = new ConcurrentHashMap<>( );
        try
        {
            Field componentsField = ComponentContainer.class.getDeclaredField( "components" );
            componentsField.setAccessible( true );
            componentsField.set( container , testComponents );
            container.testAdd( component );
            Object componentsObject1 = componentsField.get( container );
            Map< ?, ? > components1 = testComponents.getClass( ).cast( componentsObject1 );
            Map< ?, ? > entityMap = ( Map< ?, ? > ) components1.get( TestGameComponent.class );
            assertFalse( "Condition was not false" , entityMap.keySet( ).isEmpty( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the internal remove operation works correctly by return true ONLY when the specified component was removed from the entity
     */
    public void testInternalRemove( )
    {
        TestComponentContainer container = new TestComponentContainer( );
        TestGameComponent component = new TestGameComponent( );
        Map< String, GameComponent > testComponents = new ConcurrentHashMap<>( );
        testComponents.put( "test" , component );
        Map< Class< ? extends GameComponent >, Map< String, GameComponent > > allComponents = new ConcurrentHashMap<>( );
        allComponents.put( TestGameComponent.class , testComponents );
        try
        {
            Field componentsField = ComponentContainer.class.getDeclaredField( "components" );
            componentsField.setAccessible( true );
            componentsField.set( container , allComponents );
            Object componentsObject1 = componentsField.get( container );
            Map< ?, ? > components1 = allComponents.getClass( ).cast( componentsObject1 );
            Map< ?, ? > entityMap1 = ( Map< ?, ? > ) components1.get( TestGameComponent.class );
            assertTrue( "Condition was not true" , entityMap1.containsKey( "test" ) && entityMap1.containsValue( component ) );
            assertTrue( "Condition was not true" , container.testRemove( "test" , TestGameComponent.class ) );
            assertFalse( "Condition was not false" , container.testRemove( "invalid key" , TestGameComponent.class ) );
            assertFalse( "Condition was not false" , container.testRemove( "test" , TestGameComponent.class ) );
            assertFalse( "Condition was not false" , container.testRemove( "test" , InvalidComponent.class ) );
            assertFalse( "Condition was not false" , container.testRemove( "invalid key" , InvalidComponent.class ) );
        }
        catch ( IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the internal remove operation that takes in an entity id does not fail and immediately returns false if an invalid key is provided
     */
    public void testInternalRemoveInvalidKey( )
    {
        TestComponentContainer container = new TestComponentContainer( );
        assertFalse( "Condition was not false" , container.testRemove( "invalid key" ) );
    }

    /**
     * Test whether the get method creates a new empty map if the specified class type does not exist
     */
    public void testGet( )
    {
        TestComponentContainer container = new TestComponentContainer( );
        TestGameComponent component = new TestGameComponent( );
        Map< String, GameComponent > testComponents = new ConcurrentHashMap<>( );
        testComponents.put( "test" , component );
        Map< Class< ? extends GameComponent >, Map< String, GameComponent > > allComponents = new ConcurrentHashMap<>( );
        allComponents.put( TestGameComponent.class , testComponents );
        try
        {
            Field componentsField = ComponentContainer.class.getDeclaredField( "components" );
            componentsField.setAccessible( true );
            componentsField.set( container , allComponents );
            Object componentsObject1 = componentsField.get( container );
            Map< ?, ? > components1 = allComponents.getClass( ).cast( componentsObject1 );
            Map< ?, ? > entityMap1 = ( Map< ?, ? > ) components1.get( TestGameComponent.class );
            assertTrue( "Condition was not true" , entityMap1.containsKey( "test" ) && entityMap1.containsValue( component ) );
            assertFalse( "Condition was not false" , container.get( TestGameComponent.class ).isEmpty( ) );
            assertTrue( "Condition was not true" , container.get( InvalidComponent.class ).isEmpty( ) );

            assertTrue( "Condition was not true" , components1.containsKey( InvalidComponent.class ) );
        }
        catch ( IllegalAccessException | SecurityException | NoSuchFieldException | IllegalArgumentException e )
        {
            throw new RuntimeException( e );
        }
    }


    /**
     * Dummy game component
     */
    private static class TestGameComponent extends GameComponent
    {

    }

    /**
     * Dummy invalid game component
     */
    private static class InvalidComponent extends GameComponent
    {

    }

    /**
     * Dummy test component container
     */
    private static class TestComponentContainer extends ComponentContainer
    {

        public final void testAdd( String id , GameComponent... gameComponents )
        {
            internalAdd( id , gameComponents );
        }

        public final void testAdd( GameComponent... gameComponents )
        {
            internalAdd( gameComponents );
        }

        public final boolean testRemove( String id , Class< ? extends GameComponent > type )
        {
            return internalRemove( id , type );
        }

        public final boolean testRemove( String id )
        {
            return internalRemove( id );
        }

        @Override
        public void draw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha )
        {

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