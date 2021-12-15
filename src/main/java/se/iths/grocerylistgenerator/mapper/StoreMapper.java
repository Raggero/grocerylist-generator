package se.iths.grocerylistgenerator.mapper;

import se.iths.grocerylistgenerator.dto.StoreDto;
import se.iths.grocerylistgenerator.dto.StoreDto;
import se.iths.grocerylistgenerator.model.Store;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StoreMapper {

    public StoreMapper() {
    }

    public Store mapp(StoreDto storeDto) {
        return new Store(storeDto.getName(), storeDto.getStoreContactInfo());
    }

    public StoreDto mapp(Store store) {
        return new StoreDto(store.getId(), store.getName(), store.getStoreContactInfo());
    }

    public List<StoreDto> mapp(List<Store> stores) {
        return stores
                .stream()
                .map(this::mapp)
                .collect(Collectors.toList());
    }

    public Optional<StoreDto> mapp(Optional<Store> optionalStore) {
        if (optionalStore.isEmpty())
            return Optional.empty();
        return Optional.of(mapp(optionalStore.get()));
    }
}