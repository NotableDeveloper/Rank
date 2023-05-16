package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

@DataJpaTest
public class ProfessorRepositoryTest {
    @Autowired
    ProfessorRepository proferssorRepository;
    static ArrayList<Professor> professors;
}

