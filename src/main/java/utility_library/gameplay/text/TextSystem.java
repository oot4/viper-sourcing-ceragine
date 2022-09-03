package utility_library.gameplay.text;

import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;
import utility_library.gameplay.rendering.api.SpriteContainer;
import utility_library.gameplay.rendering.core.UserInterfaceSpriteComponent;

import java.util.Arrays;
import java.util.Map;

public final class TextSystem extends GameSystem
{

    private boolean equals( int[] a , int[] b )
    {
        return Arrays.equals( a , b );
    }

    private void addNewTextSprite( String entity , TextComponent component )
    {
        UserInterfaceSpriteComponent text = UserInterfaceSpriteComponent.from( component.fontSpritesheet( ) , component.tiles( ).length , 1 );
        text.sprite( ).replaceTiles( component.tiles( ) );
        text.sprite( ).setScale( component.scale( ) );
        add( entity , text );
    }

    @Override
    protected void internalInitialize( )
    {
        //Nothing to initialize
    }

    @Override
    protected void internalTerminate( )
    {
        //Nothing to terminate
    }

    @Override
    protected void internalPerform( )
    {
        Map< String, GameComponent > messages = get( TextComponent.class );
        Map< String, GameComponent > sprites = get( UserInterfaceSpriteComponent.class );
        messages.forEach( ( entity , gameComponent ) -> {
            TextComponent text = ( TextComponent ) gameComponent;
            if ( sprites.containsKey( entity ) )
            {
                SpriteContainer container = ( UserInterfaceSpriteComponent ) sprites.get( entity );
                if ( ! equals( container.sprite( ).tiles( ) , text.tiles( ) ) )
                {
                    remove( entity , UserInterfaceSpriteComponent.class );
                    addNewTextSprite( entity , text );
                }
            }
            else
            {
                addNewTextSprite( entity , text );
            }
        } );
    }

}