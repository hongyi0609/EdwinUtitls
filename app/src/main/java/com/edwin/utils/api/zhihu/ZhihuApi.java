package com.edwin.utils.api.zhihu;


import com.edwin.utils.bean.UpdateItem;
import com.edwin.utils.bean.image.ImageResponse;
import com.edwin.utils.bean.zhihu.ZhihuDaily;
import com.edwin.utils.bean.zhihu.ZhihuStory;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Edwin on 2016/3/6 0006.
 * @author Edwin
 */
public interface ZhihuApi {

    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDaily();

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> getTheDaily(@Path("date") String date);

    @GET("/api/4/news/{id}")
    Observable<ZhihuStory> getZhihuStory(@Path("id") String id);

    @GET("http://lab.zuimeia.com/wallpaper/category/1/?page_size=1")
    Observable<ImageResponse> getImage();

    @GET("http://caiyao.name/releases/MrUpdate.json")
    Observable<UpdateItem> getUpdateInfo();
}
