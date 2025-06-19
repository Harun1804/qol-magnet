package com.galaxy.items.magnet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BasicMagnet extends BaseMagnet {
    private boolean magnetOn = false;

    public BasicMagnet(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            magnetOn = !magnetOn; // Toggle magnet state
            user.sendMessage(
                Text.literal("Magnet " + (magnetOn ? "ON" : "OFF")),
                true
            );
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        if (!world.isClient && magnetOn && entity instanceof PlayerEntity user) {
            // Attract items within a range of 5 blocks
            attractItemsWithAnimation(5.0, world, user);
        }
    }
}
