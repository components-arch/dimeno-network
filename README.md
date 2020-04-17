## dimeno-network
基于OkHttp3网络框架

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
GET  
自定义Task继承 **GetTask**  
```java
new TestGetTask(new LoadingCallback<String>() {
    @Override
    public void onSuccess(PluginVersion data) {
        
    }

    @Override
    public void onError(int code, String message) {
        
    }
}).exe();
``` 
POST_JSON  
自定义Task继承 **PostJsonTask**  
```java
new TestPostJsonTask(new LoadingCallback<String>() {
    @Override
    public void onSuccess(PluginVersion data) {
        
    }

    @Override
    public void onError(int code, String message) {
        
    }
}).exe();
``` 
POST_FORM  
自定义Task继承 **PostFormTask**  
```java
new TestPostFormTask(new LoadingCallback<String>() {
    @Override
    public void onSuccess(PluginVersion data) {
        
    }

    @Override
    public void onError(int code, String message) {
        
    }
}).exe();
``` 

UPLOAD  
自定义Task继承 **UploadTask**  
```java
new TestUploadTask(new ProgressCallback<String>() {
    @Override
    public void onSuccess(PluginVersion data) {
        
    }
    
    @Override
    public void onProgress(int progress) {
        
    }
    
    @Override
    public void onError(int code, String message) {
        
    }
}).exe();
``` 
