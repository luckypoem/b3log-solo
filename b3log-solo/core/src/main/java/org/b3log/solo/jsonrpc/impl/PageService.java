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
package org.b3log.solo.jsonrpc.impl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.Keys;
import org.b3log.latke.action.ActionException;
import org.b3log.latke.action.util.Paginator;
import org.b3log.latke.model.Pagination;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.SortDirection;
import org.b3log.latke.repository.Transaction;
import org.b3log.latke.util.Strings;
import org.b3log.solo.web.action.StatusCodes;
import org.b3log.solo.jsonrpc.AbstractGAEJSONRpcService;
import org.b3log.solo.jsonrpc.PermalinkException;
import org.b3log.solo.model.Page;
import org.b3log.solo.repository.PageRepository;
import org.b3log.solo.repository.impl.PageRepositoryImpl;
import org.b3log.solo.util.Pages;
import org.b3log.solo.util.Permalinks;
import org.b3log.solo.util.Users;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Page service for JavaScript client.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @version 1.0.1.2, Jun 29, 2011
 * @since 0.3.1
 */
public final class PageService extends AbstractGAEJSONRpcService {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
            Logger.getLogger(PageService.class.getName());
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
     * User utilities.
     */
    private Users userUtils = Users.getInstance();

    /**
     * Gets a page by the specified request json object.
     *
     * @param requestJSONObject the specified request json object, for example,
     * <pre>
     * {
     *     "oId": ""
     * }
     * </pre>
     * @return for example,
     * <pre>
     * {
     *     "page": {
     *         "oId": "",
     *         "pageTitle": "",
     *         "pageContent": ""
     *         "pageOrder": int,
     *         "pagePermalink": "",
     *         "pageCommentCount": int,
     *     },
     *     "sc": "GET_PAGE_SUCC"
     * }
     * </pre>
     * @throws ActionException action exception
     */
    public JSONObject getPage(final JSONObject requestJSONObject)
            throws ActionException {
        final JSONObject ret = new JSONObject();

        try {
            final String pageId = requestJSONObject.getString(Keys.OBJECT_ID);
            final JSONObject page = pageRepository.get(pageId);
            ret.put(Page.PAGE, page);

            ret.put(Keys.STATUS_CODE, StatusCodes.GET_PAGE_SUCC);

            LOGGER.log(Level.FINER, "Got page [oId={0}]", pageId);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new ActionException(e);
        }

        return ret;
    }

