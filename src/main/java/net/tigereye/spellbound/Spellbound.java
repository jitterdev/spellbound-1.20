package net.tigereye.spellbound;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.item.ItemGroups;
import net.minecraft.resource.ResourceType;
import net.tigereye.spellbound.config.SBConfig;
import net.tigereye.spellbound.data.ProspectorManager;
import net.tigereye.spellbound.registration.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;


public class Spellbound implements ModInitializer{
    
    public static final String MODID = "spellbound";
    public static final boolean DEBUG = false;
    public static final Logger LOGGER = LogManager.getLogger();
    public static SBConfig config;

    @Override
    public void onInitialize() {
        AutoConfig.register(SBConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(SBConfig.class).getConfig();

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ProspectorManager());

        SBItems.register();
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((content) -> {
            content.add(SBItems.BAG_OF_ROCKS);
            content.add(SBItems.BAG_OF_TROPHIES);
            content.add(SBItems.IRON_PEBBLE);
            content.add(SBItems.COPPER_PEBBLE);
            content.add(SBItems.GOLD_PEBBLE);
            content.add(SBItems.DIAMOND_SHARD);
            content.add(SBItems.EMERALD_SHARD);
            //content.add(SBItems.ANCIENT_SHARD);
        });
        SBEnchantments.register();
        SBStatusEffects.register();
        SBTags.register();
        SBNetworking.register();
    }
}