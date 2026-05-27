-- V9: Adiciona campos do contrato MQTT à tabela pesagem_evento (NPSV-304)
-- unidade_medida  → enum do ESP32 (G, KG, ML, L, UN)
-- estavel         → flag de estabilidade calculada no firmware
-- resultado_rn01  → resultado da validação RN01 calculada no backend

ALTER TABLE pesagem_evento
    ADD COLUMN IF NOT EXISTS unidade_medida VARCHAR(10),
    ADD COLUMN IF NOT EXISTS estavel        BOOLEAN,
    ADD COLUMN IF NOT EXISTS resultado_rn01 VARCHAR(30);