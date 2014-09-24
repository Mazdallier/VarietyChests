/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.varietychests.item;

import de.sanandrew.mods.varietychests.block.BlockCustomChest;
import de.sanandrew.mods.varietychests.util.ChestType;
import de.sanandrew.mods.varietychests.util.VarietyChests;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemBlockCustomChest
        extends ItemBlock
{
    public ItemBlockCustomChest(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public int getMetadata(int meta) {
        return 0;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOff, float yOff, float zOff) {
        int placingX = x;
        int placingY = y;
        int placingZ = z;

        Block block = world.getBlock(x, y, z);

        if( block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1 ) {
            side = 1;
        } else if( block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, z) ) {
            if( side == 0 ) {
                --placingY;
            }

            if( side == 1 ) {
                ++placingY;
            }

            if( side == 2 ) {
                --placingZ;
            }

            if( side == 3 ) {
                ++placingZ;
            }

            if( side == 4 ) {
                --placingX;
            }

            if( side == 5 ) {
                ++placingX;
            }
        }

        return this.canPlaceChestAt(world, placingX, placingY, placingZ, ChestType.getTypeFromItemStack(stack))
                && super.onItemUse(stack, player, world, x, y, z, side, xOff, yOff, zOff);
    }


    private boolean canPlaceChestAt(World world, int x, int y, int z, String type) {
        int typeCounter = 0;

        BlockCustomChest cChest = (BlockCustomChest) VarietyChests.customChest;

        if( cChest.getChestType(world, x - 1, y, z).equals(type) ) {
            ++typeCounter;
        }

        if( cChest.getChestType(world, x + 1, y, z).equals(type) ) {
            ++typeCounter;
        }

        if( cChest.getChestType(world, x, y, z - 1).equals(type) ) {
            ++typeCounter;
        }

        if( cChest.getChestType(world, x, y, z + 1).equals(type) ) {
            ++typeCounter;
        }

        return typeCounter <= 1
                && !this.hasAdjancentChest(world, x - 1, y, z, cChest, type)
                && !this.hasAdjancentChest(world, x + 1, y, z, cChest, type)
                && !this.hasAdjancentChest(world, x, y, z - 1, cChest, type)
                && !this.hasAdjancentChest(world, x, y, z + 1, cChest, type);
    }

    private boolean hasAdjancentChest(World world, int x, int y, int z, BlockCustomChest chest, String type) {
        return chest.getChestType(world, x, y, z).equals(type)
                && (chest.getChestType(world, x - 1, y, z).equals(type)
                    || chest.getChestType(world, x + 1, y, z).equals(type)
                    || chest.getChestType(world, x, y, z - 1).equals(type)
                    || chest.getChestType(world, x, y, z + 1).equals(type));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.field_150939_a.getUnlocalizedName() + "." + ChestType.getTypeFromItemStack(stack);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs tab, List stacks) {
        for( String s : ChestType.getTypeNames() ) {
            stacks.add(ChestType.getNewItemStackFromType(s, 1));
        }
    }
}
