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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class RandomCrate extends JavaPlugin {
	
	protected static List<Material> itemList = new ArrayList<Material>();
	protected static int numberOfItems = 0;
	
	@Override
	public void onEnable() {
		
		try {
			Scanner input = new Scanner(new File(getDataFolder() + File.separator + "RandomCrateItems.txt"));
			fillItemList(input);
			input.close();
			
		} catch (FileNotFoundException e) {
			getLogger().info("RandomCrateItems.txt not found. Creating new file.");
			getDataFolder().mkdir();
			Writer newFile = null;
			try {
					newFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getDataFolder() + 
							File.separator + "RandomCrateItems.txt")));
					
					newFile.write("Diamond\n");
					newFile.write("Egg\n");
					newFile.write("Chest\n");
					
					newFile.close();
					
					fillItemList(new Scanner(new File(getDataFolder() + 
							File.separator + "RandomCrateItems.txt")));
			}
			catch (IOException ex) {
				getLogger().info("Couldn't create RandomCrateItems.txt; Will not be able to open Mystery Crates");
			}
		}
		
		getCommand("crate").setExecutor(new RandomCrateCommandExecutor());
		getServer().getPluginManager().registerEvents(new RandomCrateListener(), this);
	}
	
	@Override 
	public void onDisable() {
		itemList = null;
		numberOfItems = 0;
	}
	
	private void fillItemList(Scanner input) {
		Material currItem;
		String currItemName;
		
		while(input.hasNextLine()) {
			
			currItemName = input.nextLine();
			currItemName = currItemName.replace(' ', '_');
			currItem = Material.getMaterial(currItemName.toUpperCase());
			if(currItem != null) { // ensure we have a legal item
			   itemList.add(currItem);
			   numberOfItems++;
			} else {
				getLogger().info(currItemName + " is not a valid item name");
			}
		}
		
		if(numberOfItems == 0) 
			getLogger().info("Since there is no items, no Mystery Crates can be opened");
	}
}
