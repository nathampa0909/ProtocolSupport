package protocolsupport.protocol.transformer.mcpe;

import net.minecraft.server.v1_8_R3.ItemStack;
import gnu.trove.map.hash.TIntObjectHashMap;

public class PEStorage {

	private final TIntObjectHashMap<ItemInfo> items = new TIntObjectHashMap<>();
	private final TIntObjectHashMap<ItemStack[]> armor = new TIntObjectHashMap<>();

	public void addItemInfo(int entityId, float locX, float locY, float locZ, float speedX, float speedY, float speedZ) {
		items.put(entityId, new ItemInfo(locX, locY, locZ, speedX, speedY, speedZ));
	}

	public ItemInfo getItemInfo(int entityId) {
		return items.get(entityId);
	}

	public void removeItemsInfo(int[] ids) {
		for (int id : ids) {
			items.remove(id);
		}
	}

	public static class ItemInfo {
		protected float x;
		protected float y;
		protected float z;
		protected float speedX;
		protected float speedY;
		protected float speedZ;

		public ItemInfo(float x, float y, float z, float speedX, float speedY, float speedZ) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.speedX = speedX;
			this.speedY = speedY;
			this.speedZ = speedZ;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public float getZ() {
			return z;
		}

		public float getSpeedX() {
			return speedX;
		}

		public float getSpeedY() {
			return speedY;
		}

		public float getSpeedZ() {
			return speedZ;
		}
	}

	public void setArmorSlot(int entityId, int slot, ItemStack itemstack) {
		ItemStack[] armorc = getArmor(entityId);
		armorc[armorc.length - slot - 1] = itemstack;
	}

	public ItemStack[] getArmor(int entityId) {
		ItemStack[] armorc = armor.get(entityId);
		if (armorc == null) {
			armorc = new ItemStack[4];
			armor.put(entityId, armorc);
		}
		return armorc;
	}

}