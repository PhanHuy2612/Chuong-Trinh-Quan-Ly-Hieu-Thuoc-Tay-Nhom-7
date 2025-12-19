package controller;

import dao.ThongKeDAO;
import entities.ThongKe;
import java.util.List;

public class ThongKeController {
    private final ThongKeDAO TK_DAO = new ThongKeDAO();
    
    public List<ThongKe> getStatistic7DaysAgo() {
        return TK_DAO.select7DaysAgo();
    }
    
    public List<ThongKe> getStatisticDaysByMonthYear(int month, int year) {
        return TK_DAO.selectDaysByMonthYear(month, year);
    }
}
