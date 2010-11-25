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

package org.b3log.solo.google.auth;

import com.google.inject.Inject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.solo.jsonrpc.impl.PreferenceService;
import org.b3log.solo.model.Preference;
import org.json.JSONObject;

/**
 * Buzz OAuth callback.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @version 1.0.0.3, Nov 24, 2010
 */
public final class BuzzOAuthCallback extends HttpServlet {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Logger.
     */
    private static final Logger LOGGER =
            Logger.getLogger(BuzzOAuthCallback.class.getName());
    /**
     * Sleep interval in milliseconds.
     */
    public static final long SLEEP_INTERVAL = 3000;
    /**
     * Preference service.
     */
    @Inject
    private PreferenceService preferenceService;

    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response)
            throws ServletException, IOException {
        final String requestToken = request.getParameter("oauth_token");
        final String verifier = request.getParameter("oauth_verifier");
        LOGGER.log(Level.FINE,
                   "OAuth callback from Google[requestToken={0}, verifier={1}",
                   new String[]{requestToken, verifier});

        try {
            OAuths.sign(requestToken, verifier, BuzzOAuth.getHttpTransport());
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new ServletException(e);
        }

        try {
            final JSONObject preference = preferenceService.getPreference();
            preference.put(Preference.GOOGLE_BUZZ_TOKEN, requestToken);
            preference.put(Preference.GOOGLE_BUZZ_VERIFIER, verifier);

            final JSONObject requestJSONObject = new JSONObject();
            requestJSONObject.put(Preference.PREFERENCE, preference);

            preferenceService.updatePreference(preference, request, response);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new ServletException(e);
        }

        response.sendRedirect("/admin-index.do");
    }
}