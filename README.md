# dimeno-network
基于OkHttp3网络框架

[![Platform](https://img.shields.io/badge/Platform-Android-00CC00.svg?style=flat)](https://www.android.com)
[![](https://jitpack.io/v/dimeno-tech/dimeno-network.svg)](https://jitpack.io/#dimeno-tech/dimeno-network)

### 自定义全局配置
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

### 请求类型
<table style="width:100%;text-align:center">
    <th>类型</th>
    <th>继承关系</th>
    <tr>
        <td>GET</td>
        <td>GetTask</td>
    </tr>
    <tr>
        <td>POST_JSON</td>
        <td>PostJsonTask</td>
    </tr>
    <tr>
        <td>POST_FORM</td>
        <td>PostFormTask</td>
    </tr>
    <tr>
        <td>UPLOAD</td>
        <td>UploadTask</td>
    </tr>
</table>

网络请求Task支持方法

``` java
public interface Task {
    Call exe(Object... params);
    Call retry();
    void onSetupParams(Object... params);
    Task setTag(Object tag);
    String getApi();
    Task put(String key, Object value);
    Task putFile(String key, String filePath);
    Task addHeader(String name, String value);
}
```

### 发起请求
1、使用可变参数形式请求

```java
new TestGetTask(new LoadingCallback<EntityType>() {
    @Override
    public void onSuccess(EntityType data) {
        
    }
}).exe("abc", 1, true);
```
重写onSetupParams方法手动处理参数

```java
public class TestGetTask extends GetTask {
    public <EntityType> TestGetTask(RequestCallback<EntityType> callback) {
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
new TestUploadTask(new ProgressCallback<String>() {
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