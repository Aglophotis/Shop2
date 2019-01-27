package ru.mirea.data.shop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.mirea.data.shop.data.SqlHelper;
import ru.mirea.data.shop.entities.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepository {

    @Autowired
    SqlHelper sqlHelper;

    public int updateItem(Item item) {
        String sql = "UPDATE items SET name = ?, price = ?, count = ?, type = ? WHERE id = ?";
        try (PreparedStatement pstmt  = sqlHelper.getConnection().prepareStatement(sql)){
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getPrice());
            pstmt.setInt(3, item.getCount());
            pstmt.setString(4, item.getType());
            pstmt.setInt(5, item.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Item getStuffById(int id){
        String sql = "SELECT * FROM items WHERE type = 'Stuff' AND id = ?";
        return executeSql(id, sql);
    }

    public Item getPetById(int id){
        String sql = "SELECT * FROM items WHERE type = 'Pet' AND id = ?";
        return executeSql(id, sql);
    }

    public List<Item> getAllStuffs(){
        String sql = "SELECT * FROM items WHERE type = 'Stuff'";
        return getAllItems(sql);
    }

    public List<Item> getAllPets(){
        String sql = "SELECT * FROM items WHERE type = 'Pet'";
        return getAllItems(sql);
    }

    public Item getItemById(int id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        return executeSql(id, sql);
    }

    private Item executeSql(int arg, String sql){
        try (PreparedStatement pstmt  = sqlHelper.getConnection().prepareStatement(sql)){
            pstmt.setInt(1, arg);
            ResultSet rs  = pstmt.executeQuery();
            List<Item> list = createItemsList(rs);
            return list.size() == 0 ? null : list.get(0);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int insertIntoItems(String name, String type, int price, int count) {
        String sql = "INSERT INTO items(name, type, price, count) VALUES(?,?,?,?)";
        try (PreparedStatement pstmt = sqlHelper.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setInt(3, price);
            pstmt.setInt(4, count);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  -1;
        }
    }

    private List<Item> getAllItems(String sql) {
        try (PreparedStatement pstmt  = sqlHelper.getConnection().prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            return createItemsList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Item> createItemsList(ResultSet rs){
        ArrayList<Item> list = new ArrayList<>();
        try{
            while (rs.next()) {
                Item item = new Item(rs.getInt("id"), rs.getString("type")
                        ,rs.getString("name"), rs.getInt("price")
                        ,rs.getInt("count"));
                list.add(item);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return list;
    }
}
