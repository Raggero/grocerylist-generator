package se.iths.grocerylistgenerator.service;

import org.springframework.stereotype.Service;
import se.iths.grocerylistgenerator.dto.StoreDto;
import se.iths.grocerylistgenerator.exception.BadRequestException;
import se.iths.grocerylistgenerator.exception.EntityNotFoundException;
import se.iths.grocerylistgenerator.mapper.StoreMapper;
import se.iths.grocerylistgenerator.entity.Store;
import se.iths.grocerylistgenerator.repository.StoreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    public StoreService(StoreRepository storeRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
    }

    public StoreDto createStore(StoreDto storeDto) {
        isValidStoreDto(storeDto);
        checkStoreNotInDatabase(storeDto);
        return storeMapper.mapp(storeRepository.save(storeMapper.mapp(storeDto)));
    }

    private void isValidStoreDto(StoreDto storeDto) {
        if (storeDto.getName() == null || storeDto.getName().isEmpty()) {
            throw new BadRequestException("Invalid input, you must enter a name for the store!");
        }
    }

    private void checkStoreNotInDatabase(StoreDto storeDto) {
        Optional<Store> store = findStoreByName(storeDto.getName());
        if (store.isPresent()) {
            throw new BadRequestException("The store already exists in the database! Id: "
                    + store.get().getId() + ", Name: " + store.get().getName());
        }
    }

    private Optional<Store> findStoreByName(String name) {
        return storeRepository.findByName(name);
    }

    public StoreDto findStoreById(Long id) {
        return storeMapper.mapp(findById(id));
    }

    public Store findById(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Store with id: " + id + " not found"));
    }

    public List<StoreDto> findAllStores() {
        return storeMapper.mapp(storeRepository.findAll());
    }


}
