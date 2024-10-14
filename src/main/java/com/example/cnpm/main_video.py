import os
import sqlite3
import time

import cv2


# Database
def connect_to_database():
    try:
        conn = sqlite3.connect("D:\Work\Project\CNPM_ChamCong\database.db")
        return conn
    except sqlite3.Error as e:
        print("Lỗi khi kết nối đến cơ sở dữ liệu: ", e)
        return None


def insert_attendance(conn, user_id, checkin_time, datetime):
    try:
        cursor = conn.cursor()
        late = 1 if checkin_time >= "08:00:00" else 0
        cursor.execute("INSERT INTO Attendance (userID, WorkDate, CheckInTime,Late) VALUES (?,?,?,?)",
                       (user_id, datetime, checkin_time, late))
        conn.commit()
        print("Đã thêm dữ liệu check-in vào cơ sở dữ liệu")
    except sqlite3.Error as e:
        print("Lỗi khi thêm dữ liệu check-in: ", e)


def get_user_info(conn, user_id):
    try:
        cursor = conn.cursor()
        cursor.execute("SELECT Name FROM Users WHERE UserID = ?", (user_id,))
        user_info = cursor.fetchone()
        return user_info[0]
    except sqlite3.Error as e:
        print("Lỗi khi truy vấn thông tin người dùng: ", e)
        return ""


conn = connect_to_database()


# Get the current directory
def get_current_dir():
    return os.path.dirname(os.path.abspath(__file__))


path = get_current_dir() + "\\images\\"

# Encode faces from a folder
from simple_facerec import SimpleFacerec

sfr = SimpleFacerec()
sfr.load_encoding_images(path)

# Load camera
cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()

    # Detect faces
    face_locations, face_names = sfr.detect_known_faces(frame)
    for face_loc, name in zip(face_locations, face_names):
        y1, x2, y2, x1 = face_loc[0], face_loc[1], face_loc[2], face_loc[3]

        cv2.putText(frame, name, (x1, y1 - 10), cv2.FONT_HERSHEY_DUPLEX, 1, (0, 0, 200), 2)
        cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 0, 200), 4)

    cv2.imshow("Camera", frame)

    key = cv2.waitKey(1)
    if key == 27:
        cv2.destroyWindow("Camera")
        checkinTime = time.time()
        pTime = time.strftime("%H:%M:%S", time.localtime(checkinTime))
        dTime = time.strftime("%Y-%m-%d", time.localtime(checkinTime))

        cnt = 0
        for rname in face_names:
            if rname == "Unknown":
                cnt += 1

        if len(face_names) == 0 or len(face_names) == cnt:
            cv2.destroyAllWindows()
            cv2.imshow("Khong phat hien khuon mat da duoc dang ky   " + pTime + " " + dTime, frame)

            # Print to terminal
            print("Khong phat hien khuon mat nao da duoc dang ky  " + pTime + " " + dTime)
        else:
            cv2.destroyAllWindows()
            cv2.imshow("Check-in thanh cong   " + pTime + " " + dTime, frame)

        key2 = cv2.waitKey(0)
        if key2 == 27:
            cv2.destroyAllWindows()
            break

# Insert to database + Print to terminal
dTime = time.strftime("%Y-%m-%d", time.localtime(checkinTime))
for rname in face_names:
    if rname != "Unknown":
        user_id = rname
        insert_attendance(conn, user_id, pTime, dTime)
        print("Check-in thành công: " + get_user_info(conn, user_id))
print("Thời gian check-in: " + pTime + " " + dTime)
conn.close()

cap.release()
cv2.destroyAllWindows()
