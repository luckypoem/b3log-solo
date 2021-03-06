/*
 * Copyright (c) 2009, 2010, 2011, 2012, B3log Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.b3log.solo.model;

/**
 * This class defines all cache model relevant keys.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @version 1.0.0.2, Dec 27, 2010
 */
public final class Cache {

    /**
     * Cache.
     */
    public static final String CACHE = "cache";
    /**
     * Cached count.
     */
    public static final String CACHE_CACHED_COUNT = "cacheCachedCount";
    /**
     * Cache hit count.
     */
    public static final String CACHE_HIT_COUNT = "cacheHitCount";
    /**
     * Cache hit bytes.
     */
    public static final String CACHE_HIT_BYTES = "cacheHitBytes";
    /**
     * Cached bytes.
     */
    public static final String CACHE_CACHED_BYTES = "cacheCachedBytes";
    /**
     * Cache miss count.
     */
    public static final String CACHE_MISS_COUNT = "cacheMissCount";

    /**
     * Private default constructor.
     */
    private Cache() {
    }
}
