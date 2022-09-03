/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.standard;

import ecs_architecture.api.GameplayAPI;
import junit.framework.TestCase;

import java.awt.event.KeyAdapter;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A test class for a system container
 */
public class SystemContainerTest extends TestCase
{

    /**
     * Tests whether the internal add operation works correctly and links the provided GameplayAPI
     */
    public void testInternalAdd( )
    {
        TestSystemContainer container = new TestSystemContainer( );
        TestGameSystem system = new TestGameSystem( );
        Map< Class< ? extends GameSystem >, GameSystem > testSystems = new ConcurrentHashMap<>( );
        try
        {
            Field systemsField = SystemContainer.class.getDeclaredField( "systems" );
            systemsField.setAccessible( true );
            systemsField.set( container , testSystems );
            Object systemsObject1 = systemsField.get( container );
            Map< ?, ? > systems1 = testSystems.getClass( ).cast( systemsObject1 );
            assertTrue( "Condition was not true" , systems1.isEmpty( ) );
            container.internalAdd( container.gameplayAPI , system );
            assertTrue( "Condition was not true" , systems1.containsKey( system.getClass( ) ) );
            TestGameSystem addedSystem = ( TestGameSystem ) systems1.get( TestGameSystem.class );
            Field gameplayAPIField = GameSystem.class.getDeclaredField( "model" );
            gameplayAPIField.setAccessible( true );
            GameplayAPI gameplayAPI = ( GameplayAPI ) gameplayAPIField.get( addedSystem );
            assertSame( "Objects are not the same" , gameplayAPI , container.gameplayAPI );
        }
        catch ( IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the method to activate a system works correctly, throws an exception when the game system provided is null or not contained in the system container. Also, tests that a system is initialized after activation
     */
    public void testActivate( )
    {
        TestSystemContainer container = new TestSystemContainer( );
        TestGameSystem system = new TestGameSystem( );
        Map< Class< ? extends GameSystem >, GameSystem > testSystems = new ConcurrentHashMap<>( );
        testSystems.put( TestGameSystem.class , system );
        try
        {
            Field systemsField = SystemContainer.class.getDeclaredField( "systems" );
            systemsField.setAccessible( true );
            systemsField.set( container , testSystems );
            Object systemsObject1 = systemsField.get( container );
            Map< ?, ? > systems1 = testSystems.getClass( ).cast( systemsObject1 );
            TestGameSystem addedSystem = ( TestGameSystem ) systems1.get( TestGameSystem.class );
            assertFalse( "Condition was not false" , systems1.isEmpty( ) );
            assertFalse( "Condition was not false" , addedSystem.internalInitializeMethodWasCalled.getStatus( ) );
            container.activate( TestGameSystem.class );
            assertTrue( "Condition was not true" , addedSystem.internalInitializeMethodWasCalled.getStatus( ) );
            assertTrue( "Condition was not true" , addedSystem.isEnabled( ) );
        }
        catch ( IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the method to deactivate a system works correctly, throws an exception when the game system provided is null or not contained in the system container. Also, tests that the system a system is terminated after deactivation
     */
    public void testDeactivate( )
    {
        TestSystemContainer container = new TestSystemContainer( );
        TestGameSystem system = new TestGameSystem( );
        system.setEnabled( true );
        system.initialize( );
        Map< Class< ? extends GameSystem >, GameSystem > testSystems = new ConcurrentHashMap<>( );
        testSystems.put( TestGameSystem.class , system );
        try
        {
            Field systemsField = SystemContainer.class.getDeclaredField( "systems" );
            systemsField.setAccessible( true );
            systemsField.set( container , testSystems );
            Object systemsObject1 = systemsField.get( container );
            Map< ?, ? > systems1 = testSystems.getClass( ).cast( systemsObject1 );
            TestGameSystem addedSystem = ( TestGameSystem ) systems1.get( TestGameSystem.class );
            assertFalse( "Condition was not false" , systems1.isEmpty( ) );
            assertTrue( "Condition was not true" , addedSystem.internalInitializeMethodWasCalled.getStatus( ) );
            assertFalse( "Condition was not false" , addedSystem.internalRemoveMethodWasCalled.getStatus( ) );
            container.deactivate( TestGameSystem.class );
            assertTrue( "Condition was not true" , addedSystem.internalRemoveMethodWasCalled.getStatus( ) );
            assertFalse( "Condition was not false" , addedSystem.isEnabled( ) );
        }
        catch ( IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Dummy test system
     */
    private static class TestGameSystem extends GameSystem
    {

        final Status internalInitializeMethodWasCalled;

        final Status internalRemoveMethodWasCalled;

        public TestGameSystem( )
        {
            super( null );
            internalInitializeMethodWasCalled = new Status( false );
            internalRemoveMethodWasCalled = new Status( false );
        }

        @Override
        protected void internalInitialize( )
        {
            internalInitializeMethodWasCalled.setStatus( true );
        }

        @Override
        protected void internalTerminate( )
        {
            internalRemoveMethodWasCalled.setStatus( true );
        }

        @Override
        protected void internalPerform( )
        {
        }

    }

    /**
     * Dummy invalid system
     */
    private static class InvalidGameSystem extends GameSystem
    {

        final Status internalInitializeMethodWasCalled;

        final Status internalRemoveMethodWasCalled;

        public InvalidGameSystem( )
        {
            super( null );
            internalInitializeMethodWasCalled = new Status( false );
            internalRemoveMethodWasCalled = new Status( false );
        }

        @Override
        protected void internalInitialize( )
        {
            internalInitializeMethodWasCalled.setStatus( true );
        }

        @Override
        protected void internalTerminate( )
        {
            internalRemoveMethodWasCalled.setStatus( true );
        }

        @Override
        protected void internalPerform( )
        {

        }

    }

    /**
     * Dummy system container
     */
    private static class TestSystemContainer extends SystemContainer
    {

        final GameplayAPI gameplayAPI = new GameplayAPI( )
        {
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
            public void draw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha )
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
        };

        public void testAdd( GameplayAPI gameplayAPI , GameSystem... gameSystems )
        {
            internalAdd( gameplayAPI , gameSystems );
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

    /**
     * Status used to indicate whether something is true or not
     */
    private static final class Status
    {

        private boolean status;

        public Status( boolean status )
        {
            this.status = status;
        }

        public void setStatus( boolean status )
        {
            this.status = status;
        }

        public boolean getStatus( )
        {
            return status;
        }

    }

}