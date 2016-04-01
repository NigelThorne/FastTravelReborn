package de.germanspacebuild.plugins.fasttravel.commands;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.DBType;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Felix on 01.04.2016.
 *
 * @author Felix
 */
public class ConvertDBCommand implements CommandExecutor {

    private FastTravel plugin;
    private IOManager io;

    public ConvertDBCommand(FastTravel plugin) {
        this.plugin = plugin;
        this.io = plugin.getIOManger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission(FastTravel.PERMS_BASE + "convert")) {
            io.send(sender, io.translate("Perms.Not"));
            return true;
        }

        String newDBType = args[0];

        switch (newDBType.toLowerCase()) {
            case "file":
                DBType.setDBType(DBType.File);
                io.send(sender, io.translate("Command.Convert.File"));
                plugin.getConfig().set("Plugin.Database", "File");
                break;
            case "sqlite":
                DBType.setDBType(DBType.SQLite);
                plugin.getConfig().set("Plugin.Database", "SQLite");
                io.send(sender, io.translate("Command.Convert.SQLite"));
                break;
            default:
                io.send(sender, io.translate("Command.Convert.Invalid"));
        }

        //Reloading database
        FastTravelDB.save();
        FastTravelDB.load();

        return true;
    }
}