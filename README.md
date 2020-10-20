# dimeno-network
>基于OkHttp3网络框架

[![Platform](https://img.shields.io/badge/Platform-Android-00CC00.svg?style=flat)](https://www.android.com)
[![](https://jitpack.io/v/dimeno-tech/dimeno-network.svg)](https://jitpack.io/#dimeno-tech/dimeno-network)

### 依赖导入

项目根目录

``` gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

模块目录

``` gradle
dependencies {
	implementation 'com.github.dimeno-tech:dimeno-network:0.0.9'
}
```

### 自定义全局配置
> 如未初始化，网络请求请写明全路径

```java
Network.init(NetConfig.Builder().build());
```
NetConfig支持方法

```java
public static class Builder {
    public Builder baseUrl(String baseUrl);
    public Builder netInterceptor(Interceptor interceptor);
    public Builder interceptor(Interceptor interceptor);
    public CookieJar cookieJar;
    public Builder retryOnConnectionFailure(boolean retry);
    public Builder connectTimeout(long connectTimeout);
    public Builder readTimeout(long readTimeout);
    public Builder writeTimeout(long writeTimeout);
    public NetConfig build();
}
```

### 请求类型

| 类型 |继承关系|
| :---: | :---: |
| GET | GetTask |
| POST_JSON | PostJsonTask |
| POST_FORM | PostFormTask |
| UPLOAD | UploadTask |

网络请求Task支持方法

``` java
public interface Task {
    void onSetupParams(Object... params);
    String getApi();
    Task put(String key, Object value);
    Task putFile(String key, String filePath);
    Task addHeader(String name, String value);
    Task setTag(Object tag);
    Task setLoadingPage(LoadingPage page);
    Call exe(Object... params);
    Call retry();
}
```

### 发起请求
1、使用可变参数形式请求

```java
new TestTask(new LoadingCallback<EntityType>() {
    @Override
    public void onSuccess(EntityType data) {
        
    }
}).exe("abc", 1, true);
```
重写onSetupParams方法手动处理参数

```java
public class TestTask extends GetTask {
    public <EntityType> TestTask(RequestCallback<EntityType> callback) {
        super(callback);
    }

    @Override
    public void onSetupParams(Object... params) {
        put("param1", params[0]);
        put("param2", params[1]);
        put("param3", params[2]);
    }

    @Override
    public String getApi() {
        return "your api address";
    }
}
```
2、链式传递参数

```java
new TestTask(new ProgressCallback<String>() {
	@Override
	public void onSuccess(String data) {

	}

	@Override
	public void onProgress(int progress) {

	}
}).setTag(this)
	.put("token", token)
	.putFile("sourceFile", "file url")
	.addHeader("","")
	.exe();
```
无需重写onSetupParams方法

### 设置加载页
setLoadingPage(LoadingPage page)
> **注意**：  
> 1、page为null无效  
> 2、page构造参数为null无效

1、使用默认加载页 **DefaultLoadingPage**

``` java
new TestTask(new LoadingCallback<String>() {
    @Override
    public void onSuccess(String data) {
    
    }
}).setLoadingPage(new DefaultLoadingPage(recycler)).exe();
```

2、自定义加载页

继承 **AbsLoadingPage** 并处理对应逻辑，如有疑问请参照 **DefaultLoadingPage**