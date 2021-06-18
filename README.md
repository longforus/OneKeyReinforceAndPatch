# 一键实现打release包 -> 360加固 -> 存档,并在后期一键实现针对某一版本生成Sophix补丁的gradle脚本 

最近升级项目把Bugly-Tinker换成了Sophix,因为使用了阿里的tlog服务,之前用Bugly的更新服务,现在也没有使用了,索性就把CrashReport和Hotfix都切过来吧,还有一点是Bugly-Tinker的更新比较不及时,AGP的版本需要留在3.5.3才能有比较好的体验,升级了之后需要诸多调整,而且补丁也从3.5.3的几KB增加到了几百KB,从服务支持上来说,阿里也好很多,有问题也比较好咨询.我现在粉阿里了.用了好几年的Bugly-Tinker,也非常感谢.

## 情况

迁移之前呢,Bugly-Tinker是使用gradle task来生成补丁包,配置虽然繁琐一点,用起来还是比较方便的,切到Sophix后,可以使用有图形界面的SophixPatchTool来生成补丁,有的人喜欢图形界面的直观,而我喜欢一点就好的畅快,用图形界面也有一点繁琐,每次要选包,填密码什么的,不方便.好在SophixPatchTool也是支持命令行生成的,虽然还有一些小问题,还好我都克服了:

### 存在的小问题

1. windows版本的SophixPatchTool在命令行下不工作```:

    ![](README.assets/nowork.png)

    什么也没输出,之前就没有搞定,可能是我的终端的问题吧,未深究.前段时间用黑苹果没有这个问题,可是后来黑苹果🍎用腻了,又换回win10,这下怎么办呢?

    别忘了"win10是最好的Linux发行版",用wsl就行了啊,所以我接下来的**补丁生成操作都在wsl2的Ubuntu20.4中运行**.没有的请自行百度安装wsl.其他系统用户请自行,修改相关代码.

2. 命令行模式下不支持资源对比.官方回复:`命令行使用时，对资源的对比有些问题，如果需要资源对比，先用图形化界面。不对比资源，再用命令行试下`,所以在资源文件有改动的时候还是需要手动图形界面操作,还好,我目前还没有这种情况,这也导致后续的`profile.json`中需要配置`"isIgnoreRes": true`.

3. 这是删除了`productFlavors`的版本,我的项目是支持2个`flavorDimension`的,支持按不同的flavor组合,分别存档和补丁,但是配置相对复杂很多,每个人的情况也不一样,需要的话可以自行支持,或者后面有机会我也可以搞一个示例.

## 功能

### 一键实现打release包 -> 360加固 -> 存档

配置好以后,只需要执行`:app:assembleRelease`任务,就可以实现:

```tex

> Task :app:assembleRelease
C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release
开始360加固
待加固APK路径=C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release\app-release.apk
sdkPath = C:\Android\Sdk
JiaguJarPath = D:\360jiagubao_windows_64\jiagu\jiagu.jar
apkBakupPath = D:\releasePackageBackup\testOneKey\5702
java -jar D:\360jiagubao_windows_64\jiagu\jiagu.jar -update
java -jar D:\360jiagubao_windows_64\jiagu\jiagu.jar -config -x86
java -jar D:\360jiagubao_windows_64\jiagu\jiagu.jar -jiagu C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release\app-release.apk C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release

################################################
#                                              #
#        ## #   #    ## ### ### ##  ###        #
#       # # #   #   # #  #  # # # #  #         #
#       ### #   #   ###  #  # # ##   #         #
#       # # ### ### # #  #  ### # # ###        #
#                                              #
# Obfuscation by Allatori Obfuscator v5.6 DEMO #
#                                              #
#           http://www.allatori.com            #
#                                              #
################################################

checking new version...
已经是最新版本

################################################
#                                              #
#        ## #   #    ## ### ### ##  ###        #
#       # # #   #   # #  #  # # # #  #         #
#       ### #   #   ###  #  # # ##   #         #
#       # # ### ### # #  #  ### # # ###        #
#                                              #
# Obfuscation by Allatori Obfuscator v5.6 DEMO #
#                                              #
#           http://www.allatori.com            #
#                                              #
################################################

已选增强服务：  X86架构
config saving succeed.

################################################
#                                              #
#        ## #   #    ## ### ### ##  ###        #
#       # # #   #   # #  #  # # # #  #         #
#       ### #   #   ###  #  # # ##   #         #
#       # # ### ### # #  #  ### # # ###        #
#                                              #
# Obfuscation by Allatori Obfuscator v5.6 DEMO #
#                                              #
#           http://www.allatori.com            #
#                                              #
################################################

