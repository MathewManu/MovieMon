@if "%DEBUG%" == "" @echo off

@rem installation folder
set APP_HOME="C:\Program Files\MovieMon"
mkdir %APP_HOME%

@rem Db folder
set DB_HOME=%APP_HOME%\db
mkdir %DB_HOME%
icacls %DB_HOME% /grant Everyone:M

@rem Thumbnail folder
set TN_HOME=%APP_HOME%\Thumbnails
mkdir %TN_HOME%
icacls %TN_HOME% /grant Everyone:M

@rem Log folder
set LOG_HOME=%APP_HOME%\Logs
mkdir %LOG_HOME%
icacls %LOG_HOME% /grant Everyone:M

set LIB_HOME=%APP_HOME%\lib
mkdir %LIB_HOME%

@rem detecting java
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
echo %JAVA_EXE%


set CMD_LINE_ARGS=%*
echo %CMD_LINE_ARGS%

@rem copy the lib folder 
copy  lib %LIB_HOME%

@rem setting classpath
set CLASSPATH=%APP_HOME%\lib\*

@rem Execute MovieMon
"%JAVA_EXE%" -cp %CLASSPATH% imdb.TestApp %CMD_LINE_ARGS%