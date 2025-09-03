package com.yungnickyoung.minecraft.betterdeserttemples.mixin.pharaoh;

import com.yungnickyoung.minecraft.betterdeserttemples.entity.IPharaohData;
import com.yungnickyoung.minecraft.betterdeserttemples.util.PharaohUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(Zombie.class)
public class ZombieMixin {
    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void betterdeserttemples_readPharaohOriginalSpawnPosFromNbt(ValueInput valueInput, CallbackInfo ci) {
        if (PharaohUtil.isPharaoh(this)) {
            List<ValueInput> originalSpawnPos = valueInput.childrenListOrEmpty("bdtOriginalSpawnPos").stream().toList();
            Optional<Vec3> vec3 = valueInput.read("bdtOriginalSpawnPos", Vec3.CODEC);
            if (vec3.isEmpty()) {
//                BetterDesertTemplesCommon.LOGGER.error("Pharaoh entity is missing original spawn position data. Unable to read original spawn position.");
                return;
            }
            ((IPharaohData) this).setOriginalSpawnPos(vec3.get());
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void betterdeserttemples_writePharaohOriginalSpawnPosToNbt(ValueOutput valueOutput, CallbackInfo ci) {
        if (PharaohUtil.isPharaoh(this)) {
            Vec3 originalSpawnPos = ((IPharaohData) this).getOriginalSpawnPos();

            if (originalSpawnPos == null) {
//                BetterDesertTemplesCommon.LOGGER.error("Pharaoh entity is missing original spawn position data. Unable to write original spawn position to NBT.");
                return;
            }

            valueOutput.store("bdtOriginalSpawnPos", Vec3.CODEC, originalSpawnPos);
        }
    }
}
