
```
  ██╗   ██╗ ██████╗ ██╗    ██╗ █████╗ ██╗
  ╚██╗ ██╔╝██╔═══██╗██║    ██║██╔══██╗██║
   ╚████╔╝ ██║   ██║██║ █╗ ██║███████║██║
    ╚██╔╝  ██║   ██║██║███╗██║██╔══██║██║
     ██║   ╚██████╔╝╚███╔███╔╝██║  ██║██║
     ╚═╝    ╚═════╝  ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝
```
A Grasscutter spoon trying to make more things work.

**Note:** For support please join [Grasscutter Discord](https://discord.gg/grasscutter).

## Current feature
* mostly working except scripts related

## Requirement
- [JDK 17.0.4.1] (https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- mitmproxy
- Game resources

## Quick setup guide
1. Run `gradlew.bat`

2. Buidling JAR
```
./gradlew jar
```

3. Create a `resources` folder in the directory where grasscutter.jar is located and move your `BinOutput, ExcelBinOutput, Readables, Scripts, Subtitle, TextMap` folders there ([Get it from here](https://git.crepe.moe/grasscutters/Grasscutter_Resources))

4. Run Yowai by open `run.cmd`. **Make sure mongodb service is running before open this.**


