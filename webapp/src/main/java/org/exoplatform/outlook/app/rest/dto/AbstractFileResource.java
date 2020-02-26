package org.exoplatform.outlook.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * The type Abstract file resource.
 */
public abstract class AbstractFileResource extends ResourceSupport {

  protected Metadata        metadata;

  protected EmbeddedContent embedded;

  /**
   * Instantiates a new General info box.
   */
  public AbstractFileResource() {
  }

  /**
   * Instantiates a new General info box.
   *
   * @param metadata the metadata
   * @param content the content
   */
  public AbstractFileResource(PagedResources.PageMetadata metadata, Collection<?> content) {
    this.metadata = new Metadata(metadata);
    embedded = new EmbeddedContent(content);
  }

  /**
   * Iterator iterator.
   *
   * @return the iterator
   */
  public Iterator<?> iterator() {
    return this.embedded.content.iterator();
  }

  /**
   * Gets metadata.
   *
   * @return the metadata
   */
  @JsonProperty("_metadata")
  public Metadata getMetadata() {
    return metadata;
  }

  /**
   * Sets metadata.
   *
   * @param metadata the metadata
   */
  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Gets embedded.
   *
   * @return the embedded
   */
  @JsonProperty("_embedded")
  public EmbeddedContent getEmbedded() {
    return embedded;
  }

  /**
   * Sets embedded.
   *
   * @param embedded the embedded
   */
  public void setEmbedded(EmbeddedContent embedded) {
    this.embedded = embedded;
  }

  /**
   * The type Metadata.
   */
  protected class Metadata {

    /**
     * The Metadata.
     */
    protected PagedResources.PageMetadata metadata;

    /**
     * Instantiates a new Metadata.
     *
     * @param metadata the metadata
     */
    public Metadata(PagedResources.PageMetadata metadata) {
      this.metadata = metadata;
    }

    /**
     * Gets metadata.
     *
     * @return the metadata
     */
    @JsonProperty("page")
    public PagedResources.PageMetadata getMetadata() {
      return metadata;
    }

    /**
     * Sets metadata.
     *
     * @param metadata the metadata
     */
    public void setMetadata(PagedResources.PageMetadata metadata) {
      this.metadata = metadata;
    }
  }

  /**
   * The type Embedded content.
   */
  protected class EmbeddedContent {

    protected Collection<?> content;

    /**
     * Instantiates a new Embedded content.
     *
     * @param content the content
     */
    public EmbeddedContent(Collection<?> content) {
      this.content = content;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    @XmlAnyElement
    @XmlElementWrapper
    @JsonProperty("children")
    public Collection<?> getContent() {
      return Collections.unmodifiableCollection(this.content);
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(Collection<?> content) {
      this.content = content;
    }
  }
}
