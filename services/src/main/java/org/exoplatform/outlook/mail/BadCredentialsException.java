
/*
 * Copyright (C) 2003-2016 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.outlook.mail;

/**
 * OutlookUser credentials failed (unauthorized) in Anaplan API.
 * 
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:pnedonosko@exoplatform.com">Peter Nedonosko</a>
 * @version $Id: BadCredentialsException.java 00000 May 11, 2016 pnedonosko $
 * 
 */
public class BadCredentialsException extends MailServerException {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -1431333385461359936L;

  /**
   * Instantiates a new bad credentials exception.
   *
   * @param message {@link String}
   */
  public BadCredentialsException(String message) {
    super(message);
  }

  /**
   * Instantiates a new bad credentials exception.
   *
   * @param message {@link String}
   * @param cause {@link Throwable}
   */
  public BadCredentialsException(String message, Throwable cause) {
    super(message, cause);
  }

}
