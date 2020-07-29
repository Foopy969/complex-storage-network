package net.complex.storage.network.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class TestScreen extends Screen {

    public TestScreen(String title) {
        super(new LiteralText(title));
    }
    
    @Override
    public boolean isPauseScreen() { 
        return false; 
    }

}