package net.complex.storage.network.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
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

    @Override
    protected void init() {
        this.addCloseButton();
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta){
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(MatrixStack matrices, int vOffset) {
        super.renderBackground(matrices, vOffset);
    }

    protected void addCloseButton() {
        this.addButton(new ButtonWidget(this.width / 2 - 100, 196, 200, 20, ScreenTexts.DONE, (buttonWidget) -> {
           this.client.openScreen((Screen)null);
        }));
    }
}