    /**
     * Gets pages by the specified request json object.
     *
     * @param requestJSONObject the specified request json object, for example,
     * <pre>
     * {
     *     "paginationCurrentPageNum": 1,
     *     "paginationPageSize": 20,
     *     "paginationWindowSize": 10
     * }, see {@link Pagination} for more details
     * </pre>
     * @return for example,
     * <pre>
     * {
     *     "pagination": {
     *         "paginationPageCount": 100,
     *         "paginationPageNums": [1, 2, 3, 4, 5]
     *     },
     *     "pages": [{
     *         "oId": "",
     *         "pageTitle": "",
     *         "pageCommentCount": int,
     *         "pageOrder": int,
     *         "pagePermalink": ""
     *      }, ....]
     *     "sc": "GET_PAGES_SUCC"
     * }
     * </pre>
     * @throws ActionException action exception
     * @see Pagination
     */
    public JSONObject getPages(final JSONObject requestJSONObject)
            throws ActionException {
        final JSONObject ret = new JSONObject();
        try {
            final int currentPageNum = requestJSONObject.getInt(
                    Pagination.PAGINATION_CURRENT_PAGE_NUM);
            final int pageSize = requestJSONObject.getInt(
                    Pagination.PAGINATION_PAGE_SIZE);
            final int windowSize = requestJSONObject.getInt(
                    Pagination.PAGINATION_WINDOW_SIZE);

            final Query query = new Query().setCurrentPageNum(currentPageNum).
                    setPageSize(pageSize).
                    addSort(Page.PAGE_ORDER, SortDirection.ASCENDING);
            final JSONObject result = pageRepository.get(query);
            final int pageCount = result.getJSONObject(Pagination.PAGINATION).
                    getInt(Pagination.PAGINATION_PAGE_COUNT);

            final JSONObject pagination = new JSONObject();
            final List<Integer> pageNums =
                    Paginator.paginate(currentPageNum, pageSize, pageCount,
                                       windowSize);
            pagination.put(Pagination.PAGINATION_PAGE_COUNT, pageCount);
            pagination.put(Pagination.PAGINATION_PAGE_NUMS, pageNums);

            final JSONArray pages = result.getJSONArray(Keys.RESULTS);
            for (int i = 0; i < pages.length(); i++) { // remove unused properties
                final JSONObject page = pages.getJSONObject(i);
                page.remove(Page.PAGE_CONTENT);
            }

            ret.put(Pagination.PAGINATION, pagination);
            ret.put(Page.PAGES, pages);
            ret.put(Keys.STATUS_CODE, StatusCodes.GET_PAGES_SUCC);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new ActionException(e);
        }

        return ret;
    }

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
     * @param request the specified http servlet request
     * @param response the specified http servlet response
     * @return for example,
     * <pre>
     * {
     *     "sc": "UPDATE_PAGE_SUCC"
     * }
     * </pre>
     * @throws ActionException action exception
     * @throws IOException io exception
     */
    public JSONObject updatePage(final JSONObject requestJSONObject,
                                 final HttpServletRequest request,
                                 final HttpServletResponse response)
            throws ActionException, IOException {
        final JSONObject ret = new JSONObject();
        if (!userUtils.isAdminLoggedIn(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return ret;
        }

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
                    ret.put(Keys.STATUS_CODE,
                            StatusCodes.UPDATE_PAGE_FAIL_INVALID_PERMALINK_FORMAT);

                    throw new PermalinkException("Update page fail, caused by invalid permalink format["
                                                 + ret + "]");
                }

                if (!oldPermalink.equals(permalink)
                    && permalinks.exist(permalink)) {
                    ret.put(Keys.STATUS_CODE,
                            StatusCodes.UPDATE_PAGE_FAIL_DUPLICATED_PERMALINK);

                    throw new PermalinkException("Update page fail, caused by duplicated permalink["
                                                 + permalink + "]");
                }
            }
            newPage.put(Page.PAGE_PERMALINK, permalink);

            pageRepository.update(pageId, newPage);

            transaction.commit();
            ret.put(Keys.STATUS_CODE, StatusCodes.UPDATE_PAGE_SUCC);

