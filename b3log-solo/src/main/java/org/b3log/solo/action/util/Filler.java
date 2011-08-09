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
package org.b3log.solo.action.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.solo.util.Articles;
import org.b3log.solo.model.Article;
import org.b3log.solo.repository.ArticleRepository;
import org.b3log.solo.repository.TagRepository;
import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.action.util.Paginator;
import org.b3log.latke.model.Pagination;
import org.b3log.latke.model.User;
import org.b3log.latke.repository.FilterOperator;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.SortDirection;
import org.b3log.latke.util.CollectionUtils;
import org.b3log.latke.util.Dates;
import org.b3log.latke.util.Locales;
import org.b3log.solo.model.ArchiveDate;
import org.b3log.solo.model.Link;
import org.b3log.solo.model.Preference;
import org.b3log.solo.repository.CommentRepository;
import org.b3log.solo.repository.LinkRepository;
import org.b3log.solo.SoloServletListener;
import org.b3log.solo.model.Comment;
import org.b3log.solo.model.Common;
import org.b3log.solo.model.Skin;
import org.b3log.solo.model.Statistic;
import org.b3log.solo.repository.ArchiveDateRepository;
import org.b3log.solo.repository.PageRepository;
import org.b3log.solo.repository.StatisticRepository;
import org.b3log.solo.repository.UserRepository;
import org.b3log.solo.repository.impl.ArchiveDateGAERepository;
import org.b3log.solo.repository.impl.ArticleGAERepository;
import org.b3log.solo.repository.impl.CommentGAERepository;
import org.b3log.solo.repository.impl.LinkGAERepository;
import org.b3log.solo.repository.impl.PageGAERepository;
import org.b3log.solo.repository.impl.StatisticGAERepository;
import org.b3log.solo.repository.impl.TagGAERepository;
import org.b3log.solo.repository.impl.UserGAERepository;
import org.b3log.solo.util.Tags;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Filler utilities.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @version 1.0.4.2, Aug 9, 2011
 */
public final class Filler {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
            Logger.getLogger(Filler.class.getName());
    /**
     * Article repository.
     */
    private ArticleRepository articleRepository =
            ArticleGAERepository.getInstance();
    /**
     * Comment repository.
     */
    private CommentRepository commentRepository =
            CommentGAERepository.getInstance();
    /**
     * Archive date repository.
     */
    private ArchiveDateRepository archiveDateRepository =
            ArchiveDateGAERepository.getInstance();
    /**
     * Tag repository.
     */
    private TagRepository tagRepository = TagGAERepository.getInstance();
    /**
     * Article utilities.
     */
    private Articles articleUtils = Articles.getInstance();
    /**
     * Tag utilities.
     */
    private Tags tagUtils = Tags.getInstance();
    /**
     * Link repository.
     */
    private LinkRepository linkRepository = LinkGAERepository.getInstance();
    /**
     * Page repository.
     */
    private PageRepository pageRepository = PageGAERepository.getInstance();
    /**
     * Statistic repository.
     */
    private StatisticRepository statisticRepository =
            StatisticGAERepository.getInstance();
    /**
     * User repository.
     */
    private UserRepository userRepository = UserGAERepository.getInstance();
    /**
     * {@code true} for published.
     */
    private static final boolean PUBLISHED = true;

