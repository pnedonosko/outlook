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

import org.exoplatform.outlook.jcr.File;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import javax.jcr.RepositoryException;
import java.util.Objects;

/**
 * The type File dto.
 */
public class FileDTO extends AbstractFileResource {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(FileDTO.class);

  private String           name;

  private String           fullPath;

  private String           pathLabel;

  private String           url;

  private String           webdavUrl;

  private String           title;

  /**
   * Instantiates a new Subfile info.
   *
   * @param file the file
   */
  public FileDTO(File file) {
    setName(file.getName());
    try {
      setFullPath(file.getNode().getPath());
    } catch (RepositoryException e) {
      LOG.error(e);
    }
    setPathLabel(file.getPathLabel());
    setUrl(file.getUrl());
    setWebdavUrl(file.getWebdavUrl());
    setTitle(file.getTitle());
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets url.
   *
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets url.
   *
   * @param url the url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Gets full path.
   *
   * @return the full path
   */
  public String getFullPath() {
    return fullPath;
  }

  /**
   * Sets full path.
   *
   * @param fullPath the full path
   */
  public void setFullPath(String fullPath) {
    this.fullPath = fullPath;
  }

  /**
   * Gets path label.
   *
   * @return the path label
   */
  public String getPathLabel() {
    return pathLabel;
  }

  /**
   * Sets path label.
   *
   * @param pathLabel the path label
   */
  public void setPathLabel(String pathLabel) {
    this.pathLabel = pathLabel;
  }

  /**
   * Gets webdav url.
   *
   * @return the webdav url
   */
  public String getWebdavUrl() {
    return webdavUrl;
  }

  /**
   * Sets webdav url.
   *
   * @param webdavUrl the webdav url
   */
  public void setWebdavUrl(String webdavUrl) {
    this.webdavUrl = webdavUrl;
  }

  /**
   * Gets title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets title.
   *
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    FileDTO fileDTO = (FileDTO) o;
    return Objects.equals(getName(), fileDTO.getName()) && Objects.equals(getFullPath(), fileDTO.getFullPath())
        && Objects.equals(getPathLabel(), fileDTO.getPathLabel()) && Objects.equals(getUrl(), fileDTO.getUrl())
        && Objects.equals(getWebdavUrl(), fileDTO.getWebdavUrl()) && Objects.equals(getTitle(), fileDTO.getTitle());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getFullPath(), getPathLabel(), getUrl(), getWebdavUrl(), getTitle());
  }
}
