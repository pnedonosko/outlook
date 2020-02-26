package org.exoplatform.outlook.app.rest.dto;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.Collection;
import java.util.List;

/**
 * The type File resource (this is a container for simple DTO collections).
 */
public class FileResource extends AbstractFileResource {
  public FileResource(PagedResources.PageMetadata metadata, Collection<?> content, List<Link> links) {
    super(metadata, content);
    add(links);
  }
}
