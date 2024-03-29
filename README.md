# Actuator

## 엔드포인트 목록
- `beans`: 스프링 컨테이너에 등록된 스프링 빈을 보여준다.
- `conditions`: `condition`을 통해서 빈을 등록할 때 평가 조건과 일치하거나 일치하지 않는 이유를 표시한다.
- `configprops`: `@ConfigurationProperties`를 보여준다.
- `env`: `Environment` 정보를 보여준다.
- `health`: 애플리케이션 헬스 정보를 보여준다.
- `httpexchanges`: HTTP 호출 응답 정보를 보여준다. `HttpExchangeRepository`를 구현한 빈을 별도로 등록해야 한다.
- `info`: 애플리케이션 정보를 보여준다. 
- `loggers`: 애플리케이션 로거 설정을 보여주고 변경도 할 수 잇다.
- `metrics`: 애플리케이션의 메트릭 정보를 보여준다.
- `mappings`: `@RequestMapping` 정보를 보여준다.
- `threaddump`: 쓰레드 덤프를 실행해서 보여준다.
- `shutdown`: 애플리케이션을 종료한다. 이 기능은 **기본으로 비활성화** 되어 있다.

# 마이크로미터
- 표준 측정 방식
- 여러 구현체를 붙일 수 있다.
- 약간의 오차를 감안하고 실시간으로 대략의 데이터를 보는 목적으로 사용한다.
## MetricRegistry
- 마이크로미터 구현체를 제공하는 핵심 포인트
- 스프링을 통해서 주입받아서 사용하고, 이곳을 통해서 카운터, 게이지 등을 등록한다.
### Counter(카운터)
- 단조롭게 증가하는 단일 누적 측정항목
  - 단일값
  - 보통 하나씩 증가
  - 누적이므로 전체값을 포함
- 값을 증가하거나 0으로 초기화하는 것은 가능
- 마이크로미터에서 값을 감소하는 기능도 지원하지만, **목적에 맞지 않음**
- 예) HTTP 요청 수

### Timer
- 시간을 측정하는데 사용된다.
  - 카운터와 유사, 실행 시간도 함께 측정할 수 있다.
- `seconds_count`: 누적 실행 수 - `카운터`
- `seconds_sum`: 실행 시간의 합 - `sum`
- `seconds_max`: 최대 실행 시간(가장 오래걸린 실행 시간) - `게이지`
  - 내부에 타임 윈도우라는 개념이 있어서 1~3분 마다 최대 실행 시간이 다시 계산된다.

### Gauge(게이지)
- 임의로 오르내릴 수 있는 단일 숫자 값을 나타내는 메트릭
- 값의 현재 상태를 보는데 사용
- 값이 증가하거나 감소할 수 있음
- 예) 차량의 속도, CPU/메모리 사용량
**카운터와 게이지를 구분할 때는 값이 감소할 수 있는가를 고민해보자**

### Tag, 레이블
- Tag를 사용하면 데이터를 나누어서 확인할 수 있다.
- Tag는 카디널리티가 낮으면서 그룹화 할 수 있는 단위에 사용해야 한다.
  - 예) 성별, 주문 상태, 결제 수단[신용카드, 현금] 등등
- 카디널리티가 높으면 안 된다.
  - 예) 주문번호, PK

# 프로메테우스
- `.` 대신 `_` 포맷을 사용
- 로그 수 처럼 지속해서 숫자가 증가하는 메트릭을 카운터라 한다.
  - 카운터의 메트릭의 마지막에는 관례상 `_total`을 붙임
## 퀵스타트
```bash
docker-compose up -d
```

## 필터
- `{ }`를 사용해서 필터링 가능
 - 예시1: `{uri != "/actuator/prometheus"}` 
 - 예시2: `{method =~ "GET|POST"}`
### 레이블 일치 연산자
- `=`: 제공된 문자열과 정확히 동일한 레이블 선택
- `!=`: 제공된 문자열과 같지 않은 레이블 선택
- `=~`: 제공된 문자열과 정규식 일치하는 레이블 선택
- `!~`: 제공된 문자열과 정규식 일치하지 않는 레이블 선택
### 함수
- `topk(3, http_server_requests_seconds_count)`: 상위 3개만 조회 
- `sum(http_server_requests_seconds_count)`: 값의 합계
- `sum by(method, status) (http_server_requests_seconds_count)`: SQL `group by` 기능과 유사
- `count(http_server_requests_seconds_count)`: 메트릭 자체의 수 카운트 
- `http_server_requests_seconds_count offset 5m`: 5분 전 조회 
- `http_server_requests_seconds_count[1m]`: 범위 벡터 선택, 1분동안 데이터를 풀어서 조회 

## 게이지와 카운터
### 게이지(Gauge)
- 오르고 내리고 하는 값, 현재 상태를 그대로 출력하면 된다.
- 예시) CPU 사용량, 메모리 사용량, 사용중인 커넥션
### 카운터(Counter)
- 단순하게 증가하는 단일 누적 값
- 예시) HTTP 요청 수, 로그 발생 수

**단순 누적값 만으로는 분당 얼마나 요청이 어느정도 증가했는지 파악하기 어렵기 때문에 `increase` 또는 `rate` 함수를 사용해야 함**

- `increase(http_server_requests_seconds_count{uri="/log"}[1m])`
- `rate(http_server_requests_seconds_count{uri="/log"}[1m])`
- `irate(http_server_requests_seconds_count{uri="/log"}[1m])`: 급격하게 증가하는 상황에서 사용하기 좋다

# 그라파나
## 유용한 대시보드
- [스프링부트 대시보드](https://grafana.com/grafana/dashboards/11378-justai-system-monitor/)
  - 메트릭 쿼리 일부는 수정 필요
- [JVM 대시보드](https://grafana.com/grafana/dashboards/4701-jvm-micrometer/)
