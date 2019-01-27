package ru.mirea.data.shop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.mirea.data.shop.data.SqlHelper;
import ru.mirea.data.shop.entities.Currency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CurrencyRepository {

    @Autowired
    SqlHelper sqlHelper;

    public List<Currency> getAllCurrencies(){
        String sql = "SELECT * FROM currency";
        try (Statement stmt  = sqlHelper.getConnection().createStatement()){
            ResultSet rs  = stmt.executeQuery(sql);
            return createCurrenciesList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Currency getCurrencyById(int id){
        String sql = "SELECT * FROM currency WHERE id = ?";
        try (PreparedStatement pstmt  = sqlHelper.getConnection().prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs  = pstmt.executeQuery();
            List<Currency> list = createCurrenciesList(rs);
            return list.size() == 0 ? null : list.get(0);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int insertIntoCurrency(String currency, double exchange_rate){
        String sql = "INSERT INTO currency(currency, exchange_rate) VALUES(?,?)";
        try (PreparedStatement pstmt = sqlHelper.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, currency);
            pstmt.setDouble(2, exchange_rate);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  -1;
        }
    }


    private ArrayList<Currency> createCurrenciesList(ResultSet rs){
        ArrayList<Currency> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Currency currency = new Currency(rs.getInt("id"), rs.getString("currency")
                        , (rs.getDouble("exchange_rate")));
                list.add(currency);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return list;
    }
}
