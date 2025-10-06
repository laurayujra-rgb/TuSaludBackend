package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.RoomsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RoomsService {

    @Autowired
    private RoomsRepository roomsRepository;

    public List<RoomsEntity> getAllRooms() {
        return roomsRepository.findAll();
    }

    public List<RoomsEntity> getAllRoomsByStatus() {
        return roomsRepository.findAllByRoomsStatus();
    }

    public Optional<RoomsEntity> getRoomById(Long id) {
        return Optional.ofNullable(roomsRepository.findByIdAndByRoomsStatus(id, 1));
    }

    public Optional<RoomsEntity> createRoom(RoomsEntity roomsEntity) {
        roomsEntity.setRoomOccupied(false);
        return Optional.of(roomsRepository.save(roomsEntity));
    }

    public Optional<RoomsEntity> updateRoom(Long id, RoomsEntity roomsEntity) {
        Optional<RoomsEntity> existing = roomsRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();

        RoomsEntity room = existing.get();
        room.setRoomName(roomsEntity.getRoomName());
        return Optional.of(roomsRepository.save(room));
    }

    public Optional<RoomsEntity> deleteRoom(Long id) {
        Optional<RoomsEntity> existing = roomsRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();

        RoomsEntity room = existing.get();
        room.setRoomStatus(0);
        return Optional.of(roomsRepository.save(room));
    }

    // 🔹 Salas disponibles
    public List<RoomsEntity> getAvailableRooms() {
        return roomsRepository.findAvailableRooms();
    }

    // 🔹 Salas ocupadas
    public List<RoomsEntity> getOccupiedRooms() {
        return roomsRepository.findOccupiedRooms();
    }
}
