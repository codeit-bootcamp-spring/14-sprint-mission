import { api, ApiError } from './api.js';

const DEFAULT_AVATAR = '/images/default-avatar.svg';

/* ===================== state ===================== */

const state = {
    currentUser: null,          // UserResponseDto
    users: [],                  // UserResponseDto[]
    channels: [],               // ChannelResponseDto[]
    selectedChannelId: null,
    messagesByChannel: new Map(),   // channelId -> MessageResponseDto[]  (세션 로컬)
    profileCache: new Map()         // binaryId -> dataURL
};

/* ===================== helpers ===================== */

const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => Array.from(document.querySelectorAll(selector));

function escapeHtml(value) {
    if (value === null || value === undefined) return '';
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

function formatDateTime(value) {
    if (!value) return '-';
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return '-';
    return date.toLocaleString('ko-KR', { dateStyle: 'short', timeStyle: 'short' });
}

function toast(message, type = 'success') {
    const element = document.createElement('div');
    element.className = `toast toast--${type}`;
    element.textContent = message;
    $('#toastArea').appendChild(element);
    setTimeout(() => element.remove(), 3200);
}

/** API 호출을 감싸 에러를 토스트로 보여준다 */
async function run(task, successMessage) {
    try {
        const result = await task();
        if (successMessage) toast(successMessage);
        return { ok: true, result };
    } catch (error) {
        const message = error instanceof ApiError ? error.message : '알 수 없는 오류가 발생했습니다.';
        toast(message, 'error');
        console.error(error);
        return { ok: false, error };
    }
}

/** 프로필 이미지를 받아 data URL 로 변환. 캐시 사용. */
async function resolveAvatar(binaryId) {
    if (!binaryId) return DEFAULT_AVATAR;
    if (state.profileCache.has(binaryId)) return state.profileCache.get(binaryId);

    try {
        const content = await api.binaryContents.findById(binaryId);
        const url = `data:${content.contentType};base64,${content.bytes}`;
        state.profileCache.set(binaryId, url);
        return url;
    } catch {
        state.profileCache.set(binaryId, DEFAULT_AVATAR);
        return DEFAULT_AVATAR;
    }
}

function findUser(userId) {
    return state.users.find(user => user.id === userId) || null;
}

/* ===================== modal ===================== */

let modalSubmitHandler = null;

/**
 * openModal({ title, fields, submitText, onSubmit })
 * fields: [{ name, label, type, value, options, help, required }]
 *   type: text | password | email | textarea | select | checkboxList | datetime
 */
function openModal({ title, fields, submitText = '확인', onSubmit }) {
    $('#modalTitle').textContent = title;
    $('#modalSubmit').textContent = submitText;
    $('#modalBody').innerHTML = fields.map(renderField).join('');
    modalSubmitHandler = onSubmit;
    $('#modal').hidden = false;

    const firstInput = $('#modalBody').querySelector('input, select, textarea');
    if (firstInput) firstInput.focus();
}

function closeModal() {
    $('#modal').hidden = true;
    $('#modalBody').innerHTML = '';
    modalSubmitHandler = null;
}

function renderField(field) {
    const label = `<label class="field__label" for="f_${field.name}">${escapeHtml(field.label)}</label>`;
    const help = field.help ? `<div class="field__help">${escapeHtml(field.help)}</div>` : '';
    const required = field.required ? 'required' : '';
    let control;

    switch (field.type) {
        case 'select':
            control = `<select class="select" id="f_${field.name}" name="${field.name}" ${required}>
                ${field.options.map(option =>
                    `<option value="${escapeHtml(option.value)}"${option.value === field.value ? ' selected' : ''}>
                        ${escapeHtml(option.label)}
                     </option>`).join('')}
            </select>`;
            break;

        case 'checkboxList':
            control = `<div class="checkbox-list" id="f_${field.name}">
                ${field.options.map(option => `
                    <label class="checkbox-row">
                        <input type="checkbox" name="${field.name}" value="${escapeHtml(option.value)}">
                        <span>${escapeHtml(option.label)}</span>
                    </label>`).join('')}
            </div>`;
            break;

        case 'textarea':
            control = `<textarea class="input" id="f_${field.name}" name="${field.name}" rows="3" ${required}>${escapeHtml(field.value ?? '')}</textarea>`;
            break;

        default:
            control = `<input class="input" id="f_${field.name}" name="${field.name}"
                              type="${field.type || 'text'}"
                              value="${escapeHtml(field.value ?? '')}"
                              placeholder="${escapeHtml(field.placeholder ?? '')}"
                              autocomplete="off" ${required}>`;
    }

    return `<div class="field">${label}${control}${help}</div>`;
}

function readModalValues() {
    const formData = new FormData($('#modalForm'));
    const values = {};
    for (const [key, value] of formData.entries()) {
        if (values[key] === undefined) {
            values[key] = value;
        } else if (Array.isArray(values[key])) {
            values[key].push(value);
        } else {
            values[key] = [values[key], value];
        }
    }
    return values;
}

/** 빈 문자열을 null 로 바꾼다 (부분 수정 시 "값 없음"을 의미) */
function blankToNull(value) {
    return value === '' || value === undefined ? null : value;
}

/* ===================== 사용자 ===================== */

async function loadUsers() {
    const { ok, result } = await run(() => api.users.findAll());
    if (!ok) {
        $('#userList').innerHTML = '<div class="message error">사용자 목록을 불러오지 못했습니다.</div>';
        return;
    }
    state.users = result || [];
    await renderUsers();
    syncCurrentUser();
}

async function renderUsers() {
    const container = $('#userList');

    if (state.users.length === 0) {
        container.innerHTML = '<div class="message">등록된 사용자가 없습니다.</div>';
        return;
    }

    const avatars = await Promise.all(state.users.map(user => resolveAvatar(user.binaryId)));

    container.innerHTML = state.users.map((user, index) => `
        <div class="user-item">
            <img class="user-avatar" src="${avatars[index]}" alt="${escapeHtml(user.userName)}"
                 onerror="this.onerror=null;this.src='${DEFAULT_AVATAR}';">
            <div class="user-info">
                <div class="user-name">${escapeHtml(user.userName)}</div>
                <div class="user-email">${escapeHtml(user.email)}</div>
            </div>
            <div class="status-badge ${user.online ? 'online' : 'offline'}">
                ${user.online ? '온라인' : '오프라인'}
            </div>
            <div class="row-actions">
                <button class="btn btn--secondary btn--sm" data-action="user-edit" data-id="${user.id}">수정</button>
                <button class="btn btn--danger btn--sm" data-action="user-delete" data-id="${user.id}">삭제</button>
            </div>
        </div>
    `).join('');
}

function openUserCreateModal() {
    openModal({
        title: '사용자 추가',
        submitText: '생성',
        fields: [
            { name: 'name', label: '이름', required: true },
            { name: 'password', label: '비밀번호', type: 'password', required: true },
            { name: 'email', label: '이메일', type: 'email', required: true }
        ],
        onSubmit: async (values) => {
            const { ok } = await run(
                () => api.users.create({
                    name: values.name,
                    password: values.password,
                    email: values.email
                }),
                '사용자를 생성했습니다.'
            );
            if (ok) {
                closeModal();
                await loadUsers();
            }
        }
    });
}

function openUserEditModal(userId) {
    const user = findUser(userId);
    if (!user) return;

    openModal({
        title: '사용자 수정',
        submitText: '저장',
        fields: [
            { name: 'name', label: '이름', value: user.userName },
            { name: 'password', label: '비밀번호', type: 'password', help: '비워두면 변경하지 않습니다.' },
            { name: 'email', label: '이메일', type: 'email', value: user.email }
        ],
        onSubmit: async (values) => {
            const { ok } = await run(
                () => api.users.update(userId, {
                    name: blankToNull(values.name),
                    password: blankToNull(values.password),
                    email: blankToNull(values.email)
                }),
                '사용자를 수정했습니다.'
            );
            if (ok) {
                closeModal();
                await loadUsers();
            }
        }
    });
}

async function deleteUser(userId) {
    const user = findUser(userId);
    if (!user) return;
    if (!confirm(`'${user.userName}' 사용자를 삭제할까요?`)) return;

    const { ok } = await run(() => api.users.remove(userId), '사용자를 삭제했습니다.');
    if (!ok) return;

    if (state.currentUser && state.currentUser.id === userId) {
        state.currentUser = null;
        renderSession();
    }
    await loadUsers();
}

/* ===================== 로그인 ===================== */

function openLoginModal() {
    if (state.users.length === 0) {
        toast('로그인할 사용자가 없습니다. 먼저 사용자를 추가하세요.', 'info');
        return;
    }

    openModal({
        title: '로그인',
        submitText: '로그인',
        fields: [
            {
                name: 'name',
                label: '사용자',
                type: 'select',
                options: state.users.map(user => ({ value: user.userName, label: user.userName }))
            },
            { name: 'password', label: '비밀번호', type: 'password', required: true }
        ],
        onSubmit: async (values) => {
            // POST /api/auth 는 void 를 반환한다. 200이면 성공으로 보고 목록에서 사용자를 찾는다.
            const { ok } = await run(
                () => api.auth.login({ name: values.name, password: values.password }),
                '로그인했습니다.'
            );
            if (!ok) return;

            closeModal();
            await loadUsers();  // 온라인 상태가 갱신되므로 다시 조회
            state.currentUser = state.users.find(user => user.userName === values.name) || null;
            renderSession();
            await loadChannels();
        }
    });
}

function logout() {
    state.currentUser = null;
    state.channels = [];
    state.selectedChannelId = null;
    renderSession();
    renderChannels();
    renderChatSidebar();
    renderChatMain();
    toast('로그아웃했습니다.', 'info');
}

/** 사용자 목록이 갱신되면 현재 로그인 사용자 정보도 최신으로 맞춘다 */
function syncCurrentUser() {
    if (!state.currentUser) return;
    const latest = findUser(state.currentUser.id);
    state.currentUser = latest;
    renderSession();
}

async function renderSession() {
    const hasUser = Boolean(state.currentUser);

    $('#sessionEmpty').hidden = hasUser;
    $('#sessionUser').hidden = !hasUser;
    $('#loginBtn').hidden = hasUser;
    $('#logoutBtn').hidden = !hasUser;

    if (hasUser) {
        $('#sessionName').textContent = state.currentUser.userName;
        $('#sessionAvatar').src = await resolveAvatar(state.currentUser.binaryId);
    }
}

/* ===================== 채널 ===================== */

async function loadChannels() {
    if (!state.currentUser) {
        state.channels = [];
        renderChannels();
        renderChatSidebar();
        renderChatMain();
        return;
    }

    const { ok, result } = await run(() => api.channels.findAllByUserId(state.currentUser.id));
    state.channels = ok ? (result || []) : [];

    if (state.selectedChannelId && !state.channels.some(c => c.id === state.selectedChannelId)) {
        state.selectedChannelId = null;
    }

    renderChannels();
    renderChatSidebar();
    renderChatMain();
}

function renderChannels() {
    const hint = $('#channelHint');
    const container = $('#channelList');

    if (!state.currentUser) {
        hint.textContent = '';
        container.innerHTML = '<div class="message">로그인하면 참여 중인 채널을 볼 수 있습니다.</div>';
        return;
    }

    hint.textContent = `'${state.currentUser.userName}' 님이 볼 수 있는 채널입니다. `
        + '공개 채널은 전체, 비공개 채널은 참여한 것만 보입니다.';

    if (state.channels.length === 0) {
        container.innerHTML = '<div class="message">표시할 채널이 없습니다.</div>';
        return;
    }

    container.innerHTML = state.channels.map(channel => {
        const isPrivate = channel.channelType === 'PRIVATE' || channel.channelType === 'private';
        const name = channel.channelName || '(비공개 채널)';
        const participants = (channel.userIds && channel.userIds.length)
            ? `참여자 ${channel.userIds.length}명`
            : '';

        return `
            <div class="list-item">
                <div class="list-item__body">
                    <div class="list-item__title">
                        ${escapeHtml(name)}
                        <span class="pill ${isPrivate ? 'pill--private' : 'pill--public'}">
                            ${isPrivate ? 'PRIVATE' : 'PUBLIC'}
                        </span>
                    </div>
                    <div class="list-item__meta">
                        ${escapeHtml(channel.description || '설명 없음')}
                        &nbsp;·&nbsp; 최근 메시지 ${formatDateTime(channel.lastMessageAt)}
                        ${participants ? '&nbsp;·&nbsp; ' + participants : ''}
                    </div>
                    <div class="list-item__meta">id: ${escapeHtml(channel.id)}</div>
                </div>
                <div class="row-actions">
                    <button class="btn btn--secondary btn--sm"
                            data-action="channel-edit" data-id="${channel.id}"
                            ${isPrivate ? 'disabled title="비공개 채널은 수정할 수 없습니다"' : ''}>수정</button>
                    <button class="btn btn--danger btn--sm"
                            data-action="channel-delete" data-id="${channel.id}">삭제</button>
                </div>
            </div>`;
    }).join('');
}

function openPublicChannelModal() {
    openModal({
        title: '공개 채널 생성',
        submitText: '생성',
        fields: [
            { name: 'channelName', label: '채널 이름', required: true },
            { name: 'description', label: '설명', required: true }
        ],
        onSubmit: async (values) => {
            const { ok } = await run(
                () => api.channels.createPublic({
                    channelName: values.channelName,
                    description: values.description
                }),
                '공개 채널을 생성했습니다.'
            );
            if (ok) {
                closeModal();
                await loadChannels();
            }
        }
    });
}

function openPrivateChannelModal() {
    if (state.users.length === 0) {
        toast('참여자로 지정할 사용자가 없습니다.', 'info');
        return;
    }

    openModal({
        title: '비공개 채널 생성',
        submitText: '생성',
        fields: [{
            name: 'participantIds',
            label: '참여자',
            type: 'checkboxList',
            help: '비공개 채널은 이름과 설명이 없습니다.',
            options: state.users.map(user => ({ value: user.id, label: `${user.userName} (${user.email})` }))
        }],
        onSubmit: async (values) => {
            const raw = values.participantIds;
            const participantIds = raw === undefined ? [] : (Array.isArray(raw) ? raw : [raw]);

            if (participantIds.length === 0) {
                toast('참여자를 한 명 이상 선택하세요.', 'error');
                return;
            }

            const { ok } = await run(
                () => api.channels.createPrivate({ participantIds }),
                '비공개 채널을 생성했습니다.'
            );
            if (ok) {
                closeModal();
                await loadChannels();
            }
        }
    });
}

function openChannelEditModal(channelId) {
    const channel = state.channels.find(item => item.id === channelId);
    if (!channel) return;

    openModal({
        title: '채널 수정',
        submitText: '저장',
        fields: [
            { name: 'channelName', label: '채널 이름', value: channel.channelName || '' },
            { name: 'description', label: '설명', value: channel.description || '', help: '비워두면 변경하지 않습니다.' }
        ],
        onSubmit: async (values) => {
            const { ok } = await run(
                () => api.channels.update(channelId, {
                    channelName: blankToNull(values.channelName),
                    description: blankToNull(values.description)
                }),
                '채널을 수정했습니다.'
            );
            if (ok) {
                closeModal();
                await loadChannels();
            }
        }
    });
}

async function deleteChannel(channelId) {
    const channel = state.channels.find(item => item.id === channelId);
    if (!channel) return;
    if (!confirm(`'${channel.channelName || '비공개 채널'}' 을(를) 삭제할까요? 메시지와 첨부파일도 함께 삭제됩니다.`)) return;

    const { ok } = await run(() => api.channels.remove(channelId), '채널을 삭제했습니다.');
    if (!ok) return;

    state.messagesByChannel.delete(channelId);
    if (state.selectedChannelId === channelId) state.selectedChannelId = null;
    await loadChannels();
}

/* ===================== 채팅 ===================== */

function renderChatSidebar() {
    const container = $('#chatChannelList');

    if (!state.currentUser) {
        container.innerHTML = '<div class="message">로그인이 필요합니다.</div>';
        return;
    }
    if (state.channels.length === 0) {
        container.innerHTML = '<div class="message">채널이 없습니다.</div>';
        return;
    }

    container.innerHTML = state.channels.map(channel => {
        const name = channel.channelName || '(비공개 채널)';
        const active = channel.id === state.selectedChannelId ? ' is-active' : '';
        return `<button class="chat__channel${active}" type="button"
                        data-action="chat-select" data-id="${channel.id}"># ${escapeHtml(name)}</button>`;
    }).join('');
}

async function renderChatMain() {
    const header = $('#chatHeader');
    const messagesBox = $('#chatMessages');
    const input = $('#chatInput');
    const sendBtn = $('#chatSendBtn');

    const channel = state.channels.find(item => item.id === state.selectedChannelId);
    const canSend = Boolean(state.currentUser && channel);

    input.disabled = !canSend;
    sendBtn.disabled = !canSend;

    if (!channel) {
        header.innerHTML = '<span class="chat__title">채널을 선택하세요</span>';
        messagesBox.innerHTML = '<div class="message">왼쪽에서 채널을 선택하면 메시지를 보낼 수 있습니다.</div>';
        return;
    }

    header.innerHTML = `<span class="chat__title"># ${escapeHtml(channel.channelName || '비공개 채널')}</span>`;

    const messages = state.messagesByChannel.get(channel.id) || [];
    if (messages.length === 0) {
        messagesBox.innerHTML = '<div class="message">이 세션에서 보낸 메시지가 없습니다.</div>';
        return;
    }

    const avatars = await Promise.all(messages.map(message => {
        const author = findUser(message.userId);
        return resolveAvatar(author ? author.binaryId : null);
    }));

    messagesBox.innerHTML = messages.map((message, index) => {
        const author = findUser(message.userId);
        return `
            <div class="message-item">
                <img class="message-item__avatar" src="${avatars[index]}" alt=""
                     onerror="this.onerror=null;this.src='${DEFAULT_AVATAR}';">
                <div class="message-item__body">
                    <div class="message-item__head">
                        <span class="message-item__author">${escapeHtml(author ? author.userName : '알 수 없음')}</span>
                        <span class="message-item__time">${formatDateTime(message.createdAt)}</span>
                    </div>
                    <div class="message-item__text">${escapeHtml(message.message)}</div>
                </div>
                <div class="message-item__actions">
                    <button class="btn btn--secondary btn--sm" data-action="message-edit" data-id="${message.messageId}">수정</button>
                    <button class="btn btn--danger btn--sm" data-action="message-delete" data-id="${message.messageId}">삭제</button>
                </div>
            </div>`;
    }).join('');

    messagesBox.scrollTop = messagesBox.scrollHeight;
}

async function sendMessage(text) {
    if (!state.currentUser || !state.selectedChannelId) return;

    const { ok, result } = await run(() => api.messages.create({
        userId: state.currentUser.id,
        channelId: state.selectedChannelId,
        message: text
    }));
    if (!ok) return;

    const list = state.messagesByChannel.get(state.selectedChannelId) || [];
    list.push(result);
    state.messagesByChannel.set(state.selectedChannelId, list);

    await renderChatMain();
    loadChannels();   // lastMessageAt 갱신 (대기하지 않음)
}

function findLocalMessage(messageId) {
    for (const [channelId, list] of state.messagesByChannel) {
        const index = list.findIndex(message => message.messageId === messageId);
        if (index !== -1) return { channelId, index, message: list[index] };
    }
    return null;
}

function openMessageEditModal(messageId) {
    const found = findLocalMessage(messageId);
    if (!found) return;

    openModal({
        title: '메시지 수정',
        submitText: '저장',
        fields: [{ name: 'message', label: '내용', type: 'textarea', value: found.message.message, required: true }],
        onSubmit: async (values) => {
            const { ok } = await run(
                () => api.messages.update(messageId, { message: values.message }),
                '메시지를 수정했습니다.'
            );
            if (!ok) return;

            // PATCH 응답이 void 라 로컬 상태를 직접 갱신한다
            const list = state.messagesByChannel.get(found.channelId);
            list[found.index] = { ...found.message, message: values.message };
            closeModal();
            await renderChatMain();
        }
    });
}

async function deleteMessage(messageId) {
    const found = findLocalMessage(messageId);
    if (!found) return;
    if (!confirm('이 메시지를 삭제할까요?')) return;

    const { ok } = await run(() => api.messages.remove(messageId), '메시지를 삭제했습니다.');
    if (!ok) return;

    const list = state.messagesByChannel.get(found.channelId);
    list.splice(found.index, 1);
    await renderChatMain();
    loadChannels();
}

/* ===================== 읽음 상태 ===================== */

function openReadStatusCreateModal() {
    if (state.users.length === 0 || state.channels.length === 0) {
        toast('사용자와 채널이 모두 있어야 합니다. 먼저 로그인해 채널을 불러오세요.', 'info');
        return;
    }

    openModal({
        title: '읽음 상태 생성',
        submitText: '생성',
        fields: [
            {
                name: 'channelId', label: '채널', type: 'select',
                options: state.channels.map(channel => ({
                    value: channel.id, label: channel.channelName || `(비공개) ${channel.id.slice(0, 8)}`
                }))
            },
            {
                name: 'userId', label: '사용자', type: 'select',
                options: state.users.map(user => ({ value: user.id, label: user.userName }))
            }
        ],
        onSubmit: async (values) => {
            const { ok, result } = await run(
                () => api.readStatuses.create({ channelId: values.channelId, userId: values.userId }),
                '읽음 상태를 생성했습니다.'
            );
            if (ok) {
                closeModal();
                renderReadStatus(result);
            }
        }
    });
}

function renderReadStatus(readStatus) {
    const container = $('#readStatusResult');
    if (!readStatus) {
        container.innerHTML = '';
        return;
    }

    const user = findUser(readStatus.userId);
    const channel = state.channels.find(item => item.id === readStatus.channelId);

    container.innerHTML = `
        <div class="list-item">
            <div class="list-item__body">
                <div class="list-item__title">${escapeHtml(user ? user.userName : readStatus.userId)}</div>
                <div class="list-item__meta">
                    채널: ${escapeHtml(channel ? (channel.channelName || '비공개 채널') : readStatus.channelId)}
                    &nbsp;·&nbsp; 마지막 읽음 ${formatDateTime(readStatus.lastReadAt)}
                </div>
                <div class="list-item__meta">id: ${escapeHtml(readStatus.id)}</div>
            </div>
            <div class="row-actions">
                <button class="btn btn--secondary btn--sm"
                        data-action="readstatus-edit" data-id="${readStatus.id}">시각 수정</button>
            </div>
        </div>`;
}

function openReadStatusEditModal(readStatusId) {
    const now = new Date();
    const localValue = new Date(now.getTime() - now.getTimezoneOffset() * 60000)
        .toISOString().slice(0, 16);

    openModal({
        title: '읽음 시각 수정',
        submitText: '저장',
        fields: [{
            name: 'lastReadAt', label: '마지막 읽음 시각', type: 'datetime-local',
            value: localValue, required: true
        }],
        onSubmit: async (values) => {
            const isoValue = new Date(values.lastReadAt).toISOString();
            const { ok } = await run(
                () => api.readStatuses.update(readStatusId, { lastReadAt: isoValue }),
                '읽음 시각을 수정했습니다.'
            );
            if (!ok) return;

            closeModal();
            const { ok: found, result } = await run(() => api.readStatuses.find(readStatusId));
            if (found) renderReadStatus(result);
        }
    });
}

async function findReadStatus(readStatusId) {
    const { ok, result } = await run(() => api.readStatuses.find(readStatusId));
    if (ok) renderReadStatus(result);
}

/* ===================== 파일 ===================== */

async function loadFiles() {
    const { ok, result } = await run(() => api.binaryContents.findAll());
    const container = $('#fileList');

    if (!ok) {
        container.innerHTML = '<div class="message error">파일 목록을 불러오지 못했습니다.</div>';
        return;
    }
    if (!result || result.length === 0) {
        container.innerHTML = '<div class="message">등록된 파일이 없습니다.</div>';
        return;
    }

    container.innerHTML = result.map(file => {
        const isImage = (file.contentType || '').startsWith('image/');
        const source = isImage ? `data:${file.contentType};base64,${file.bytes}` : DEFAULT_AVATAR;
        return `
            <div class="file-card">
                <img class="file-card__thumb" src="${source}" alt="${escapeHtml(file.fileName)}"
                     onerror="this.onerror=null;this.src='${DEFAULT_AVATAR}';">
                <div class="file-card__name">${escapeHtml(file.fileName || '(이름 없음)')}</div>
                <div class="file-card__meta">${escapeHtml(file.contentType || '-')}</div>
                <div class="file-card__meta">${escapeHtml(file.binaryContentId)}</div>
            </div>`;
    }).join('');
}

/* ===================== tabs ===================== */

function activateTab(name) {
    $$('.tab').forEach(tab => tab.classList.toggle('is-active', tab.dataset.tab === name));
    $$('.panel').forEach(panel => panel.classList.toggle('is-active', panel.dataset.panel === name));

    if (name === 'files') loadFiles();
    if (name === 'channels' || name === 'chat') loadChannels();
}

/* ===================== events ===================== */

function bindEvents() {
    $('#tabs').addEventListener('click', (event) => {
        const tab = event.target.closest('.tab');
        if (tab) activateTab(tab.dataset.tab);
    });

    $('#loginBtn').addEventListener('click', openLoginModal);
    $('#logoutBtn').addEventListener('click', logout);

    $('#userCreateBtn').addEventListener('click', openUserCreateModal);
    $('#publicChannelCreateBtn').addEventListener('click', openPublicChannelModal);
    $('#privateChannelCreateBtn').addEventListener('click', openPrivateChannelModal);
    $('#readStatusCreateBtn').addEventListener('click', openReadStatusCreateModal);
    $('#fileReloadBtn').addEventListener('click', loadFiles);

    // 목록 안의 버튼들 (이벤트 위임)
    document.addEventListener('click', (event) => {
        const target = event.target.closest('[data-action]');
        if (!target) return;

        const id = target.dataset.id;
        switch (target.dataset.action) {
            case 'user-edit':        openUserEditModal(id); break;
            case 'user-delete':      deleteUser(id); break;
            case 'channel-edit':     openChannelEditModal(id); break;
            case 'channel-delete':   deleteChannel(id); break;
            case 'chat-select':      state.selectedChannelId = id; renderChatSidebar(); renderChatMain(); break;
            case 'message-edit':     openMessageEditModal(id); break;
            case 'message-delete':   deleteMessage(id); break;
            case 'readstatus-edit':  openReadStatusEditModal(id); break;
        }
    });

    // 모달
    $('#modal').addEventListener('click', (event) => {
        if (event.target.hasAttribute('data-close')) closeModal();
    });
    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape' && !$('#modal').hidden) closeModal();
    });
    $('#modalForm').addEventListener('submit', (event) => {
        event.preventDefault();
        if (modalSubmitHandler) modalSubmitHandler(readModalValues());
    });

    // 채팅 전송
    $('#chatForm').addEventListener('submit', (event) => {
        event.preventDefault();
        const input = $('#chatInput');
        const text = input.value.trim();
        if (!text) return;
        input.value = '';
        sendMessage(text);
    });

    // 읽음 상태 조회
    $('#readStatusFindForm').addEventListener('submit', (event) => {
        event.preventDefault();
        const value = $('#readStatusFindInput').value.trim();
        if (value) findReadStatus(value);
    });
}

/* ===================== bootstrap ===================== */

async function init() {
    bindEvents();
    await renderSession();
    await loadUsers();
    renderChannels();
    renderChatSidebar();
    renderChatMain();
}

document.addEventListener('DOMContentLoaded', init);
