package Controller;

import Enity.ImportedPhone;
import Enity.OriginalPhone;
import Enity.Phone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PhoneController {
    private List<Phone> phones;
    private static final String FILE_PATH = "src\\Data\\mobile.csv";

    public PhoneController() {
        this.phones = readPhonesFromFile();  // Đọc dữ liệu từ file CSV khi khởi động
    }

    // Đọc dữ liệu từ file CSV
    private List<Phone> readPhonesFromFile() {
        List<Phone> phones = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 5) continue; // Đảm bảo dữ liệu hợp lệ
                String id = fields[0];
                String name = fields[1];
                double price = Double.parseDouble(fields[2]);
                int quantity = Integer.parseInt(fields[3]);
                String manufacturer = fields[4];

                // Kiểm tra xem là loại điện thoại chính hãng hay xách tay
                if (fields.length == 7) {  // Chính Hãng
                    String warrantyPeriod = fields[5];
                    String warrantyScope = fields[6];
                    phones.add(new OriginalPhone(id, name, price, quantity, manufacturer, warrantyPeriod, warrantyScope));
                } else if (fields.length == 6) {  // Xách Tay
                    String importCountry = fields[5];
                    String condition = fields[6];
                    phones.add(new ImportedPhone(id, name, price, quantity, manufacturer, importCountry, condition));
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file CSV: " + e.getMessage());
        }
        return phones;
    }

    // Ghi dữ liệu vào file CSV
    private void writePhonesToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (Phone phone : phones) {
                if (phone instanceof OriginalPhone) {
                    OriginalPhone original = (OriginalPhone) phone;
                    writer.write(String.format("%s,%s,%f,%d,%s,%s,%s\n",
                            original.getId(), original.getName(), original.getPrice(),
                            original.getQuantity(), original.getManufacturer(),
                            original.getWarrantyPeriod(), original.getWarrantyScope()));
                } else if (phone instanceof ImportedPhone) {
                    ImportedPhone imported = (ImportedPhone) phone;
                    writer.write(String.format("%s,%s,%f,%d,%s,%s,%s\n",
                            imported.getId(), imported.getName(), imported.getPrice(),
                            imported.getQuantity(), imported.getManufacturer(),
                            imported.getImportCountry(), imported.getCondition()));
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file CSV: " + e.getMessage());
        }
    }

    // Thêm điện thoại
    public void addPhone(Phone phone) {
        phones.add(phone);
        writePhonesToFile();  // Ghi lại dữ liệu vào file CSV
    }

    // Xóa điện thoại
    public boolean removePhone(String id) {
        for (Phone phone : phones) {
            if (phone.getId().equals(id)) {
                phones.remove(phone);
                writePhonesToFile();  // Ghi lại dữ liệu sau khi xóa
                return true;
            }
        }
        return false;
    }

    // Lấy tất cả các điện thoại
    public List<Phone> getAllPhones() {
        return phones;
    }

    // Tìm kiếm điện thoại theo tên
    public List<Phone> searchPhone(String name) {
        List<Phone> result = new ArrayList<>();
        for (Phone phone : phones) {
            if (phone.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(phone);
            }
        }
        return result;
    }
}
