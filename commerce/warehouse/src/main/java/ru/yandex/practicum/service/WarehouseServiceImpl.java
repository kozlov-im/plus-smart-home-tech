package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.feignClient.ShoppingStoreClient;
import ru.yandex.practicum.mapper.WarehouseProductMapper;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.request.NewProductInWarehouseRequest;

import java.security.SecureRandom;
import java.util.*;

@SuppressWarnings("checkstyle:Regexp")
@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseProductMapper warehouseProductMapper;
    private final ShoppingStoreClient shoppingStoreClient;

    private static final List<AddressDto> ADDRESSES;
    private static final Random RANDOM = new SecureRandom();


    static {
        ADDRESSES = Arrays.asList(
                new AddressDto("ADDRESS_1", "ADDRESS_1", "ADDRESS_1", "ADDRESS_1", "ADDRESS_1"),
                new AddressDto("ADDRESS_2", "ADDRESS_2", "ADDRESS_2", "ADDRESS_2", "ADDRESS_2")
        );
    }

    private static final AddressDto CURRENT_ADDRESS = ADDRESSES.get(RANDOM.nextInt(ADDRESSES.size()));

    @Override
    public void addNewProductToWarehouse(NewProductInWarehouseRequest request) {
        checkProductAlreadyInWarehouse(request.getProductId());
        WarehouseProduct product = warehouseProductMapper.mapToWarehouseProduct(request);
        warehouseRepository.save(product);
    }

    @Override
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        WarehouseProduct product = getWarehouseProduct(request.getProductId());
        product.setQuantity(product.getQuantity() + request.getQuantity());
        warehouseRepository.save(product);
        updateQuantityInShoppingStore(product);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return CURRENT_ADDRESS;
    }

    @Override
    public BookedProductsDto checkProductsForBooking(ShoppingCartDto shoppingCartDto) {

        double totalWeight = 0;
        double totalVolume = 0;
        boolean fragile = false;

        for (Map.Entry<UUID, Integer> entry : shoppingCartDto.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            int quantity = entry.getValue();

            WarehouseProduct product = warehouseRepository.findById(productId).orElseThrow(
                    () -> new NoSpecifiedProductInWarehouseException("specified productId " + productId + " was not found"));

            if (product.getQuantity() < quantity) {
                throw new NoSpecifiedProductInWarehouseException("amount of quantity for productId " + productId + " not enough");
            }

            totalWeight += product.getWeight() * quantity;
            totalVolume += product.getWeight() * product.getHeight() * product.getDepth() * quantity;
            fragile |= product.isFragile();
        }
        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(fragile)
                .build();
    }

    private void checkProductAlreadyInWarehouse(UUID productId) {
        warehouseRepository.findById(productId).ifPresent(warehouseProduct -> {
            throw new SpecifiedProductAlreadyInWarehouseException("Product already present in the warehouse"); });
    }

    private WarehouseProduct getWarehouseProduct(UUID productId) {
       return warehouseRepository.findById(productId).orElseThrow(
                () -> new NoSpecifiedProductInWarehouseException("Product is not found"));
    }

    private void updateQuantityInShoppingStore(WarehouseProduct product) {
        int quantity = product.getQuantity();
        QuantityState quantityState = (quantity == 0) ? QuantityState.ENDED
                : (quantity <= 10) ? QuantityState.FEW
                : (quantity <= 100) ? QuantityState.ENOUGH
                : QuantityState.MANY;
        try {
            shoppingStoreClient.updateProductQuantity(product.getProductId(), quantityState);
        } catch (FeignException e) {
            log.error("Feign client error");
        }
    }
}
