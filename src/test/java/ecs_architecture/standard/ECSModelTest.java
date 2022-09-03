/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.standard;

import junit.framework.TestCase;

import java.awt.event.KeyAdapter;

/**
 * A test class for an ECS model
 */
public class ECSModelTest extends TestCase
{

    /**
     * Tests whether the method to initialize the model works correctly and all added systems are enabled plus initialized
     */
    public void testInitialize( )
    {
        DummyECSModel dummyECSModel = new DummyECSModel( );
        DummySystem0 dummySystem0 = new DummySystem0( );
        DummySystem1 dummySystem1 = new DummySystem1( );
        DummySystem2 dummySystem2 = new DummySystem2( );
        dummyECSModel.testAdd( dummySystem0 , dummySystem1 , dummySystem2 );
        dummyECSModel.testMode( );
        dummyECSModel.initialize( );
        assertTrue( "Condition was not true" , dummyECSModel.systems( ).get( DummySystem0.class ).isEnabled( ) && dummySystem0.initialized.getStatus( ) );
        assertTrue( "Condition was not true" , dummyECSModel.systems( ).get( DummySystem1.class ).isEnabled( ) && dummySystem1.initialized.getStatus( ) );
        assertTrue( "Condition was not true" , dummyECSModel.systems( ).get( DummySystem2.class ).isEnabled( ) && dummySystem2.initialized.getStatus( ) );
    }

    /**
     * Tests whether the method to terminate the model works correctly and all added systems are disabled plus terminated. Also tests that update flag is reset
     */
    public void testTerminate( )
    {
        DummyECSModel dummyECSModel = new DummyECSModel( );
        DummySystem0 dummySystem0 = new DummySystem0( );
        DummySystem1 dummySystem1 = new DummySystem1( );
        DummySystem2 dummySystem2 = new DummySystem2( );
        dummyECSModel.testAdd( dummySystem0 , dummySystem1 , dummySystem2 );
        dummyECSModel.testMode( );
        dummyECSModel.initialize( );
        dummyECSModel.terminate( );
        assertTrue( "Condition was not true" , ! dummyECSModel.systems( ).get( DummySystem0.class ).isEnabled( ) && dummySystem0.terminated.getStatus( ) );
        assertTrue( "Condition was not true" , ! dummyECSModel.systems( ).get( DummySystem1.class ).isEnabled( ) && dummySystem1.terminated.getStatus( ) );
        assertTrue( "Condition was not true" , ! dummyECSModel.systems( ).get( DummySystem2.class ).isEnabled( ) && dummySystem2.terminated.getStatus( ) );
        assertFalse( "Condition was not false" , dummyECSModel.isUpdating( ) );
    }

    /**
     * Tests whether the method to perform updates for the model works correctly and all added systems are updated as well. Also tests that the update flag is reset after the operation
     */
    public void testPerform( )
    {
        DummyECSModel dummyECSModel = new DummyECSModel( );
        DummySystem0 dummySystem0 = new DummySystem0( );
        DummySystem1 dummySystem1 = new DummySystem1( );
        DummySystem2 dummySystem2 = new DummySystem2( );
        dummyECSModel.testAdd( dummySystem0 , dummySystem1 , dummySystem2 );
        dummyECSModel.testMode( );
        dummyECSModel.initialize( );
        dummyECSModel.perform( );
        assertTrue( "Condition was not true" , dummySystem0.performed.getStatus( ) );
        assertTrue( "Condition was not true" , dummySystem1.performed.getStatus( ) );
        assertTrue( "Condition was not true" , dummySystem2.performed.getStatus( ) );
        assertFalse( "Condition was not false" , dummyECSModel.isUpdating( ) );
    }

    /**
     * Dummy ecs model class
     */
    private static class DummyECSModel extends ECSModel
    {

        public void testAdd( GameSystem... gameSystems )
        {
            internalAdd( this , gameSystems );
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
     * Dummy system
     */
    private static class DummySystem0 extends GameSystem
    {

        final Status initialized = new Status( false );

        Status terminated = new Status( false );

        Status performed = new Status( false );

        public DummySystem0( )
        {
            super( null );
        }

        @Override
        protected void internalInitialize( )
        {
            initialized.setStatus( true );
        }

        @Override
        protected void internalTerminate( )
        {
            terminated = new Status( true );
        }

        @Override
        protected void internalPerform( )
        {
            performed = new Status( true );
        }

    }

    /**
     * Dummy system
     */
    private static class DummySystem1 extends GameSystem
    {

        final Status initialized = new Status( false );

        Status terminated = new Status( false );

        Status performed = new Status( false );

        public DummySystem1( )
        {
            super( null );
        }

        @Override
        protected void internalInitialize( )
        {
            initialized.setStatus( true );
        }

        @Override
        protected void internalTerminate( )
        {
            terminated = new Status( true );
        }

        @Override
        protected void internalPerform( )
        {
            performed = new Status( true );
        }

    }

    /**
     * Dummy system
     */
    private static class DummySystem2 extends GameSystem
    {

        final Status initialized = new Status( false );

        Status terminated = new Status( false );

        Status performed = new Status( false );

        public DummySystem2( )
        {
            super( null );
        }

        @Override
        protected void internalInitialize( )
        {
            initialized.setStatus( true );
        }

        @Override
        protected void internalTerminate( )
        {
            terminated = new Status( true );
        }

        @Override
        protected void internalPerform( )
        {
            performed = new Status( true );
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