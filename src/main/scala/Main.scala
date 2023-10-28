
object Main extends App {
    def loadArgs() {
        val opts = new OptRegistry

        val inputFile = opts.addOption(new RequiredCommandLineOption(
            new StringOptRegister, "input-file", Some('i'),
            "the path to the file to be processed"
        ))
        val separator = opts.addOption(new CommandLineOption(
            new StringOptRegister().withDefault(","), "separator", None,
            "the separator to be used for parsing the input CSV file"
        ))
        val format = opts.addOption(new CommandLineOption(
            new EnumOptRegister(SupportedFormat).withDefault(SupportedFormat.CSV),
            "format", None,
            "the format of the output file"
        ))
        val outputSeparator = opts.addOption(new CommandLineOption(
            new StringOptRegister().withDefault(","), "output-separator", None,
            "the separator to be used for the output file, in case the selected format is CSV"
        ))
        val headers = opts.addOption(new CommandLineOption(
            new FlagOptRegister, "headers", None,
            "whether to include header row in the output"
        ))
        val outputFile = opts.addOption(new CommandLineOption(
            new StringOptRegister, "output-file", Some('o'),
            "the file to write the output into; if unspecified, write to STDOUT"
        ))
        val stdout = opts.addOption(new CommandLineOption(
            new FlagOptRegister, "stdout", None,
            "whether to write the output to STDOUT; if output file is not specified, this is considered to be true implicitly"
        ))
        val help = opts.addOption(new CommandLineOption(
            new FlagOptRegister, "help", Some('h'),
            "when specified, don't do anything, just print help and exit"
        ))

        ArgLoader.load(opts, this.args)

        println(if (inputFile.optRegister.hasValue) f"input file: ${inputFile.optRegister.get}" else "input file unspecified")
        println(if (separator.optRegister.hasValue) f"separator: ${separator.optRegister.get}" else "separator unspecified")
        println(if (format.optRegister.hasValue) f"format: ${format.optRegister.get}" else "format unspecified")
    }

    println("Hello, World!")
    loadArgs()
}
