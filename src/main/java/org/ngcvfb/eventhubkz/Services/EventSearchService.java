package org.ngcvfb.eventhubkz.Services;





import org.ngcvfb.eventhubkz.Models.EventDocument;
import org.ngcvfb.eventhubkz.Models.EventModel;
import org.ngcvfb.eventhubkz.Models.Tag;
import org.ngcvfb.eventhubkz.Repository.EventSearchRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventSearchService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final EventSearchRepository eventSearchRepository;

    public EventSearchService(ElasticsearchOperations elasticsearchOperations, EventSearchRepository eventSearchRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.eventSearchRepository = eventSearchRepository;
    }


    public EventDocument saveEventToSearch(EventModel eventModel) {
        EventDocument eventDocument = new EventDocument();
        eventDocument.setId(eventModel.getId().toString());
        eventDocument.setTitle(eventModel.getTitle());
        eventDocument.setAvatarUrl(eventModel.getMainImageUrl());
        eventDocument.setDescription(eventModel.getShortDescription());
        eventDocument.setTags(eventModel.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        eventDocument.setEventDate(eventModel.getEventDate());
        eventDocument.setLocation(eventModel.getLocation());
        return eventSearchRepository.save(eventDocument);
    }


    public List<EventDocument> searchEvents(String searchTerm) {
        Criteria criteria = new Criteria("title").contains(searchTerm)
                .or(new Criteria("description").contains(searchTerm))
                .or(new Criteria("tags").contains(searchTerm));
        Query query = new CriteriaQuery(criteria);

        SearchHits<EventDocument> searchHits = elasticsearchOperations.search(query, EventDocument.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

    }

    public void deleteEventFromSearch(String id) {
        eventSearchRepository.deleteById(id);
    }
}
