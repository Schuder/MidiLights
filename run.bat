javac MidiDecompiler.java
cd midiFiles
for /r %%i in (*) do (
	
	java MidiDecompiler %%i %%i
)
pause