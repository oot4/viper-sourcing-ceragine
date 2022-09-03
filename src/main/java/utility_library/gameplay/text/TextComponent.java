package utility_library.gameplay.text;

import ecs_architecture.standard.GameComponent;

import java.util.Arrays;

public final class TextComponent extends GameComponent
{

    private static final String CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ():;[]abcdefghijklmnopqrstuvwxyz|/,.$#0123456789'-?!*%>+^ ";

    private int scale;

    private int[] tiles;

    private String fontSpritesheet;

    private TextComponent( String fontSpritesheet , int scale , int[] tiles )
    {
        this.fontSpritesheet = fontSpritesheet;
        this.scale = scale;
        this.tiles = tiles;
    }

    private static int[] toIntArray( String text )
    {
        int[] characterArray = new int[ text.length( ) ];
        Arrays.setAll( characterArray , i -> CHARACTER_SET.indexOf( text.charAt( i ) ) );
        return characterArray;
    }

    public static TextComponent from( String text , String fontSpritesheet , int scale )
    {
        int[] characterArray = toIntArray( text );
        return new TextComponent( fontSpritesheet , scale , characterArray );
    }

    int scale( )
    {
        return scale;
    }

    public void setScale( int scale )
    {
        this.scale = scale;
    }

    int[] tiles( )
    {
        return tiles;
    }

    public void setText( String text )
    {
        tiles = toIntArray( text );
    }

    String fontSpritesheet( )
    {
        return fontSpritesheet;
    }

    public void setFontSpritesheet( String fontSpritesheet )
    {
        this.fontSpritesheet = fontSpritesheet;
    }

}