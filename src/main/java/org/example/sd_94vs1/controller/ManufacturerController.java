package org.example.sd_94vs1.controller;


import org.example.sd_94vs1.entity.product.Manufacturer;
import org.example.sd_94vs1.repository.Product.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ManufacturerController {
    @Autowired
    ManufacturerRepository manufacturerRepository;
    //    Lấy nhà dung cấp
    @GetMapping("/manufacturer")
    public String getManufacturerPage(Model model) {
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        model.addAttribute("manufacturers", manufacturers);
        return "admin/manufacturer/index";  // Ensure this is the correct path to your template
    }

    @PostMapping("/add-manufacturer")
    public String addManufacturer(@RequestParam String name,
                                  @RequestParam(required = false) String country,
                                  RedirectAttributes redirectAttributes) {

        String manufacturerCode = "ma" + String.format("%05d", (int)(Math.random() * 100000));
        // Kiểm tra lỗi
        if (manufacturerCode == null || manufacturerCode.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Mã nhà cung cấp không được để trống!");
            return "redirect:/admin/add-manufacturer"; // Quay lại trang thêm nhà cung cấp
        }
        if (name == null || name.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Tên nhà cung cấp không được để trống!");
            return "redirect:/admin/add-manufacturer"; // Quay lại trang thêm nhà cung cấp
        }

        // Tạo đối tượng Manufacturer
        Manufacturer manufacturer = Manufacturer.builder()
                .manufacturerCode(manufacturerCode)
                .name(name)
                .country(country)
                .build();

        // Lưu nhà cung cấp vào cơ sở dữ liệu
        manufacturerRepository.save(manufacturer);

        // Thông báo thành công
        redirectAttributes.addFlashAttribute("successMessage", "Thêm nhà cung cấp thành công!");
        return "redirect:/admin/manufacturer"; // Chuyển hướng về trang danh sách nhà cung cấp
    }

    @PostMapping("/delete-manufacturer/{manufacturerCode}")
    public String deleteManufacturer(@PathVariable String manufacturerCode, RedirectAttributes redirectAttributes) {
        // Kiểm tra xem nhà cung cấp có tồn tại không
        Optional<Manufacturer> manufacturerOpt = manufacturerRepository.findById(manufacturerCode);

        if (manufacturerOpt.isPresent()) {
            // Xóa nhà cung cấp, MySQL sẽ tự động xóa các sản phẩm liên quan nhờ vào ON DELETE CASCADE
            manufacturerRepository.delete(manufacturerOpt.get());

            // Thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage", "Xóa nhà cung cấp và các sản phẩm liên quan thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhà cung cấp với mã đã cho!");
        }

        return "redirect:/admin/manufacturer"; // Quay lại trang danh sách nhà cung cấp
    }



}
