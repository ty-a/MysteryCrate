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
package com.faceyspacies.RandomCrate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.gravitydevelopment.updater.Updater;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class RandomCrate extends JavaPlugin {
	
	protected static List<ItemStack> commonItemList = new ArrayList<ItemStack>();
	protected static List<ItemStack> rareItemList = new ArrayList<ItemStack>();
	protected static List<ItemStack> vrareItemList = new ArrayList<ItemStack>();
	protected static char[] distributionList = new char[100];
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig(); // shouldn't override actual config if it exists
		
		if(this.getConfig().getBoolean("auto-update")) {
			Updater updater = new Updater(this, 78005, this.getFile(), Updater.UpdateType.DEFAULT, false);
			Updater.UpdateResult result = updater.getResult();
			switch(result) {
			case SUCCESS:
				getLogger().info("Found new version; Updating. Reload or Restart Server to finalize.");
				break;
			case DISABLED:
				break;
			case FAIL_APIKEY:
				getLogger().info("An invalid API key was provided. Please ensure you have the correct key in the Updater/config.yml file.");
				break;
			case FAIL_BADID:
				break;
			case FAIL_DBO:
				getLogger().info("Unable to contact dev.bukkit.org; unable to check for update");
				break;
			case FAIL_DOWNLOAD:
				getLogger().info("A new version is available, but failed to download it.");
				break;
			case FAIL_NOVERSION:
				break;
			case NO_UPDATE:
				break;
			case UPDATE_AVAILABLE:
				break;
			}
		}
		else if(this.getConfig().getBoolean("check-for-updates")) {
			Updater updater = new Updater(this, 78005, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			
			if(updater.getResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE)) {
				getLogger().info("A new version of NoEscape is available! Please update soon!");

			}
		}
		
		getCommand("crate").setExecutor(new RandomCrateCommandExecutor(this));
		getServer().getPluginManager().registerEvents(new RandomCrateListener(this), this);
		
		commonItemList = new ArrayList<ItemStack>();
		rareItemList = new ArrayList<ItemStack>();
	    vrareItemList = new ArrayList<ItemStack>();
	    
	    int distribution = getConfig().getInt("vrare-rarity");
	    int currLocation = 0;
	    for(int i = 0; i < distribution; i++ ) {
	    	distributionList[currLocation] = 'v';
	    	currLocation++;
	    }
	    
	    distribution = getConfig().getInt("rare-rarity");
	    for(int i = 0; i < distribution; i++ ) {
	    	distributionList[currLocation] = 'r';
	    	currLocation++;
	    }
	    
	    distribution = getConfig().getInt("common-rarity");
	    for(int i = 0; i < distribution; i++ ) {
	    	distributionList[currLocation] = 'c';
	    	currLocation++;
	    }

		fillItemLists();
	}
	
	@Override 
	public void onDisable() {
		commonItemList = null;
		rareItemList = null;
		vrareItemList = null;
	}
	
	@SuppressWarnings("deprecation")
	private void fillItemLists() {
		
		Map<String, ?> itemsFromConfig = getConfig().getConfigurationSection("item-list").getValues(false);
		int itemId = -1;
		String itemLore = "";
		String rarityType = "";
		String itemName = "";
		String enchantment;
		int enchantmentLevel = 1;
		int rarity;
		int itemAmount;
		ItemStack newItem;
		
		for (String currNode : itemsFromConfig.keySet()) {
			
			try {
				itemId = Integer.parseInt(currNode);
			}
			catch (NumberFormatException  ex) {	}
			
			itemLore = getConfig().getString("item-list." + currNode + ".lore", "");
			rarityType = getConfig().getString("item-list." + currNode + ".rarity-type", "common");
			rarity = getConfig().getInt("item-list." + currNode + ".rarity", 1);
			itemAmount = getConfig().getInt("item-list." + currNode + ".amount", 1);
			itemName = getConfig().getString("item-list." + currNode + ".name", "");
			enchantment = getConfig().getString("item-list." + currNode + ".enchantment");
			enchantmentLevel = getConfig().getInt("item-list." + currNode + ".enchantment-level", 1);
			
			if(itemId == -1) {
				newItem = new ItemStack(Material.getMaterial(((currNode.replace(" ", "_")).toUpperCase().trim())), itemAmount);
			} else {
				newItem = new ItemStack(Material.getMaterial(itemId), itemAmount);
			}
			
			if(!itemLore.equals("") || !itemName.equals("")) {
				ItemMeta meta = newItem.getItemMeta();
				List<String> lore = new ArrayList<String>();

				lore.add(itemLore);
				meta.setDisplayName(itemName);
				meta.setLore(lore);
				newItem.setItemMeta(meta);
			}
			
			if(enchantment != null) {
				Enchantment newEnchant = Enchantment.getByName(((enchantment.toUpperCase()).replace(" ",  "_")).trim());
				if(newEnchant == null) {
					getLogger().info(enchantment + " is not a valid enchantment.");
				} else {
					if(enchantmentLevel > newEnchant.getMaxLevel()) {
						enchantmentLevel = newEnchant.getMaxLevel();
					}
					try {
						newItem.addEnchantment(newEnchant, enchantmentLevel);
					} 
					catch (IllegalArgumentException ex) {
						getLogger().warning("Unable to add enchantment " + enchantment + " to " + currNode);
					}
				}
			}
			
			if(rarityType.equals("common")) {
				for(int j = 0; j < rarity; j++) {
					commonItemList.add(newItem);
				}
			} else if(rarityType.equals("rare")) {
				for(int j = 0; j < rarity; j++) {
					rareItemList.add(newItem);
				}
			} else {
				for(int j = 0; j < rarity; j++) {
					vrareItemList.add(newItem);
				}
			}
			
			itemId = -1;
			itemLore = "";
			rarityType = "";
		}
	}
}