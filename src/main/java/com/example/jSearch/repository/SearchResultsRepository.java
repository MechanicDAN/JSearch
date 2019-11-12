package com.example.jSearch.repository;

import com.example.jSearch.entity.SearchResults;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchResultsRepository extends CrudRepository<SearchResults, String> {

}
