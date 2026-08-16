/**
 * 백엔드 REST API 래퍼.
 * 컨트롤러에 실제로 존재하는 엔드포인트만 정의한다.
 */

const BASE_URL = '/api';

export class ApiError extends Error {
    constructor(message, status, body) {
        super(message);
        this.name = 'ApiError';
        this.status = status;
        this.body = body;
    }
}

async function request(method, path, body) {
    const options = { method, headers: {} };

    if (body !== undefined) {
        options.headers['Content-Type'] = 'application/json';
        options.body = JSON.stringify(body);
    }

    let response;
    try {
        response = await fetch(BASE_URL + path, options);
    } catch (e) {
        throw new ApiError('서버에 연결할 수 없습니다.', 0, null);
    }

    // 204 / void 응답은 본문이 비어 있다
    const text = await response.text();
    let data = null;
    if (text) {
        try {
            data = JSON.parse(text);
        } catch {
            data = text;
        }
    }

    if (!response.ok) {
        const message = (data && typeof data === 'object' && data.message)
            ? data.message
            : `요청에 실패했습니다 (${response.status})`;
        throw new ApiError(message, response.status, data);
    }

    return data;
}

export const api = {
    users: {
        /** GET /api/user/findAll */
        findAll: () => request('GET', '/user/findAll'),
        /** POST /api/user  { name, password, email } */
        create: (payload) => request('POST', '/user', payload),
        /** PATCH /api/user/{id}  { name, password, email } */
        update: (id, payload) => request('PATCH', `/user/${id}`, payload),
        /** DELETE /api/user/{id}/delete */
        remove: (id) => request('DELETE', `/user/${id}/delete`)
    },

    auth: {
        /** POST /api/auth  { name, password } — 응답 본문 없음(void) */
        login: (payload) => request('POST', '/auth', payload)
    },

    channels: {
        /** GET /api/channel/{userId}/findAll */
        findAllByUserId: (userId) => request('GET', `/channel/${userId}/findAll`),
        /** POST /api/publicChannel  { channelName, description } */
        createPublic: (payload) => request('POST', '/publicChannel', payload),
        /** POST /api/privateChannel  { participantIds: [] } */
        createPrivate: (payload) => request('POST', '/privateChannel', payload),
        /** PATCH /api/channel/{id}  { channelName, description } */
        update: (id, payload) => request('PATCH', `/channel/${id}`, payload),
        /** DELETE /api/channel/{id}/delete */
        remove: (id) => request('DELETE', `/channel/${id}/delete`)
    },

    messages: {
        /** POST /api/message  { userId, channelId, message } */
        create: (payload) => request('POST', '/message', payload),
        /** PATCH /api/message/{id}  { message } */
        update: (id, payload) => request('PATCH', `/message/${id}`, payload),
        /** DELETE /api/message/{id}/delete */
        remove: (id) => request('DELETE', `/message/${id}/delete`)
        // 목록 조회 엔드포인트는 컨트롤러에 존재하지 않는다.
    },

    readStatuses: {
        /** POST /api/readStatus  { channelId, userId } */
        create: (payload) => request('POST', '/readStatus', payload),
        /** PATCH /api/readStatus/{id}  { lastReadAt } */
        update: (id, payload) => request('PATCH', `/readStatus/${id}`, payload),
        /** GET /api/readStatus/{id}/find */
        find: (id) => request('GET', `/readStatus/${id}/find`)
    },

    userStatuses: {
        /** PATCH /api/userStatus/{id}  { userId, lastActiveAt } */
        update: (id, payload) => request('PATCH', `/userStatus/${id}`, payload)
    },

    binaryContents: {
        /** GET /api/binaryContent */
        findAll: () => request('GET', '/binaryContent'),
        /** GET /api/binaryContent/{id} */
        findById: (id) => request('GET', `/binaryContent/${id}`)
    }
};
