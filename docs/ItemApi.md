# ItemApi

All URIs are relative to *https://taptosnap.nonprod.kube.lab49cloud.com/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getItems**](ItemApi.md#getItems) | **GET** /item/list | Get the list of items
[**uploadImage**](ItemApi.md#uploadImage) | **POST** /item/image | Upload image for the given item


<a name="getItems"></a>
# **getItems**
> kotlin.Array&lt;Item&gt; getItems()

Get the list of items

Get the list of items

### Example
```kotlin
// Import classes:
//import com.lab49.taptosnap.infrastructure.*
//import com.lab49.taptosnap.models.*

val apiInstance = ItemApi()
try {
    val result : kotlin.Array<Item> = apiInstance.getItems()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ItemApi#getItems")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ItemApi#getItems")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;Item&gt;**](Item.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="uploadImage"></a>
# **uploadImage**
> UploadImageResponse uploadImage(imageLabel, image)

Upload image for the given item

Upload image for the given item

### Example
```kotlin
// Import classes:
//import com.lab49.taptosnap.infrastructure.*
//import com.lab49.taptosnap.models.*

val apiInstance = ItemApi()
val imageLabel : kotlin.String = imageLabel_example // kotlin.String | 
val image : java.io.File = BINARY_DATA_HERE // java.io.File | 
try {
    val result : UploadImageResponse = apiInstance.uploadImage(imageLabel, image)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ItemApi#uploadImage")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ItemApi#uploadImage")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **imageLabel** | **kotlin.String**|  |
 **image** | **java.io.File**|  |

### Return type

[**UploadImageResponse**](UploadImageResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json

