package com.conan.xconan.net.strategy;

import com.conan.xconan.net.core.ApiCache;
import com.conan.xconan.net.mode.CacheResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Description: 缓存策略--优先网络
 * @author: Conan
 * @date: 16/12/31 14:32.
 */
public class FirstRemoteStrategy<T> extends CacheStrategy<T> {
    @Override
    public <T> Observable<CacheResult<T>> execute(ApiCache apiCache, String cacheKey, Observable<T> source, Class<T> clazz) {
        Observable<CacheResult<T>> cache = loadCache(apiCache, cacheKey, clazz);
        cache.onErrorReturn(new Func1<Throwable, CacheResult<T>>() {
            @Override
            public CacheResult<T> call(Throwable throwable) {
                return null;
            }
        });
        Observable<CacheResult<T>> remote = loadRemote(apiCache, cacheKey, source);
        return Observable.concat(remote, cache).firstOrDefault(null, new Func1<CacheResult<T>, Boolean>() {
            @Override
            public Boolean call(CacheResult<T> tResultData) {
                return tResultData != null && tResultData.getCacheData() != null;
            }
        });
    }
}
