package com.ecommerce.campusai.service;
import com.ecommerce.campusai.entity.StudyRoom;
import com.ecommerce.campusai.entity.StudyRoomSeat;
import com.ecommerce.campusai.mapper.StudyRoomSeatMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class StudyRoomSeatService {
    private final StudyRoomSeatMapper mapper;

    public StudyRoomSeatService(StudyRoomSeatMapper mapper) {
        this.mapper = mapper;
    }

    public List<StudyRoomSeat> list() {
        return mapper.list();
    }

    public List<StudyRoomSeat> listSeatsByRoom(Integer roomId) {
        return mapper.listByRoomId(roomId);
    }

    public List<StudyRoom> listRooms() {
        return mapper.listRooms();
    }

    public int importSeats(MultipartFile file) throws IOException {
        Workbook wb = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        int count = 0;
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            try {
                Integer roomId = (int) row.getCell(0).getNumericCellValue();
                String code = row.getCell(1).getStringCellValue();
                
                if (roomId != null && code != null && !code.trim().isEmpty()) {
                    StudyRoomSeat seat = new StudyRoomSeat();
                    seat.setRoomId(roomId);
                    seat.setSeatCode(code.trim());
                    mapper.insert(seat);
                    count++;
                }
            } catch (Exception e) {
                System.err.println("第" + (i + 1) + "行数据导入失败: " + e.getMessage());
            }
        }
        
        wb.close();
        return count;
    }
}