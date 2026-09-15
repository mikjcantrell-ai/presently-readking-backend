package com.presentlyreading.repository;

import com.presentlyreading.model.ContactInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {

    List<ContactInquiry> findAllByOrderByReceivedDateDesc();

    long countByReadFalse();
}
