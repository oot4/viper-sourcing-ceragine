package utility_library.gameplay.rendering.core;

import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;
import render_engine.core.RenderLayer;
import utility_library.gameplay.kinetics.TranslationComponent;
import utility_library.structures.Sprite;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class SpriteRenderSystem extends GameSystem
{

    private Map< String, GameComponent > transforms;

    private Map< String, GameComponent > floorSprites;

    private Map< String, GameComponent > environmentSprites;

    private Map< String, GameComponent > characterSprites;

    private Map< String, GameComponent > userInterfaceSprites;

    private Map< String, GameComponent > overlaySprites;

    private EntityComparator entityComparator;

    private int offset( int index , int size , int modifier )
    {
        int offset;
        if ( size % 2 == 0 )
        {
            offset = size / 2;
        }
        else
        {
            offset = ( size - 1 ) / 2;
        }
        return ( index - offset ) * modifier;
    }

    private boolean validateSpriteGrid( Sprite sprite )
    {
        if ( sprite == null )
        {
            return false;
        }
        if ( sprite.width( ) <= 0 || sprite.height( ) <= 0 )
        {
            return false;
        }
        return sprite.spritesheet( ) != null && ! sprite.spritesheet( ).isBlank( );
    }

    private void internalDraw( Sprite sprite , TranslationComponent translationComponent )
    {
        if ( ! validateSpriteGrid( sprite ) )
        {
            return;
        }
        int bounds = sprite.tiles( ).length;
        int index = 0;
        while ( index < bounds )
        {
            double x = translationComponent.getX( );
            double y = translationComponent.getY( ) + 1;
            int modifier = RenderLayer.TILE_SIZE * sprite.scale( );
            x += sprite.offset( ).x;
            y += sprite.offset( ).y;
            x += offset( index % sprite.width( ) , sprite.width( ) , modifier );
            y += offset( index / sprite.width( ) , sprite.height( ) , modifier );
            draw( sprite.spritesheet( ) , x , y , sprite.tiles( )[ index ] , sprite.mirrored( )[ index ] , sprite.scale( ) , sprite.alpha( ) );
            index++;
        }
    }

    private void removeSpriteIfExists( String entity , boolean removeEnvironment , boolean removeCharacter , boolean removeUserInterface )
    {
        if ( removeEnvironment && environmentSprites.containsKey( entity ) )
        {
            remove( entity , EnvironmentSpriteComponent.class );
        }
        if ( removeCharacter && characterSprites.containsKey( entity ) )
        {
            remove( entity , CharacterSpriteComponent.class );
        }
        if ( removeUserInterface && userInterfaceSprites.containsKey( entity ) )
        {
            remove( entity , UserInterfaceSpriteComponent.class );
        }
        if ( overlaySprites.containsKey( entity ) )
        {
            remove( entity , OverlaySpriteComponent.class );
        }
    }

    private void drawFloor( )
    {
        List< String > entities = floorSprites.keySet( ).stream( ).filter( transforms::containsKey ).sorted( entityComparator ).collect( Collectors.toList( ) );
        for ( String entity : entities )
        {
            removeSpriteIfExists( entity , true , true , true );
            internalDraw( ( ( FloorSpriteComponent ) floorSprites.get( entity ) ).sprite( ) , ( TranslationComponent ) transforms.get( entity ) );
        }
    }

    private void drawEnvironment( )
    {
        List< String > entities = environmentSprites.keySet( ).stream( ).filter( transforms::containsKey ).sorted( entityComparator ).collect( Collectors.toList( ) );
        for ( String entity : entities )
        {
            removeSpriteIfExists( entity , false , true , true );
            internalDraw( ( ( EnvironmentSpriteComponent ) environmentSprites.get( entity ) ).sprite( ) , ( TranslationComponent ) transforms.get( entity ) );
        }
    }

    private void drawCharacter( )
    {
        List< String > entities = characterSprites.keySet( ).stream( ).filter( transforms::containsKey ).sorted( entityComparator ).collect( Collectors.toList( ) );
        for ( String entity : entities )
        {
            removeSpriteIfExists( entity , false , false , true );
            internalDraw( ( ( CharacterSpriteComponent ) characterSprites.get( entity ) ).sprite( ) , ( TranslationComponent ) transforms.get( entity ) );
        }
    }

    private void drawUserInterface( )
    {
        List< String > entities = userInterfaceSprites.keySet( ).stream( ).filter( transforms::containsKey ).sorted( entityComparator ).collect( Collectors.toList( ) );
        for ( String entity : entities )
        {
            removeSpriteIfExists( entity , false , false , false );
            internalDraw( ( ( UserInterfaceSpriteComponent ) userInterfaceSprites.get( entity ) ).sprite( ) , ( TranslationComponent ) transforms.get( entity ) );
        }
    }

    private void drawOverlay( )
    {
        List< String > entities = overlaySprites.keySet( ).stream( ).filter( transforms::containsKey ).sorted( entityComparator ).collect( Collectors.toList( ) );
        for ( String entity : entities )
        {
            internalDraw( ( ( OverlaySpriteComponent ) overlaySprites.get( entity ) ).sprite( ) , ( TranslationComponent ) transforms.get( entity ) );
        }
    }

    @Override
    protected void internalInitialize( )
    {
        entityComparator = new EntityComparator( );
        transforms = get( TranslationComponent.class );
        floorSprites = get( FloorSpriteComponent.class );
        environmentSprites = get( EnvironmentSpriteComponent.class );
        characterSprites = get( CharacterSpriteComponent.class );
        userInterfaceSprites = get( UserInterfaceSpriteComponent.class );
        overlaySprites = get( OverlaySpriteComponent.class );
    }

    @Override
    protected void internalTerminate( )
    {
        //Nothing to terminate
    }

    @Override
    protected void internalPerform( )
    {
        drawFloor( );
        drawEnvironment( );
        drawCharacter( );
        drawUserInterface( );
        drawOverlay( );
    }

    private class EntityComparator implements Comparator< String >
    {

        @Override
        public int compare( String o1 , String o2 )
        {
            TranslationComponent entity1 = ( TranslationComponent ) get( TranslationComponent.class ).get( o1 );
            TranslationComponent entity2 = ( TranslationComponent ) get( TranslationComponent.class ).get( o2 );
            if ( Math.ceil( entity1.getY( ) ) > Math.ceil( entity2.getY( ) ) )
            {
                if ( Math.floor( entity1.getX( ) ) > Math.ceil( entity2.getX( ) ) )
                {
                    return 1;
                }
                return 2;
            }
            if ( entity1.getY( ) < entity2.getY( ) )
            {
                if ( entity1.getX( ) < entity2.getX( ) )
                {
                    return - 2;
                }

                return - 1;
            }
            return 0;
        }

    }

}