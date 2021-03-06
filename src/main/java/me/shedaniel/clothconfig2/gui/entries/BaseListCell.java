package me.shedaniel.clothconfig2.gui.entries;

import net.minecraft.client.gui.AbstractParentElement;

import java.util.Optional;

public abstract class BaseListCell extends AbstractParentElement {
    
    public abstract Optional<String> getError();
    
    public abstract int getCellHeight();
    
    public abstract void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta);
    
}