package utility_library.gameplay.rendering.core;

import ecs_architecture.standard.GameComponent;
import utility_library.gameplay.rendering.api.SpriteContainer;
import utility_library.structures.Sprite;

public final class CharacterSpriteComponent extends GameComponent implements SpriteContainer
{

    private final SpriteProxy proxy;

    private CharacterSpriteComponent( SpriteProxy proxy ) { this.proxy = proxy; }

    public static CharacterSpriteComponent from( String spritesheet , int width , int height )
    {
        SpriteProxy proxy = SpriteProxy.from( spritesheet , width , height );
        return new CharacterSpriteComponent( proxy );
    }

    @Override
    public Sprite sprite( )
    {
        return proxy.sprite( );
    }

}