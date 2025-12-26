@echo off
chcp 65001
echo 正在构建 Maven 目录结构...

set TARGET_DIR=local_repo\mods\flammpfeil\slashblade\SlashBlade_Resharped\1.3.43
set SOURCE_FILE=libs\sb.jar
set TARGET_FILE=SlashBlade_Resharped-1.3.43.jar

if not exist "%SOURCE_FILE%" (
    echo [错误] 没找到 libs\sb.jar ！
    echo 请确保你把下载的 jar 包放在 libs 文件夹里，并改名为 sb.jar
    pause
    exit
)

if not exist "%TARGET_DIR%" (
    echo 创建目录: %TARGET_DIR%
    mkdir "%TARGET_DIR%"
)

echo 正在移动并重命名文件...
copy "%SOURCE_FILE%" "%TARGET_DIR%\%TARGET_FILE%"

if exist "%TARGET_DIR%\%TARGET_FILE%" (
    echo.
    echo [成功] 文件夹结构已修复！
    echo [成功] 文件已就位: local_repo\mods\flammpfeil\slashblade\SlashBlade_Resharped\1.3.43\%TARGET_FILE%
    echo.
    echo 现在你可以回到 IDEA 刷新 Gradle 并运行了。
) else (
    echo [失败] 文件移动失败，请检查权限。
)

pause