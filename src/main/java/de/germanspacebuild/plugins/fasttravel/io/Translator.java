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

package de.germanspacebuild.plugins.fasttravel.io;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.io.language.Language;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Translator {

    private static FastTravel plugin;
    private static HashMap<String, YamlConfiguration> languages;
    private static String language;
    private static IOManager io;

    public Translator(FastTravel plugin) {
        Translator.plugin = plugin;
    }

    public void init() {
        Configuration config = plugin.getConfig();
        io = plugin.getIOManger();
        languages = new HashMap<>();
        language = config.getString("IO.Language", "en");
        List<Language> langs = Language.getLanguages();
        List<File> loadedFiles = new ArrayList<>();
        for (Language lang : langs) {
            lang.createLanguageFile();
            lang.updateLanguage();
            loadedFiles.add(lang.getFile());
            languages.put(lang.getName(), lang.getKeys());
        }
        File[] langFiles = plugin.getLangDir().listFiles();
        for (File langFile : langFiles) {
            if (loadedFiles.indexOf(langFile) == -1) loadLanguageFile(langFile);
        }
        if (languages.get(language) == null) {
            language = "en";
            config.set("IO.Language", "en");
        }
    }

    private void loadLanguageFile(File languageFile) {
        YamlConfiguration lang = new YamlConfiguration();
        try {
            lang.load(languageFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        languages.put(languageFile.getName().split(".lang")[0], lang);
    }

    public String getLanguage() {
        return language;
    }

    public HashMap<String, YamlConfiguration> getLanguages() {
        return languages;
    }

    public String getKey(String key) {
        if (languages.get(language) == null)
            return "Language " + language + " doesn't seem to exist. Please change it in the config.yml!";
        if (languages.get(language).getString(key) != null)
            return languages.get(language).getString(key);
        else
            io.sendConsole("Key " + key + " couldn't be found. Please check your lang Files and report to plugin dev.");
            return "Key " + key + " couldn't be found. Please check your lang Files and report to plugin dev.";
    }
}
