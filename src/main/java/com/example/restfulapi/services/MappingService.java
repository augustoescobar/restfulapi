package com.example.restfulapi.services;

import com.example.restfulapi.dtos.AbstractListRequestDTO;
import com.example.restfulapi.dtos.AbstractListResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MappingService {

    private Logger logger = LoggerFactory.getLogger(MappingService.class);

    @Value("${app.api.list.paging.defaultSize}")
    private Integer defaultPageSize;

    @Value("${app.api.list.paging.defaultPage}")
    private Integer defaultPageNumber;

    @Value("${app.api.list.sorting.defaultDirection}")
    private Sort.Direction defaultSortDirection;

    @Value("${app.api.list.sorting.defaultProperty}")
    private String defaultSortProperty;

    @Autowired
    private ObjectMapper objectMapper;

    public <T extends AbstractListRequestDTO> Pageable toPageRequest(T requestDTO) {
        return PageRequest.of(
                Optional.ofNullable(requestDTO.getPageIndex()).orElse(defaultPageNumber),
                Optional.ofNullable(requestDTO.getPageSize()).orElse(defaultPageSize),
                Optional.ofNullable(requestDTO.getSortDirection()).orElse(defaultSortDirection),
                Optional.ofNullable(requestDTO.getSortProperty()).orElse(defaultSortProperty)
        );
    }

    public <S, T extends AbstractListResponseDTO<S>> T toListResponseDTO(Page<S> page, Class<T> clazz) {
        try {
            T listResponseDTO = clazz.newInstance();
            listResponseDTO.setContent(page.getContent());
            listResponseDTO.setTotalElements(page.getTotalElements());
            listResponseDTO.setPageSize(page.getSize());
            listResponseDTO.setPageNumber(page.getNumber());
            listResponseDTO.setTotalPages(page.getTotalPages());
            listResponseDTO.setFirst(page.isFirst());
            listResponseDTO.setLast(page.isLast());
            return listResponseDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> String toJson(T obj) {

        try {

            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {

            logger.error("[MappingService#toJson] Failed to convert object into json: ", e);

            return null;
        }
    }
}
