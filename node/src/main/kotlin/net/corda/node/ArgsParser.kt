package net.corda.node

import com.typesafe.config.Config
import joptsimple.OptionParser
import net.corda.node.services.config.ConfigHelper
import java.io.PrintStream
import java.nio.file.Path
import java.nio.file.Paths

class ArgsParser {
    private val optionParser = OptionParser()
    // The intent of allowing a command line configurable directory and config path is to allow deployment flexibility.
    // Other general configuration should live inside the config file unless we regularly need temporary overrides on the command line
    private val baseDirectoryArg = optionParser
            .accepts("base-directory", "The node working directory where all the files are kept")
            .withRequiredArg()
            .defaultsTo(".")
    private val configFileArg = optionParser.accepts("config-file", "The path to the config file").withOptionalArg()
    private val logToConsoleArg = optionParser.accepts("log-to-console", "If set, prints logging to the console as well as to a file.")
    private val helpArg = optionParser.accepts("help").forHelp()

    fun parse(vararg args: String): CmdLineOptions {
        val optionSet = optionParser.parse(*args)
        val baseDirectory = Paths.get(optionSet.valueOf(baseDirectoryArg)).normalize().toAbsolutePath()
        val configFile =  optionSet.valueOf(configFileArg)?.let { Paths.get(it).normalize() }
        val help = optionSet.has(helpArg)
        return CmdLineOptions(help, optionSet.has(logToConsoleArg), baseDirectory, configFile)
    }

    fun printHelp(sink: PrintStream) = optionParser.printHelpOn(sink)
}

data class CmdLineOptions(val help: Boolean, val logToConsole: Boolean, val baseDirectory: Path, val configFile: Path?) {
    fun loadConfig(allowMissingConfig: Boolean = false, configOverrides: Map<String, Any?> = emptyMap()): Config {
        return ConfigHelper.loadConfig(baseDirectory, configFile, allowMissingConfig, configOverrides)
    }
}