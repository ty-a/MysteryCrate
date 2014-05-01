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
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomCrateCommandExecutor implements CommandExecutor {

	private JavaPlugin plugin;
	
	public RandomCrateCommandExecutor(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("crate")) { // If the player typed /crate then do the following...
			
			if(!sender.hasPermission("RandomCrate.crate")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("random-crates-no-permission")));
				return false;
			}
			
			if(args.length < 1)
				// ensure we have enough params
				return false;
			
			if(args[0].equalsIgnoreCase("give")) {
				Player player;
				int numberOfCrates = 1;
				
				if(args.length < 2) {
					// sender is the target
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("random-crates-console-didnt-give-name")));
						return false;
					}
					
					player = (Player) sender;
				}
				

				// end no set target
				else 
					player = Bukkit.getServer().getPlayer(args[1]);

				if(player == null) {
					sender.sendMessage(args[1] + " " + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("random-crate-not-online")));
					return true;
				}
				
				if(args.length == 3) {
					try {
						numberOfCrates = Integer.parseInt(args[2]);
					}
					catch(NumberFormatException ex) {
						return false;
					}
				}
				
				sender.sendMessage(player.getName() + ChatColor.translateAlternateColorCodes('&', " " + plugin.getConfig().getString("random-crate-has-been-given")));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("random-crate-given-crate")));
				// assertion: player is a valid online player now
				
				PlayerInventory inventory = player.getInventory();
				
				ItemStack item = new ItemStack(Material.CHEST, numberOfCrates);
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				
				lore.add(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("random-crate-lore")));
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("random-crate-name")));
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				HashMap<Integer, ItemStack> result = inventory.addItem(item);
				if(result.size() > 0) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("random-crate-inv-full")));
				}
			} else {
				return false;
			}
			return true;
		}
		return false; 
	}
}
