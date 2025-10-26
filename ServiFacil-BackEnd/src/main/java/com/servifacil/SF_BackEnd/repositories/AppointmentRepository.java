package com.servifacil.SF_BackEnd.repositories;

import com.servifacil.SF_BackEnd.models.AppointmentModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentModel, Integer> {

    @Override
    Optional<AppointmentModel> findById(Integer integer);

    List<AppointmentModel> findAllByClientUserId(int clientId);

    List<AppointmentModel> findAllByServiceServiceId(int serviceId);

    List<AppointmentModel> findAllByAppointmentStatus(String apStatus);

    @Transactional
    @Modifying
    @Query(value = "select * from tb_appointments where Client_ID = :clientId and Ap_Status = :apStatus", nativeQuery = true)
    List<AppointmentModel> findAllAppointmentByUserByStatus(@Param("clientId") int clientId, @Param("apStatus") String apStatus);

    @Transactional
    @Modifying
    @Query(value = "select * from tb_appointments where Service_ID = :serviceId and Ap_Status = :apStatus", nativeQuery = true)
    List<AppointmentModel> findAllAppointmentByServiceByStatus(@Param("serviceId") int serviceId, @Param("apStatus") String apStatus);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tb_appointments SET Ap_Status = :apStatus WHERE Appointment_ID = :appointmentId", nativeQuery = true)
    int updateAppointmentStatus(@Param("appointmentId") int appointmentId, @Param("apStatus") String status);

    boolean existsByService_ServiceIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            int serviceId,
            LocalDateTime endDate,
            LocalDateTime startDate
    );

    @Procedure(procedureName = "spInsertAppointment")
    int spInsertAppointment(
            @Param("vClient_ID") int clientId,
            @Param("vService_ID") int serviceId,
            @Param("vStart_Date") LocalDateTime startDate,
            @Param("vEnd_Date") LocalDateTime endDate,
            @Param("vAp_Status") String apStatus
            );
}
