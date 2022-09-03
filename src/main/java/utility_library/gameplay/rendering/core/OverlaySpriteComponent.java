package utility_library.gameplay.rendering.core;

import ecs_architecture.standard.GameComponent;
import utility_library.gameplay.rendering.api.SpriteContainer;
import utility_library.structures.Sprite;

public final class OverlaySpriteComponent extends GameComponent implements SpriteContainer
{

    private final SpriteProxy proxy;

    private OverlaySpriteComponent( SpriteProxy proxy ) { this.proxy = proxy; }

    public static OverlaySpriteComponent from( String spritesheet , int width , int height )
    {
        SpriteProxy proxy = SpriteProxy.from( spritesheet , width , height );
        return new OverlaySpriteComponent( proxy );
    }

    @Override
    public Sprite sprite( )
    {
        return proxy.sprite( );
    }

}