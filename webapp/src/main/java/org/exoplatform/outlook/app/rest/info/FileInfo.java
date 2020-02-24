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
package org.exoplatform.outlook.app.rest.info;

import org.exoplatform.outlook.jcr.File;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import javax.jcr.RepositoryException;
import java.util.Objects;

/**
 * The type File info.
 */
public class FileInfo extends GeneralInfoBox {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(FileInfo.class);

  private String           name;

  // TODO Do we really need this on file class, why not in the abstract and then impl where/what required?
  // See below
  @Deprecated
  private boolean          isFolder;  

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
  public FileInfo(File file) {
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
   * Is folder boolean.
   *
   * @return the boolean
   */
  @Deprecated //TODO we don't need it at all, as it never used outside, no sense to rely on it in hash/equals
  public boolean isFolder() {
    return false;
  }

  /**
   * Sets folder.
   *
   * @param isFolder is the folder boolean
   */
  public void setIsFolder(boolean isFolder) {
    assert isFolder : "File cannot be a folder";
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
    // TODO super should not be used as it doesn't contain business logic
    if (!super.equals(o))
      return false;
    FileInfo fileInfo = (FileInfo) o;
    return isFolder() == fileInfo.isFolder() && Objects.equals(getName(), fileInfo.getName())
        && Objects.equals(getFullPath(), fileInfo.getFullPath()) && Objects.equals(getPathLabel(), fileInfo.getPathLabel())
        && Objects.equals(getUrl(), fileInfo.getUrl()) && Objects.equals(getWebdavUrl(), fileInfo.getWebdavUrl())
        && Objects.equals(getTitle(), fileInfo.getTitle());
  }

  @Override
  public int hashCode() {
    // TODO super.hashCode() should not be used - it doesn't contain business logic
    return Objects.hash(super.hashCode(),
                        getName(),
                        isFolder(),
                        getFullPath(),
                        getPathLabel(),
                        getUrl(),
                        getWebdavUrl(),
                        getTitle());
  }
}
