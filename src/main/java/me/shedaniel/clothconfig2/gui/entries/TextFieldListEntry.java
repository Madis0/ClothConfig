package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.Lists;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class TextFieldListEntry<T> extends TooltipListEntry<T> {
    
    protected TextFieldWidget textFieldWidget;
    protected Button resetButton;
    protected Supplier<T> defaultValue;
    protected T original;
    protected List<IGuiEventListener> widgets;
    
    protected TextFieldListEntry(String fieldName, T original, String resetButtonKey, Supplier<T> defaultValue) {
        this(fieldName, original, resetButtonKey, defaultValue, null);
    }
    
    protected TextFieldListEntry(String fieldName, T original, String resetButtonKey, Supplier<T> defaultValue, Supplier<Optional<String[]>> tooltipSupplier) {
        super(fieldName, tooltipSupplier);
        this.defaultValue = defaultValue;
        this.original = original;
        this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 148, 18, "") {
            @Override
            public void render(int int_1, int int_2, float float_1) {
                boolean f = isFocused();
                setFocused(TextFieldListEntry.this.getParent().getFocused() == TextFieldListEntry.this && TextFieldListEntry.this.getFocused() == this);
                textFieldPreRender(this);
                super.render(int_1, int_2, float_1);
                setFocused(f);
            }
            
            @Override
            public void writeText(String string_1) {
                super.writeText(stripAddText(string_1));
            }
        };
        textFieldWidget.setMaxStringLength(999999);
        textFieldWidget.setText(String.valueOf(original));
        textFieldWidget.func_212954_a(s -> {
            if (!original.equals(s))
                getScreen().setEdited(true);
        });
        this.resetButton = new Button(0, 0, Minecraft.getInstance().fontRenderer.getStringWidth(I18n.format(resetButtonKey)) + 6, 20, I18n.format(resetButtonKey), widget -> {
            TextFieldListEntry.this.textFieldWidget.setText(String.valueOf(defaultValue.get()));
            getScreen().setEdited(true);
        });
        this.widgets = Lists.newArrayList(textFieldWidget, resetButton);
    }
    
    protected static void setTextFieldWidth(TextFieldWidget widget, int width) {
        widget.setWidth(width);
    }
    
    protected String stripAddText(String s) {
        return s;
    }
    
    protected void textFieldPreRender(TextFieldWidget widget) {
    
    }
    
    @Override
    public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        super.render(index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);
        MainWindow window = Minecraft.getInstance().mainWindow;
        this.resetButton.active = isEditable() && getDefaultValue().isPresent() && !isMatchDefault(textFieldWidget.getText());
        this.resetButton.y = y;
        this.textFieldWidget.setEnabled(isEditable());
        this.textFieldWidget.y = y + 1;
        if (Minecraft.getInstance().fontRenderer.getBidiFlag()) {
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(I18n.format(getFieldName()), window.getScaledWidth() - x - Minecraft.getInstance().fontRenderer.getStringWidth(I18n.format(getFieldName())), y + 5, 16777215);
            this.resetButton.x = x;
            this.textFieldWidget.x = x + resetButton.getWidth();
            setTextFieldWidth(textFieldWidget, 148 - resetButton.getWidth() - 4);
        } else {
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(I18n.format(getFieldName()), x, y + 5, 16777215);
            this.resetButton.x = x + entryWidth - resetButton.getWidth();
            this.textFieldWidget.x = x + entryWidth - 148;
            setTextFieldWidth(textFieldWidget, 148 - resetButton.getWidth() - 4);
        }
        resetButton.render(mouseX, mouseY, delta);
        textFieldWidget.render(mouseX, mouseY, delta);
    }
    
    protected abstract boolean isMatchDefault(String text);
    
    @Override
    public Optional<T> getDefaultValue() {
        return defaultValue == null ? Optional.empty() : Optional.ofNullable(defaultValue.get());
    }
    
    @Override
    public List<? extends IGuiEventListener> children() {
        return widgets;
    }
    
}
