package dekanat.service;

import dekanat.entity.ControlEntity;
import dekanat.repository.ControlRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ControlService {

    private final ControlRepo controlRepo;

    public ControlService(ControlRepo controlRepo) {
        this.controlRepo = controlRepo;
    }

    public List<String> getControlOfType(String type){
        return controlRepo.findAll().stream().filter(controlModel -> Objects.equals(controlModel.getType(), type)).map(ControlEntity::getTitle).toList();
    }



}
