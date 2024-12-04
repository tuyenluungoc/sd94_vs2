package org.example.sd_94vs1.service;

import org.example.sd_94vs1.entity.Inventory;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;
import org.example.sd_94vs1.repository.InventoryRepository;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.example.sd_94vs1.repository.ShoppingCartProductsRepository;
import org.example.sd_94vs1.repository.ShoppingCartRepository;
import org.example.sd_94vs1.repository.WarrantyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private WarrantyRepo warrantyRepository;
    @Autowired
    private DetailedProductRepository detailedProductRepository;

    @Autowired
    private ShoppingCartProductsRepository shoppingCartProductsRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;


    public void generateAndSaveInventoryFromDetailedProduct() {
        // Lấy tất cả các bản ghi DetailedProduct
        List<DetailedProduct> detailedProducts = detailedProductRepository.findAll();

        // Duyệt qua từng DetailedProduct
        for (DetailedProduct detailedProduct : detailedProducts) {
            // Lấy thông tin Product từ DetailedProduct
            Product product = detailedProduct.getProduct();  // Giả sử là đã có quan hệ giữa DetailedProduct và Product

            // Tạo nhiều bản ghi Inventory từ mỗi DetailedProduct
            for (int i = 0; i < detailedProduct.getQuantity(); i++) {
                Inventory inventory = new Inventory();
                inventory.setInventoryCode(generateInventoryCode());  // Tạo mã inventoryCode ngẫu nhiên hoặc theo quy tắc của bạn
                inventory.setDetailedProduct(detailedProduct);  // Gắn DetailedProduct
                inventory.setProduct(product);  // Gắn Product
                inventory.setQuantity(1);  // Mỗi bản ghi đại diện cho một sản phẩm
                inventory.setStatus("available");  // Trạng thái mặc định là có sẵn
                inventory.setDateReceived(new Date());  // Ngày nhận
                inventory.setDateUpdated(new Date());  // Ngày cập nhật

                // Sinh IMEI cho Inventory
                String imei = generateImeiForInventory(product, detailedProduct);
                inventory.setImei(imei);

                // Lưu Inventory vào cơ sở dữ liệu
                inventoryRepository.save(inventory);
            }
        }
    }



    private String generateImeiForInventory(Product product, DetailedProduct detailedProduct) {
        // TAC: 6 số từ productCode, đảm bảo chỉ có số
        String tac = String.format("%06d", Math.abs(product.getProductCode().hashCode()) % 1000000);

        // FAC: Lấy 2 chữ số đầu từ detailedProductCode (nếu không đủ, sẽ dùng 0 để bổ sung)
        String fac = detailedProduct.getDetailedProductCode().replaceAll("\\D", "").substring(0, Math.min(2, detailedProduct.getDetailedProductCode().length()));

        // SNR: 6 số từ detailedProductCode (dùng hashCode() và Math.abs() để đảm bảo là số)
        String snr = String.format("%06d", Math.abs(detailedProduct.getDetailedProductCode().hashCode()) % 1000000);

        // Check digit: 1 số ngẫu nhiên
        int checkDigit = (int) (Math.random() * 10);

        // Kết hợp để tạo IMEI base
        String imeiBase = tac + fac + snr + checkDigit;

        // Thêm UUID để đảm bảo tính duy nhất, chỉ lấy phần đầu của UUID để không vượt quá độ dài 15 ký tự
        String uniqueImei = imeiBase + UUID.randomUUID().toString().replaceAll("-", "").substring(0, Math.max(0, 15 - imeiBase.length()));

        // Nếu IMEI vẫn dài hơn 15 ký tự, cắt lại
        if (uniqueImei.length() > 15) {
            uniqueImei = uniqueImei.substring(0, 15);
        }

        // Trả về IMEI duy nhất và giới hạn 15 ký tự
        return uniqueImei;
    }


    // Phương thức tạo mã InventoryCode ngẫu nhiên hoặc theo quy tắc của bạn
    private String generateInventoryCode() {
        return "iv" + (int) (Math.random() * 1000000);
    }






}


