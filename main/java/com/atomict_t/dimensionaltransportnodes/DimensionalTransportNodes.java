package com.atomict_t.dimensionaltransportnodes;

import com.atomict_t.dimensionaltransportnodes.init.ModBlocks;
import com.atomict_t.dimensionaltransportnodes.init.ModItems;
import com.atomict_t.dimensionaltransportnodes.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Config.modId, name = Config.modName, version = Config.version, acceptedMinecraftVersions = Config.acceptedMinecraftVersions)
public class DimensionalTransportNodes {
	
	@Mod.Instance(Config.modId)
	public static DimensionalTransportNodes instance;
	
	@SidedProxy(clientSide = Config.clientProxy, serverSide = Config.commonProxy)
	public static CommonProxy proxy;	
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		System.out.println(Config.modName + " is loading!");
		
		DTNPacketHandler.registerMessages(Config.modId);
		
		ModItems.init();
		ModBlocks.init();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
