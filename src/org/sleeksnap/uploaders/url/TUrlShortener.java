/**
 * Sleeksnap, the open source cross-platform screenshot uploader
 * Copyright (C) 2012 Nikki <nikki@nikkii.us>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sleeksnap.uploaders.url;

import java.util.HashMap;
import java.util.Map;

import org.sleeksnap.http.HttpUtil;
import org.sleeksnap.upload.URLUpload;
import org.sleeksnap.uploaders.UploadException;
import org.sleeksnap.uploaders.Uploader;

/**
 * A url shortener for http://turl.ca
 * http://turl.ca/apidoc.php
 * 
 * @author Nikki
 * 
 */
public class TUrlShortener extends Uploader<URLUpload> {

	/**
	 * The base URL
	 */
	private static final String TURL_BASE = "http://turl.ca/";
	
	/**
	 * The API URL
	 */
	private static final String API_URL = TURL_BASE + "api.php";

	@Override
	public String getName() {
		return "TUrl.ca";
	}

	@Override
	public String upload(URLUpload url) throws Exception {
		//Format the request
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("url", url.getURL().toString());
		//Implode the request properties (Automatically encodes)
		String resp = HttpUtil.executeGet(API_URL + '?' + HttpUtil.implode(req));
		//Response is in STATUS:data format, data can be an error message or the shortened url.
		String status = resp.substring(0, resp.indexOf(':'));
		String data = resp.substring(resp.indexOf(':')+1);
		if (status.equalsIgnoreCase("ERROR")) {
			throw new UploadException(data);
		}
		return TURL_BASE + data;
	}
}
