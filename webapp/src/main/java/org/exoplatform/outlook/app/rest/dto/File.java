/*
 * Copyright (C) 2003-2020 eXo Platform SAS.
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
package org.exoplatform.outlook.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import javax.jcr.RepositoryException;
import java.util.Objects;

/**
 * The type File.
 */
public class File extends AbstractFileResource {

  /** The Constant LOG. */
  private static final Log                         LOG = ExoLogger.getLogger(File.class);

  /**
   * The File.
   */
  protected final org.exoplatform.outlook.jcr.File file;

  /**
   * Instantiates a new File.
   *
   * @param file the file
   */
  public File(org.exoplatform.outlook.jcr.File file) {
    this.file = file;
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  @JsonProperty("name")
  public String getName() {
    return file.getName();
  }

  /**
   * Gets url.
   *
   * @return the url
   */
  @JsonProperty("url")
  public String getUrl() {
    return file.getUrl();
  }

  /**
   * Gets full path.
   *
   * @return the full path
   */
  @JsonProperty("fullPath")
  public String getFullPath() {
    String fullPath = "";

    try {
      fullPath = file.getNode().getPath();
    } catch (RepositoryException e) {
      LOG.error("Error getting a full path to file", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a full path to file");
    }

    return fullPath;
  }

  /**
   * Gets path label.
   *
   * @return the path label
   */
  @JsonProperty("pathLabel")
  public String getPathLabel() {
    return file.getPathLabel();
  }

  /**
   * Gets webdav url.
   *
   * @return the webdav url
   */
  @JsonProperty("webdavUrl")
  public String getWebdavUrl() {
    return file.getWebdavUrl();
  }

  /**
   * Gets title.
   *
   * @return the title
   */
  @JsonProperty("title")
  public String getTitle() {
    return file.getTitle();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    File file = (File) o;
    return Objects.equals(getName(), file.getName()) && Objects.equals(getFullPath(), file.getFullPath())
        && Objects.equals(getPathLabel(), file.getPathLabel()) && Objects.equals(getUrl(), file.getUrl())
        && Objects.equals(getWebdavUrl(), file.getWebdavUrl()) && Objects.equals(getTitle(), file.getTitle());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getFullPath(), getPathLabel(), getUrl(), getWebdavUrl(), getTitle());
  }
}
