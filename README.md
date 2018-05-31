# 针对weex的简单Android手势密码解锁插件


说明：
====================================
本插件是针对weex做的一个手势密码解锁插件，具体的效果可以查看[基于eros的手势密码Demo](https://github.com/shawn-tangsc/eros-gesture-demo)。

ios端请看[ios手势密码解锁](https://github.com/shawn-tangsc/WeexPlugin-HMGesUnlock).

引用方式：
====================================
**Step 1.** 在project/build.gradle中添加

```
	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```

**Step 2.** 在app/build.gradle中添加

```
	dependencies {
	        implementation 'com.github.PoisonousMilkPowder:WeexPlugin-HMGesUnlock:v1.0.2'
	}
```
__注意__

目前只可以替换默认的背景图片

在`app/src/main/res/drawable`下放入自己的背景图片，名称为`gesture_bg`

weex中的使用方式：
====================================


1. 在需要使用的文件中引入该mudule

	
```
const hmGesUnlock = weex.requireModule('hmGesUnlock');
```

2. 创建手势密码

	
```
## 成功后会回调
hmGesUnlock.addGesturePage(function (flag) {
    ...
});
```
	
3. 验证手势密码

	
```
## 成功后会回调
hmGesUnlock.checkGesturePage(function (flag) {
    ...
});
```
