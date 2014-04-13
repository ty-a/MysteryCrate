/*Copyright (C) 2014 TyA <tyler@faceyspacies.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files 
(the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
 subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.faceyspacies.MysteryCrate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public final class MysteryCrateListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		
		if(MysteryCrate.numberOfItems == 0) {
			return; // nothing for us to do
		}
		
		Player player = event.getPlayer();
		ItemStack handItem = player.getItemInHand();
	    
		if(handItem.getItemMeta().getDisplayName() == null)
			return;
		
		if(handItem.getItemMeta().getDisplayName().equals("Mystery Crate")) {
			
			player.sendMessage("You open a Mystery Crate!");
	    		
	   		PlayerInventory inventory = player.getInventory();
			ItemStack item = new ItemStack(Material.CHEST, 1);
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			
			lore.add("Open for a prize!");
			meta.setDisplayName("Mystery Crate");
			meta.setLore(lore);
			item.setItemMeta(meta);
			
			HashMap<Integer, ItemStack> result = inventory.removeItem(item); // removes block
			if(result.size() > 0) {
				event.getPlayer().sendMessage("You don't have a Mystery Crate!");
				return;
			}
			
			inventory.addItem(getRandomItem());
			player.updateInventory();
			
			event.setCancelled(true); // doesn't do an action with it
	    }
    }
	
	private ItemStack getRandomItem() {
		// item starts in the itemList starts at 0
		int randomNum = (int)((Math.random()) * (MysteryCrate.numberOfItems));
		return new ItemStack(MysteryCrate.itemList.get(randomNum), 1);
	}
}
