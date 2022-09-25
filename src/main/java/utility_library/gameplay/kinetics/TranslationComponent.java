package utility_library.gameplay.kinetics;

import ceragine.Game;
import ecs_architecture.standard.GameComponent;
import utility_library.algorithms.core.Clamp;

import java.awt.geom.Point2D;

public final class TranslationComponent extends GameComponent
{

    private final Point2D lastLocation;

    private final Point2D location;

    private final Point2D speed;

    private TranslationComponent( String name )
    {
        super( name );
        this.location = new Point2D.Double( );
        this.speed = new Point2D.Double( );
        lastLocation = new Point2D.Double( );
    }

    private TranslationComponent( Point2D location , Point2D speed )
    {
        this.location = location;
        this.speed = speed;
        lastLocation = new Point2D.Double( location.getX( ) , location.getY( ) );
    }

    public static TranslationComponent from( String name )
    {
        return new TranslationComponent( name );
    }

    public static TranslationComponent from( int x , int y )
    {
        return from( x , y , 0.0D , 0.0D );
    }

    public static TranslationComponent from( int x , int y , double sx , double sy )
    {
        Point2D location = new Point2D.Double( x , y );
        Point2D speed = new Point2D.Double( sx , sy );
        return new TranslationComponent( location , speed );
    }

    public double getX( )
    {
        return location.getX( );
    }

    public void setX( double x )
    {
        double px = location.getX( );
        double py = location.getY( );
        lastLocation.setLocation( px , py );
        location.setLocation( x , py );
    }

    public double getY( )
    {
        return location.getY( );
    }

    public void setY( double y )
    {
        double px = location.getX( );
        double py = location.getY( );
        lastLocation.setLocation( px , py );
        location.setLocation( px , y );
    }

    public double getLastX( )
    {
        return lastLocation.getX( );
    }

    public double getLastY( )
    {
        return lastLocation.getY( );
    }

    public double getSX( )
    {
        return speed.getX( );
    }

    public void setSX( double sx )
    {
        speed.setLocation( sx , speed.getY( ) );
    }

    public double getSY( )
    {
        return speed.getY( );
    }

    public void setSY( double vy )
    {
        speed.setLocation( speed.getX( ) , vy );
    }

    public void setLocation( double x , double y )
    {
        double px = location.getX( );
        double py = location.getY( );
        lastLocation.setLocation( px , py );
        location.setLocation( x , y );
    }

    public void setSpeed( double sx , double sy )
    {
        double speedX = Clamp.toDouble( sx , - 100.0D , 100.0D );
        double speedY = Clamp.toDouble( sy , - 100.0D , 100.0D );
        speed.setLocation( speedX * 10.0D , speedY * 10.0D );
    }

    private double vx( )
    {
        double speedX = Math.sqrt( speed.getX( ) * speed.getX( ) );
        double velocity = Math.ceil( speedX * Game.delta( ) );
        if ( speed.getX( ) < 0 )
        {
            return velocity * ( - 1.0D );
        }
        return velocity;
    }

    private double vy( )
    {
        double speedY = Math.sqrt( speed.getY( ) * speed.getY( ) );
        double velocity = Math.ceil( speedY * Game.delta( ) );
        if ( speed.getY( ) < 0 )
        {
            return velocity * ( - 1.0D );
        }
        return velocity;
    }

    public void moveLeft( )
    {
        double x = location.getX( ) - vx( );
        setX( x );
    }

    public void moveRight( )
    {
        double x = location.getX( ) + vx( );
        setX( x );
    }

    public void moveDown( )
    {
        double y = location.getY( ) - vy( );
        setY( y );
    }

    public void moveUp( )
    {
        double y = location.getY( ) + vy( );
        setY( y );
    }

}