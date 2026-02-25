package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.DiningTableRequest;
import com.example.restaurantmanagement.dto.response.DiningTableResponse;
import com.example.restaurantmanagement.entity.DiningTable;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.mapper.DiningTableMapper;
import com.example.restaurantmanagement.repository.DiningTableRepository;
import com.example.restaurantmanagement.service.DiningTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiningTableServiceImpl implements DiningTableService {

    private final DiningTableRepository tableRepository;
    private final DiningTableMapper tableMapper;

    @Override
    @Transactional
    public DiningTableResponse create(DiningTableRequest request) {
        if (tableRepository.existsByTableCode(request.getTableCode()))
            throw new AppException(ErrorCode.TABLE_CODE_ALREADY_EXISTS);

        DiningTable table = tableMapper.toEntity(request);
        table.setStatus(TableStatus.AVAILABLE);
        return tableMapper.toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional(readOnly = true)
    public DiningTableResponse getById(Integer id) {
        return tableMapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiningTableResponse> getAll(Pageable pageable) {
        return tableRepository.findAll(pageable).map(tableMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiningTableResponse> search(String keyword, TableStatus status, Pageable pageable) {
        return tableRepository.search(keyword, status, pageable).map(tableMapper::toResponse);
    }

    @Override
    @Transactional
    public DiningTableResponse update(Integer id, DiningTableRequest request) {
        DiningTable table = findById(id);

        if (!table.getTableCode().equals(request.getTableCode())
                && tableRepository.existsByTableCode(request.getTableCode()))
            throw new AppException(ErrorCode.TABLE_CODE_ALREADY_EXISTS);

        tableMapper.updateEntity(request, table);
        return tableMapper.toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional
    public DiningTableResponse updateStatus(Integer id, TableStatus status) {
        DiningTable table = findById(id);
        table.setStatus(status);
        return tableMapper.toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!tableRepository.existsById(id))
            throw new AppException(ErrorCode.TABLE_NOT_FOUND);
        tableRepository.deleteById(id);
    }

    private DiningTable findById(Integer id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND));
    }
}

