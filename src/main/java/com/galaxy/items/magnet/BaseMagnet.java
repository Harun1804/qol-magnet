package com.galaxy.items.magnet;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

public class BaseMagnet extends Item {
    public BaseMagnet(Settings settings) {
        super(settings);
    }

    public void attractItemsWithAnimation(double range, World world, PlayerEntity user) {
        List<ItemEntity> items = world.getEntitiesByClass(
                ItemEntity.class,
                user.getBoundingBox().expand(range),
                item -> item.isAlive() && item.getStack() != null
        );
        for (ItemEntity item : items) {
            double dx = user.getX() - item.getX();
            double dy = user.getY() + 1.0 - item.getY();
            double dz = user.getZ() - item.getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double speed = 0.45; // Increase speed for smoother movement
            if (dist > 0.1) {
                // Set velocity directly towards the player for smooth movement
                item.setVelocity(
                        (dx / dist) * speed,
                        (dy / dist) * speed,
                        (dz / dist) * speed
                );
            }
            if (dist < 1.5) {
                if (user.getInventory().insertStack(item.getStack().copy())) {
                    item.discard();
                    world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS);
                }
            }
        }
    }
}
