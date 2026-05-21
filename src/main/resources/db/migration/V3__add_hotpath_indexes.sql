-- 핫패스 쿼리 대상 인덱스 추가 (#8)
-- 인덱스 라이프사이클(추가/변경/제거) 전체를 Flyway 버전 파일로 이력 관리.
-- 엔티티 `@Index` / `@Table(indexes = ...)` 어노테이션은 사용하지 않음 (코드/DB 불일치 차단).
-- IF NOT EXISTS 로 멱등 처리하여 로컬에서 이미 같은 이름의 인덱스가 만들어진 환경에서도 안전.

-- P1: diary 핫패스 복합 인덱스
-- 대상 쿼리
--   - findDiariesByUserIdAndDate(userId, date)            — 일기 작성 시 중복 체크
--   - findByUserIdAndDateBetweenOrderByDateAsc(userId, ..) — 월별/기간 조회 (ORDER BY date 까지 인덱스로 처리)
CREATE INDEX IF NOT EXISTS idx_diary_user_id_date ON diary (user_id, date);
-- P3. refresh_token 인덱스
-- 대상 쿼리
--   - findByTokenHash
--   - deleteAllByUserId
CREATE INDEX IF NOT EXISTS idx_refresh_token_token_hash ON refresh_token (token_hash);
CREATE INDEX IF NOT EXISTS idx_refresh_token_user_id ON refresh_token (user_id);