loading success
begin jiagu task
prepare to upload
上传开始
上传进度0%
上传进度2%
上传进度4%
......
上传进度100%
上传成功
基础加固服务：DEX文件加密，防二次打包，APK大小优化，防DEX内存截取，DEX VMP保护
已选增强服务：  X86架构
加固中...
加固中...
加固中...
加固中...
加固完成加固成功
下载开始
time out HttpException Forbidden true  https://beijing.xstore.qihu.com/jiaguenc/-release_3513a5cd_enc.apk?response-content-disposition=attachment;%20filename=-release_3513a5cd_enc.apk&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=0179f985423cffd8cce4b753ff7aded2/20210616/beijing/s3/aws4_request&X-Amz-Date=20210616T092154Z&X-Amz-SignedHeaders=host&X-Amz-Expires=7200&X-Amz-Signature=518045eb4f3e86d11a452d5e191258df1574b461b57628a5712441de999d89c3
下载失败403 Forbidden
下载进度，使用代理重试下载
下载开始
下载进度0%
下载进度2%
下载进度11%
下载进度86%
下载进度100%
下载成功
任务完成_已加固
C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release\app-release_570
开始对齐
C:\Android\Sdk\build-tools\30.0.3\zipalign.exe -v -p 4 C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release\app-release_570_jiagu.apk C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release\app-release_570_jiagu_align.apk
Verifying alignment of C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release\app-release_570_jiagu_align.apk (4)...
      49 AndroidManifest.xml (OK - compressed)
    1271 META-INF/androidx.activity_activity.version (OK - compressed)
    1369 META-INF/androidx.annotation_annotation-experimental.version (OK - compressed)
    1462 META-INF/androidx.appcompat_appcompat-resources.version (OK - compressed)
    1545 META-INF/androidx.appcompat_appcompat.version (OK - compressed)
    1631 META-INF/androidx.arch.core_core-runtime.version (OK - compressed)
    1712 META-INF/androidx.cardview_cardview.version (OK - compressed)
 ......
 4131513 res/yP.xml (OK - compressed)
 4132104 res/yf.xml (OK - compressed)
 4132438 res/yx.xml (OK - compressed)
 4132903 res/z1.xml (OK - compressed)
 4133141 res/z3.xml (OK - compressed)
 4133409 res/zH.xml (OK - compressed)
 4133959 res/zH1.xml (OK - compressed)
 4134506 res/zH2.xml (OK - compressed)
 4134977 res/zq.xml (OK - compressed)
 4135408 resources.arsc (OK)
Verification succesful
开始签名
 C:\Android\Sdk\build-tools\30.0.3\apksigner.bat sign --ks C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch/test.jks --ks-pass pass:123456 --v1-signing-enabled --v2-signing-enabled --ks-key-alias key0 --pass-encoding ibm437 --in C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release\app-release_570_jiagu_align.apk --out C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build\outputs\apk\release/5702.apk 
加固结束
备份apk到:D:\releasePackageBackup\testOneKey\5702
备份mapping.txt到:D:\releasePackageBackup\testOneKey\5702
全部结束

Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.7.1/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 1m 11s
37 actionable tasks: 8 executed, 29 up-to-date
17:22:06: Task execution finished ':app:assembleRelease'.

```

#### 配置

1. 下载安装360加固保,需要登录成功保存密码一次.

2. 在项目根目录的`local.properties`中配置相应的路径:

    ```properties
    jiagu.dir=D\:\\360jiagubao_windows_64\\jiagu\\jiagu.jar
    releaseApkBackupDir=D\:\\releasePackageBackup\\testOneKey
    ```

3. 运行`:app:assembleRelease`就OK了,完成后会自动打开存档文件夹.



### 一键实现针对某一版本生成Sophix补丁

​	配置好以后,只需要执行`:app:buildSophixPatch`任务,就可以实现:

```tex
......
> Task :app:assembleRelease

> Task :app:buildSophixPatch
buildDir = C:\Users\void\AndroidStudioProjects\OneKeyReinforceAndPatch\app\build

 {
     "AesKey": "",
     "ClassFilterFilePath": "",
     "KeyAlias": "key0",
     "KeyPassword": "123456",
     "KeyStorePassword": "123456",
     "KeyStorePath": "/mnt/c/Users/void/AndroidStudioProjects/OneKeyReinforceAndPatch/test.jks",
     "NewApkPath":"/mnt/c/Users/void/AndroidStudioProjects/OneKeyReinforceAndPatch/app/build/outputs/apk/release/app-release.apk",
     "OldApkPath": "/mnt/d/releasePackageBackup/testOneKey/5702/app-release.apk",
     "OutputDirPath": "/mnt/d/releasePackageBackup/testOneKey/5702",
     "isForceColdFix": false,
     "isIgnoreRes": true,
     "isIgnoreSo": false
 }
 
