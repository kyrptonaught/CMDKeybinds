package net.kyrptonaught.cmdkeybind.config.clothconfig;

import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ButtonEntry extends TooltipListEntry<String> {

    private ButtonWidget buttonWidget, resetButton;
    private List<Element> widgets;

    public ButtonEntry(Text fieldName, Consumer<ButtonEntry> click) {
        super(fieldName, null, false);
        this.buttonWidget = new ButtonWidget(0, 0, 150, 20, fieldName, widget -> {
            click.accept(this);
        });
        this.resetButton = new ButtonWidget(0, 0, MinecraftClient.getInstance().textRenderer.getWidth(new TranslatableText("text.cloth-config.reset_value")) + 6, 20, new TranslatableText("text.cloth-config.reset_value"), widget -> {
            getConfigScreen().setEdited(true);
        });

        this.widgets = Lists.newArrayList(buttonWidget, resetButton);
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);
        Window window = MinecraftClient.getInstance().getWindow();
        this.buttonWidget.active = isEditable();
        this.buttonWidget.y = y;

        if (MinecraftClient.getInstance().textRenderer.isRightToLeft()) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, getFieldName(), window.getScaledWidth() - x - MinecraftClient.getInstance().textRenderer.getWidth(getFieldName()), y + 5, 16777215);

            this.buttonWidget.x = x + resetButton.getWidth() + 2;
            this.buttonWidget.setWidth(150 - resetButton.getWidth() - 2);
        } else {
            MinecraftClient.getInstance().textRenderer.draw(matrices, getFieldName(), x, y + 5, 16777215);
            this.buttonWidget.x = x + entryWidth - 150;
            this.buttonWidget.setWidth(150 - resetButton.getWidth() - 2);
        }
        buttonWidget.render(matrices, mouseX, mouseY, delta);
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