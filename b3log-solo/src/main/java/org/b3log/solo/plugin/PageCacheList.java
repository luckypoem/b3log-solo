/*
 * Copyright (c) 2009, 2010, 2011, B3log Team
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

package org.b3log.solo.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.b3log.latke.plugin.AbstractPlugin;
import java.util.Map;
import java.util.logging.Logger;
import org.b3log.latke.Keys;
import org.b3log.latke.action.util.PageCaches;
import org.b3log.solo.model.Page;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Page cache list plugin.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.2, Jun 18, 2011
 */
public final class PageCacheList extends AbstractPlugin {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
            Logger.getLogger(PageCacheList.class.getName());

    @Override
    public void plug(final Map<String, Object> dataModel) {
        final List<JSONObject> pages = new ArrayList<JSONObject>();
        final Set<String> keys = PageCaches.getKeys();
        for (final String key : keys) {
            LOGGER.log(Level.FINER, "Cached page[key={0}]", key);
            try {
                final JSONObject page = new JSONObject();
                page.put(Keys.PAGE_CACHE_KEY, key);
                pages.add(page);
            } catch (final JSONException ex) {
                LOGGER.log(Level.SEVERE, "Page cache plug failed", ex);
            }
        }

        dataModel.put(Page.PAGES, pages);

        super.plug(dataModel);
    }

    @Override
    public void unplug() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getViewName() {
        return "admin-cache-list.ftl";
    }
}
