package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeGuiWrapper;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeSideSettings;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GuiElement;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GEText;
import com.atomict_t.dimensionaltransportnodes.utils.__;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class InOutFilterToggle extends GuiElement {
	GEText inButton = new GEText(this, "Input");
	GEText outButton = new GEText(this, "Output");
	
	public int activeFilter = -1;
	public boolean activeFilterChanged = false;
	
	public InOutFilterToggle(GuiElement parent) {
		super(parent);
		
		inButton.setColor(0x00aa00, 0x00ee00, 0x00ff00);
		outButton.setColor(0xaa0000, 0xee0000, 0xff5555);
		
		this.y = 20;
		this.height = inButton.height;
		
		int face = DTNodeGuiWrapper.fBar.activeFace;
		if(face >= 0){
			DTNodeSideSettings current = ((DTNodeTileEntity)te).getSide(face);
			if(current != null && current.isInput && current.isOutput)
				activeFilter = 0;
		}
//		if(sideSettings.isInput){
//			inButton.active = true;
//			activeFilter = 0;
//		} else if(sideSettings.isOutput){
//			outButton.active = true;
//			activeFilter = 1;
//		}
	}
	
	@Override
	public void render(Minecraft mc, int mouseX, int mouseY) {
		DTNodeSideSettings sideSettings = ((DTNodeTileEntity)te).getSide(DTNodeGuiWrapper.fBar.activeFace);
		
		if(!sideSettings.isOutput)
			activeFilter = 0;
		if(!sideSettings.isInput)
			activeFilter = 1;
		
		if(activeFilter == 0){
			inButton.active = true;
			outButton.active = false;
		} else if(activeFilter == 1){
			inButton.active = false;
			outButton.active = true;
		} else {
			inButton.active = false;
			outButton.active = false;
		}
		
		if(sideSettings.isInput){
			inButton.render(mc, mouseX, mouseY);
		} else {
			outButton.x = 0;
		}
		
		if(sideSettings.isInput && sideSettings.isOutput){
			mc.fontRendererObj.drawString("/", getX() + inButton.width, getY(), 0x000000);
			outButton.x = inButton.width + mc.fontRendererObj.getCharWidth('/');
		}
		
		if(sideSettings.isOutput){
			outButton.render(mc, mouseX, mouseY);
		}
//		if(mouseOver(mouseX, mouseY))
//			mc.fontRendererObj.drawString("Click to toggle", mouseX + 10, mouseY - 10, 0xffffff);
        GlStateManager.color(1,1,1,1);
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(inButton.mouseClicked(mouseX, mouseY, mouseButton)){
			activeFilter = 0;
			activeFilterChanged = true;
			return true;
		}
		if(outButton.mouseClicked(mouseX, mouseY, mouseButton)){
			activeFilter = 1;
			activeFilterChanged = true;
			return true;
		}
		return false;
	}
}
