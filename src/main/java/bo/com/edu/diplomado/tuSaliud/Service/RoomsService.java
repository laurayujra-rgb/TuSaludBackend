package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.PersonsEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.RoomsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomsService {
    @Autowired
    private RoomsRepository roomsRepository;

    public List<RoomsEntity> getAllRooms(){
        return roomsRepository.findAll();
    }
    public List<RoomsEntity> getAllRoomsByStatus(){
        return roomsRepository.findAllByRoomsStatus();
    }
    public Optional<RoomsEntity> getRoomById(Long id){
        return Optional.of(roomsRepository.findByIdAndByRoomsStatus(id, 1));
    }
    public Optional<RoomsEntity> createRoom(RoomsEntity roomsEntity){
        return Optional.of(roomsRepository.save(roomsEntity));
    }
    public Optional<RoomsEntity> updateRoom(Long id, RoomsEntity roomsEntity){
        Optional<RoomsEntity> existingRoom = roomsRepository.findById(id);
        if(existingRoom.isEmpty()){
            return Optional.empty();
        }
        RoomsEntity room = existingRoom.get();
        room.setRoomName(roomsEntity.getRoomName());
        return Optional.of(roomsRepository.save(room));
    }
    public Optional<RoomsEntity> deleteRoom(Long id){
        Optional<RoomsEntity> existingRoom = roomsRepository.findById(id);
        if(existingRoom.isEmpty()){
            return Optional.empty();
        }
        RoomsEntity room = existingRoom.get();
        room.setRoomStatus(0);
        return Optional.of(roomsRepository.save(room));
    }

}
