// gui_dialog_NV/banthuoc/CartManager.java
package gui_dialog_NV.banthuoc;

import entity.Thuoc;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class CartManager{

    private final ObservableList<CartItem> items = FXCollections.observableArrayList();
    private final Map<Thuoc, CartItem> itemMap = new HashMap<>();

    private final DoubleProperty totalProperty = new SimpleDoubleProperty(0.0);

    public CartManager() {
        items.addListener((ListChangeListener<CartItem>) change -> recalculateTotal());
    }

    public void add(Thuoc thuoc) {
        add(thuoc, 1);
    }

    public void add(Thuoc thuoc, int quantity) {
        if (thuoc == null || quantity <= 0) return;

        CartItem existing = itemMap.get(thuoc);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(thuoc, quantity);
            items.add(newItem);
            itemMap.put(thuoc, newItem);
        }
        // recalculateTotal() sẽ được gọi tự động bởi listener trên items
    }

    public void updateQuantity(Thuoc thuoc, int newQuantity) {
        CartItem item = itemMap.get(thuoc);
        if (item != null) {
            if (newQuantity <= 0) {
                remove(thuoc);
            } else {
                item.setQuantity(newQuantity);
                // Vì chỉ thay đổi quantity (không thay đổi danh sách items)
                // → listener không chạy → cần gọi thủ công recalculate
                recalculateTotal();
            }
        }
    }

    public void remove(Thuoc thuoc) {
        CartItem item = itemMap.remove(thuoc);
        if (item != null) {
            items.remove(item);
            // recalculateTotal() sẽ được gọi tự động bởi listener
        }
    }

    public void clear() {
        items.clear();
        itemMap.clear();
        // Tổng về 0
        recalculateTotal();
    }

    private void recalculateTotal() {
        double sum = items.stream()
                .mapToDouble(item -> item.getThuoc().getGiaBan() * item.getQuantity())
                .sum();
        totalProperty.set(sum);
    }

    public double getTotal() {
        return totalProperty.get();
    }

    public ReadOnlyDoubleProperty totalProperty() {
        return totalProperty;
    }

    public ObservableList<CartItem> getItems() {
        return items;
    }

    /** Kiểm tra giỏ có rỗng không */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Lấy số lượng sản phẩm trong giỏ */
    public int getItemCount() {
        return items.size();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public static class CartItem {
        private final Thuoc thuoc;
        private int quantity;

        public CartItem(Thuoc thuoc, int quantity) {
            this.thuoc = thuoc;
            this.quantity = quantity;
        }

        public Thuoc getThuoc() { return thuoc; }
        public int getQuantity() { return quantity; }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getThanhTien() {
            return thuoc.getGiaBan() * quantity;
        }

        @Override
        public String toString() {
            return thuoc.getTenThuoc() + " x" + quantity;
        }
    }
}