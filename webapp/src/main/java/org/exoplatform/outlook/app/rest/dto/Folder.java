package org.exoplatform.outlook.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.exoplatform.outlook.OutlookException;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.core.NodetypeConstant;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.jcr.RepositoryException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The type Folder.
 */
public class Folder extends AbstractFileResource {

  /** The Constant LOG. */
  private static final Log                           LOG           = ExoLogger.getLogger(Folder.class);

  /**
   * The Date formatter.
   */
  protected final SimpleDateFormat                   dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

  /**
   * The Folder.
   */
  protected final org.exoplatform.outlook.jcr.Folder folder;

  /**
   * Instantiates a new Folder.
   *
   * @param folder the folder
   * @param metadata the metadata
   * @param links the links
   */
  public Folder(org.exoplatform.outlook.jcr.Folder folder, PagedResources.PageMetadata metadata, List<Link> links) {

    setMetadata(new Metadata(metadata));
    add(links);

    this.folder = folder;

    Set<org.exoplatform.outlook.jcr.Folder> fullSubfolders = null;

    try {
      fullSubfolders = folder.getSubfolders();
    } catch (RepositoryException e) {
      LOG.error(e);
      LOG.error("Error getting a folder subfolders", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a folder subfolders");
    } catch (OutlookException e) {
      LOG.error("Error getting a folder subfolders", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a folder subfolders");
    }

    Set<Subfolder> subfolders = null;

    if (fullSubfolders != null) {
      subfolders = new HashSet<>(fullSubfolders.size());

      for (org.exoplatform.outlook.jcr.Folder fold : fullSubfolders) {
        subfolders.add(new Subfolder(fold));
      }
    }

    Set<org.exoplatform.outlook.jcr.File> fullFiles = null;

    try {
      fullFiles = folder.getFiles();
    } catch (RepositoryException e) {
      LOG.error("Error getting a folder files", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a folder files");
    } catch (OutlookException e) {
      LOG.error("Error getting a folder files", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a folder files");
    }

    Set<File> subfiles = null;

    if (fullFiles != null) {
      subfiles = new HashSet<>(fullFiles.size());

      for (org.exoplatform.outlook.jcr.File f : fullFiles) {
        subfiles.add(new File(f));
      }
    }

    Children children = new Children(subfiles, subfolders);

    Collection<Children> contextParams = new ArrayList();
    contextParams.add(children);
    setEmbedded(new EmbeddedContent(contextParams));
  }

  /**
   * Gets path.
   *
   * @return the path
   */
  @JsonProperty("path")
  public String getPath() {
    return folder.getPath();
  }

  /**
   * Gets full path.
   *
   * @return the full path
   */
  @JsonProperty("fullPath")
  public String getFullPath() {
    return folder.getFullPath();
  }

  /**
   * Gets explorer path (path label).
   *
   * @return the path label
   */
  @JsonProperty("explorerPath")
  public String getExplorerPath() {
    return folder.getPathLabel();
  }

  /**
   * Gets explorer link (url).
   *
   * @return the explorer link
   */
  @JsonProperty("explorerLink")
  public String getExplorerLink() {
    return folder.getUrl();
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  @JsonProperty("name")
  public String getName() {
    return folder.getName();
  }

  /**
   * Gets title.
   *
   * @return the title
   */
  @JsonProperty("title")
  public String getTitle() {
    return folder.getTitle();
  }

  /**
   * Gets created.
   *
   * @return the created
   */
  @JsonProperty("created")
  public String getCreated() {
    String created = "";
    try {
      created = dateFormatter.format(folder.getNode().getProperty(NodetypeConstant.EXO_DATE_CREATED).getDate().getTime());
    } catch (RepositoryException e) {
      LOG.error("Error getting a folder creation date (created)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a folder creation date (created)");
    }
    return created;
  }

  /**
   * Gets modified.
   *
   * @return the modified
   */
  @JsonProperty("modified")
  public String getModified() {
    String modified = "";
    try {
      modified = dateFormatter.format(folder.getNode().getProperty(NodetypeConstant.EXO_LAST_MODIFIED_DATE).getDate().getTime());
    } catch (RepositoryException e) {
      LOG.error("Error getting a folder modification date (modified)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a folder modification date (modified)");
    }
    return modified;
  }

  /**
   * Gets author.
   *
   * @return the author
   */
  @JsonProperty("author")
  public String getAuthor() {
    String author = "";
    try {
      author = folder.getNode().getProperty(NodetypeConstant.EXO_OWNER).getString();
    } catch (RepositoryException e) {
      LOG.error("Error getting a folder author (author)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a folder author (author)");
    }
    return author;
  }

  /**
   * Gets last modifier.
   *
   * @return the last modifier
   */
  @JsonProperty("lastModifier")
  public String getLastModifier() {
    String lastModifier = "";
    try {
      lastModifier = folder.getNode().getProperty(NodetypeConstant.EXO_LAST_MODIFIER).getString();
    } catch (RepositoryException e) {
      LOG.error("Error getting a folder last modifier (lastModifier)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a folder last modifier (lastModifier)");
    }
    return lastModifier;
  }

  /**
   * The type Subfolder.
   */
  protected class Subfolder {

    /**
     * The Subfolder.
     */
    protected final org.exoplatform.outlook.jcr.Folder subfolder;

    /**
     * Instantiates a new Subfolder.
     *
     * @param subfolder the subfolder
     */
    public Subfolder(org.exoplatform.outlook.jcr.Folder subfolder) {
      this.subfolder = subfolder;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    @JsonProperty("title")
    public String getTitle() {
      return subfolder.getTitle();
    }

    /**
     * Gets last modified.
     *
     * @return the last modified
     */
    @JsonProperty("lastModified")
    public String getLastModified() {
      String lastModified = "";
      try {
        lastModified = dateFormatter.format(subfolder.getNode()
                                                     .getProperty(NodetypeConstant.EXO_LAST_MODIFIED_DATE)
                                                     .getDate()
                                                     .getTime());
      } catch (RepositoryException e) {
        LOG.error("Error getting a subfolder last modified date (lastModified)", e);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                          "Error getting a subfolder last modified date (lastModified)");
      }
      return lastModified;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      Subfolder that = (Subfolder) o;
      return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getLastModified(), that.getLastModified());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getTitle(), getLastModified());
    }
  }

  /**
   * The type Children.
   */
  protected class Children {

    protected final Set<File>      files;

    protected final Set<Subfolder> folders;

    /**
     * Instantiates a new Children.
     *
     * @param files the files
     * @param folders the folders
     */
    public Children(Set<File> files, Set<Subfolder> folders) {
      this.files = files;
      this.folders = folders;
    }

    /**
     * Gets files.
     *
     * @return the files
     */
    @JsonProperty("files")
    public Set<File> getFiles() {
      return files;
    }

    /**
     * Gets folders.
     *
     * @return the folders
     */
    @JsonProperty("folders")
    public Set<Subfolder> getFolders() {
      return folders;
    }
  }
}
