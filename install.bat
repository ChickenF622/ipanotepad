for %%* in (.) do set app=%%~nx*
adb install -r build\outputs\apk\%app%-debug.apk
