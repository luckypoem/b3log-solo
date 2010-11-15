/*
 * Copyright (c) 2009, 2010, B3log Team
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

package org.b3log.solo.event.sync.impl;

import com.google.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.b3log.latke.Keys;
import org.b3log.latke.event.Event;
import org.b3log.latke.event.EventException;
import org.b3log.latke.event.EventManager;
import org.b3log.solo.event.sync.AbstractRemoveArticleProcessor;
import org.b3log.solo.event.sync.BlogSyncStatusCodes;
import org.b3log.solo.model.BlogSync;
import org.b3log.solo.sync.SyncException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This listener is responsible for blog sync remove article from CnBlogs.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @version 1.0.0.4, Nov 3, 2010
 */
public final class CnBlogsRemoveArticleProcessor
        extends AbstractRemoveArticleProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
            Logger.getLogger(CnBlogsRemoveArticleProcessor.class.getName());

    /**
     * Constructs a {@link CnBlogsRemoveArticleProcessor} object with the
     * specified event manager.
     *
     * @param eventManager the specified event manager
     */
    @Inject
    public CnBlogsRemoveArticleProcessor(final EventManager eventManager) {
        super(eventManager);
    }

    @Override
    public String getExternalBloggingSys() {
        return BlogSync.BLOG_SYNC_CNBLOGS;
    }

    @Override
    public void action(final Event<JSONObject> event) throws EventException {
        final JSONObject eventData = event.getData();
        JSONObject result = null;
        JSONObject events = null;
        JSONObject blogSyncCnBlogs = null;
        try {
            result = eventData.getJSONObject(Keys.RESULTS);
            final JSONObject status = result.getJSONObject(Keys.STATUS);

            events = status.optJSONObject(Keys.EVENTS);
            if (null == events) {
                events = new JSONObject();
                status.put(Keys.EVENTS, events);
            }

            blogSyncCnBlogs =
                    events.optJSONObject(BlogSync.BLOG_SYNC_CNBLOGS);
            if (null == blogSyncCnBlogs) {
                blogSyncCnBlogs = new JSONObject();
                events.put(BlogSync.BLOG_SYNC_CNBLOGS, blogSyncCnBlogs);
            }
        } catch (final JSONException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new EventException(e);
        }

        try {
            final JSONObject rslt = removeArticle(event);
            try {
                blogSyncCnBlogs.put(Keys.CODE, rslt.getString(Keys.STATUS_CODE));
            } catch (final JSONException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                throw new EventException(ex);
            }
        } catch (final SyncException e) {
            try {
                blogSyncCnBlogs.put(Keys.CODE,
                                    BlogSyncStatusCodes.BLOG_SYNC_FAIL);
                blogSyncCnBlogs.put(Keys.MSG, e.getMessage());
            } catch (final JSONException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                throw new EventException(ex);
            }
        } catch (final EventException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            try {
                blogSyncCnBlogs.put(Keys.CODE,
                                    BlogSyncStatusCodes.BLOG_SYNC_FAIL);
                blogSyncCnBlogs.put(Keys.MSG, "Unknown exception :-(");
            } catch (final JSONException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                throw new EventException(ex);
            }
        }
    }
}