    /**
     * Fills articles in index.ftl.
     *
     * @param dataModel data model
     * @param currentPageNum current page number
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillIndexArticles(final Map<String, Object> dataModel,
                                  final int currentPageNum,
                                  final JSONObject preference)
            throws Exception {
        final int pageSize =
                preference.getInt(Preference.ARTICLE_LIST_DISPLAY_COUNT);
        final int windowSize =
                preference.getInt(Preference.ARTICLE_LIST_PAGINATION_WINDOW_SIZE);

        final Query query = new Query().setCurrentPageNum(currentPageNum).
                setPageSize(pageSize).
                addFilter(Article.ARTICLE_IS_PUBLISHED,
                          FilterOperator.EQUAL, PUBLISHED);

        if (preference.getBoolean(Preference.ENABLE_ARTICLE_UPDATE_HINT)) {
            query.addSort(Article.ARTICLE_UPDATE_DATE, SortDirection.DESCENDING);
        } else {
            query.addSort(Article.ARTICLE_CREATE_DATE, SortDirection.DESCENDING);
        }

        query.addSort(Article.ARTICLE_PUT_TOP, SortDirection.DESCENDING);

        final JSONObject result = articleRepository.get(query);
        final int pageCount = result.getJSONObject(Pagination.PAGINATION).
                getInt(Pagination.PAGINATION_PAGE_COUNT);

        final List<Integer> pageNums = Paginator.paginate(currentPageNum,
                                                          pageSize,
                                                          pageCount,
                                                          windowSize);
        if (0 != pageNums.size()) {
            dataModel.put(Pagination.PAGINATION_FIRST_PAGE_NUM,
                          pageNums.get(0));
            dataModel.put(Pagination.PAGINATION_LAST_PAGE_NUM,
                          pageNums.get(pageNums.size() - 1));
        }
        dataModel.put(Pagination.PAGINATION_PAGE_COUNT, pageCount);
        dataModel.put(Pagination.PAGINATION_PAGE_NUMS, pageNums);

        final List<JSONObject> articles = org.b3log.latke.util.CollectionUtils.
                jsonArrayToList(result.getJSONArray(Keys.RESULTS));
        setArticlesExProperties(articles, preference);

        dataModel.put(Article.ARTICLES, articles);
    }

    /**
     * Fills links.
     *
     * @param dataModel data model
     * @throws JSONException json exception
     * @throws RepositoryException repository exception
     */
    private void fillLinks(final Map<String, Object> dataModel)
            throws JSONException, RepositoryException {
        final Map<String, SortDirection> sorts =
                new HashMap<String, SortDirection>();
        sorts.put(Link.LINK_ORDER, SortDirection.ASCENDING);
        final Query query = new Query().addSort(Link.LINK_ORDER,
                                                SortDirection.ASCENDING);
        final JSONObject linkResult = linkRepository.get(query);
        final List<JSONObject> links = org.b3log.latke.util.CollectionUtils.
                jsonArrayToList(linkResult.getJSONArray(Keys.RESULTS));

        dataModel.put(Link.LINKS, links);
    }

