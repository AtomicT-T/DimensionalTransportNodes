package com.atomict_t.dimensionaltransportnodes.proxy;

import com.atomict_t.dimensionaltransportnodes.Config;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		System.out.println(Config.modId + ":" + id);
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Config.modId + ":" + id, "inventory"));
	}
}
