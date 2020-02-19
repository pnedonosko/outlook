package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.jcr.Folder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.jcr.RepositoryException;
import java.util.HashSet;
import java.util.Set;

/**
 * The Folder info.
 */
public class FolderInfo {

  /** The Constant LOG. */
  private static final Log   LOG = ExoLogger.getLogger(FolderInfo.class);

  private String             path;

  private String             pathLabel;

  private String             url;

  private Set<SubfolderInfo> subfolders;

    /**
     * Instantiates a new Folder info.
     *
     * @param folder the folder
     */
    public FolderInfo(Folder folder) {
    setPath(folder.getPath());
    setPathLabel(folder.getPathLabel());
    setUrl(folder.getUrl());

    Set<Folder> fullSubfolders = null;

    try {
      fullSubfolders = folder.getSubfolders();
    } catch (RepositoryException e) {
      e.printStackTrace();
    } catch (OutlookException e) {
      e.printStackTrace();
    }

    if (fullSubfolders != null) {
      subfolders = new HashSet<>(fullSubfolders.size());

      fullSubfolders.forEach((v) -> {
        subfolders.add(new SubfolderInfo(v));
      });
    }

  }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
    return path;
  }

    /**
     * Sets path.
     *
     * @param path the path
     */
    public void setPath(String path) {
    this.path = path;
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
     * Gets subfolders.
     *
     * @return the subfolders
     */
    public Set<SubfolderInfo> getSubfolders() {
    return subfolders;
  }

    /**
     * Sets subfolders.
     *
     * @param subfolders the subfolders
     */
    public void setSubfolders(Set<SubfolderInfo> subfolders) {
    this.subfolders = subfolders;
  }

  private class SubfolderInfo {

    private String title;

    private String lastModified;

      /**
       * Instantiates a new Subfolder info.
       *
       * @param subfolder the subfolder
       */
      public SubfolderInfo(Folder subfolder) {
      setTitle(subfolder.getTitle());
      setLastModified(subfolder.getLastModified());
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

      /**
       * Gets last modified.
       *
       * @return the last modified
       */
      public String getLastModified() {
      return lastModified;
    }

      /**
       * Sets last modified.
       *
       * @param lastModified the last modified
       */
      public void setLastModified(String lastModified) {
      this.lastModified = lastModified;
    }
  }
}
