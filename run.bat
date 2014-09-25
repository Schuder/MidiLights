for /f "usebackq delims=|" %%f in (`dir /b "T:\Programming\Elijah Houk\Git\MidiLights\midiFiles"`) do (
	java MidiDecompiler "%CD%/midiFiles/%%f" out/%%f
)
pause