package protocolsupport.protocol.v_1_6.clientboundtransformer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.Deflater;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import protocolsupport.protocol.DataStorage;
import protocolsupport.protocol.PacketDataSerializer;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.Packet;

public class PlayPacketTransformer implements PacketTransformer {

	//TODO: filter invalid blocks and entities

	@Override
	public void tranform(Channel channel, int packetId, Packet packet, PacketDataSerializer serializer) throws IOException {
		PacketDataSerializer packetdata = new PacketDataSerializer(Unpooled.buffer(), serializer.getVersion());
		packet.b(packetdata);
		switch (packetId) {
			case 0x00: { //PacketPlayOutKeepAlive
				serializer.writeByte(0x00);
				serializer.writeInt(packetdata.readVarInt());
				return;
			}
			case 0x01: { //PacketPlayOutLogin
				serializer.writeByte(0x01);
				serializer.writeInt(packetdata.readInt());
				int gamemode = packetdata.readByte();
				int dimension = packetdata.readByte();
				int difficulty = packetdata.readByte();
				int maxplayers = packetdata.readByte();
				serializer.writeString(packetdata.readString(32767));
				serializer.writeByte(gamemode);
				serializer.writeByte(dimension);
				serializer.writeByte(difficulty);
				serializer.writeByte(0);
				serializer.writeByte(maxplayers);
				return;
			}
			case 0x02: { //PacketPlayOutChat
				serializer.writeByte(0x03);
				serializer.writeString(ChatSerializer.a(packetdata.d()));
				return;
			}
			case 0x03: { //PacketPlayOutUpdateTime
				serializer.writeByte(0x04);
				serializer.writeLong(packetdata.readLong());
				serializer.writeLong(packetdata.readLong());
				return;
			}
			case 0x04: { //PacketPlayOutEntityEquipment
				serializer.writeByte(0x05);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeShort(packetdata.readShort());
				serializer.a(packetdata.i());
				return;
			}
			case 0x05: { //PacketPlayOutSpawnPosition
				serializer.writeByte(0x06);
				BlockPosition blockPos = packetdata.c();
				serializer.writeInt(blockPos.getX());
				serializer.writeInt(blockPos.getY());
				serializer.writeInt(blockPos.getZ());
				return;
			}
			case 0x06: { //PacketPlayOutUpdateHealth
				serializer.writeByte(0x08);
				serializer.writeFloat(packetdata.readFloat());
				serializer.writeShort(packetdata.readVarInt());
				serializer.writeFloat(packetdata.readFloat());
				return;
			}
			case 0x07: { //PacketPlayOutRespawn
				serializer.writeByte(0x09);
				serializer.writeInt(packetdata.readInt());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				serializer.writeShort(256);
				serializer.writeString(packetdata.readString(32767));
				return;
			}
			case 0x08: { // PacketPlayOutPosition
				serializer.writeByte(0x0D);
				Player player = DataStorage.getPlayer(channel.remoteAddress());
				double x = packetdata.readDouble();
				double y = packetdata.readDouble() /*+ 1.63*/;
				double z = packetdata.readDouble();
				float yaw = packetdata.readFloat();
				float pitch = packetdata.readFloat();
				short field = packetdata.readByte();
				Location location = player.getLocation();
				if ((field & 0x01) != 0) {
					x += location.getX();
				}
				if ((field & 0x02) != 0) {
					y += location.getY();
				}
				if ((field & 0x04) != 0) {
					z += location.getX();
				}
				if ((field & 0x08) != 0) {
					yaw += location.getYaw();
				}
				if ((field & 0x10) != 0) {
					pitch += location.getPitch();
				}
				serializer.writeDouble(x);
				serializer.writeDouble(y + 1.63);
				serializer.writeDouble(y);
				serializer.writeDouble(z);
				serializer.writeFloat(yaw);
				serializer.writeFloat(pitch);
				serializer.writeBoolean(false);
				return;
			}
			case 0x09: { //PacketPlayOutHeldItemSlot
				serializer.writeByte(0x10);
				serializer.writeShort(packetdata.readByte());
				return;
			}
			case 0x0A: { //PacketPlayOutBed
				serializer.writeByte(0x11);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeByte(0);
				BlockPosition blockPos = packetdata.c();
				serializer.writeInt(blockPos.getX());
				serializer.writeByte(blockPos.getY());
				serializer.writeInt(blockPos.getZ());
				return;
			}
			case 0x0B: { //PacketPlayOutAnimation
				serializer.writeByte(0x12);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeByte(packetdata.readByte());
				return;
			}
			case 0x0C: { //PacketPlayOutNamedEntitySpawn
				serializer.writeByte(0x14);
				serializer.writeInt(packetdata.readVarInt());
				UUID uuid = packetdata.g();
				String playerName = DataStorage.getTabName(channel.remoteAddress(), uuid);
				serializer.writeString(playerName != null ? playerName : "Unknown");
				serializer.writeInt(packetdata.readInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				serializer.writeShort(packetdata.readShort());
				serializer.writeBytes(packetdata.readBytes(packetdata.readableBytes()).array());
				return;
			}
			case 0x0D: { //PacketPlayOutCollect
				serializer.writeByte(0x16);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeInt(packetdata.readVarInt());
				return;
			}
			case 0x0E: { //TODO
				return;
			}
			case 0x0F: { //TODO
				return;
			}
			case 0x10: { //PacketPlayOutSpawnEntityPainting
				serializer.writeByte(0x19);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeString(packetdata.readString(13));
				BlockPosition blockPos = packetdata.c();
				int x = blockPos.getX();
				int z = blockPos.getZ();
				int direction = packetdata.readByte();
				switch (direction) {
					case 0: {
						--z;
						break;
					}
					case 1: {
						++x;
						break;
					}
					case 2: {
						++z;
						break;
					}
					case 3: {
						--x;
						break;
					}
				}
				serializer.writeInt(x);
				serializer.writeInt(blockPos.getY());
				serializer.writeInt(z);
				serializer.writeInt(direction);
				return;
			}
			case 0x11: { //PacketPlayOutSpawnEntityExperienceOrb
				serializer.writeByte(0x1A);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeShort(packetdata.readShort());
				return;
			}
			case 0x12: { //PacketPlayOutEntityVelocity
				serializer.writeByte(0x1C);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeShort(packetdata.readShort());
				serializer.writeShort(packetdata.readShort());
				serializer.writeShort(packetdata.readShort());
				return;
			}
			case 0x13: { //PacketPlayOutEntityDestroy
				serializer.writeByte(0x1D);
				int count = packetdata.readVarInt();
				serializer.writeByte(count);
				for (int i = 0; i < count; i++) {
					serializer.writeInt(packetdata.readVarInt());
				}
				return;
			}
			case 0x14: { //PacketPlayOutEntity
				serializer.writeByte(0x1E);
				serializer.writeInt(packetdata.readVarInt());
				return;
			}
			case 0x15: { //PacketPlayOutRelEntityMove
				serializer.writeByte(0x1F);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				return;
			}
			case 0x16: { //PacketPlayOutEntityLook
				serializer.writeByte(0x20);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				return;
			}
			case 0x17: { //PacketPlayOutRelEntityMoveLook
				serializer.writeByte(0x21);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				return;
			}
			case 0x18: { //PacketPlayOutEntityTeleport
				serializer.writeByte(0x22);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				return;
			}
			case 0x19: { //PacketPlayOutEntityHeadRotation
				serializer.writeByte(0x23);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeByte(packetdata.readByte());
				return;
			}
			case 0x1A: { //PacketPlayOutEntityStatus
				serializer.writeByte(0x26);
				serializer.writeInt(packetdata.readInt());
				serializer.writeByte(packetdata.readByte());
				return;
			}
			case 0x1B: { //PacketPlayOutAttachEntity
				serializer.writeByte(0x27);
				serializer.writeInt(packetdata.readInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeBoolean(packetdata.readBoolean());
			}
			case 0x1C: { //PacketPlayOutEntityMetadata
				serializer.writeByte(0x28);
				serializer.writeByte(packetdata.readVarInt());
				serializer.writeBytes(packetdata.readBytes(packetdata.readableBytes())); //TODO : filter metadata
				return;
			}
			case 0x1D: { //PacketPlayOutEntityEffect
				serializer.writeByte(0x29);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeByte(packetdata.readByte());
				serializer.writeByte(packetdata.readByte());
				serializer.writeShort(packetdata.readVarInt());
				return;
			}
			case 0x1E: { //PacketPlayOutRemoveEntityEffect
				serializer.writeByte(0x2A);
				serializer.writeInt(packetdata.readVarInt());
				serializer.writeByte(packetdata.readByte());
				return;
			}
			case 0x1F: { //PacketPlayOutExperience
				serializer.writeByte(0x2B);
				serializer.writeFloat(packetdata.readFloat());
				serializer.writeShort(packetdata.readVarInt());
				serializer.writeShort(packetdata.readVarInt());
				return;
			}
			case 0x20: { //PacketPlayOutUpdateAttributes
				serializer.writeByte(0x2C);
				serializer.writeInt(packetdata.readVarInt());
				int ascount = packetdata.readInt();
				serializer.writeInt(ascount);
				for (int i = 0; i < ascount; i++) {
					serializer.writeString(packetdata.readString(64));
					serializer.writeDouble(packetdata.readDouble());
					int amcount = packetdata.readVarInt();
					serializer.writeShort(amcount);
					for (int j = 0; j < amcount; j++) {
						serializer.writeLong(packetdata.readLong());
						serializer.writeLong(packetdata.readLong());
						serializer.writeDouble(packetdata.readDouble());
						serializer.writeByte(packetdata.readByte());
					}
				}
				return;
			}
			case 0x21: { //PacketPlayOutMapChunk
				serializer.writeByte(0x33);
				serializer.writeInt(packetdata.readInt());
				serializer.writeInt(packetdata.readInt());
				serializer.writeBoolean(packetdata.readBoolean());
				int bitmap = packetdata.readShort() & 0xFFFF;
				serializer.writeShort(bitmap);
				serializer.writeShort(0);
				byte[] data = ChunkUtils.to16ChunkData(packetdata.a(), bitmap);
				final Deflater deflater = new Deflater(4);
				try {
					deflater.setInput(data, 0, data.length);
					deflater.finish();
					byte[] networkdata = new byte[data.length];
					int size = deflater.deflate(networkdata);
					serializer.writeInt(size);
					serializer.writeBytes(networkdata, 0, size);
				} finally {
					deflater.end();
				}
				return;
			}
			case 0x22: { //PacketPlayOutMultiBlockChange
				serializer.writeByte(0x34);
				serializer.writeInt(packetdata.readInt());
				serializer.writeInt(packetdata.readInt());
				int count = packetdata.readVarInt();
				serializer.writeShort(count);
				final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(count * 4);
				final DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
				for (int i = 0; i < count; i++) {
					dataoutputstream.writeShort(packetdata.readUnsignedShort());
					int id = packetdata.readVarInt();
					dataoutputstream.writeShort(((id >> 4) << 4) | (id & 0xF));
				}
				serializer.writeInt(dataoutputstream.size());
				serializer.writeBytes(bytearrayoutputstream.toByteArray());
				return;
			}
			case 0x23: { //PacketPlayOutBlockChange
				serializer.writeByte(0x35);	
				BlockPosition blockPos = packetdata.c();
				serializer.writeInt(blockPos.getX());
				serializer.writeByte(blockPos.getY());
				serializer.writeInt(blockPos.getZ());
				int stateId = packetdata.readVarInt();
				serializer.writeShort(stateId >> 4);
				serializer.writeByte(stateId & 0xF);
			}
			case 0x24: { //PacketPlayOutBlockAction
				serializer.writeByte(0x36);
				BlockPosition blockPos = packetdata.c();
				serializer.writeInt(blockPos.getX());
				serializer.writeShort(blockPos.getY());
				serializer.writeInt(blockPos.getZ());
				serializer.writeByte(packetdata.readUnsignedByte());
				serializer.writeByte(packetdata.readUnsignedByte());
				serializer.writeShort(packetdata.readVarInt());
			}
			case 0x25: { //PacketPlayOutBlockBreakAnimation
				serializer.writeByte(0x37);
				serializer.writeInt(packetdata.readVarInt());
				BlockPosition blockPos = packetdata.c();
				serializer.writeInt(blockPos.getX());
				serializer.writeInt(blockPos.getY());
				serializer.writeInt(blockPos.getY());
				serializer.writeByte(packetdata.readByte());
				return;
			}
			case 0x26: { //PacketPlayOutMapChunkBulk
				serializer.writeByte(0x38);
				// read data
				boolean skylight = packetdata.readBoolean();
				int count = packetdata.readVarInt();
				int[] x = new int[count];
				int[] y = new int[count];
				int[] bitmap = new int[count];
				for (int i = 0; i < count; i++) {
					x[i] = packetdata.readInt();
					y[i] = packetdata.readInt();
					bitmap[i] = packetdata.readShort() & 0xFFFF;
				}
				byte[][] data = new byte[count][];
				byte[] ldata;
				int pos = 0;
				for (int i = 0; i < count; i++) {
					data[i] = ChunkUtils.to16ChunkData(packetdata.readBytes(ChunkUtils.calcDataSize(Integer.bitCount(bitmap[i]), skylight, true)).array(), bitmap[i]);
					pos += data[i].length;
				}
				ldata = new byte[pos];
				pos = 0;
				for (int i = 0; i < data.length; i++) {
					System.arraycopy(data[i], 0, ldata, pos, data[i].length);
					pos += data[i].length;
				}
				// compress
				final Deflater deflater = new Deflater(4);
				try {
					deflater.setInput(ldata, 0, ldata.length);
					deflater.finish();
					byte[] networkdata = new byte[ldata.length + 100];
					int size = deflater.deflate(networkdata);
					// write data
					serializer.writeShort(count);
					serializer.writeInt(size);
					serializer.writeBoolean(skylight);
					serializer.writeBytes(networkdata, 0, size);
					for (int i = 0; i < count; i++) {
						serializer.writeInt(x[i]);
						serializer.writeInt(y[i]);
						serializer.writeShort(bitmap[i] & 0xFFFF);
						serializer.writeShort(0);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					deflater.end();
				}
				return;
			}
		}
	}

}