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

package org.b3log.solo.plugin.cache;

import java.util.logging.Level;
import org.b3log.latke.plugin.AbstractPlugin;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Admin cache plugin.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.4, Jun 25, 2011
 */
public final class AdminCache extends AbstractPlugin {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
            Logger.getLogger(AdminCache.class.getName());
    
    @Override
    public void plug(final Map<String, Object> dataModel) {
        super.plug(dataModel);
        
        LOGGER.log(Level.FINER, "Plugin[name={0}] has been plugged", getName());
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