    /**
     * Fills most used tags.
     *
     * @param dataModel data model
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillMostUsedTags(final Map<String, Object> dataModel,
                                 final JSONObject preference)
            throws Exception {
        LOGGER.finer("Filling most used tags....");
        final int mostUsedTagDisplayCnt =
                preference.getInt(Preference.MOST_USED_TAG_DISPLAY_CNT);

        final List<JSONObject> tags =
                tagRepository.getMostUsedTags(mostUsedTagDisplayCnt);
        tagUtils.removeForUnpublishedArticles(tags);

        dataModel.put(Common.MOST_USED_TAGS, tags);
    }

    /**
     * Fills archive dates.
     *
     * @param dataModel data model
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillArchiveDates(final Map<String, Object> dataModel,
                                 final JSONObject preference)
            throws Exception {
        LOGGER.finer("Filling archive dates....");
        final List<JSONObject> archiveDates =
                archiveDateRepository.getArchiveDates();

        final String localeString = preference.getString(
                Preference.LOCALE_STRING);
        final String language = Locales.getLanguage(localeString);

        for (final JSONObject archiveDate : archiveDates) {
            final long time = archiveDate.getLong(ArchiveDate.ARCHIVE_TIME);
            final String dateString = ArchiveDate.DATE_FORMAT.format(time);
            final String[] dateStrings = dateString.split("/");
            final String year = dateStrings[0];
            final String month = dateStrings[1];
            archiveDate.put(ArchiveDate.ARCHIVE_DATE_YEAR, year);

            archiveDate.put(ArchiveDate.ARCHIVE_DATE_MONTH, month);
            if ("en".equals(language)) {
                final String monthName = Dates.EN_MONTHS.get(month);
                archiveDate.put(Common.MONTH_NAME, monthName);
            }
        }

        dataModel.put(ArchiveDate.ARCHIVE_DATES, archiveDates);
    }

    /**
     * Fills most view count articles.
     *
     * @param dataModel data model
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillMostViewCountArticles(final Map<String, Object> dataModel,
                                          final JSONObject preference)
            throws Exception {
        LOGGER.finer("Filling the most view count articles....");
        final int mostCommentArticleDisplayCnt =
                preference.getInt(Preference.MOST_VIEW_ARTICLE_DISPLAY_CNT);
        final List<JSONObject> mostViewCountArticles =
                articleRepository.getMostViewCountArticles(
                mostCommentArticleDisplayCnt);

        dataModel.put(Common.MOST_VIEW_COUNT_ARTICLES, mostViewCountArticles);
    }

    /**
     * Fills most comments articles.
     *
     * @param dataModel data model
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillMostCommentArticles(final Map<String, Object> dataModel,
                                        final JSONObject preference)
            throws Exception {
        LOGGER.finer("Filling most comment articles....");
        final int mostCommentArticleDisplayCnt =
                preference.getInt(Preference.MOST_COMMENT_ARTICLE_DISPLAY_CNT);
        final List<JSONObject> mostCommentArticles =
                articleRepository.getMostCommentArticles(
                mostCommentArticleDisplayCnt);

        dataModel.put(Common.MOST_COMMENT_ARTICLES, mostCommentArticles);
    }

    /**
     * Fills post articles recently.
     *
     * @param dataModel data model
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillRecentArticles(final Map<String, Object> dataModel,
                                   final JSONObject preference)
            throws Exception {
        final int recentArticleDisplayCnt =
                preference.getInt(Preference.RECENT_ARTICLE_DISPLAY_CNT);

        final List<JSONObject> recentArticles =
                articleRepository.getRecentArticles(recentArticleDisplayCnt);

        dataModel.put(Common.RECENT_ARTICLES, recentArticles);
    }

    /**
     * Fills post comments recently.
     *
     * @param dataModel data model
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillRecentComments(final Map<String, Object> dataModel,
                                   final JSONObject preference)
            throws Exception {
        LOGGER.finer("Filling recent comments....");
        final int recentCommentDisplayCnt =
                preference.getInt(Preference.RECENT_COMMENT_DISPLAY_CNT);

        final List<JSONObject> recentComments =
                commentRepository.getRecentComments(recentCommentDisplayCnt);

        // Erase email for security reason
        for (final JSONObject comment : recentComments) {
            final String content = comment.getString(Comment.COMMENT_CONTENT).
                    replaceAll(SoloServletListener.ENTER_ESC, "&nbsp;");
            comment.put(Comment.COMMENT_CONTENT, content);
            comment.remove(Comment.COMMENT_EMAIL);
        }

        dataModel.put(Common.RECENT_COMMENTS, recentComments);
    }

    /**
     * Fills article-footer.ftl.
     *
     * @param dataModel data model
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillBlogFooter(final Map<String, Object> dataModel,
                               final JSONObject preference)
            throws Exception {
        LOGGER.finer("Filling footer....");
        final String blogTitle = preference.getString(Preference.BLOG_TITLE);
        dataModel.put(Preference.BLOG_TITLE, blogTitle);
        final String blogHost = preference.getString(Preference.BLOG_HOST);
        dataModel.put(Preference.BLOG_HOST, blogHost);

        dataModel.put(Common.VERSION, SoloServletListener.VERSION);
        dataModel.put(Common.YEAR,
                      String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    }

    /**
     * Fills article-header.ftl.
     *
     * @param dataModel data model
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillBlogHeader(final Map<String, Object> dataModel,
                               final JSONObject preference)
            throws Exception {
        LOGGER.fine("Filling header....");
        dataModel.put(Preference.LOCALE_STRING,
                      preference.getString(Preference.LOCALE_STRING));
        dataModel.put(Preference.BLOG_TITLE,
                      preference.getString(Preference.BLOG_TITLE));
        dataModel.put(Preference.BLOG_SUBTITLE,
                      preference.getString(Preference.BLOG_SUBTITLE));
        dataModel.put(Preference.HTML_HEAD,
                      preference.getString(Preference.HTML_HEAD));
        dataModel.put(Preference.META_KEYWORDS,
                      preference.getString(Preference.META_KEYWORDS));
        dataModel.put(Preference.META_DESCRIPTION,
                      preference.getString(Preference.META_DESCRIPTION));

        final JSONObject result = userRepository.get(new Query());
        final JSONArray users = result.getJSONArray(Keys.RESULTS);
        final List<JSONObject> userList = CollectionUtils.jsonArrayToList(users);
        dataModel.put(User.USERS, userList);
        for (final JSONObject user : userList) {
            user.remove(User.USER_EMAIL);
        }

        final String skinDirName = preference.getString(Skin.SKIN_DIR_NAME);
        dataModel.put(Skin.SKIN_DIR_NAME, skinDirName);
        fillMinified(dataModel);

        fillPageNavigations(dataModel);
        fillStatistic(dataModel);
    }

    /**
     * Fills minified directory and file postfix for static JavaScript, CSS.
     * 
     * @param dataModel the specified data model
     */
    public void fillMinified(final Map<String, Object> dataModel) {
        switch (Latkes.getRuntimeMode()) {
            case DEVELOPMENT:
                dataModel.put(Common.MINI_POSTFIX, "");
                break;
            case PRODUCTION:
                dataModel.put(Common.MINI_POSTFIX, Common.MINI_POSTFIX_VALUE);
                break;
            default:
                throw new AssertionError();
        }
    }

