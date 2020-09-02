# advmeds-keyboard-lib

[![](https://jitpack.io/v/advmeds-service/advmeds-keyboard-lib.svg)](https://jitpack.io/#advmeds-service/advmeds-keyboard-lib)

## Usage:

Setting the `"textCapCharacters"` input type for EditText:

![](https://github.com/advmeds-service/advmeds-keyboard-lib/blob/master/images/keyboard_id.png)

Setting the `"textPassword"`、`"textVisiblePassword"` input type for EditText:

![](https://github.com/advmeds-service/advmeds-keyboard-lib/blob/master/images/keyboard_password.png)

Setting the `"number"`、`"phone"` input type for EditText:

![](https://github.com/advmeds-service/advmeds-keyboard-lib/blob/master/images/keyboard_number.png)

In addition to the above:

![](https://github.com/advmeds-service/advmeds-keyboard-lib/blob/master/images/keyboard_qwerty.png)

## Installation:

1.Add it to your root build.gradle

    allprojects {
	    repositories {
		    ...
		    maven { url 'https://jitpack.io' }
        }
    }
    
2.Add the dependency to your app module's build.gradle

	dependencies {
	        implementation 'com.github.advmeds-service:advmeds-keyboard-lib:version'
	}

## Set default input method:

If device is rooted, you can set default input method with [`RootUtils`](https://github.com/advmeds-service/advmeds-keyboard-lib/blob/master/app/src/main/java/com/advmeds/customkeyboard/RootUtils.kt).

```kotlin
Handler().post {
	if (RootUtils.isDeviceRooted && RootUtils.canRunRootCommands()) {
		val allIMEs = (application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).inputMethodList
                val mIME = allIMEs.first { it.id.contains(BuildConfig.APPLICATION_ID) && it.id.contains(MyInputMethodService::class.java.simpleName) }
                RootUtils.excute("su -c ime enable ${mIME.id}")
                RootUtils.excute("su -c ime set ${mIME.id}")
	}
}
```
