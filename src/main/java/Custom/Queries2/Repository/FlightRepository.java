package Custom.Queries2.Repository;

import Custom.Queries2.Entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public class FlightRepository extends JpaRepository<Flight, Long>{

    List<Flight> findByStatus(FlightStatus status);

    @Query(value = "select * from flight as f where f.status =:p1 or f.status=:p2", nativeQuery = true)
    List<Flight> findByTwoStatus(@Param("p1") String p1, @Param("p2") String p2);
}
