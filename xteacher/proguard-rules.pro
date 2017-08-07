# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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

    -optimizationpasses 5

    -dontusemixedcaseclassnames

    -dontskipnonpubliclibraryclasses

    -dontoptimize

    -dontpreverify

    -verbose

    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    -keepattributes *Annotation*


    -keep class tv.danmaku.ijk.** { *; }
    -dontwarn tv.danmaku.ijk.**
    -keep class com.shuyu.gsyvideoplayer.** { *; }
    -dontwarn com.shuyu.gsyvideoplayer.**

    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService
    -keep public class * extends android.support.v4.app.Fragment

    -keep class pro.ui.activity.EducationActivity{*;}

    -ignorewarning

    -dump class_files.txt

    -printseeds seeds.txt

    -printusage unused.txt

    -printmapping mapping.txt


    -keep class com.lippi.recorder.iirfilterdesigner.** {*; }

    -keep class com.umeng.**{*;}


    -dontwarn com.lippi.recorder.utils**

    -keep class com.lippi.recorder.utils.** {
        *;
     }

    -keep class  com.lippi.recorder.utils.AudioRecorder{*;}

    -dontwarn android.support.**

    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    -keepclasseswithmembernames class * {
        native <methods>;
    }

    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    -keepnames class * implements java.io.Serializable

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

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    -keepattributes Signature
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.examples.android.model.** { *; }

    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    -keep public class * implements java.io.Serializable {*;}

    -keepclassmembers class ** {
        public void onEvent*(**);
    }
    # Only required if you use AsyncExecutor
    -keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
        <init>(java.lang.Throwable);
    }

    -keep class butterknife.** { *; }
    -dontwarn butterknife.internal.**
    -keep class **$$ViewBinder { *; }
    -keepclasseswithmembernames class * {
        @butterknife.* <fields>;
    }
    -keepclasseswithmembernames class * {
        @butterknife.* <methods>;
    }

    # Produces useful obfuscated stack traces
    # http://proguard.sourceforge.net/manual/examples.html#stacktrace
    -renamesourcefileattribute SourceFile
    -keepattributes SourceFile,LineNumberTable

     #    环信
    -keep class com.hyphenate.** {*;}
    -dontwarn  com.hyphenate.**

    -keep class android.support.v4.** {*;}

    -keep class org.xmlpull.** {*;}
    -keep class com.baidu.** {*;}
    -keep public class * extends com.umeng.**
    -keep class com.umeng.** { *; }
    -keep class com.squareup.picasso.* {*;}

    -keep class com.hyphenate.* {*;}
    -keep class com.hyphenate.chat.** {*;}
    -keep class org.jivesoftware.** {*;}
    -keep class org.apache.** {*;}
    #另外，demo中发送表情的时候使用到反射，需要keep SmileUtils,注意前面的包名，
    #不要SmileUtils复制到自己的项目下keep的时候还是写的demo里的包名
    -keep class com.hyphenate.chatuidemo.utils.SmileUtils {*;}

    #2.0.9后加入语音通话功能，如需使用此功能的api，加入以下keep
    -keep class net.java.sip.** {*;}
    -keep class org.webrtc.voiceengine.** {*;}
    -keep class org.bitlet.** {*;}
    -keep class org.slf4j.** {*;}
    -keep class ch.imvs.** {*;}

#    -keep class com.baidu.** {*;}
#    -keep class vi.com.** {*;}
#    -dontwarn com.baidu.**

    #百度
    -keep class com.baidu.** {*;}
    -keep class vi.com.** {*;}

    -keep class com.sinovoice.** {*;}
    -keep class pvi.com.** {*;}


    -dontwarn com.baidu.**
    -dontwarn vi.com.**
    -dontwarn pvi.com.**

    -keep class tv.danmaku.ijk.** { *; }
    -dontwarn tv.danmaku.ijk.**
    -keep class com.shuyu.gsyvideoplayer.** { *; }
    -dontwarn com.shuyu.gsyvideoplayer.**

