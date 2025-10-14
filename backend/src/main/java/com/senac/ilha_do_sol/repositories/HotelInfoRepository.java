package com.senac.ilha_do_sol.repositories;

import com.senac.ilha_do_sol.entities.HotelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelInfoRepository extends JpaRepository <HotelInfo, Long> {
}
