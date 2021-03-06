package protocolsupport.api.title;

import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import protocolsupport.api.chat.ChatAPI;
import protocolsupport.api.chat.components.BaseComponent;

public class TitleAPI {

	public static void sendSimpleTitle(Player player, BaseComponent title, BaseComponent subtitle, int fadeIn, int stay, int fadeOut) {
		sendSimpleTitle(player, ChatAPI.toJSON(title), ChatAPI.toJSON(subtitle), fadeIn, stay, fadeOut);
	}

	public static void sendSimpleTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		Validate.notNull(player, "Player can't be null");
		if (title == null && subtitle == null) {
			throw new IllegalArgumentException("Title and subtitle can't be both null");
		}
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		if (title != null) {
			connection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a(title)));
		}
		if (subtitle != null) {
			connection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a(subtitle)));
		}
		connection.sendPacket(new PacketPlayOutTitle(fadeIn, stay, fadeOut));
	}

	public static void removeSimpleTitle(Player player) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		connection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.CLEAR, null));
		connection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.RESET, null));
	}

}
