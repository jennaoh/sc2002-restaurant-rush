# SC2002 Restaurant Rush — AI Conversation History

## Misc AI Use

**AI Tool:** Gemini 3.8 Flash

**Project:** SC2002 Restaurant Rush

**Stage:** All

**Purpose:** Asking AI the way to compile and run the Java code if someone wasn't using VSCode with extension. It was also a way for me to manually compile each stage to test as the VSCode 'Run' button would sometimes run from cache and would need to be cleared manually which was also annoying. 

Images of AI chat is found [here](https://github.com/jennaoh/sc2002-restaurant-rush/tree/hz-branch/ai-history/misc)

---

## Conversation — How to Compile & Run Java without VSCode Extension

**Student:**

What is a way to compile all the files and run main if someone were to download the entire folder as a zip and does not necessarily have a vscode java extension environment?

**Gemini:**

This project uses only standard Java libraries across its 20 .java files under src (organized into Main.java in the default package plus boundary, control, entity, and staff), with no external JARs or resource files. Anyone who extracts the ZIP only needs a JDK (javac and java available in their terminal) — no VS Code extensions or build tools are required.

1. Simplest cross-platform way (Windows, macOS, Linux)

Open a terminal in the extracted root folder (the folder that contains src), then run:

```bash
javac -d out -sourcepath src src/Main.java
java -cp out Main
```

-   -d out puts all compiled .class files into an out directory (preserving package folders) instead of cluttering src.
  
-   -sourcepath src lets javac automatically find and compile every class referenced by Main (verified to compile all 20 .java files into 22 .class files).

-   java -cp out Main runs the compiled Main class from out.

2. Explicitly compiling all .java files

If you later add standalone .java files not directly referenced by Main, compile every file under src explicitly depending on the shell:

Windows PowerShell:
```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object FullName)
java -cp out Main
```

Windows Command Prompt (cmd):
```bat
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
del sources.txt
java -cp out Main
```

macOS / Linux / Git Bash:
```bash
javac -d out $(find src -name "*.java")
java -cp out Main
```

---


