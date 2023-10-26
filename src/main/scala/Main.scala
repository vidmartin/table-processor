
object Main extends App {
    def loadArgs() {
        val loader = new ArgLoader
        
        val inputFile = loader.addOption(new StringOpt(true), "input-file", Some('i'))
        val separator = loader.addOption(new StringOpt(","), "separator")
        val format = loader.addOption(
            new MapOpt[SupportedFormat](s => s match {
                case "md" => SupportedFormat.MD
                case "csv" => SupportedFormat.CSV
            }, SupportedFormat.CSV),
            "format"
        )
        val outputSeparator = loader.addOption(new StringOpt(","), "output-separator")
        val headers = loader.addOption(new FlagOpt, "headers")
        val outputFile = loader.addOption(new StringOpt(false), "output-file")
        val stdout = loader.addOption(new FlagOpt, "stdout")
        val help = loader.addOption(new FlagOpt, "help", Some('h'))

        loader.load(this.args)

        println(if (inputFile.hasValue) f"input file: ${inputFile.get}" else "input file unspecified")
        println(if (separator.hasValue) f"separator: ${separator.get}" else "separator unspecified")
        println(if (format.hasValue) f"format: ${format.get}" else "format unspecified")
    }

    println("Hello, World!")
    loadArgs()
}
