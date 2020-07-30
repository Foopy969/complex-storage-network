package net.complex.storage.network.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.LiteralText;

public class TestScreen extends Screen {

    public TestScreen() {
        super(new LiteralText("Title"));
    }
    
    @Override
    public boolean isPauseScreen() { 
        return false; 
    }

    protected void init() {
        this.addCloseButton();
    }
    
    public void render(){
        
    }

    protected void addCloseButton() {
        this.addButton(new ButtonWidget(this.width / 2 - 100, 196, 200, 20, ScreenTexts.DONE, (buttonWidget) -> {
           this.client.openScreen((Screen)null);
        }));
    }
}