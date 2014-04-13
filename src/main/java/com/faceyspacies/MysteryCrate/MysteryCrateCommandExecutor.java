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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class MysteryCrateCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("crate")) { // If the player typed /crate then do the following...
			
			if(!sender.hasPermission("MysteryCrate.crate")) {
				sender.sendMessage("You do not have permission to use this command");
				return false;
			}
			
			if(args.length < 1)
				// ensure we have enough params
				return false;
			
			if(args[0].equalsIgnoreCase("give")) {
				Player player;
				
				if(args.length < 2) {
					// sender is the target
					if(!(sender instanceof Player)) {
						sender.sendMessage("You must include a player name from the console.");
						return false;
					}
					
					player = (Player) sender;
				}
				// end no set target
				else 
					player = Bukkit.getServer().getPlayer(args[1]);

				if(player == null) {
					sender.sendMessage(args[1] + " is not online.");
					return true;
				}
				
				sender.sendMessage("Giving " + player.getName() + " a Mystery Crate.");
				player.sendMessage("You have been given a Mystery Crate!");
				// assertion: player is a valid online player now
				
				PlayerInventory inventory = player.getInventory();
				ItemStack item = new ItemStack(Material.CHEST, 1);
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				
				lore.add("Open for a prize!");
				meta.setDisplayName("Mystery Crate");
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				HashMap<Integer, ItemStack> result = inventory.addItem(item);
				if(result.size() > 0) {
					sender.sendMessage("Target's inventory is full");
				}
			} else {
				return false;
			}
			return true;
		}
		return false; 
	}
}
