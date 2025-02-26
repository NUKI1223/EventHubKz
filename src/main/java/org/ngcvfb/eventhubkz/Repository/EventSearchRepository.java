package org.ngcvfb.eventhubkz.Repository;

import org.ngcvfb.eventhubkz.Models.EventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EventSearchRepository extends ElasticsearchRepository<EventDocument, String> {

}
