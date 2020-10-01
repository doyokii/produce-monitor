@echo off & setlocal enabledelayedexpansion

::####################################################################################
::打包脚本(windows) v1.0
::####################################################################################

::====应用名、发布目录
set APP_NAME_SRC=monitor-task
set APP_NAME_PUBLISH=monitor-task
set PUBLISH_SERVICE_CONF=monitor-build.properties
set COMMON_CONF_NAME=monitor-task

::====获取绝对路径
set BASE_PATH=%cd%
cd ../
set CODE_PATH=%cd%\%APP_NAME_SRC%
set PUBLISH_PATH=%BASE_PATH%\%APP_NAME_PUBLISH%

::====环境检测
if "%JAVA_HOME%" == "" (
	echo please set JAVA_HOME...
	goto end
)
if "%MAVEN_HOME%" == "" (
	echo please set MAVEN_HOME...
	goto end
)
if "%PATH%" == "" (
	set PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jar\bin;%MAVEN_HOME%\bin;C:\Windows\System32
)
set CLASSPATH=.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar

::====编译打包:MAVEN
color 02
echo ---------------------Start building...--------------------
cd %CODE_PATH%
echo %CODE_PATH%
call mvn -Dmaven.test.skip=true clean install
echo ---------------------- Build done-------------------------

echo --------------Start copy file to target ....-------------- 
::====拷贝发布
cd %BASE_PATH%
if exist %PUBLISH_PATH% (
	del /f /a /s /q %PUBLISH_PATH% >nul
	rd /s /q %PUBLISH_PATH% >nul
	ping -n 1 127.1 >nul
)
md %PUBLISH_PATH%
md %PUBLISH_PATH%\conf
md %PUBLISH_PATH%\lib
echo %PUBLISH_PATH%
ping -n 1 127.1 >nul

copy %CODE_PATH%\%COMMON_CONF_NAME%\src\main\resources\*.* %PUBLISH_PATH%\conf\ >nul


::====读取需要发布的服务
for /f "delims== tokens=1,*" %%a in ('type %PUBLISH_SERVICE_CONF% ^|findstr /i "service"') do  set SERVICE_LIST="%%b"

echo %SERVICE_LIST%

::====拷贝服务文件
set suffix=-assembly
set SERVICE_COUNT=1
:SERVICE_GOON
for /f "delims=, tokens=1,*" %%i in (%SERVICE_LIST%) do (
	   echo %SERVICE_COUNT% Publish service : %%i
	   set folderName=%%i%suffix%
	   echo ==================== code_path %CODE_PATH%
	   echo ==================== folderName !folderName! 
	   echo ==================== code_path+"%%i"+target  %CODE_PATH%\%%i\target\
	   echo ==================== PUBLISH_PATH %PUBLISH_PATH%
	   XCOPY "%CODE_PATH%\target\!folderName!\*"  "%PUBLISH_PATH%\" /S /E /D >nul

	   XCOPY "%CODE_PATH%\target\classes\*.properties"  "%PUBLISH_PATH%\conf\" /Y >nul

	   XCOPY "%CODE_PATH%\bin\*"  "%PUBLISH_PATH%\%%i\bin\" /S /E /D >nul
	   
	   copy %PUBLISH_PATH%\%%i\lib\*.jar  %PUBLISH_PATH%\lib\ >nul
	REM    del /q %PUBLISH_PATH%\%%i\lib\*.jar >nul
	   
	   copy "%CODE_PATH%\%%i\target\%%i.jar" %PUBLISH_PATH%\%%i\lib\ >nul
	   ping -n 1 127.1 >nul
           set SERVICE_LIST="%%j"
	   set /a SERVICE_COUNT+=1
	   
     goto SERVICE_GOON
)
rem del /q %PUBLISH_PATH%\lib\%COMMON_CONF_NAME%-1.0.jar >nul
XCOPY "%CODE_PATH%\bin_quick\*"  "%PUBLISH_PATH%\bin\" /S /E /D >nul

::====加密
::====设置系统环境变量，增加dll路径
rem echo Encrypt core jar...
rem set PATH=%PATH%;%BASE_PATH%\crypt\encrypt
rem java -jar %BASE_PATH%\crypt\encrypt\ClassEncryptor-1.0.jar -src %PUBLISH_PATH%\lib\add-core-service-1.0.jar
rem del /q %PUBLISH_PATH%\lib\javassist-3.18.1-GA.jar >nul
rem del /q %PUBLISH_PATH%\lib\spring-core-4.0.8.RELEASE.jar >nul
rem XCOPY "%BASE_PATH%\crypt\decrypt\*"  "%PUBLISH_PATH%\lib\" /S /E /D >nul
rem echo Encrypt done.

::====生成版本信息
%BASE_PATH%\version\subwcrev.exe %CODE_PATH% %BASE_PATH%\version\VersionTemplate.xx %PUBLISH_PATH%\version.txt

echo Build succeessfull,press space key to exit...
:end
pause
