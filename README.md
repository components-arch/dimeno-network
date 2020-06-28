## dimeno-network
基于OkHttp3网络框架

[![Platform](https://img.shields.io/badge/Platform-Android-00CC00.svg?style=flat)](https://www.android.com)
[![](https://jitpack.io/v/dimeno-tech/dimeno-network.svg)](https://jitpack.io/#dimeno-tech/dimeno-network)

#### 自定义全局配置
> 可不初始化，此时将使用默认配置，每个网络请求必须写明全路径
```java
Network.init(NetConfig.Builder().build());
```
NetConfig支持方法
```java
public static class Builder {
    public Builder baseUrl(String baseUrl);
    public Builder interceptor(Interceptor interceptor);
    public Builder retryOnConnectionFailure(boolean retry);
    public Builder connectTimeout(long connectTimeout);
    public Builder readTimeout(long readTimeout);
    public Builder writeTimeout(long writeTimeout);
    public NetConfig build();
}
``` 

#### 发起请求
GET  继承 **GetTask**  
```java
new TestGetTask(new LoadingCallback<EntityType>() {
    @Override
    public void onSuccess(EntityType data) {
        
    }

    @Override
    public void onError(int code, String message) {
        
    }
}).exe();
``` 
POST_JSON  自定义Task继承 **PostJsonTask**    
```java
new TestPostJsonTask(new LoadingCallback<EntityType>() {
    @Override
    public void onSuccess(EntityType data) {
        
    }

    @Override
    public void onError(int code, String message) {
        
    }
}).exe();
``` 
POST_FORM  自定义Task继承 **PostFormTask**    
```java
new TestPostFormTask(new LoadingCallback<EntityType>() {
    @Override
    public void onSuccess(EntityType data) {
        
    }

    @Override
    public void onError(int code, String message) {
        
    }
}).exe();
``` 

UPLOAD  自定义Task继承 **UploadTask**    
```java
new TestUploadTask(new ProgressCallback<EntityType>() {
    @Override
    public void onSuccess(EntityType data) {
        
    }
    
    @Override
    public void onProgress(int progress) {
        
    }
    
    @Override
    public void onError(int code, String message) {
        
    }
}).exe();
``` 
