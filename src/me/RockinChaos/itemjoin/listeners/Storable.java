/*
 * ItemJoin
 * Copyright (C) CraftationGaming <https://www.craftationgaming.com/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.RockinChaos.itemjoin.listeners;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.RockinChaos.itemjoin.handlers.PlayerHandler;
import me.RockinChaos.itemjoin.item.ItemUtilities;
import me.RockinChaos.itemjoin.utils.ServerUtils;
import me.RockinChaos.itemjoin.utils.StringUtils;

public class Storable implements Listener {
	
   /**
	* Prevents the player from clicking to store the custom item.
	* 
	* @param event - InventoryClickEvent
	*/
	@EventHandler(ignoreCancelled = true)
	private void onInventoryStore(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		String invType = event.getView().getType().toString();
		ItemStack item = null;
		if (StringUtils.containsIgnoreCase(event.getAction().name(), "HOTBAR")) {
			if (event.getView().getBottomInventory().getSize() >= event.getHotbarButton() && (event.getHotbarButton() >= 0 || event.getHotbarButton() == -1)) {
				if (event.getHotbarButton() == -1) {
					item = PlayerHandler.getOffHandItem(player);
				} else {
					item = event.getView().getBottomInventory().getItem(event.getHotbarButton());
				}
			}
			if (item == null) { item = event.getCurrentItem(); }
		} else if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) { item = event.getCurrentItem(); } 
		else { item = event.getCursor(); }
		if (invType != null) {
			if (event.getRawSlot() > event.getInventory().getSize() && event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || event.getRawSlot() < event.getInventory().getSize()) {
				if ((invType.contains("CHEST") || invType.contains("BARREL") || invType.contains("BREWING") || invType.contains("FURNACE") || invType.contains("GRINDSTONE") || invType.contains("SHULKER_BOX") 
						|| invType.contains("HOPPER") || invType.contains("ANVIL") || invType.contains("WORKBENCH") || invType.contains("DISPENSER") || invType.contains("DROPPER")) && !ItemUtilities.getUtilities().isAllowed(player, item, "item-store")) {
					event.setCancelled(true);
					PlayerHandler.updateInventory(player, 1L);
				} else if ((invType.contains("ENCHANTING") || invType.contains("ANVIL")) && !ItemUtilities.getUtilities().isAllowed(player, item, "item-modifiable")) {
					event.setCancelled(true);
					PlayerHandler.updateInventory(player, 1L);
				}
			}
		}
	}
	
   /**
	* Prevents the player from click dragging to store the custom item.
	* 
	* @param event - InventoryDragEvent
	*/
	@EventHandler(ignoreCancelled = true)
	private void onInventoryDragToStore(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		String invType = event.getView().getType().toString();
		int inventorySize = event.getInventory().getSize();
		ItemStack item = event.getOldCursor();
		for (int i: event.getRawSlots()) {
			if (i < inventorySize) {
				if (invType != null) {
					if ((invType.contains("CHEST") || invType.contains("BARREL") || invType.contains("BREWING") || invType.contains("FURNACE") || invType.contains("GRINDSTONE") || invType.contains("SHULKER_BOX") 
						|| invType.contains("HOPPER") || invType.contains("ANVIL") || invType.contains("WORKBENCH") || invType.contains("DISPENSER") || invType.contains("DROPPER")) && !ItemUtilities.getUtilities().isAllowed(player, item, "item-store")) {
						event.setCancelled(true);
						PlayerHandler.updateInventory(player, 1L);
						break;
					} else if ((invType.contains("ENCHANTING") || invType.contains("ANVIL")) && !ItemUtilities.getUtilities().isAllowed(player, item, "item-modifiable")) {
						event.setCancelled(true);
						PlayerHandler.updateInventory(player, 1L);
						break;
					}
				}
			}
		}
	}
	
   /**
	* Prevents the player from storing the custom item in an itemframe.
	* 
	* @param event - PlayerInteractEntityEvent
	*/
	@EventHandler(ignoreCancelled = true)
	private void onInteractItemFrame(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof ItemFrame) {
			ItemStack item;
			if (ServerUtils.hasSpecificUpdate("1_9")) { item = PlayerHandler.getPerfectHandItem(event.getPlayer(), event.getHand().toString()); } 
			else { item = PlayerHandler.getPerfectHandItem(event.getPlayer(), ""); }
			Player player = event.getPlayer();
			if (!ItemUtilities.getUtilities().isAllowed(player, item, "item-store")) {
				event.setCancelled(true);
				PlayerHandler.updateInventory(player, 1L);
			}
		}
	}
	
   /**
	* Prevents the player from storing the custom item in an armor stand.
	* 
	* @param event - PlayerInteractAtEntityEvent
	*/
	@EventHandler(ignoreCancelled = true)
	private void onInteractArmorStand(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked().toString().equalsIgnoreCase("CraftArmorStand")) {
			ItemStack item;
			if (ServerUtils.hasSpecificUpdate("1_9")) { item = PlayerHandler.getPerfectHandItem(event.getPlayer(), event.getHand().toString()); } 
			else { item = PlayerHandler.getPerfectHandItem(event.getPlayer(), ""); }
			Player player = event.getPlayer();
			if (!ItemUtilities.getUtilities().isAllowed(player, item, "item-store")) {
				event.setCancelled(true);
				PlayerHandler.updateInventory(player, 1L);
			}
		} if (ServerUtils.hasSpecificUpdate("1_14") && event.getRightClicked().toString().equalsIgnoreCase("CraftFox")) {
			ItemStack item = PlayerHandler.getPerfectHandItem(event.getPlayer(), event.getHand().toString());;
			Player player = event.getPlayer();
			if (!ItemUtilities.getUtilities().isAllowed(player, item, "item-store")) {
				event.setCancelled(true);
				PlayerHandler.updateInventory(player, 1L);
			}
		}
	}
}