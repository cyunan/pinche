package com.example.pinche.mapper;

import com.example.pinche.bean.Dri_dingdan;
import com.example.pinche.bean.Identity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
@EntityScan("bean.Dri_dingdan")
public interface Dri_repository extends JpaRepository<Dri_dingdan,Integer> {
}
