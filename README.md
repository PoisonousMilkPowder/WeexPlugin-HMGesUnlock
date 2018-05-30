# 针对weex的简单Android手势密码解锁插件


说明：
====================================
本插件是针对weex做的一个手势密码解锁插件，具体的效果可以查看[基于eros的手势密码Demo](https://github.com/shawn-tangsc/eros-gesture-demo)。

引用方式：
====================================

1. 从git 下载到本地

```
## 进入你weex项目中的android根目录，执行git clone
git clone https://github.com/PoisonousMilkPowder/WeexPlugin-HMGesUnlock.git unlock

```

2. 修改`settings.gradle`文件

```
 include ':app', ':unlock'
```

3. 修改`app/build.gradle`文件

``` 
dependencies {
	...
    compile project(':unlock')
}

```
__注意__

	目前外部只可以替换默认的背景图片

	在`app/src/main/res/drawable`下放入自己的背景图片，名称为`gesture_bg`

weex中的使用方式：
====================================


1. 在需要使用的文件中引入该mudule

	
```
const hmGesUnlock = weex.requireModule('hmGesUnlock');
```

2. 添加手势密码

	
```
## 在回调中会返回成功还是失败的Bool结果
hmGesUnlock.addGesturePage(function (flag) {
    ...
});
```
	
3. 验证手势密码

	
```
## 在回调中会返回成功还是失败的Bool结果
hmGesUnlock.checkGesturePage(function (flag) {
    ...
});
```
