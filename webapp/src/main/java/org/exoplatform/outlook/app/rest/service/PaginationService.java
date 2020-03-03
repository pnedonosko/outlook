package org.exoplatform.outlook.app.rest.service;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * The type Pagination service.
 */
@Service
public class PaginationService {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(PaginationService.class);

  /**
   * Gets pagination metadata.
   *
   * @param offset the offset
   * @param limit the limit
   * @param availableElementsNumber the available elements number
   * @return the pagination metadata
   */
  public PagedResources.PageMetadata getPaginationMetadata(Integer offset, Integer limit, int availableElementsNumber) {
    int requestedDataSize = limit - offset;
    if (requestedDataSize <= 0) {
      LOG.error("Pagination error: offset - limit <= 0");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pagination error: offset - limit <= 0");
    }

    long pages = (long) Math.ceil((double) availableElementsNumber / requestedDataSize);
    int currentPostition = offset + 1;
    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(requestedDataSize,
                                                                           currentPostition,
                                                                           availableElementsNumber,
                                                                           pages);
    return metadata;
  }
}
