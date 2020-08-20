# advmeds-keyboard-lib

[![](https://jitpack.io/v/advmeds-service/advmeds-keyboard-lib.svg)](https://jitpack.io/#advmeds-service/advmeds-keyboard-lib)

## Usage:

1.Add it in your root build.gradle(Project) at the end of repositories:

    allprojects {
	    repositories {
		    ...
		    maven { url 'https://jitpack.io' }
        }
    }
    
2.Add the dependency in your root build.gradle(Module)

	dependencies {
	        implementation 'com.github.advmeds-service:advmeds-keyboard-lib:version'
	}

## Set default input method

If device is rooted, you can set default input method with [`RootUtils`](https://github.com/advmeds-service/advmeds-keyboard-lib/blob/master/app/src/main/java/com/advmeds/customkeyboard/RootUtils.kt).

```kotlin
Handler().post {
            if (RootUtils.isDeviceRooted && RootUtils.canRunRootCommands()) {
                val allIMEs = (application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).inputMethodList
                val mIME = allIMEs.first { it.id.contains(BuildConfig.APPLICATION_ID) && it.id.contains(MyInputMethodService::class.java.simpleName) }
                RootUtils.excute("ime enable ${mIME.id}")
                RootUtils.excute("ime set ${mIME.id}")
            }
        }
```
