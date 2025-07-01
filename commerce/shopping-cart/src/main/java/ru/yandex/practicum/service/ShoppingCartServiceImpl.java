package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.enums.ShoppingCartState;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.feignClient.WarehouseClient;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.ShoppingCartRepository;
import ru.yandex.practicum.request.ChangeProductQuantityRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto addProductToCard(String username, Map<UUID, Integer> products) {
        ShoppingCart shoppingCart = getShoppingCartForUser(username);
        Map<UUID, Integer> userProductsInCart = shoppingCart.getProducts();
        userProductsInCart.putAll(products);
        shoppingCart.setProducts(userProductsInCart);
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.mapToShoppingCartDto(shoppingCart);
        System.out.println(shoppingCartDto);
        warehouseClient.checkProductsForBooking(shoppingCartDto);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto getUserShoppingCart(String username) {
        ShoppingCart shoppingCart = getShoppingCartForUser(username);
        return shoppingCartMapper.mapToShoppingCartDto(shoppingCart);
    }

    @Override
    public void deactivateUserShoppingCart(String username) {
        ShoppingCart shoppingCart = getShoppingCartForUser(username);
        shoppingCart.setState(ShoppingCartState.DEACTIVATE);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto deleteProductsFromShoppingCart(String username, List<UUID> products) {
        ShoppingCart shoppingCart = getShoppingCartForUser(username);
        Map<UUID, Integer> userProductsInCart = shoppingCart.getProducts();
        userProductsInCart.keySet().removeIf(products::contains);
        shoppingCart.setProducts(userProductsInCart);
        return shoppingCartMapper.mapToShoppingCartDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto changeProductQuantityInShoppingCart(String username, ChangeProductQuantityRequest request) {
        ShoppingCart shoppingCart = getShoppingCartForUser(username);
        Map<UUID, Integer> userProductsInCart = shoppingCart.getProducts();
        userProductsInCart.put(request.getProductId(), request.getNewQuantity());
        shoppingCart.setProducts(userProductsInCart);
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.mapToShoppingCartDto(shoppingCart);
        System.out.println(shoppingCartDto);
        warehouseClient.checkProductsForBooking(shoppingCartDto);
        return shoppingCartMapper.mapToShoppingCartDto(shoppingCartRepository.save(shoppingCart));
    }


    private ShoppingCart getShoppingCartForUser(String username) {
        checkUserAuthorization(username);
        return shoppingCartRepository.findByUsernameAndState(username, ShoppingCartState.ACTIVE)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setUsername(username);
                    shoppingCart.setProducts(new HashMap<>());
                    shoppingCart.setState(ShoppingCartState.ACTIVE);
                    return shoppingCartRepository.save(shoppingCart);
                });
    }

    private void checkUserAuthorization(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("username is not authorized");
        }
    }

}
