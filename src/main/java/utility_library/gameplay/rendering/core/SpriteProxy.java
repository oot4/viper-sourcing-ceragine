package utility_library.gameplay.rendering.core;

import utility_library.gameplay.rendering.api.SpriteContainer;
import utility_library.structures.Sprite;

class SpriteProxy implements SpriteContainer
{

    private final Sprite sprite;

    private SpriteProxy( Sprite sprite )
    {
        this.sprite = sprite;
    }

    public static SpriteProxy from( String spritesheet , int width , int height )
    {
        Sprite sprite = Sprite.from( spritesheet , width , height );
        return new SpriteProxy( sprite );
    }

    @Override
    public Sprite sprite( )
    {
        return sprite;
    }

}