package com.example.cnpm;

import com.example.cnpm.DatabaseClass.RequestChangeSchedule;
import com.example.cnpm.DatabaseClass.User;
import com.example.cnpm.DatabaseClass.WorkSchedule;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DataBaseConnector {

    public static DataBaseConnector INSTANCE;
    private final String databaseName;
    private Connection connection;

    public DataBaseConnector() {
        this.databaseName = "database.db";
    }

    public static void init() {
        if (null == INSTANCE) {
            INSTANCE = new DataBaseConnector();
            INSTANCE.connect();
        }
    }

    public void connect() {
        try {
            // Load SQLite JDBC driver
//            Class.forName("org.sqlite.JDBC");

            // Establish connection to the database
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);

            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to disconnect from the database: " + e.getMessage());
        }
    }

    /* Kiem tra UserId co ton tai trong bang */
    public int checkUserId(String userID) {
        try {
            String query = "SELECT COUNT(*) AS cnt FROM Users WHERE UserID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt("cnt");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /* Tra cuu ca lam viec cu the cua nhan vien*/
    public void getWorkScheduleForEmployee(int UsersId) {
        try {
            // Chuẩn bị truy vấn SQL
            String sql = "SELECT * FROM WorkSchedule WHERE UserID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, UsersId);

            // Thực hiện truy vấn và lấy kết quả
            ResultSet resultSet = statement.executeQuery();

            // Xử lý kết quả
            while (resultSet.next()) {
                int workScheduleId = resultSet.getInt("WorkScheduleID");
                int userId = resultSet.getInt("UserID");
                String workDate = resultSet.getString("WorkDate");
                String shift = resultSet.getString("Shift");

                // Xử lý thông tin lịch làm việc, ví dụ in ra console
                System.out.println("WorkScheduleID: " + workScheduleId + ", UserID: " + userId + ", WorkDate: " + workDate + ", Shift: " + shift);
            }
        } catch (SQLException e) {
            System.out.println("Truy vấn thất bại: " + e.getMessage());
        }
    }

    /*Xem thong tin ca lam viec */
    public void getAttendanceCountForShift(String date, String shift) {
        try {
            String query = "SELECT COUNT(*) AS present_count FROM WorkSchedule " +
                    "WHERE WorkDate = ? AND Shift = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, date);
            statement.setString(2, shift);

            ResultSet resultSet = statement.executeQuery();
            int presentCount = resultSet.getInt("present_count");

            System.out.println("Number of employees present in shift " + shift + " on " + date + ": " + presentCount);

            // Now, get the absent count
            int totalEmployees = getTotalEmployees();
            int absentCount = totalEmployees - presentCount;
            System.out.println("Number of employees absent in shift " + shift + " on " + date + ": " + absentCount);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Tinh tong so nhan vien */
    private int getTotalEmployees() {
        try {
            String query = "SELECT COUNT(*) AS total_employees FROM Users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getInt("total_employees");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* Xep hang nhan vien*/
    public void rankEmployees() {
        try {
            String query = "SELECT UserID, " +
                    "(SELECT COUNT(*) FROM Attendance WHERE UserID = Users.UserID AND Late = true) AS late_count, " +
                    "(SELECT COUNT(*) FROM LeaveRequest WHERE UserID = Users.UserID AND RequestType = 'Nghỉ phép' AND Status = 'Đã duyệt') AS leave_count, " +
                    "(SELECT COUNT(*) FROM LeaveRequest WHERE UserID = Users.UserID AND RequestType = 'Tăng ca' AND Status = 'Đã duyệt') AS overtime_count " +
                    "FROM Users";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int userId = resultSet.getInt("UserID");
                int lateCount = resultSet.getInt("late_count");
                int leaveCount = resultSet.getInt("leave_count");
                int overtimeCount = resultSet.getInt("overtime_count");

                // Calculate ranking based on criteria (you can define your own logic here)
                int totalPoints = lateCount * 2 + leaveCount * 5 + overtimeCount * 3;

                // You can store or print this information as needed
                System.out.println("UserID: " + userId + " - Total Points: " + totalPoints);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Lấy RoleID của người dùng dựa trên UserID */
    public int getRoleID(String userID) {
        try {
            String query = "SELECT RoleID FROM Users WHERE UserID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt("RoleID");
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý lỗi theo ý bạn
            return -1;
        }
    }

    /*Truy vấn thông tin từ UserID*/
    public String getUserInfoByUserID(String userID, int queryType) {
        try {
            String columnName = ""; // Tên cột tương ứng với loại truy vấn

            // Xác định tên cột dựa trên loại truy vấn
            switch (queryType) {
                case 1:
                    columnName = "Name";
                    break;
                case 2:
                    columnName = "PhoneNumber";
                    break;
                case 3:
                    columnName = "Email";
                    break;
            }

            // Chuẩn bị truy vấn SQL để lấy thông tin từ UserID
            String sql = "SELECT " + columnName + " FROM Users WHERE UserID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userID);

            // Thực hiện truy vấn và lấy kết quả
            ResultSet resultSet = statement.executeQuery();
            // Lấy giá trị từ kết quả truy vấn
            return resultSet.getString(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    /* Kiểm tra thông tin đăng nhập */
    public String kiemTraDangNhap(String tenDangNhap, String matKhau) {
        try {
            // Chuẩn bị truy vấn SQL
            String sql = "SELECT UserID FROM Users WHERE Username = ? AND PasswordHash = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, tenDangNhap);
            statement.setString(2, matKhau);

            // Thực hiện truy vấn và lấy kết quả
            ResultSet resultSet = statement.executeQuery();

            // Kiểm tra xem tập kết quả có hàng nào không
            if (resultSet.next()) {
                // Người dùng được tìm thấy, trả về UserID
                String userId = resultSet.getString("UserID");
                return userId;
            } else {
                // Người dùng không được tìm thấy, trả về null
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*Xep hang phong ban*/
    public void calculateDepartmentRanking(int departmentId) {
        try {
            String query = "SELECT COUNT(*) AS EmployeeCount FROM Users WHERE DepartmentID = ?";
            PreparedStatement employeeCountStmt = connection.prepareStatement(query);
            employeeCountStmt.setInt(1, departmentId);
            ResultSet employeeCountResult = employeeCountStmt.executeQuery();

            int employeeCount = 0;
            if (employeeCountResult.next()) {
                employeeCount = employeeCountResult.getInt("EmployeeCount");
            }

            String performanceQuery = "SELECT SUM(Overtime) AS TotalOvertime, " +
                    "SUM(DaysOff) AS TotalDaysOff, " +
                    "SUM(Late) AS TotalLate " +
                    "FROM PerformanceData WHERE UserID IN " +
                    "(SELECT UserID FROM Users WHERE DepartmentID = ?)";
            PreparedStatement performanceStmt = connection.prepareStatement(performanceQuery);
            performanceStmt.setInt(1, departmentId);
            ResultSet performanceResult = performanceStmt.executeQuery();

            int totalOvertime = 0;
            int totalDaysOff = 0;
            int totalLate = 0;
            if (performanceResult.next()) {
                totalOvertime = performanceResult.getInt("TotalOvertime");
                totalDaysOff = performanceResult.getInt("TotalDaysOff");
                totalLate = performanceResult.getInt("TotalLate");
            }

            // Bây giờ bạn có thể tính toán xếp hạng của phòng/ban dựa trên các chỉ số hiệu suất đã tính được
            // Ví dụ:
            double departmentRanking = (totalOvertime + totalDaysOff + totalLate) / (double) employeeCount;

            System.out.println("Department Ranking for Department ID " + departmentId + ": " + departmentRanking);

            // Đóng các tài nguyên
            employeeCountResult.close();
            employeeCountStmt.close();
            performanceResult.close();
            performanceStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserProfileFromId(String id) {
        String sql = "SELECT *  FROM Users,Departments,Roles WHERE UserID = ? AND Users.RoleID = Roles.RoleID AND Users.DepartmentID = Departments.DepartmentID";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String userId = resultSet.getString("UserID");
                String name = resultSet.getString("Name");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String email = resultSet.getString("Email");
                String role = resultSet.getString("RoleName");
                String department = resultSet.getString("DepartmentName");

                return new User(userId, name, email, department, phoneNumber, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateProfile(User user) {
        String sql = "UPDATE Users SET Name = ?, PhoneNumber = ?, Email = ? WHERE UserID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPhone());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getId());
            executeUpdate(statement);
            System.out.println("Update profile successfully");
            statement.close();
        } catch (SQLException e) {
            System.err.println("Update profile failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<WorkSchedule> getScheduleFromIdAndTime(String id_user, int year, int month) {
        String sql = "SELECT * FROM WorkSchedule WHERE UserID = ? AND strftime('%m', WorkDate) = ? AND strftime('%Y', WorkDate) = ?";
        List<WorkSchedule> listWorkSchedule = new ArrayList<>();
        System.out.println("id_user: " + id_user + " month: " + month + " year: " + year);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id_user);
            statement.setString(2, String.valueOf(month));
            statement.setString(3, String.valueOf(year));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String workDateString = resultSet.getString("WorkDate"); // Get the date as a string
                java.util.Date workDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    workDate = sdf.parse(workDateString); // Parse the string into a Date object
                } catch (ParseException e) {
                    e.printStackTrace(); // Handle parsing exception
                }

                String shift = resultSet.getString("Shift");
                String id_schedule = resultSet.getString("WorkScheduleID");
                WorkSchedule workSchedule = new WorkSchedule(id_schedule, id_user, workDate, shift);
                listWorkSchedule.add(workSchedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listWorkSchedule;
    }

    public void sentRequestFromIdAndTime(String id_user, String type, Date date) {
        String sql = "INSERT INTO LeaveRequest (UserID, RequestType, RequestDate, Status, RequestID) VALUES (?, ?, ?, ?, ?)";
        try {
            String randomID = UUID.randomUUID().toString();
            PreparedStatement statement = connection.prepareStatement(sql);

            // Chuyển đổi ngày thành chuỗi "yyyy-MM-dd"
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(date);

            statement.setString(1, id_user);
            statement.setString(2, type);
            statement.setString(3, formattedDate); // Sử dụng chuỗi đã được định dạng
            statement.setString(4, "Pending");
            statement.setString(5, randomID);
            executeUpdate(statement);
            System.out.println("Sent request successfully");
        } catch (SQLException e) {
            System.err.println("Sent request failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<RequestChangeSchedule> getLeaveRequestsForUser() {
        List<RequestChangeSchedule> requests = new ArrayList<>();

        String sql = "SELECT lr.RequestID, lr.UserID, lr.RequestType, lr.RequestDate, lr.Status, u.Name " +
                "FROM LeaveRequest lr " +
                "INNER JOIN Users u ON lr.UserID = u.UserID";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy thông tin từ kết quả truy vấn và tạo các đối tượng RequestChangeSchedule
                String requestID = resultSet.getString("RequestID");
                String userId = resultSet.getString("UserID");
                String requestType = resultSet.getString("RequestType");
                String workDateString = resultSet.getString("RequestDate"); // Get the date as a string
                java.util.Date requestDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    requestDate = sdf.parse(workDateString); // Parse the string into a Date object
                } catch (ParseException e) {
                    e.printStackTrace(); // Handle parsing exception
                }
                String status = resultSet.getString("Status");
                String userName = resultSet.getString("Name");

                RequestChangeSchedule request = new RequestChangeSchedule(requestID, userId, requestType, requestDate, status, userName);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public List<RequestChangeSchedule> getLeaveRequestsForUserByUserId(String UserId) {
        List<RequestChangeSchedule> requests = new ArrayList<>();

        String sql = "SELECT lr.RequestID, lr.UserID, lr.RequestType, lr.RequestDate, lr.Status, u.Name " +
                "FROM LeaveRequest lr " +
                "INNER JOIN Users u ON lr.UserID = u.UserID WHERE lr.UserID = ?";
        try {

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, UserId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Lấy thông tin từ kết quả truy vấn và tạo các đối tượng RequestChangeSchedule
                String requestID = resultSet.getString("RequestID");
                String userId = resultSet.getString("UserID");
                String requestType = resultSet.getString("RequestType");
                String workDateString = resultSet.getString("RequestDate"); // Get the date as a string
                java.util.Date requestDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    requestDate = sdf.parse(workDateString); // Parse the string into a Date object
                } catch (ParseException e) {
                    e.printStackTrace(); // Handle parsing exception
                }
                String status = resultSet.getString("Status");
                String userName = resultSet.getString("Name");

                RequestChangeSchedule request = new RequestChangeSchedule(requestID, userId, requestType, requestDate, status, userName);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;

    }

    public void changeStatusRequests(String RequestID, String NewStatus) {
        String sql = "UPDATE LeaveRequest SET Status = ? WHERE RequestID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, NewStatus);
            statement.setString(2, RequestID);
            executeUpdate(statement);
            System.out.println("Update status successfully");
        } catch (SQLException e) {
            System.err.println("Update status failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void executeUpdate(PreparedStatement preparedStatement) {
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}