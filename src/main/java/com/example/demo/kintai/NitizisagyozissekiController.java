package com.example.demo.kintai;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/kintai/nitizisagyozisseki")
public class NitizisagyozissekiController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserUtil userutil;

    // 一覧表示処理
    @GetMapping("")
    public String getNitizisagyozissekiList(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchSyainbango,
            @RequestParam(defaultValue = "sagyobi") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {

        String baseSql = "SELECT * FROM nitizisagyozisseki";
        String whereClause = "";
        if (searchSyainbango != null && !searchSyainbango.isEmpty()) {
            whereClause = " WHERE syainbango LIKE ?";
        }
        String orderClause = " ORDER BY " + sortField + " " + sortDir;
        String limitClause = " LIMIT ? OFFSET ?";

        String finalSql = baseSql + whereClause + orderClause + limitClause;

        List<NitizisagyozissekiMessage> resultList = (searchSyainbango != null && !searchSyainbango.isEmpty())
                ? jdbcTemplate.query(finalSql, new Object[]{"%" + searchSyainbango + "%", size, (page - 1) * size},
                new BeanPropertyRowMapper<>(NitizisagyozissekiMessage.class))
                : jdbcTemplate.query(finalSql, new Object[]{size, (page - 1) * size},
                new BeanPropertyRowMapper<>(NitizisagyozissekiMessage.class));

        // 総件数取得
        String countSql = "SELECT COUNT(*) FROM nitizisagyozisseki" + whereClause;
        int totalItems = (searchSyainbango != null && !searchSyainbango.isEmpty())
                ? jdbcTemplate.queryForObject(countSql, new Object[]{"%" + searchSyainbango + "%"}, Integer.class)
                : jdbcTemplate.queryForObject(countSql, Integer.class);

        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("nitizisagyozisseki", resultList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("searchSyainbango", searchSyainbango);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("content", "kintai/nitizisagyozisseki/list");

        return "kintai/common/layout";
    }
}