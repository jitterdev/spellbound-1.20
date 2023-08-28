package net.tigereye.spellbound.registration;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.tigereye.spellbound.Spellbound;

public class SBDamageSource {
    public static final RegistryKey<DamageType> SB_INFIDELITY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Spellbound.MODID, "sbInfidelity"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }
}