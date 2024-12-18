package com.servPet.pet.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pet.model.PetDTO;
import com.servPet.pet.model.PetService;
import com.servPet.pet.model.PetVO;

@Controller
public class PetController {

    private final MebService mebService;
    private final PetService petService;

    public PetController(PetService petService, MebService mebService) {
        this.petService = petService;
        this.mebService = mebService;
    }

    @GetMapping("/pets")
    public String showPetManagementPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/front_end/login";
        }

        String email = principal.getName();
        Optional<MebVO> memberOpt = mebService.findMemberByEmail(email);

        if (memberOpt.isPresent()) {
            MebVO member = memberOpt.get();
            model.addAttribute("member", member);

            // 根據 MEB_ID 查詢寵物列表
            List<PetVO> pets = petService.getPetsByMemberId(member.getMebId());
            if (pets == null || pets.isEmpty()) {
                model.addAttribute("message", "目前沒有寵物資料！");
            }
            model.addAttribute("pets", pets);
        } else {
            return "redirect:/front_end/login";
        }

        return "/front_end/pets"; // 返回 Thymeleaf 模板名稱
    }

    @GetMapping("/pets/image/{id}")
    public ResponseEntity<byte[]> getPetImage(@PathVariable Integer id) {
        Optional<PetVO> petOpt = petService.findPetById(id);

        if (petOpt.isPresent() && petOpt.get().getPetImg() != null) {
            byte[] imageData = petOpt.get().getPetImg();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        }

        // 返回預設圖片
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/pets/add")
    public String addPet(@ModelAttribute PetDTO petDTO,
                         @RequestParam("petImageFile") MultipartFile petImageFile,
                         Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/front_end/login";
            }

            String email = principal.getName();
            Optional<MebVO> memberOpt = mebService.findMemberByEmail(email);
            if (memberOpt.isPresent()) {
                MebVO member = memberOpt.get();
                petDTO.setMebId(member.getMebId());
            } else {
                return "redirect:/front_end/login";
            }

            petService.savePetFromDTO(petDTO, petImageFile);
            return "redirect:/pets";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/pets/update")
    public ResponseEntity<String> updatePet(
            @RequestParam("petId") Integer petId,
            @RequestParam("petName") String petName,
            @RequestParam("petType") String petType,
            @RequestParam(value = "petImageFile", required = false) MultipartFile petImageFile) {
        try {
            Optional<PetVO> petOpt = petService.findPetById(petId);
            if (petOpt.isEmpty()) {
                return new ResponseEntity<>("Pet not found", HttpStatus.NOT_FOUND);
            }

            PetVO pet = petOpt.get();
            pet.setPetName(petName);
            pet.setPetType(petType);

            if (petImageFile != null && !petImageFile.isEmpty()) {
                pet.setPetImage(petImageFile.getBytes());
            }

            petService.updatePet(pet);
            return new ResponseEntity<>("Pet updated successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update pet due to IO error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/delete/{id}")
    public String deletePet(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            petService.deletePet(id); // 呼叫服務層刪除寵物
            redirectAttributes.addFlashAttribute("successMessage", "寵物刪除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/pets"; // 重定向到寵物列表頁面
    }
}
