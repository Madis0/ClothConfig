package me.shedaniel.clothconfig2.gui.entries;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FloatListListEntry extends BaseListEntry<Float, FloatListListEntry.FloatListCell> {
    
    private float minimum, maximum;
    
    @Deprecated
    public FloatListListEntry(String fieldName, List<Float> value, boolean defaultExpended, Supplier<Optional<String[]>> tooltipSupplier, Consumer<List<Float>> saveConsumer, Supplier<List<Float>> defaultValue, String resetButtonKey) {
        super(fieldName, tooltipSupplier, defaultValue, baseListEntry -> new FloatListCell(0, (FloatListListEntry) baseListEntry), saveConsumer, resetButtonKey);
        this.minimum = -Float.MAX_VALUE;
        this.maximum = Float.MAX_VALUE;
        for(float f : value)
            cells.add(new FloatListCell(f, this));
        this.widgets.addAll(cells);
        expended = defaultExpended;
    }
    
    @Override
    public List<Float> getValue() {
        return cells.stream().map(cell -> Float.valueOf(cell.widget.getText())).collect(Collectors.toList());
    }
    
    public FloatListListEntry setMaximum(float maximum) {
        this.maximum = maximum;
        return this;
    }
    
    public FloatListListEntry setMinimum(float minimum) {
        this.minimum = minimum;
        return this;
    }
    
    @Override
    protected FloatListCell getFromValue(Float value) {
        return new FloatListCell(value, this);
    }
    
    public static class FloatListCell extends BaseListCell {
        
        private Function<String, String> stripCharacters = s -> {
            StringBuilder stringBuilder_1 = new StringBuilder();
            char[] var2 = s.toCharArray();
            int var3 = var2.length;
            
            for(int var4 = 0; var4 < var3; ++var4)
                if (Character.isDigit(var2[var4]) || var2[var4] == '-' || var2[var4] == '.')
                    stringBuilder_1.append(var2[var4]);
            
            return stringBuilder_1.toString();
        };
        private TextFieldWidget widget;
        private boolean isSelected;
        private FloatListListEntry listListEntry;
        
        public FloatListCell(float value, FloatListListEntry listListEntry) {
            this.listListEntry = listListEntry;
            widget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 100, 18, "") {
                @Override
                public void render(int int_1, int int_2, float float_1) {
                    boolean f = isFocused();
                    setFocused(isSelected);
                    try {
                        float i = Float.valueOf(getText());
                        if (i < listListEntry.minimum || i > listListEntry.maximum)
                            widget.setEditableColor(16733525);
                        else
                            widget.setEditableColor(14737632);
                    } catch (NumberFormatException ex) {
                        widget.setEditableColor(16733525);
                    }
                    super.render(int_1, int_2, float_1);
                    setFocused(f);
                }
                
                @Override
                public void addText(String string_1) {
                    super.addText(stripCharacters.apply(string_1));
                }
            };
            widget.setMaxLength(999999);
            widget.setHasBorder(false);
            widget.setText(value + "");
            widget.setChangedListener(s -> {
                if (!(value + "").equalsIgnoreCase(s))
                    listListEntry.getScreen().setEdited(true);
            });
        }
        
        @Override
        public Optional<String> getError() {
            try {
                float i = Float.valueOf(widget.getText());
                if (i > listListEntry.maximum)
                    return Optional.of(I18n.translate("text.cloth-config.error.too_large", listListEntry.maximum));
                else if (i < listListEntry.minimum)
                    return Optional.of(I18n.translate("text.cloth-config.error.too_small", listListEntry.minimum));
            } catch (NumberFormatException ex) {
                return Optional.of(I18n.translate("text.cloth-config.error.not_valid_number_float"));
            }
            return Optional.empty();
        }
        
        @Override
        public int getCellHeight() {
            return 20;
        }
        
        @Override
        public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            widget.setWidth(entryWidth - 12);
            widget.x = x;
            widget.y = y + 1;
            widget.setIsEditable(listListEntry.isEditable());
            this.isSelected = isSelected;
            widget.render(mouseX, mouseY, delta);
            if (isSelected && listListEntry.isEditable())
                fill(x, y + 12, x + entryWidth - 12, y + 13, getError().isPresent() ? 0xffff5555 : 0xffe0e0e0);
        }
        
        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(widget);
        }
        
    }
    
}
