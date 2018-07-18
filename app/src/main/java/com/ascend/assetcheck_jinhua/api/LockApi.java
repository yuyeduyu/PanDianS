package com.ascend.assetcheck_jinhua.api;

import com.ascend.assetcheck_jinhua.result.LoadResultBack;
import com.ascend.assetcheck_jinhua.result.getLoadTaskResultBack;
import com.ascend.assetcheck_jinhua.result.upLoadResult;

import java.util.Map;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 作者：lishanhui on 2018-05-21.
 * 描述：
 */

public interface LockApi {
//    //房源同步接口
//    @FormUrlEncoded
//    @POST("")
//    Observable<HttpResponseByDescBean> SynchroData(@FieldMap Map<String, Object> map);
//
    //登录
    @FormUrlEncoded
    @POST("login.do")
    Observable<BaseResult> Login(@Field("userName") String userName, @Field("userPassword") String userPassword);

    // 下载盘点任务
    @POST("getTask.do")
    Observable<LoadResultBack> getLoadTask();

  // 盘点
  @FormUrlEncoded
    @POST("downloadTask.do")
    Observable<getLoadTaskResultBack> downloadTask(@Field("id") String id);
    // 上传盘点数据
    @FormUrlEncoded
    @POST("uploadAppResult.do")
    Observable<upLoadResult> upLoadTask(@Field("result") String id);
}
