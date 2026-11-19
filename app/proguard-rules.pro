# ProGuard / R8 rules for Calcsy

# Keep all classes and members in com.example.calcsy package
-keep class com.example.calcsy.** { *; }
-keepclassmembers class com.example.calcsy.** { *; }

# Keep Android view constructors
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
