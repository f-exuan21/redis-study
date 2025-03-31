import http from "k6/http";
import { check } from "k6";

//executor 유형
//     constant-vus: 지정된 수의 가상 사용자가 일정 시간 동안 유지됩니다.
//     ramping-vus: VU 수를 단계적으로 증가/감소시킵니다.
//     constant-arrival-rate: 초당 고정된 요청 수(Iteration 수)를 유지합니다.
//     ramping-arrival-rate: 초당 요청 수를 단계적으로 변화시킵니다.
//     per-vu-iterations: 각 VU가 지정된 횟수만큼 스크립트를 실행합니다.

// === 1) 환경 변수 혹은 고정 값으로 N(DAU) 설정 ===
// 방법 A) 고정 값
// const N = 100_000; // 예시: 10만 DAU
//
// 방법 B) 환경 변수로 받기 (k6 run --env DAU=100000 script.js)
//         없으면 기본값 100000
const N = __ENV.DAU ? parseInt(__ENV.DAU) : 100000;

// === 2) Throughput 계산 ===
const dailyConnections = 2 * N;           // 1일 총 접속 수 (2N)
const avgRPS = dailyConnections / 86400;  // X = 2N / 86400
const peakRPS = Math.floor(avgRPS * 10);             // 10X

// === 3) 테스트 시나리오: 피크 RPS(10X)를 일정 시간 동안 유지 ===
//
// - executor: constant-arrival-rate
//   : 일정 초당 요청 횟수를 유지하는 방식
// - rate: peakRPS (10X)
// - timeUnit: '1s'  (초당 rate)
// - duration: '30s' (30초간 유지 예시)
// - preAllocatedVUs: 충분히 할당(동시 요청을 감당할 수 있어야 함)
// - maxVUs: 최대로 가능할 수 있는 동시 사용자 수
//
// thresholds - 예시
//  - p(95) 응답 시간 200ms 이하
//  - 실패율(http_req_failed)이 1% 이하
export const options = {
    scenarios: {
        peakTest: {
            executor: 'constant-arrival-rate',
            rate: peakRPS,         // 초당 요청 수
            timeUnit: '1s',        // rate 기준 단위
            duration: '30s',       // 테스트 유지 시간(예: 30초)
            preAllocatedVUs: 500,  // 기본 할당 VU 수 (요청 속도에 맞춰 충분히 할당)
            maxVUs: 1000,          // 최대 동시 VU
        },
    },
    thresholds: {
        // http_req_duration: ['p(95)<200'],  // 95퍼센타일 응답시간 < 200ms
        // http_req_failed: ['rate<0.01'],    // 요청 실패율 < 1%
    },
};

export default function movies() {

    const randomShowtime = Math.floor(Math.random() * 500) + 1;

    const randomSeatArr = [];

    const startNumber = Math.floor(Math.random() * 50) + 1;
    const count = Math.floor(Math.random() * 5) + 1;

    for(let i = 0; i < count; i++) {
        randomSeatArr.push(startNumber + i);
    }

    const payload = JSON.stringify({
        seatIds: randomSeatArr,
        showtimeId: randomShowtime
    });

    const headers = {
        'Content-Type': 'application/json',
    };

    const url = `http://host.docker.internal:8080/book`;

    const response = http.post(url, payload, { headers });

    check(response, {
        "status is 200": (r) => r.status === 200,
        "중복 예외 메시지 포함": (r) => r.status === 400 && r.body.includes('이미 예약된 영화 및 좌석임.'),
        "다른 사용자가 예약 중": (r) => r.status === 400 && r.body.includes('다른 사용자가 예약 중입니다.'),
        "좌석 선택 오류": (r) => r.status === 400 && r.body.includes('좌석은 연속적이고 동일 라인 내에 있어야 합니다.'),
    });
}