# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
############################### 第一部分 公共########################################
-dontwarn okio.**
#忽略警告
-ignorewarnings
#-dontwarn
#指定代码的压缩级别
-optimizationpasses 5
#包名不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件.因为要过滤logcat,所以屏蔽掉
-dontoptimize
##预校验
-dontpreverify
#混淆时是否记录日志
-verbose
#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
##保护注解
-keepattributes *Annotation*


##############################日志数据  build/outputs中查看#############################

#未混淆的类和成员  被各种-keep选项保留的类和成员变量信息
-printseeds seeds.txt
#列出从 apk 中删除的代码  保存 unused or dead code 的信息
-printusage unused.txt
#描述apk内所有class文件的内部结构
#-printdump dump.txt
#列出源代码中被删除在apk中不存在的代码

#表示混淆前后代码的对照表，这个文件非常重要。源码到混淆之后代码的映射信息
#如果你的代码混淆后会产生bug的话，log提示中是混淆后的代码，希望定位到源代码的话就可以根据mapping.txt反推。
-printmapping mapping.txt
-printmapping build/outputs/mapping/release/mapping.txt

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature
-keepattributes InnerClass

###############################本项目 混淆编写部分###################################################

# 编译时删除Log代码  android.util.Log
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static int e(...);
    public static *** wtf(...);
    public static *** println(...);
}

############不混淆第三方库###########

#Android 6.0权限混淆
-dontwarn com.zhy.m.**
-keep class com.zhy.m.** {*;}
-keep interface com.zhy.m.** { *; }
-keep class **$$PermissionProxy { *; }
#支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#微信
-keep class com.tencent.wxop.**{*;}
-keep class com.tencent.mm.sdk.** { *;}
#银联支付
-keep class com.unionpay.**{*;}
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.**{*;}
#保证百度类不能被混淆，否则会出现网络不可用等运行时异常
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-keep class com.baidu.location.** {*;}
-dontwarn com.baidu.**
#nineoldandroids
-keep class com.nineoldandroids.** { *;}
#android-logging-log4j-1.0.3.jar
-keep class de.mindpipe.android.logging.log4j.LogConfigurator.** { *;}
#log4j-1.2.17.jar
-keep class org.apache.log4j.** { *;}
#universal-image-loader-1.9.3.jar
-keep class com.nostra13.universalimageloader.** { *;}
#lite-http-2.2.0.jar
-keep class com.litesuits.http.** { *;}
#v4包和v7包
-dontwarn android.support.**
-keep class android.support.** { *;}
-keep interface android.support.app.** { *; }
#xUtils
-keep class org.xutils.** { *; }
#Gson
-keep class com.google.gson.** { *; }

############不混淆库文件和实体类，Activity,Fragment##########
-keep class com.baidu.mapapi.clusterutil.** { *; }
-keep class com.com.zbar.lib.** { *; }
-keep class com.zhichen.parking.lib.** { *; }
-keep class com.zhichen.parking.library.** { *; }

-keep class com.zhichen.parking.modle.** { *; }



############其他##########
#自定义控件
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
#不混淆资源类
-keep class **.R$* {*;}
-keepclassmembers class **.R$* {
    public static <fields>;
}
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}