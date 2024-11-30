package org.example.sd_94vs1.repository.Product;

import org.example.sd_94vs1.entity.product.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
    Optional<ProductType> findByProductTypeCode(String productTypeCode);

    boolean existsByProductTypeCode(String productTypeCode);
   // Bạn có thể định nghĩa các phương thức truy vấn tùy chỉnh tại đây
}
