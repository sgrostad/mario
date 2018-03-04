package com.sgrostad.gfx;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontLoader {

    public static Font loadFont(String path, float size){
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(Font.PLAIN, size);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
