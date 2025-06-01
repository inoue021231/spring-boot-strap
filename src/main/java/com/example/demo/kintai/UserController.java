package com.example.demo.kintai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserController {

    // モックデータ用のメモリ内リスト（staticで一時的保持）
    private final List<Map<String, String>> userList = new ArrayList<>();

    public UserController() {
        userList.add(new HashMap<>(Map.of(
            "id", "1",
            "name", "山田太郎",
            "role", "管理者",
            "birthdate", "1990-01-01",
            "loginCount", "5"
        )));
        userList.add(new HashMap<>(Map.of(
            "id", "2",
            "name", "佐藤花子",
            "role", "一般",
            "birthdate", "1995-05-15",
            "loginCount", "2"
        )));
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("content", "users");
        model.addAttribute("userList", userList);
        return "layout";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Map<String, String> emptyUser = new HashMap<>();
        emptyUser.put("id", "");
        emptyUser.put("name", "");
        emptyUser.put("role", "");
        emptyUser.put("birthdate", "");
        emptyUser.put("loginCount", "");
        model.addAttribute("user", emptyUser);
        model.addAttribute("content", "user_form");
        return "layout";
    }

    @PostMapping("/save")
    public String save(@RequestParam Map<String, String> userData) {
        String id = userData.get("id");

        if (id == null || id.isEmpty()) {
            id = String.valueOf(userList.size() + 1);
            userData.put("id", id);
            userList.add(userData);
        } else {
            for (Map<String, String> user : userList) {
                if (user.get("id").equals(id)) {
                    user.put("name", userData.get("name"));
                    user.put("role", userData.get("role"));
                    user.put("birthdate", userData.get("birthdate"));
                    user.put("loginCount", userData.get("loginCount"));
                    break;
                }
            }
        }
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Map<String, String> user = userList.stream()
            .filter(u -> u.get("id").equals(id))
            .findFirst()
            .orElse(new HashMap<>());

        // 万が一キーが欠けている場合でも空で埋める（安全対策）
        user.putIfAbsent("name", "");
        user.putIfAbsent("role", "");
        user.putIfAbsent("birthdate", "");
        user.putIfAbsent("loginCount", "");

        model.addAttribute("user", user);
        model.addAttribute("content", "user_form");
        return "layout";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        userList.removeIf(user -> user.get("id").equals(id));
        return "redirect:/users";
    }

    @GetMapping("/delete-confirm/{id}")
    public String confirmDelete(@PathVariable String id, Model model) {
        Map<String, String> user = userList.stream()
            .filter(u -> u.get("id").equals(id))
            .findFirst()
            .orElse(new HashMap<>());

        user.putIfAbsent("name", "");
        user.putIfAbsent("role", "");
        user.putIfAbsent("birthdate", "");
        user.putIfAbsent("loginCount", "");

        model.addAttribute("user", user);
        model.addAttribute("content", "user_delete");
        return "layout";
    }
}