cmd /c wsl /mnt/d/SophixPatchToolLinux-3.2.5/SophixPatchTool --profiles /mnt/d/SophixPatchToolLinux-3.2.5/profiles.json
xlxl dexjar "/mnt/d/SophixPatchToolLinux-3.2.5/SophixDex.jar" true
("-Dfile.encoding=UTF-8", "-jar", "/mnt/d/SophixPatchToolLinux-3.2.5/SophixDex.jar", "-c", "patch", "-s", "/mnt/d/releasePackageBackup/testOneKey/5702/app-release.apk", "-f", "/mnt/c/Users/void/AndroidStudioProjects/OneKeyReinforceAndPatch/app/build/outputs/apk/release/app-release.apk", "-w", "/mnt/d/releasePackageBackup/testOneKey/5702", "-k", "/mnt/c/Users/void/AndroidStudioProjects/OneKeyReinforceAndPatch/test.jks", "-p", "123456", "-a", "key0", "-e", "123456")
":prepare"
"  tool version: 3.0.0"
"  Param{srcApkPath:'/mnt/d/releasePackageBackup/testOneKey/5702/app-release.apk', fixedApkPath:'/mnt/c/Users/void/AndroidStudioProjects/OneKeyReinforceAndPatch/app/build/outputs/apk/release/app-release.apk', patchOut:'/mnt/d/releasePackageBackup/testOneKey/5702', signFilePath:'/mnt/c/Users/void/AndroidStudioProjects/OneKeyReinforceAndPatch/test.jks', signFilePass:'*****', signAlias:'*****', 
......
":aesEncrypt 0ms"
"Patch path:/mnt/d/releasePackageBackup/testOneKey/5702/sophix-patch.jar"
"Patch size:2KB"
"Total time:22682ms"
"BUILD SUCCESS"
Process execute ok

Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.7.1/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 34s
37 actionable tasks: 7 executed, 30 up-to-date
17:05:35: Task execution finished ':app:buildSophixPatch'.

```

#### 配置

1. 如果是win10的话,安装wsl,我的环境是wsl2+Ubuntu20.4,其他系统自行调整相关代码.

2. 在项目根目录的`local.properties`中配置相应的路径:

    ```properties
    releaseApkBackupDir=D\:\\releasePackageBackup\\testOneKey
    patchTool.dir=D\:\\SophixPatchToolLinux-3.2.5
    ```

3. app的`build.gradle`中混淆配置`'proguard-rules-append.pro'`:

    ```groovy
    release {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro','proguard-rules-append.pro'
        signingConfig signingConfigs.config
    }
    ```

    这个用于自动处理mapping.txt文件配置,保证打补丁的混淆和基线包一致.`proguard-rules-append.pro`可以是空文件.

4. 在`jiaguAndPatch.gradle`文件中,制定要修复的版本号:

    ```groovy
    ......
    //需要补的版本
    def oldVersion = "5702"
    def patchToolDir =properties.getProperty("patchTool.dir").toString()
    def taskName = gradle.startParameter.taskNames
    ......
    ```

    注意这个版本需要是按照脚本的规则存档到对应的目录的.这样才能自动读取到基线包和mapping文件.

5. 在`jiaguAndPatch.gradle`中配置自己的签名相关信息,jks文件默认放在项目根目录.

    ```json
    {
         "KeyAlias": "key0",
         "KeyPassword": "123456",
         "KeyStorePassword": "123456",
         "KeyStorePath": "${winPath2wslPath(rootProject.rootDir.absolutePath)}/test.jks",
    }
    ```

    

6. 运行`:app:buildSophixPatch`就OK了,完成后会自动打开存档文件夹.`sophix-patch.jar`就是我们要的补丁了.



## 结语

不能满足需求的请自行修改适配,如果对你有帮助欢迎star:star:,你的支持是我的动力,谢谢. :heart_eyes:

项目链接:[https://github.com/longforus/OneKeyReinforceAndPatch](https://github.com/longforus/OneKeyReinforceAndPatch)



![](README.assets/qqpyimg1623837913.jpg)