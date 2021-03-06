package me.shedaniel.clothconfig2.api;

import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;

import java.util.Optional;

public abstract class AbstractConfigEntry<T> extends DynamicElementListWidget.ElementEntry<AbstractConfigEntry<T>> {
    private ClothConfigScreen screen;
    
    public abstract String getFieldName();
    
    public abstract T getValue();
    
    public Optional<String> getError() {
        return Optional.empty();
    }
    
    public abstract Optional<T> getDefaultValue();
    
    public final ClothConfigScreen.ListWidget getParent() {
        return screen.listWidget;
    }
    
    public final ClothConfigScreen getScreen() {
        return screen;
    }
    
    @Deprecated
    public final void setScreen(ClothConfigScreen screen) {
        this.screen = screen;
    }
    
    public abstract void save();
    
    @Override
    public int getItemHeight() {
        return 24;
    }
}
