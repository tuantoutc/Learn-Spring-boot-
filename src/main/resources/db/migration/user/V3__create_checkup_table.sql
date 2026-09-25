CREATE TABLE IF NOT EXISTS checkups(
    id BIGSERIAL PRIMARY KEY,
    diagnosis VARCHAR(255) NOT NULL UNIQUE,
    patient_id BIGINT,
    constraint fk_checkup_patient
    foreign key (patient_id)
    references patients(id)
);