# Job Portal Backend API

Hệ thống quản lý tin tuyển dụng và kết nối ứng viên chuyên nghiệp, được xây dựng bằng **Spring Boot 3** và **PostgreSQL**.

---

## 🚀 Tính năng nổi bật

- **Xác thực Bảo mật**: JWT Bearer Tokens, Mã OTP qua Email khi đăng ký và Khôi phục mật khẩu.
- **Tìm kiếm nâng cao**: Sử dụng JPA Specification để lọc việc làm và ứng viên theo nhiều tiêu chí (Kỹ năng, Kinh nghiệm, Lương...).
- **Quy trình Duyệt tin (Moderation)**: Admin phê duyệt tin tuyển dụng trước khi hiển thị công khai.
- **Thương hiệu Công ty**: Quản lý profile công ty, logo và thông tin chi tiết dành cho Nhà tuyển dụng.
- **Tương tác Ứng viên**: Nộp đơn trực tuyến kèm CV, lưu việc làm yêu thích và báo cáo tin tuyển dụng vi phạm.

---

## 🛠 Tech Stack

- **Backend**: Spring Boot 3.2.3, Spring Data JPA, Spring Security.
- **Database**: PostgreSQL with HikariCP optimizations.
- **Security**: JWT (io.jsonwebtoken), BCrypt password hashing.
- **Documentation**: SpringDoc OpenAPI (Swagger UI).
- **Communication**: Spring Mail (SMTP).

---

## 📖 Hướng dẫn API (Tóm tắt)

Hệ thống đã tích hợp **Swagger UI**, bạn có thể truy cập tài liệu tương tác tại:
`http://localhost:8080/swagger-ui/index.html`

### 1. Xác thực (`/api/auth`)
| Route | Method | Description |
| :--- | :--- | :--- |
| `/register` | POST | Đăng ký người dùng mới. |
| `/login` | POST | Lấy token JWT. |
| `/verify-otp` | POST | Xác thực email kích hoạt tài khoản. |
| `/forgot-password` | POST | Yêu cầu link đặt lại mật khẩu. |

### 2. Việc làm (`/api/vacancies`)
| Route | Method | Description |
| :--- | :--- | :--- |
| `/` | POST | Đăng tin (Recruiter). |
| `/search` | GET | Tìm kiếm tin tuyển dụng nâng cao. |
| `/status/{s}` | GET | Lọc tin theo trạng thái (OPEN, CLOSED). |

### 3. Công cụ Nhà tuyển dụng (`/api/recruiters`)
- `GET /search-applicants`: Tìm kiếm ứng viên dựa trên Kỹ năng (skills), Học vấn (education), Kinh nghiệm (experience).

---



## 🛠 Run Dự án

1. **Build**: `mvn clean install`
2. **Run**: `mvn spring-boot:run`
3. **Docs**: Truy cập `/swagger-ui/index.html` để thử nghiệm các API.
