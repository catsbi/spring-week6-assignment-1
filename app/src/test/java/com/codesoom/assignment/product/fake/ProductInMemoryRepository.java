package com.codesoom.assignment.product.fake;

import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 인 메모리로 상품을 저장한다.
 */
public class ProductInMemoryRepository implements ProductRepository {
    private static final ProductInMemoryRepository instance = new ProductInMemoryRepository();

    private static final Map<Long, Product> store = new HashMap<>();
    private static Long sequence = 0L;

    private IdGenerator<Long> idGenerator;

    private ProductInMemoryRepository() {
        idGenerator = new ProductIdGenerator();
    }

    public static ProductInMemoryRepository getInstance() {
        return instance;
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Product> findById(Long id) {
        if (store.containsKey(id)) {
            return Optional.of(store.get(id));
        }
        return Optional.empty();
    }

    @Override
    public <S extends Product> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();

        for (S entity : entities) {
            result.add((S) save(entity));
        }

        return result;
    }

    @Override
    public void deleteAll() {
        store.clear();
        sequence = 0L;
    }

    @Override
    public void delete(Product product) {
        store.remove(product.getId());
    }

    @Override
    public Product save(Product product) {
        if (isNew(product)) {
            final Long generatedId = idGenerator.generate(sequence);
            final Product newProduct = Product.builder()
                    .id(generatedId)
                    .name(product.getName())
                    .maker(product.getMaker())
                    .price(product.getPrice())
                    .imageUrl(product.getImageUrl()).build();

            store.put(generatedId, newProduct);

            return newProduct;
        }

        final Product source = store.get(product.getId());
        source.update(product);

        return source;
    }

    private boolean isNew(Product product) {
        final Long id = product.getId();

        return Objects.isNull(id) || !store.containsKey(id);
    }
}
