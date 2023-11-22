package com.service.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.spring.domain.*;
import com.service.spring.service.AdminService;
import com.service.spring.service.RankService;
import com.service.spring.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SnackController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RankService rankService;

    @GetMapping("selectAllByAdmin.do")
    public String doSelectAllAdmin(Model model) {
        try {
            List<Snack> snacks = adminService.selectAll();
            System.out.println(snacks);
            model.addAttribute("snacks", snacks);
            model.addAttribute("title", "관리자 - 전체 재고 조회");
            return "adminInventory";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("selectAllByStudent.do")
    public String doSelectAllStudent(Model model) {
        System.out.println("✅ Select All Snack Controller");
        try {
            List<Snack> snacks = studentService.selectAll();
            System.out.println(snacks);
//            System.out.println("snacks size = " + snacks.size());
            model.addAttribute("snacks", snacks);
            model.addAttribute("title", "학생 - 전체 재고 조회");
            return "studentInventory";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping( "student/fetchSnack.do")
    public String doFetchSnack(@RequestBody List<Snack> list, HttpSession session) {
        System.out.println("✅ Fetch Snack Controller");
        System.out.println(list);
        try {
            Member loginUser = (Member) session.getAttribute("loginUser");
            for (Snack s : list) {
                studentService.fetchSnack(s);
                History history = new History(loginUser.getMemberId(), s.getSnackId(), s.getQuantity());
                studentService.registerHistory(history);
            }
            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❗️ERROR");
        }
        return "index";
    }

    @GetMapping("voteSnack.do")
    public String doVoteSnackStudent(Model model, HttpSession session) {
        try {
            Member loginUser = (Member) session.getAttribute("loginUser");
            int res = studentService.voteCheck(loginUser);
            System.out.println("✅ vote check. res = " + res);
            if (res > 0) {
                return "redirect:http://localhost:9999/index.jsp";
            }
            List<Snack> snacks = studentService.selectAll();
            model.addAttribute("snacks", snacks);
            model.addAttribute("title", "학생 - 간식 투표");
            return "vote";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping( "api/voteSnack.do")
    public String doVoteSnackPost(@RequestBody List<Snack> list, HttpSession session) {
        System.out.println("✅ Vote Post Snack Controller");
        System.out.println(list);
        System.out.println(session.getAttribute("loginUser"));
        Member loginUser = (Member) session.getAttribute("loginUser");
        try {
            for (Snack s : list) {
                studentService.voteSnack(new Vote(loginUser.getMemberId(), s.getSnackId()));
            }
            return "index";
        } catch (Exception e) {
            System.out.println("❗️ERROR");
        }
        return "index";
    }

    @GetMapping("getMemberRank.do")
    public String doGetMemberRank(Model model, HttpSession session) {
        try {
            List<MemberRank> memberRank = rankService.getMemberRank();
            
            ObjectMapper mapper = new ObjectMapper();
            String jsonMemberRank = mapper.writeValueAsString(memberRank);
            
            model.addAttribute("list", jsonMemberRank);
            model.addAttribute("title", "멤버 랭킹");
            return "rank_page";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("getSnackRank.do")
    public String doGetSnackRank(Model model, HttpSession session) {
        try {
            List<SnackRank> snackRank = rankService.getSnackRank();
            model.addAttribute("list", snackRank);
            model.addAttribute("title", "스낵 랭킹");
            return "snackRankTmp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("getMemberRankBySnack.do")
    @ResponseBody
    public List<MemberRank> doGetMemberRankBySnack(Model model, HttpSession session, @RequestParam String snackId) {
        try {
            System.out.println("✅ getMemberRankBySnack Controller - snackId = " + snackId);
            List<MemberRank> memberRankBySnack = rankService.getMemberRankBySnack(new Snack(Long.parseLong(snackId)));

            System.out.println(memberRankBySnack);
            model.addAttribute("memberRankList", memberRankBySnack);
            return memberRankBySnack;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("❗️ not returned");
        return null;
    }

    @PostMapping("tempSnackUpdate.do")
    public String doUpdateSnack(Model model, HttpSession session) {
        try {
            final int snackDisplay = 100;
            final int otherDisplay = 50;
            List<Snack> snackList = NaverShopSearch.makeList(101, "snack", "과자", snackDisplay);
            snackList.addAll(NaverShopSearch.makeList(201, "snack", "과자", snackDisplay));
            snackList.addAll(NaverShopSearch.makeList(301, "snack", "과자", snackDisplay));
            snackList.addAll(NaverShopSearch.makeList(101, "jelly", "젤리", otherDisplay));
            snackList.addAll(NaverShopSearch.makeList(101, "coffee", "음료", otherDisplay));
            System.out.println("✅Snack Update. snackList.length = " + snackList.size());
            for (Snack s : snackList) {
                adminService.registerSnack(s);
            }
            return "index";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    @GetMapping("/orderSnack.do")
    public String doOrderSnack(Model model, HttpSession session) {
        System.out.println("✅ Order Snack Controller");
        return "order";
    }

    static class Thing {
        int weight, value;

        public Thing(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }
    }

    static int[] getSelectedItemCount(int[][] dp, ArrayList<Thing> things, int N, int budget) {
        int[] selectedItemCounts = new int[things.size()];

        int i = things.size();
        int j = budget;

        while (i > 0 && j > 0) {
            if (dp[i][j] != dp[i - 1][j]) {
                System.out.println("i = " + i + "  j = " + j);
                selectedItemCounts[i]++;
                j -= things.get(i).weight;
            } else {
                i--;
            }
        }

        return selectedItemCounts;
    }

    @GetMapping("/knapsack.do")
    @ResponseBody
    public List<Snack> knapsack(Model model, HttpSession session, @RequestParam int budget) throws Exception {

        try {
            System.out.println("budget = " + budget);
            List<Snack> snacks = new ArrayList<>();

            List<VoteWithSnackInfo> voteWithSnackInfos = adminService.viewVote();
            System.out.println(voteWithSnackInfos);

            int N = voteWithSnackInfos.size();
            ArrayList<Thing> things = new ArrayList<>();
//            things.add(new Thing(0, 0));        // index를 1부터 하기 위해 null값 하나 추가

            for(int i = 0 ; i < N; i ++ ) {
                int v = voteWithSnackInfos.get(i).getPrice();  // 물건의 무게
                int c = voteWithSnackInfos.get(i).getCount();  // 물건의 가치
                int k = 5;   // 물건의 개수

                int tempK = 1;
                while(tempK <= k) {
                    things.add(new Thing(tempK * v, tempK * c));
                    k -= tempK;
                    tempK *= 2;
                }
                if(k != 0) {
                    things.add(new Thing(k * v, k * c));
                }
            }

            System.out.println("things.length = " + things.size());
            int[][] dp = new int[things.size() + 10][budget + 10];
            for(int i = 1 ; i <= things.size() ; i ++) {
                for(int j = 1 ; j <= budget; j ++) {
                    if(j < things.get(i - 1).weight) {
                        dp[i][j] = dp[i - 1][j];
                    } else {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - things.get(i - 1).weight] + things.get(i - 1).value);
                    }
                }
            }

            int[] selectedItemCounts = getSelectedItemCount(dp, things, N, budget);
            System.out.println("Selected Item Counts:");
            for (int i = 1; i < selectedItemCounts.length; i++) {
                System.out.println("Item " + i + ": " + selectedItemCounts[i]);
            }

            return snacks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
