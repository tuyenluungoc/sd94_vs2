package org.example.sd_94vs1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.sd_94vs1.entity.warranty.Warranty;
import org.example.sd_94vs1.entity.warranty.WarrantyClaim;
import org.example.sd_94vs1.repository.InventoryRepository;
import org.example.sd_94vs1.repository.WarrantyRepo;
import org.example.sd_94vs1.service.WarrantyClaimService;
import org.example.sd_94vs1.service.WarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequestMapping("/bao-hanh")
public class BaoHanhController {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    WarrantyRepo warrantyRepository;

    @Autowired
    private WarrantyClaimService warrantyClaimService;
    @Autowired
    private WarrantyService warrantyService;

    @GetMapping("/bao-hanh")
    public String getBaoHanhPage(Model model, HttpSession session) {
        return "web/bao-hanh";
    }

    @GetMapping("/validate-warranty-code/{warrantyCode}")
    public ResponseEntity<Boolean> validateWarrantyCode(@PathVariable String warrantyCode) {
        boolean isValid = warrantyClaimService.isWarrantyCodeValid(warrantyCode);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/yeu-cau-bao-hanh/{warrantyCode}")
    public ResponseEntity<String> createWarrantyClaim(@RequestBody WarrantyClaim warrantyClaim,@PathVariable String warrantyCode) {
        try {
//             Tìm thực thể Warranty dựa trên mã code
            Warranty warranty = warrantyRepository.findByWarrantyCode(warrantyCode);
            System.out.println(warranty);

            if (warranty == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã bảo hành không hợp lệ!");
            }
//             Gắn đối tượng Warranty vào WarrantyClaim

            warrantyClaim.setWarranty(warranty);
            // Lưu yêu cầu bảo hành
            warrantyClaimService.createWarrantyClaim(warrantyClaim);

            return ResponseEntity.ok("Yêu cầu bảo hành đã được thêm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }


    @GetMapping("/search")
    public String searchWarranty(@RequestParam("imei") String imei, Model model) {
        Map<String, Object> result = warrantyService.findWarrantyByImei(imei);

        if (result.containsKey("errorMessage")) {
            model.addAttribute("errorMessage", result.get("errorMessage"));
        } else {
            model.addAttribute("productName", result.get("name"));
            model.addAttribute("warrantyStatus", result.get("warrantyStatus"));
            model.addAttribute("endDate", result.get("endDate"));
        }

        return "web/bao-hanh";
    }
}
