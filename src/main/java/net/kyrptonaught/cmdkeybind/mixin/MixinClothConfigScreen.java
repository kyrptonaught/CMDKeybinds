package net.kyrptonaught.cmdkeybind.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import net.kyrptonaught.cmdkeybind.config.ClothConfigInterface;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(ClothConfigScreen.class)
public abstract class MixinClothConfigScreen implements ClothConfigInterface {
    @Shadow
    private LinkedHashMap<String, List<AbstractConfigEntry>> tabbedEntries;

    @Override
    public void cmd_save() {
        Map<String, List<Pair<String, Object>>> map = Maps.newLinkedHashMap();
        tabbedEntries.forEach((s, abstractListEntries) -> {
            List list = abstractListEntries.stream().map((entry) -> new Pair(entry.getFieldName(), entry.getValue())).collect(Collectors.toList());
            map.put(s, list);
        });
        for (List<AbstractConfigEntry> abstractConfigEntries : Lists.newArrayList(tabbedEntries.values())) {
            for (Object o : abstractConfigEntries) {
                AbstractConfigEntry entry = (AbstractConfigEntry) o;
                entry.save();
            }
        }
        onSave(map);
    }

    @Shadow
    public void onSave(Map<String, List<Pair<String, Object>>> o) {
    }
}
