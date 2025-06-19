package com.galaxy.items.magnet;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseMagnet extends Item {
    private final Set<Item> blacklist = new HashSet<>();
    private final Set<Item> whitelist = new HashSet<>();
    private boolean useWhitelist = false;

    public BaseMagnet(Settings settings) {
        super(settings);
    }

    public void addToBlacklist(Item item) {
        blacklist.add(item);
    }

    public void addToWhitelist(Item item) {
        whitelist.add(item);
    }

    public void setUseWhitelist(boolean useWhitelist) {
        this.useWhitelist = useWhitelist;
    }

    private boolean isAllowed(Item item) {
        if (useWhitelist) {
            return whitelist.contains(item);
        } else {
            return !blacklist.contains(item);
        }
    }

    public void attractItemsWithAnimation(double range, World world, PlayerEntity user) {
        List<ItemEntity> items = world.getEntitiesByClass(
                ItemEntity.class,
                user.getBoundingBox().expand(range),
                item -> item.isAlive() && item.getStack() != null && isAllowed(item.getStack().getItem())
        );
        for (ItemEntity item : items) {
            double dx = user.getX() - item.getX();
            double dy = user.getY() + 1.0 - item.getY();
            double dz = user.getZ() - item.getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double speed = 0.45;
            if (dist > 0.1) {
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
