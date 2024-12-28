package View;

import Enity.ImportedPhone;
import Enity.OriginalPhone;
import Enity.Phone;

import java.util.List;
import java.util.Scanner;

public class PhoneView {

    private Scanner scanner = new Scanner(System.in);
    private String warrantyPeriod;

    // Hiển thị danh sách điện thoại
    public void displayPhones(List<Phone> phones) {
        // Định dạng bảng hiển thị
        System.out.println("+----+----------------------+------------------+------------+-------------------+----------------------------+-------------------+--------------------+--------------+");
        System.out.println("| ID | Tên Điện Thoại        | Giá Bán         | Số Lượng  | Nhà Sản Xuất      | Thời Gian Bảo Hành         | Phạm Vi Bảo Hành  | Quốc Gia Xách Tay  | Trạng Thái  |");
        System.out.println("+----+----------------------+------------------+------------+-------------------+----------------------------+-------------------+--------------------+--------------+");

        // Hiển thị từng điện thoại
        for (Phone phone : phones) {
            if (phone instanceof OriginalPhone) {
                OriginalPhone originalPhone = (OriginalPhone) phone;
                System.out.printf("| %-2s | %-20s | %-16.2f | %-10d | %-18s | %-26s | %-17s | %-18s | %-12s |\n",
                        originalPhone.getId(),
                        originalPhone.getName(),
                        originalPhone.getPrice(),
                        originalPhone.getQuantity(),
                        originalPhone.getManufacturer(),
                        originalPhone.getWarrantyPeriod(),
                        originalPhone.getWarrantyScope(),
                        "", // Không áp dụng cho Chính Hãng
                        ""); // Không áp dụng cho Chính Hãng
            } else if (phone instanceof ImportedPhone) {
                ImportedPhone importedPhone = (ImportedPhone) phone;
                System.out.printf("| %-2s | %-20s | %-16.2f | %-10d | %-18s | %-26s | %-17s | %-18s | %-12s |\n",
                        importedPhone.getId(),
                        importedPhone.getName(),
                        importedPhone.getPrice(),
                        importedPhone.getQuantity(),
                        importedPhone.getManufacturer(),
                        "", // Không áp dụng cho Xách Tay
                        "", // Không áp dụng cho Xách Tay
                        importedPhone.getImportCountry(),
                        importedPhone.getCondition());
            }
        }

        System.out.println("+----+----------------------+------------------+------------+-------------------+----------------------------+-------------------+--------------------+--------------+");
    }


    // Chọn thêm điện thoại
    public Phone addPhoneInput(String phoneType, String phoneId) {
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Tên điện thoại: ");
        String name = scanner.nextLine();

        System.out.print("Giá bán: ");
        double price = -1;
        while (price <= 0) {
            try {
                price = scanner.nextDouble();
                if (price <= 0) {
                    System.out.print("Giá bán phải là số nguyên dương. Mời bạn nhập lại: ");
                }
            } catch (Exception e) {
                System.out.print("Giá bán phải là một số. Mời bạn nhập lại: ");
                scanner.nextLine(); // Đọc bỏ dòng không hợp lệ
            }
        }

        System.out.print("Số lượng: ");
        int quantity = -1;
        while (quantity <= 0) {
            try {
                quantity = scanner.nextInt();
                if (quantity <= 0) {
                    System.out.print("Số lượng phải là số nguyên dương. Mời bạn nhập lại: ");
                }
            } catch (Exception e) {
                System.out.print("Số lượng phải là một số nguyên. Mời bạn nhập lại: ");
                scanner.nextLine(); // Đọc bỏ dòng không hợp lệ
            }
        }
        scanner.nextLine(); // Đọc bỏ dòng trống

        scanner.nextLine();  // Đọc dòng trống

        System.out.print("Nhà sản xuất: ");
        String manufacturer = scanner.nextLine();

        if (phoneType.equals("Chính Hãng")) {
            System.out.print("Thời gian bảo hành: ");
            String warrantyPeriod = scanner.nextLine().trim();

            while (true) {
                // Kiểm tra nếu không phải là số nguyên dương
                if (!warrantyPeriod.matches("\\d+")) {
                    System.out.println("Thời gian bảo hành phải là số nguyên dương. Mời bạn nhập lại.");
                    warrantyPeriod = scanner.nextLine().trim();
                } else {
                    // Kiểm tra nếu thời gian bảo hành vượt quá 720 ngày
                    if (Integer.parseInt(warrantyPeriod) <= 0 || Integer.parseInt(warrantyPeriod) > 720) {
                        System.out.println("Thời gian bảo hành phải là số nguyên dương và không vượt quá 720 ngày. Mời bạn nhập lại.");
                        warrantyPeriod = scanner.nextLine().trim();
                    } else {
                        break;  // Nếu hợp lệ, thoát khỏi vòng lặp
                    }
                }
            }

            System.out.print("Phạm vi bảo hành (trong nước, quốc tế): ");
            String warrantyScope = scanner.nextLine().trim();

            // Lặp lại yêu cầu nhập cho đến khi nhận được giá trị hợp lệ
            while (!warrantyScope.equalsIgnoreCase("trong nước") && !warrantyScope.equalsIgnoreCase("quốc tế")) {
                System.out.print("Phạm vi bảo hành phải là 'trong nước' hoặc 'quốc tế'. Mời bạn nhập lại: ");
                warrantyScope = scanner.nextLine().trim();
            }


            return new OriginalPhone(id, name, price, quantity, manufacturer, warrantyPeriod, warrantyScope);
        } else if (phoneType.equals("Xách Tay")) {
            System.out.print("Quốc gia xách tay: ");
            String importCountry = scanner.nextLine().trim();

            //equalsIgnoreCase() so sánh chuỗi không phân biệt chữ hoa hay chữ thường.
            while (importCountry.equalsIgnoreCase("Việt Nam")) {
                System.out.print("Quốc gia xách tay không được là Việt Nam. Mời bạn nhập lại: ");
                importCountry = scanner.nextLine().trim();
            }

            System.out.print("Trạng thái (Mới, Cũ): ");
            String condition = scanner.nextLine();
            return new ImportedPhone(id, name, price, quantity, manufacturer, importCountry, condition);
        }

        return null; // Chỉ có thể là Chính Hãng hoặc Xách Tay, không bao giờ trả về null
    }
}
