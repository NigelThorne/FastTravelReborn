/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2016 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.germanspacebuild.plugins.fasttravel.commands;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import de.germanspacebuild.plugins.fasttravel.menu.SignMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by oneill011990 on 15.12.2014
 * for FastTravelReborn
 *
 * @author oneill011990
 */
public class MenuCommand implements CommandExecutor {

    private FastTravel plugin;
    private IOManager io;

    public MenuCommand(FastTravel plugin) {
        this.plugin = plugin;
        io = plugin.getIOManger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player) || !plugin.getConfig().getBoolean("Plugin.Menus")) {
            io.sendTranslation(sender, "Command.Menu.Disabled");
            return true;
        }

        if (!sender.hasPermission(FastTravel.PERMS_BASE + "menu")) {
            io.sendTranslation(sender, "Perms.Not");
            return true;
        }

        Player player = (Player) sender;

        SignMenu menu = new SignMenu(player);

        SignMenu.menus.add(menu);

        menu.open(1);

        return true;
    }
}
