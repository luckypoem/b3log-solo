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

import junit.framework.Assert;
import org.b3log.latke.model.User;
import org.b3log.latke.util.Requests;
import org.b3log.solo.AbstractTestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

/**
 * {@link UserQueryService} test case.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @version 1.0.0.0, Dec 31, 2011
 */
public class UserQueryServiceTestCase extends AbstractTestCase {

    /**
     * Add User.
     * 
     * @throws Exception exception
     */
    @Test
    public void addUser() throws Exception {
        final UserMgmtService userMgmtService = getUserMgmtService();

        final JSONObject requestJSONObject = new JSONObject();

        requestJSONObject.put(User.USER_NAME, "user1 name");
        requestJSONObject.put(User.USER_EMAIL, "test1@gmail.com");
        requestJSONObject.put(User.USER_PASSWORD, "pass1");

        final String id = userMgmtService.addUser(requestJSONObject);
        Assert.assertNotNull(id);

        final UserQueryService userQueryService = getUserQueryService();
        Assert.assertNotNull(userQueryService.getUser(id));
    }

    /**
     * Get User.
     * 
     * @throws Exception exception
     */
    @Test(dependsOnMethods = "addUser")
    public void getUser() throws Exception {
        final UserQueryService userQueryService = getUserQueryService();
        Assert.assertNull(userQueryService.getUser("not found"));
    }

    /**
     * Get User By Email.
     * 
     * @throws Exception exception
     */
    @Test(dependsOnMethods = "addUser")
    public void getUserByEmail() throws Exception {
        final UserQueryService userQueryService = getUserQueryService();

        final JSONObject user =
                userQueryService.getUserByEmail("test1@gmail.com");
        Assert.assertNotNull(user);
    }

    /**
     * Get Users.
     * 
     * @throws Exception exception
     */
    @Test(dependsOnMethods = "addUser")
    public void getUsers() throws Exception {
        final UserQueryService userQueryService = getUserQueryService();

        final JSONObject paginationRequest =
                Requests.buildPaginationRequest("1/20/10");
        final JSONObject result = userQueryService.getUsers(paginationRequest);
        final JSONArray users = result.getJSONArray(User.USERS);
        Assert.assertEquals(users.length(), 1);
    }

    /**
     * Get Login URL.
     */
    public void getLoginURL() {
        final UserQueryService userQueryService = getUserQueryService();
        final String loginURL = userQueryService.getLoginURL("redirectURL");

        Assert.assertEquals(loginURL, "/login?goto=redirectURL");
    }

    /**
     * Get Logout URL.
     */
    public void getLogoutURL() {
        final UserQueryService userQueryService = getUserQueryService();
        final String logoutURL = userQueryService.getLogoutURL();

        Assert.assertEquals(logoutURL, "/login?goto=/");
    }
}