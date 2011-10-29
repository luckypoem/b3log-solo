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
package org.b3log.solo.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.b3log.latke.Keys;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.Transaction;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.util.Ids;
import org.b3log.latke.util.Strings;
import org.b3log.solo.model.Page;
import org.b3log.solo.repository.PageRepository;
import org.b3log.solo.repository.impl.PageRepositoryImpl;
import org.b3log.solo.util.Pages;
import org.b3log.solo.util.Permalinks;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Page management service.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @version 1.0.0.0, Oct 27, 2011
 * @since 0.4.0
 */
public final class PageMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
            Logger.getLogger(PageMgmtService.class.getName());
    /**
     * Page repository.
     */
    private PageRepository pageRepository = PageRepositoryImpl.getInstance();
    /**
     * Page utilities.
     */
    private Pages pageUtils = Pages.getInstance();
    /**
     * Permalink utilities.
     */
    private Permalinks permalinks = Permalinks.getInstance();
    /**
     * Language service.
     */
    private LangPropsService langPropsService = LangPropsService.getInstance();

    /**
     * Updates a page by the specified request json object.
     *
     * @param requestJSONObject the specified request json object, for example,
     * <pre>
     * {
     *     "page": {
     *         "oId": "",
     *         "pageTitle": "",
     *         "pageContent": "",
     *         "pageOrder": int,
     *         "pageCommentCount": int,
     *         "pagePermalink": ""
     *     }
     * }, see {@link Page} for more details
     * </pre>
     * @throws ServiceException service exception
     */
    public void updatePage(final JSONObject requestJSONObject)
            throws ServiceException {

        final Transaction transaction = pageRepository.beginTransaction();
        try {
            final JSONObject page =
                    requestJSONObject.getJSONObject(Page.PAGE);
            final String pageId = page.getString(Keys.OBJECT_ID);
            final JSONObject oldPage = pageRepository.get(pageId);
            final JSONObject newPage =
                    new JSONObject(page, JSONObject.getNames(page));
            newPage.put(Page.PAGE_ORDER, oldPage.getInt(Page.PAGE_ORDER));
            newPage.put(Page.PAGE_COMMENT_COUNT,
                        oldPage.getInt(Page.PAGE_COMMENT_COUNT));
            String permalink = page.optString(Page.PAGE_PERMALINK).trim();

            final String oldPermalink = oldPage.getString(Page.PAGE_PERMALINK);
            if (!oldPermalink.equals(permalink)) {
                if (Strings.isEmptyOrNull(permalink)) {
                    permalink = "/pages/" + pageId + ".html";
                }

                if (!permalink.startsWith("/")) {
                    permalink = "/" + permalink;
                }

                if (permalinks.invalidPagePermalinkFormat(permalink)) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }

                    throw new ServiceException(langPropsService.get(
                            "invalidPermalinkFormatLabel"));
                }


                if (!oldPermalink.equals(permalink)
                    && permalinks.exist(permalink)) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }

                    throw new ServiceException(langPropsService.get(
                            "duplicatedPermalinkLabel"));
                }
            }
            newPage.put(Page.PAGE_PERMALINK, permalink);

            pageRepository.update(pageId, newPage);

            transaction.commit();

            LOGGER.log(Level.FINER, "Updated a page[oId={0}]", pageId);
        } catch (final JSONException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new ServiceException(e);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new ServiceException(e);
        }
    }

    /**
     * Removes a page specified by the given page id.
     *
     * @param pageId the given page id
     * @throws ServiceException service exception
     */
    public void removePage(final String pageId)
            throws ServiceException {
        final Transaction transaction = pageRepository.beginTransaction();
        try {
            LOGGER.log(Level.FINER, "Removing a page[oId={0}]", pageId);
            pageUtils.removePageComments(pageId);
            pageRepository.remove(pageId);

            transaction.commit();

        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.SEVERE, "Removes a page[id=" + pageId + "] failed",
                       e);

            throw new ServiceException(e);
        }
    }

    /**
     * Adds a page with the specified request json object.
     * 
     * @param requestJSONObject the specified request json object, for example,
     * <pre>
     * {
     *     "page": {
     *         "pageTitle": "",
     *         "pageContent": "",
     *         "pagePermalink": "" // optional
     *     }
     * }, see {@link Page} for more details
     * </pre>
     * @return generated link id
     * @throws ServiceException if permalink format checks failed or persists
     * failed
     */
    public String addPage(final JSONObject requestJSONObject)
            throws ServiceException {
        final Transaction transaction = pageRepository.beginTransaction();
        try {
            final JSONObject page =
                    requestJSONObject.getJSONObject(Page.PAGE);
            page.put(Page.PAGE_COMMENT_COUNT, 0);
            final int maxOrder = pageRepository.getMaxOrder();
            page.put(Page.PAGE_ORDER, maxOrder + 1);

            String permalink = page.optString(Page.PAGE_PERMALINK);
            if (Strings.isEmptyOrNull(permalink)) {
                permalink = "/pages/" + Ids.genTimeMillisId() + ".html";
            }

            if (!permalink.startsWith("/")) {
                permalink = "/" + permalink;
            }

            if (permalinks.invalidPagePermalinkFormat(permalink)) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }

                throw new ServiceException(langPropsService.get(
                        "invalidPermalinkFormatLabel"));
            }

            if (permalinks.exist(permalink)) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }

                throw new ServiceException(langPropsService.get(
                        "duplicatedPermalinkLabel"));
            }


            page.put(Page.PAGE_PERMALINK, permalink);

            final String ret = pageRepository.add(page);

            transaction.commit();

            return ret;
        } catch (final JSONException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new ServiceException(e);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new ServiceException(e);
        }
    }

    /**
     * Changes the order of a page specified by the given page id to the 
     * specified target order.
     *
     * @param pageId the given page id
     * @param targetPageOrder the specified target order
     * @throws ServiceException service exception
     */
    public void changeOrder(final String pageId, final int targetPageOrder)
            throws ServiceException {

        final Transaction transaction = pageRepository.beginTransaction();
        try {
            final JSONObject page1 = pageRepository.get(pageId);
            final JSONObject page2 = pageRepository.getByOrder(targetPageOrder);

            final int srcPageOrder = page1.getInt(Page.PAGE_ORDER);

            // Swaps
            page2.put(Page.PAGE_ORDER, srcPageOrder);
            page1.put(Page.PAGE_ORDER, targetPageOrder);

            pageRepository.update(page1.getString(Keys.OBJECT_ID), page1);
            pageRepository.update(page2.getString(Keys.OBJECT_ID), page2);

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.SEVERE, "Changes page's order failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Gets the {@link PageMgmtService} singleton.
     *
     * @return the singleton
     */
    public static PageMgmtService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    /**
     * Private constructor.
     */
    private PageMgmtService() {
    }

    /**
     * Singleton holder.
     *
     * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
     * @version 1.0.0.0, Oct 27, 2011
     */
    private static final class SingletonHolder {

        /**
         * Singleton.
         */
        private static final PageMgmtService SINGLETON =
                new PageMgmtService();

        /**
         * Private default constructor.
         */
        private SingletonHolder() {
        }
    }
}