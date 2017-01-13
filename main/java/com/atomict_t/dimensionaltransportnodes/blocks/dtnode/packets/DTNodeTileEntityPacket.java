package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.packets;

import java.io.IOException;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;
import com.jcraft.jorbis.Block;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DTNodeTileEntityPacket implements IMessage {

	public NBTTagCompound nbt = null;
	public BlockPos pos = null;

	
	public DTNodeTileEntityPacket() {
	}
	
	public DTNodeTileEntityPacket(BlockPos pos, NBTTagCompound nbt) {
		this.pos = pos;
		this.nbt = nbt;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		
		int nbtlen = buf.readInt();
		byte[] nbtByteArray = buf.readBytes(nbtlen).array();
		try {
			this.nbt = (NBTTagCompound)JsonToNBT.getTagFromJson(new String(nbtByteArray));
		} catch (NBTException e) {
			e.printStackTrace();
		}
		
/*		try {
			this.nbt = ((PacketBuffer)buf).readNBTTagCompoundFromBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		
		String s = nbt.toString();
		byte[] ca = s.getBytes();
		buf.writeInt(ca.length);
		buf.writeBytes(ca);
	}
	
    public static class Handler implements IMessageHandler<DTNodeTileEntityPacket, IMessage> {
        @Override
        public IMessage onMessage(DTNodeTileEntityPacket message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things
            // here.
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(DTNodeTileEntityPacket message, MessageContext ctx) {
            // This code is run on the server side. So you can do server-side calculations here
        	DTNodeTileEntity te = ((DTNodeTileEntity)ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.pos));
        	te.deserializeNBT(message.nbt);
        	te.markDirty();
        	IBlockState state = te.getWorld().getBlockState(te.getPos());
        	te.getWorld().notifyBlockUpdate(te.getPos(), state, state,3);
        }
    }
}