    /**
     * Fills article-side.ftl.
     *
     * @param dataModel data model
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void fillSide(final Map<String, Object> dataModel,
                         final JSONObject preference)
            throws Exception {
        LOGGER.fine("Filling side....");
        fillLinks(dataModel);
//        fillRecentArticles(dataModel, preference);
        fillRecentComments(dataModel, preference);
        fillMostUsedTags(dataModel, preference);
        fillMostCommentArticles(dataModel, preference);
        fillMostViewCountArticles(dataModel, preference);
        fillArchiveDates(dataModel, preference);

        final String noticeBoard =
                preference.getString(Preference.NOTICE_BOARD);
        dataModel.put(Preference.NOTICE_BOARD, noticeBoard);
    }

    /**
     * Fills page navigations.
     *
     * @param dataModel data model
     * @throws Exception exception
     */
    private void fillPageNavigations(final Map<String, Object> dataModel)
            throws Exception {
        LOGGER.finer("Filling page navigations....");
        final List<JSONObject> pages = pageRepository.getPages();

        dataModel.put(Common.PAGE_NAVIGATIONS, pages);
    }

    /**
     * Fills statistic.
     *
     * @param dataModel data model
     * @throws Exception exception
     */
    private void fillStatistic(final Map<String, Object> dataModel)
            throws Exception {
        LOGGER.finer("Filling statistic....");
        final JSONObject statistic =
                statisticRepository.get(Statistic.STATISTIC);

        statistic.remove(Statistic.STATISTIC_BLOG_ARTICLE_COUNT);
        statistic.remove(Statistic.STATISTIC_BLOG_COMMENT_COUNT);

        dataModel.put(Statistic.STATISTIC, statistic);
    }

    /**
     * Sets some extra properties into the specified article with the specified 
     * preference.
     * 
     * <p>
     * Article ext properties:
     * <pre>
     * {
     *     ...., 
     *     "authorName": "",
     *     "authorId": "",
     *     "hasUpdated": boolean
     * }
     * </pre>
     * </p>
     * 
     * @param article the specified article
     * @param preference the specified preference
     * @throws JSONException json exception
     * @see #setArticlesExProperties(java.util.List, org.json.JSONObject) 
     */
    public void setArticleExProperties(final JSONObject article,
                                       final JSONObject preference)
            throws JSONException {
        final JSONObject author = articleUtils.getAuthor(article);
        final String authorName = author.getString(User.USER_NAME);
        article.put(Common.AUTHOR_NAME, authorName);
        final String authorId = author.getString(Keys.OBJECT_ID);
        article.put(Common.AUTHOR_ID, authorId);

        if (preference.getBoolean(Preference.ENABLE_ARTICLE_UPDATE_HINT)) {
            article.put(Common.HAS_UPDATED,
                        articleUtils.hasUpdated(article));
        } else {
            article.put(Common.HAS_UPDATED, false);
        }
    }

    /**
     * Sets some extra properties into the specified article with the specified 
     * preference.
     * 
     * <p>
     * The batch version of method 
     * {@linkplain #setArticleExProperties(org.json.JSONObject, org.json.JSONObject)}.
     * </p>
     *
     * <p>
     * Article ext properties:
     * <pre>
     * {
     *     ...., 
     *     "authorName": "",
     *     "authorId": "",
     *     "hasUpdated": boolean
     * }
     * </pre>
     * </p>
     *
     * @param articles the specified articles
     * @param preference the specified preference
     * @throws JSONException json exception
     * @see #setArticleExProperties(org.json.JSONObject, org.json.JSONObject) 
     */
    public void setArticlesExProperties(final List<JSONObject> articles,
                                        final JSONObject preference)
            throws JSONException {
        for (final JSONObject article : articles) {
            setArticleExProperties(article, preference);
        }
    }

    /**
     * Gets the {@link Filler} singleton.
     *
     * @return the singleton
     */
    public static Filler getInstance() {
        return SingletonHolder.SINGLETON;
    }

    /**
     * Private default constructor.
     */
    private Filler() {
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
        private static final Filler SINGLETON = new Filler();

        /**
         * Private default constructor.
         */
        private SingletonHolder() {
        }
    }
}
