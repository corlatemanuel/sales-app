package Service;

import Entity.*;
import Repo.Repository;
import java.util.List;
import java.util.Optional;

public class ServiceProduse {
    private Repository<Produs> entRepository;

    public ServiceProduse(Repository<Produs> entRepository) {
        this.entRepository = entRepository;
    }

    public void adaugaEntitate(Produs ent) throws Exception {
        entRepository.add(ent);
    }

    public List<Produs> toateEntitatile() {
        return entRepository.findAll();
    }

    public Optional<Produs> entDupaID(int id) {return entRepository.findById(id);}

    public void modificaEntitatea(Produs ent) throws Exception {
        entRepository.update(ent);
    }

    public void stergeEntitatea(int id) throws Exception {
        entRepository.delete(id);
    }
}