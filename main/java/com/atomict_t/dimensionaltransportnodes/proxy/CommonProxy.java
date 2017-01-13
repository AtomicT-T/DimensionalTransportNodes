package com.atomict_t.dimensionaltransportnodes.proxy;

import com.atomict_t.dimensionaltransportnodes.DTNGuiHandler;
import com.atomict_t.dimensionaltransportnodes.DimensionalTransportNodes;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void init(FMLInitializationEvent e) {
	    NetworkRegistry.INSTANCE.registerGuiHandler(DimensionalTransportNodes.instance, new DTNGuiHandler());
	}
	
	public void registerItemRenderer(Item itemBase, int i, String name) {
	}

}
