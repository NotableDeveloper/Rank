package NotableDeveloper.rank.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProfessorRepositoryTest {
    @Autowired
    ProferssorRepository proferssorRepository;
    static ArrayList<Professor> professors;
}

