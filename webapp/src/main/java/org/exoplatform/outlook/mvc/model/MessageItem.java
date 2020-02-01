
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
package org.exoplatform.outlook.mvc.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import juzu.Format;

/**
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:pnedonosko@exoplatform.com">Peter Nedonosko</a>
 * @version $Id: MessageItem.java 00000 Jul 11, 2016 pnedonosko $
 */
@Deprecated // TODO not used as Juzu doesn't work with such bean structure
public class MessageItem {

  /** The id. */
  public String                    id;
  
  /** The body. */
  public String                    body;

  /** The subject. */
  @NotNull
  public String                    subject;

  /** The user. */
  @NotNull
  public EmailAddressDetails user;

  /** The from. */
  @NotNull
  public EmailAddressDetails from;

  /** The to. */
  public List<EmailAddressDetails> to;

  /** The created. */
  @Format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  public Date                      created;

  /** The modified. */
  @Format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  public Date                      modified;

}