            LOGGER.log(Level.FINER, "Updated a page[oId={0}]", pageId);
        } catch (final PermalinkException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            if (transaction.isActive()) {
                transaction.rollback();
            }

            return ret;
        }

        return ret;
    }

    /**
     * Removes a page by the specified request json object.
     *
     * @param requestJSONObject the specified request json object, for example,
     * <pre>
     * {
     *     "oId": "",
     * }
     * </pre>
     * @param request the specified http servlet request
     * @param response the specified http servlet response
     * @return for example,
     * <pre>
     * {
     *     "sc": "REMOVE_PAGE_SUCC"
     * }
     * </pre>
     * @throws ActionException action exception
     * @throws IOException io exception
     */
    public JSONObject removePage(final JSONObject requestJSONObject,
                                 final HttpServletRequest request,
                                 final HttpServletResponse response)
            throws ActionException, IOException {
        final JSONObject ret = new JSONObject();
        if (!userUtils.isAdminLoggedIn(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return ret;
        }

        final Transaction transaction = pageRepository.beginTransaction();
        try {
            final String pageId = requestJSONObject.getString(Keys.OBJECT_ID);
            LOGGER.log(Level.FINER, "Removing a page[oId={0}]", pageId);
            pageUtils.removePageComments(pageId);
            pageRepository.remove(pageId);

            transaction.commit();
            ret.put(Keys.STATUS_CODE, StatusCodes.REMOVE_PAGE_SUCC);

            LOGGER.log(Level.FINER, "Removed a page[oId={0}]", pageId);
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, e.getMessage(), e);

            throw new ActionException(e);
        }

        return ret;
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
     * @param request the specified http servlet request
     * @param response the specified http servlet response
     * @return for example,
     * <pre>
     * {
     *     "oId": generatedPageId,
     *     "sc": ADD_PAGE_SUCC
     * }
     * </pre>
     * @throws ActionException action exception
     * @throws IOException io exception
     */
    public JSONObject addPage(final JSONObject requestJSONObject,
                              final HttpServletRequest request,
                              final HttpServletResponse response)
            throws ActionException, IOException {
        final JSONObject ret = new JSONObject();
        if (!userUtils.isAdminLoggedIn(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return ret;
        }

        final Transaction transaction = pageRepository.beginTransaction();
        try {
            final JSONObject page =
                    requestJSONObject.getJSONObject(Page.PAGE);
            page.put(Page.PAGE_COMMENT_COUNT, 0);
            final int maxOrder = pageRepository.getMaxOrder();
            page.put(Page.PAGE_ORDER, maxOrder + 1);
            final String pageId = pageRepository.add(page);
            String permalink = page.optString(Page.PAGE_PERMALINK);
            if (Strings.isEmptyOrNull(permalink)) {
                permalink = "/pages/" + pageId + ".html";
            }

            if (!permalink.startsWith("/")) {
                permalink = "/" + permalink;
            }

            if (permalinks.invalidPagePermalinkFormat(permalink)) {
                ret.put(Keys.STATUS_CODE,
                        StatusCodes.ADD_PAGE_FAIL_INVALID_PERMALINK_FORMAT);

                throw new PermalinkException("Add page fail, caused by invalid permalink format["
                                             + ret + "]");
            }

            if (permalinks.exist(permalink)) {
                ret.put(Keys.STATUS_CODE,
                        StatusCodes.ADD_PAGE_FAIL_DUPLICATED_PERMALINK);

                throw new PermalinkException("Add page fail, caused by duplicated permalink["
                                             + permalink + "]");
            }
            page.put(Page.PAGE_PERMALINK, permalink);

            pageRepository.update(pageId, page);

            transaction.commit();
            ret.put(Keys.OBJECT_ID, pageId);

            ret.put(Keys.STATUS_CODE, StatusCodes.ADD_PAGE_SUCC);
        } catch (final PermalinkException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            if (transaction.isActive()) {
                transaction.rollback();
            }

            return ret;
        }

        return ret;
    }

    /**
     * Changes page order by the specified page id and order.
     *
     * @param pageId the specified page id
     * @param pageOrder the specified order
     * @param request the specified http servlet request
     * @param response the specified http servlet response
     * @return {@code true} if changed, {@code false} otherwise
     * @throws ActionException action exception
     * @throws IOException io exception
     */
    public boolean changeOrder(final String pageId, final int pageOrder,
                               final HttpServletRequest request,
                               final HttpServletResponse response)
            throws ActionException, IOException {
        if (!userUtils.isAdminLoggedIn(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        final Transaction transaction = pageRepository.beginTransaction();
        try {
            final JSONObject page1 = pageRepository.get(pageId);
            final String page1Id = pageId;
            final JSONObject page2 = pageRepository.getByOrder(pageOrder);
            final String page2Id = page2.getString(Keys.OBJECT_ID);
            final int oldPage1Order = page1.getInt(Page.PAGE_ORDER);

            final JSONObject newPage2 =
                    new JSONObject(page2, JSONObject.getNames(page2));
            newPage2.put(Page.PAGE_ORDER, oldPage1Order);
            final JSONObject newPage1 =
                    new JSONObject(page1, JSONObject.getNames(page1));
            newPage1.put(Page.PAGE_ORDER, pageOrder);

            pageRepository.update(page2Id, newPage2);
            pageRepository.update(page1Id, newPage1);

            transaction.commit();

            return true;
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, e.getMessage(), e);

            return false;
        }
    }

    /**
     * Gets the {@link PageService} singleton.
     *
     * @return the singleton
     */
    public static PageService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    /**
     * Private default constructor.
     */
    private PageService() {
    }

    /**
     * Singleton holder.
     *
     * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
     * @version 1.0.0.0, Jan 12, 2011
     */
    private static final class SingletonHolder {

        /**
         * Singleton.
         */
        private static final PageService SINGLETON = new PageService();

        /**
         * Private default constructor.
         */
        private SingletonHolder() {
        }
    }
}
