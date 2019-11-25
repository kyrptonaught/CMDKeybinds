package net.kyrptonaught.cmdkeybind.config.clothconfig;

import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ButtonEntry extends TooltipListEntry<String> {

    private ButtonWidget buttonWidget, resetButton;
    private List<Element> widgets;

    public ButtonEntry(String fieldName, Consumer<ButtonEntry> click) {
        super(fieldName, null, false);
        this.buttonWidget = new ButtonWidget(0, 0, 150, 20, I18n.translate(fieldName), widget -> {
            click.accept(this);
        });
        this.resetButton = new ButtonWidget(0, 0, MinecraftClient.getInstance().textRenderer.getStringWidth(I18n.translate("text.cloth-config.reset_value")) + 6, 20, I18n.translate("text.cloth-config.reset_value"), widget -> {
            getScreen().setEdited(true, isRequiresRestart());
        });

        this.widgets = Lists.newArrayList(buttonWidget, resetButton);
    }

    @Override
    public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        super.render(index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);
        Window window = MinecraftClient.getInstance().getWindow();
        this.buttonWidget.active = isEditable();
        this.buttonWidget.y = y;

        if (MinecraftClient.getInstance().textRenderer.isRightToLeft()) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(I18n.translate(getFieldName()), window.getScaledWidth() - x - MinecraftClient.getInstance().textRenderer.getStringWidth(I18n.translate(getFieldName())), y + 5, 16777215);

            this.buttonWidget.x = x + resetButton.getWidth() + 2;
            this.buttonWidget.setWidth(150 - resetButton.getWidth() - 2);
        } else {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(I18n.translate(getFieldName()), x, y + 5, 16777215);
            this.buttonWidget.x = x + entryWidth - 150;
            this.buttonWidget.setWidth(150 - resetButton.getWidth() - 2);
        }
        buttonWidget.render(mouseX, mouseY, delta);
    }

    @Override
    public String getValue() {
        return "YE";
    }

    @Override
    public Optional<String> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public void save() {

    }

    @Override
    public List<? extends Element> children() {
        return widgets;
    }
}