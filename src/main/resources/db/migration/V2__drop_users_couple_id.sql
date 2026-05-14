-- V1 baseline은 코드 기준이라 couple_id가 없고, 운영 DB(dev)에만 잔존.
-- IF EXISTS로 두 환경 모두에서 idempotent하게 실행되도록 한다.
ALTER TABLE users
    DROP COLUMN IF EXISTS couple_id;
