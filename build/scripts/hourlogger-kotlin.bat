@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  hourlogger-kotlin startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and HOURLOGGER_KOTLIN_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\HourLogger-Standard-Edition-2.0.jar;%APP_HOME%\lib\tornadofx-1.5.8.jar;%APP_HOME%\lib\commons-csv-1.4.jar;%APP_HOME%\lib\webcam-capture-0.3.10.jar;%APP_HOME%\lib\javase-2.2.0.jar;%APP_HOME%\lib\javax.json-1.0.4.jar;%APP_HOME%\lib\kotlin-stdlib-1.0.5-2.jar;%APP_HOME%\lib\kotlin-reflect-1.0.5-2.jar;%APP_HOME%\lib\bridj-0.6.2.jar;%APP_HOME%\lib\slf4j-api-1.7.2.jar;%APP_HOME%\lib\javase-3.1.0.jar;%APP_HOME%\lib\core-2.2.0.jar;%APP_HOME%\lib\kotlin-runtime-1.0.5-2.jar;%APP_HOME%\lib\core-3.1.0.jar

@rem Execute hourlogger-kotlin
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %HOURLOGGER_KOTLIN_OPTS%  -classpath "%CLASSPATH%" team1793.HourLogger %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable HOURLOGGER_KOTLIN_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%HOURLOGGER_KOTLIN_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
